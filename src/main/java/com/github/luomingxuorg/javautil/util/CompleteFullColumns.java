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
 * File Name : CompleteFullColumns.java
 * Repo: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javautil.util;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于生成mybatis的mapper.xml配置文件
 * 具体使用请移步README
 */
public class CompleteFullColumns
{
    /**
     * @param filePath           sql DDL文件
     * @param formerColumnResult 形如{@code <result column="comment" jdbcType="VARCHAR" property="comment"/>}的string
     */
    public static void generate(String filePath, String formerColumnResult)
    {
        try
        {
            File file = new File(filePath);
            InputStream stream = new FileInputStream(file);

            CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(stream);
            String tableName = createTable.getTable().getName();
            List<ColumnDefinition> columns = createTable.getColumnDefinitions();

            Pattern pattern;
            String columnName;
            for (ColumnDefinition item : columns)
            {
                columnName = item.getColumnName();
                pattern = Pattern.compile(String.format("(column=\"%s\")", columnName));
                Matcher matcher = pattern.matcher(formerColumnResult);
                formerColumnResult = matcher.replaceAll(String.format("column=\"%s_%s\"", tableName, columnName));
                System.out.println(String.format("%s.%s as %s_%s,", tableName, columnName, tableName, columnName));
            }
            System.out.println(formerColumnResult);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
