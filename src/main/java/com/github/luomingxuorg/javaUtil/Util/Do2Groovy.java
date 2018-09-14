/*
 * Copyright 2018-2018 LuomingXuOrg
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
 * File Name : Do2Groovy.java
 * Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javaUtil.Util;

import com.google.common.base.CaseFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Do2Groovy
{
    /**
     * 将DO转化为数据库定义groovy文件
     *
     * @param savePath  保存的路径(文件夹)
     * @param classPath do文件路径(文件夹)
     */
    public static void toGroovy(String savePath, String classPath)
    {
        File dir = new File(classPath);

        try
        {
            if (dir.isDirectory())
            {
                File[] files = dir.listFiles();

                if (files != null)
                {
                    for (File file : files)
                    {
                        //文件名, 去掉后缀名
                        String fileName = file.getName().substring(0, file.getName().indexOf("."));

                        //包名后面加".", 后面才是类名
                        String className = getPackage(file) + "." + fileName;
                        byte[] b = getGroovy(Class.forName(className)).getBytes();

                        String tableName = fileName.substring(0, fileName.indexOf("DO"));
                        //groovy文件保存路径
                        String groovyPath = savePath + "\\" + tableName + ".groovy";

                        //写入文件
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(groovyPath));
                        fileOutputStream.write(b, 0, b.length);
                        fileOutputStream.close();
                        System.out.println("DONE");
                    }
                    System.out.println(String.format("total: %s done.", files.length));
                }
                else
                { System.err.println("No files in this directory. "); }
            }
            else
            { System.err.println("Path is not a directory. "); }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String getPackage(File file)
    {
        StringBuilder sb = new StringBuilder();

        //读取文件
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] b = new byte[2048];

            while (fileInputStream.read(b) != -1)
            {
                sb.append(new String(b, StandardCharsets.UTF_8));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //进行正则匹配获取包名
        Pattern pattern = Pattern.compile("package ([\\s\\S]*)dataobject");
        Matcher matcher = pattern.matcher(sb.toString());
        if (matcher.find())
        {
            return matcher.group();
        }

        return null;
    }

    /**
     * 生成groovy文件的string
     * @param clazz 类名
     * @param <T> 泛型
     * @return string
     */
    private static <T> String getGroovy(Class<T> clazz)
    {
        StringBuilder sb = new StringBuilder();

        String simpleName = clazz.getSimpleName();
        String fileName = simpleName.substring(0, simpleName.indexOf("DO"));
        String tableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fileName);

        sb.append("package script.db\n\n");

        sb.append(String.format("databaseChangeLog(logicalFilePath: '%s.groovy')\n", fileName));
        sb.append("{\n");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sb.append(String.format("changeSet(id: '%s-add-table-%s', author: 'Luoming Xu')\n",
                sdf.format(new Date(System.currentTimeMillis())), tableName));
        sb.append("{\n");

        sb.append(String.format("createTable(tableName: \"%s\")\n", tableName));
        sb.append("{\n");

        //id
        sb.append("column(name: 'id', type: 'BIGINT', autoIncrement: true, remarks: '表ID，主键，unsigned bigint、单表时自增、步长为 1')\n" +
                "{\n" +
                "constraints(primaryKey: true)\n" +
                "}\n");

        //获取所有类名
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);

            //类名--小写
            String temp = field.getName().toLowerCase();

            //必有的行, 不需要重复添加
            if (temp.contains("lastupdate") || temp.contains("creat") ||
                temp.contains("serial") || temp.contains("objectversionnumber") ||
                temp.contains("id"))
            {
                continue;
            }

            //驼峰转下划线, 变为数据库表名
            String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
            //获取jdbcType
            String type = getJdbcType(field);
            //添加行. remark需要手动添加
            sb.append(String.format("column(name: '%s', type: '%s', remarks: '')\n", columnName, type));
        }

        //必须有的行
        sb.append("column(name: \"created_by\", type: \"BIGINT\", defaultValue: \"0\", remarks: '创建人')\n");
        sb.append("column(name: \"creation_date\", type: \"DATETIME not null\", defaultValueComputed: \"CURRENT_TIMESTAMP\", remarks: '创建时间')\n");
        sb.append("column(name: \"last_updated_by\", type: \"BIGINT\", defaultValue: \"0\", remarks: '修改人')\n");
        sb.append("column(name: \"last_update_date\", type: \"DATETIME not null on update current_timestamp\", defaultValueComputed: \"CURRENT_TIMESTAMP\", remarks: '最后修改时间')\n");
        sb.append("column(name: \"object_version_number\", type: \"BIGINT\", defaultValue: \"1\", remarks: '版本号')\n");

        sb.append("}\n");
        sb.append("}\n");
        sb.append("}\n");

        return sb.toString();
    }

    private static String getJdbcType(Field field)
    {
        Class clazz = field.getType();

        if (clazz.equals(String.class))
        {
            //默认需要长度
            return "varchar(50)";
        }
        if (clazz.equals(Long.class))
        {
            return "bigint";
        }
        if (clazz.equals(Double.class) || clazz.equals(BigDecimal.class))
        {
            return "decimal";
        }
        if (clazz.equals(Integer.class))
        {
            return "int";
        }
        if (clazz.equals(Date.class))
        {
            return "datetime";
        }

        return "error. no such type";
    }
}