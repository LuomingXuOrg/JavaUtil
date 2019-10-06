/*
 * Copyright (c) 2019
 * Author : Luoming Xu
 * Project Name : JavaUtil
 * File Name : CompleteFullColumns.java
 * CreateTime: 2019/10/06 14:02:31
 * LastModifiedDate : 2019/10/6 下午2:02
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
