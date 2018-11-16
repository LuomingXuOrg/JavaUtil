/*
 *  Copyright 2018-2018 LuomingXuOrg
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
 *  limitations under the License.
 *
 *  Author : Luoming Xu
 *  File Name : GenerateFrameworkFileBySQLDeprecated.java
 *  Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javaUtil.Util;

import com.google.common.base.CaseFormat;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作为学习, 保留在此, 不再使用
 */
@Deprecated
public class GenerateFrameworkFileBySQLDeprecated
{
    /**
     * 数据库--表名
     */
    private static String tableName = "";

    /**
     * 步骤
     * <ul>
     * <li>读取文件, 将文件分解到list中(一行行来分解), 去掉了第一行的"create table"语句</li>
     * <li>通过句末的","来判断一个sql语句块. 优化之前分解好的list</li>
     * <li>将list进一步优化, 存入map中, 方便之后读取</li>
     * </ul>
     *
     * @param filePath scrip.sql文件路径
     * @param saveDir  保存文件的文件夹
     */
    public static void generate(String filePath, String saveDir)
    {
        Map<String, List<String>> map = sqlSequenceSplit(splitSql(readFile(new File(filePath))));

        generateGroovy(map, saveDir);
        generateRepository(saveDir);
        generateService(saveDir);
    }

    /**
     * 生成Repository文件
     *
     * @param saveDir
     */
    private static void generateRepository(String saveDir)
    {
        String upperName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
        String lowerName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);

        String sb = "import org.springframework.stereotype.Component;\n\n" +
                "@Component\n" +
                String.format("public interface %sRepository", upperName) +
                "\n{}\n";
        writeInFile(sb.getBytes(), saveDir + "\\" + upperName + "Repository.java");

        sb = "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "@Component\n" +
                String.format("public class %sRepositoryImpl implements %sRepository\n{", upperName, upperName) +
                String.format("private final %sMapper %sMapper;\n", upperName, lowerName) +
                "@Autowired\n" +
                String.format("public %sRepositoryImpl(%sMapper %sMapper)\n{", upperName, upperName, lowerName) +
                String.format("this.%sMapper=%sMapper;\n}\n}\n", lowerName, lowerName);

        writeInFile(sb.getBytes(), saveDir + "\\" + upperName + "RepositoryImpl.java");
    }

    /**
     * 生成Service文件
     *
     * @param saveDir
     */
    private static void generateService(String saveDir)
    {
        String upperName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
        String lowerName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);

        String sb = "import org.springframework.stereotype.Component;\n\n" +
                "@Component\n" +
                String.format("public interface %sService", upperName) +
                "\n{}\n";
        writeInFile(sb.getBytes(), saveDir + "\\" + upperName + "Service.java");

        sb = "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "@Component\n" +
                String.format("public class %sServiceImpl implements %sService\n{", upperName, upperName) +
                String.format("private final %sRepository %sRepository;\n", upperName, lowerName) +
                "@Autowired\n" +
                String.format("public %sServiceImpl(%sRepository %sRepository)\n{", upperName, upperName, lowerName) +
                String.format("this.%sRepository=%sRepository;\n}\n}\n", lowerName, lowerName);

        writeInFile(sb.getBytes(), saveDir + "\\" + upperName + "ServiceImpl.java");
    }

    /**
     * 生成groovy文件
     *
     * @param map
     * @param saveDir
     */
    private static void generateGroovy(Map<String, List<String>> map, String saveDir)
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
        sb.append("column(name: 'id', type: 'BIGINT', autoIncrement: true, remarks: '表ID，主键，unsigned bigint、单表时自增、步长为 1')\n" +
                "{\n" +
                "constraints(primaryKey: true)\n" +
                "}\n");

        //各自的行
        for (List<String> list : map.values())
        {
            String key = list.get(0);
            if (key.equals("id") || key.equals("created_by")
                    || key.equals("creation_date") || key.equals("last_updated_by")
                    || key.equals("last_update_date") || key.equals("object_version_number"))
            {
                continue;
            }

            sb.append(String.format("column(name: \'%s\'", list.get(0)));

            if (list.contains("null"))
            {
                if (list.contains("not"))
                {
                    sb.append(String.format(", type: \'%s not null\'", list.get(1).equals("date") ? "datetime" : list.get(1)));
                }
                else
                {
                    sb.append(String.format(", type: \'%s null\'", list.get(1).equals("date") ? "datetime" : list.get(1)));
                }
            }
            if (list.contains("default"))
            {
                sb.append(String.format(", defaultValue: %s", list.get(list.indexOf("default") + 1)));
            }

            if (list.contains("comment"))
            {
                StringBuilder temp = new StringBuilder();
                for (int i = list.indexOf("comment") + 1; ; i++)
                {
                    if (list.get(i).startsWith("\'"))
                    {
                        temp.append(list.get(i));
                    }
                    if (list.get(i).endsWith("\'") && !list.get(i).startsWith("\'"))
                    {
                        temp.append(list.get(i));
                    }
                    if (temp.toString().endsWith("\'"))
                    {
                        break;
                    }
                }
                sb.append(String.format(", remarks: %s", temp.toString()));
            }

            sb.append(")\n");
        }

        //必须有的行
        sb.append("column(name: \'created_by\', type: \'BIGINT\', defaultValue: \'0\', remarks: '创建人')\n");
        sb.append("column(name: \'creation_date\', type: \'DATETIME not null\', defaultValueComputed: \'CURRENT_TIMESTAMP\', remarks: '创建时间')\n");
        sb.append("column(name: \'last_updated_by\', type: \'BIGINT\', defaultValue: \'0\', remarks: '修改人')\n");
        sb.append("column(name: \'last_update_date\', type: \'DATETIME not null on update current_timestamp\', defaultValueComputed: \'CURRENT_TIMESTAMP\', remarks: '最后修改时间')\n");
        sb.append("column(name: \'object_version_number\', type: \'BIGINT\', defaultValue: \'1\', remarks: '版本号')\n");

        sb.append("}\n");
        sb.append("}\n");
        sb.append("}\n");

        writeInFile(sb.toString().getBytes(), saveDir + "\\" + tableName + ".groovy");
    }

    private static void writeInFile(byte[] buffer, String savePath)
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

    /**
     * 将最终的SQL语句储存为map, 方便最后生成各类文件
     *
     * @param list
     * @return
     */
    private static Map<String, List<String>> sqlSequenceSplit(List<String> list)
    {
        Map<String, List<String>> map = new LinkedHashMap<>();

        for (String item : list)
        {
            String[] arr = item.split("(\\s+)");
            List<String> arrTemp = new ArrayList<>(Arrays.asList(arr));
            arrTemp.removeIf(temp -> temp.equals(""));
            map.put(arr[1], arrTemp);
        }

        return map;
    }

    /**
     * 读取文件, 并将文件通过换行, 分成一个个list
     *
     * @param file
     * @return
     */
    private static List<String> readFile(File file)
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

        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(remainBracket(sb.toString())).split("(\\s*\\r\\n)|(\\s*\\n)")));
    }

    /**
     * 通过正则, 取得括号(包括括号)的string
     *
     * @param str
     * @return
     */
    private static String remainBracket(String str)
    {
        Pattern pattern = Pattern.compile("^.*table.*");
        Matcher matcher = pattern.matcher(str);

        if (matcher.find())
        {
            tableName = matcher.group().split("\\.")[1];
        }

        pattern = Pattern.compile("\\(([\\s\\S]*)\\)");
        matcher = pattern.matcher(str);
        if (matcher.find())
        {
            return matcher.group();
        }

        return null;
    }


    /**
     * 将string通过句末的','来分割为一个个语句
     *
     * @param list
     * @return
     */
    private static List<String> splitSql(List<String> list)
    {
        List<String> listTemp = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        list.remove(0);
        list.remove(list.size() - 1);
        for (String item : list)
        {
            if (!item.endsWith(","))
            {
                sb.append(item);
            }
            else
            {
                listTemp.add(sb + item.substring(0, item.length() - 1));
                sb = new StringBuilder();
            }
        }

        //最后一个没有","来结尾, 做特殊处理
        listTemp.add(sb.toString());

        return listTemp;
    }
}
