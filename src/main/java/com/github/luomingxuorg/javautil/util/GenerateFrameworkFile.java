/*
 *  Copyright 2018-2019 LuomingXu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author : Luoming Xu
 * File Name : GenerateFrameworkFile.java
 * Repo: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javautil.util;


import com.google.common.base.CaseFormat;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 生成choerodon框架文件
 */
public class GenerateFrameworkFile
{
    /**
     * 数据库--表名
     */
    private static String tableName = "";

    private static String upperTableName = "";
    private static String lowerTableName = "";

    /**
     * 生成文件
     *
     * @param filePath scrip.sql文件路径--一个sql的create table语句
     * @param saveDir  保存文件的文件夹
     */
    public static void generate(String filePath, String saveDir)
    {
        try
        {
            File file = new File(saveDir);
            if (!(file.exists() && file.isDirectory()))
            {
                throw new Exception("保存文件夹错误");
            }

            CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(readFile(new File(filePath)));
            tableName = createTable.getTable().getName();
            upperTableName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
            lowerTableName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);
            generateGroovy(createTable, saveDir);
            generateRepository(saveDir);
            generateService(saveDir);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void generateGroovy(CreateTable createTable, String saveDir)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("package script.db\n\n");

        sb.append(String.format("databaseChangeLog(logicalFilePath: '%s.groovy')\n", tableName));
        sb.append("{\n");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sb.append(String.format("changeSet(id: '%s-add-table-%s', author: 'GeneratedBy LuomingXuOrg')\n",
                sdf.format(new Date(System.currentTimeMillis())), tableName));
        sb.append("{\n");

        sb.append(String.format("createTable(tableName: \"%s\")\n", tableName));
        sb.append("{\n");

        //id
        sb.append("column(name: 'id', type: 'bigint', autoIncrement: true, remarks: '表ID, 主键, 自增, 步长为 1')\n" +
                "{\n" +
                "constraints(primaryKey: true)\n" +
                "}\n");

        //各自的行
        for (ColumnDefinition column : createTable.getColumnDefinitions())
        {
            String name = column.getColumnName();
            String type = column.getColDataType().toString().replace(" ", "");

            if (name.equals("id") || name.equals("created_by")
                    || name.equals("creation_date") || name.equals("last_updated_by")
                    || name.equals("last_update_date") || name.equals("object_version_number"))
            {
                continue;
            }

            sb.append(String.format("column(name: \'%s\'", name));

            List<String> list = column.getColumnSpecStrings();
            if (list.contains("null"))
            {
                if (list.contains("not"))
                {
                    sb.append(String.format(", type: \'%s not null\'", type));
                }
                else
                {
                    sb.append(String.format(", type: \'%s null\'", type));
                }
            }

            if (list.contains("default"))
            {
                String defaultValue = list.get(list.indexOf("default") + 1);
                if (type.contains("date") || type.contains("time"))
                {
                    sb.append(String.format(", defaultValueComputed: \'%s\'", defaultValue));

                }
                else
                {
                    sb.append(String.format(", defaultValue: %s", defaultValue));
                }
            }

            if (list.contains("comment"))
            {
                sb.append(String.format(", remarks: %s", list.get(list.indexOf("comment") + 1)));
            }

            sb.append(")\n");
        }

        //必有的行
        sb.append("column(name: \'created_by\', type: \'bigint\', defaultValue: \'0\', remarks: '创建人')\n");
        sb.append("column(name: \'creation_date\', type: \'datetime not null\', defaultValueComputed: \'current_timestamp\', remarks: '创建时间')\n");
        sb.append("column(name: \'last_updated_by\', type: \'bigint\', defaultValue: \'0\', remarks: '修改人')\n");
        sb.append("column(name: \'last_update_date\', type: \'datetime not null on update current_timestamp\', defaultValueComputed: \'current_timestamp\', remarks: '最后修改时间')\n");
        sb.append("column(name: \'object_version_number\', type: \'datetime\', defaultValue: \'1\', remarks: '版本号')\n");

        sb.append("}\n");
        sb.append("}\n");
        sb.append("}\n");

        writeFile(sb.toString().getBytes(), saveDir + "\\" + tableName + ".groovy");
    }

    private static void generateRepository(String saveDir)
    {
        String sb = "import org.springframework.stereotype.Component;\n\n" +
                "@Component\n" +
                String.format("public interface %sRepository", upperTableName) +
                "\n{}\n";
        writeFile(sb.getBytes(), saveDir + "\\" + upperTableName + "Repository.java");

        sb = "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "@Component\n" +
                String.format("public class %sRepositoryImpl implements %sRepository\n{", upperTableName, upperTableName) +
                String.format("private final %sMapper %sMapper;\n", upperTableName, lowerTableName) +
                "@Autowired\n" +
                String.format("public %sRepositoryImpl(%sMapper %sMapper)\n{", upperTableName, upperTableName, lowerTableName) +
                String.format("this.%sMapper=%sMapper;\n}\n}\n", lowerTableName, lowerTableName);

        writeFile(sb.getBytes(), saveDir + "\\" + upperTableName + "RepositoryImpl.java");
    }

    private static void generateService(String saveDir)
    {
        String upperTableName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
        String lowerTableName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);

        String sb = "import org.springframework.stereotype.Component;\n\n" +
                "@Component\n" +
                String.format("public interface %sService", upperTableName) +
                "\n{}\n";
        writeFile(sb.getBytes(), saveDir + "\\" + upperTableName + "Service.java");

        sb = "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "@Component\n" +
                String.format("public class %sServiceImpl implements %sService\n{", upperTableName, upperTableName) +
                String.format("private final %sRepository %sRepository;\n", upperTableName, lowerTableName) +
                "@Autowired\n" +
                String.format("public %sServiceImpl(%sRepository %sRepository)\n{", upperTableName, upperTableName, lowerTableName) +
                String.format("this.%sRepository=%sRepository;\n}\n}\n", lowerTableName, lowerTableName);

        writeFile(sb.getBytes(), saveDir + "\\" + upperTableName + "ServiceImpl.java");
    }

    private static String readFile(File file)
    {
        StringBuilder sb = new StringBuilder();

        //读取文件
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] b = new byte[1024];
            int byteRead;

            while ((byteRead = fileInputStream.read(b)) != -1)
            {
                sb.append(new String(b, 0, byteRead, StandardCharsets.UTF_8));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private static void writeFile(byte[] buffer, String savePath)
    {
        //写入文件
        FileOutputStream fileOutputStream;
        try
        {
            fileOutputStream = new FileOutputStream(new File(savePath));
            fileOutputStream.write(buffer, 0, buffer.length);
            fileOutputStream.close();
            System.out.println(savePath + " DONE");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
