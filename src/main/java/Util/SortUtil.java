/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : SortUtil.java
 * CreateTime: 2018/07/26 17:05:13
 * LastModifiedDate : 18-7-26 下午5:05
 */

package Util;

import Model.Sort;
import Exception.SortException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortUtil
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final String FIELD_CLASS = "getMethodResult";
    private static final String INVOKE_RESULT = "invokeResult";

    private static final String INTEGER_CLASS = "java.lang.Integer";
    private static final String LONG_CLASS = "java.lang.Long";
    private static final String DOUBLE_CLASS = "java.lang.Double";
    private static final String DATE_CLASS = "java.util.Date";

    public static <T> void doSort(Sort sort, List<T> paramLists) throws SortException
    {
        //判断是否选择需要排序的fieldName
        if (sort.getFieldName() == null)
        {
            throw new SortException("You do not choose one field which you want to sort by it! ");
        }

        paramLists.sort((one, two) ->
        {
            try
            {
                return compare(one, two, sort.getFieldName(), sort.getDirection());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return 0;
        });
    }

    private static Integer compare(Object paramOne, Object paramTwo, String fieldName, Sort.Direction direction) throws Exception
    {
        if (!paramOne.getClass().getName().equals(paramTwo.getClass().getName()))
        {
            throw new SortException("The two params is not in one class!");
        }

        try
        {
            Map<String, Object> mapOne = getGetMethod(paramOne, fieldName);
            Map<String, Object> mapTwo = getGetMethod(paramTwo, fieldName);

            if (mapOne.get(FIELD_CLASS).equals(INTEGER_CLASS))
            {
                return compare(Integer.parseInt(mapOne.get(INVOKE_RESULT).toString()), Integer.parseInt(mapTwo.get(INVOKE_RESULT).toString()), direction);
            }
            if (mapOne.get(FIELD_CLASS).equals(DOUBLE_CLASS))
            {
                return compare(Double.valueOf(mapOne.get(INVOKE_RESULT).toString()), Double.valueOf(mapTwo.get(INVOKE_RESULT).toString()), direction);
            }
            if (mapOne.get(FIELD_CLASS).equals(LONG_CLASS))
            {
                return compare(Long.valueOf(mapOne.get(INVOKE_RESULT).toString()), Long.valueOf(mapTwo.get(INVOKE_RESULT).toString()), direction);
            }
            if (mapOne.get(FIELD_CLASS).equals(DATE_CLASS))
            {
                return compare(DATE_FORMAT.parse(DATE_FORMAT.format(mapOne.get(INVOKE_RESULT))), DATE_FORMAT.parse(DATE_FORMAT.format(mapTwo.get(INVOKE_RESULT))), direction);
            }
            else
            {
                throw new SortException("Can not solve this attribute's type!");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    private static Map<String, Object> getGetMethod(Object param, String fieldName) throws InvocationTargetException, IllegalAccessException
    {
        Map<String, Object> map = new HashMap<>();

        Class c = param.getClass();
        Method[] methods = c.getMethods();
        for (Method method : methods)
        {
            if (("get" + fieldName).toLowerCase().equals(method.getName().toLowerCase()))
            {
                Object obj = method.invoke(param);

                if (obj == null)
                {
                    throw new NullPointerException("Such fieldName has no get method or this field is null!");
                }

                map.put(FIELD_CLASS, obj.getClass().getName());
                map.put(INVOKE_RESULT, obj);

                return map;
            }
        }

        return map;
    }

    private static Integer compare(Integer one, Integer two, Sort.Direction direction)
    {
        switch (direction)
        {
            case ASC:
                return Integer.compare(one, two);
            case DESC:
                return Integer.compare(two, one);
        }

        return 0;
    }

    private static Integer compare(Double one, Double two, Sort.Direction direction)
    {
        switch (direction)
        {
            case ASC:
                return Double.compare(one, two);
            case DESC:
                return Double.compare(two, one);
        }

        return 0;
    }

    private static Integer compare(Long one, Long two, Sort.Direction direction)
    {
        switch (direction)
        {
            case ASC:
                return Long.compare(one, two);
            case DESC:
                return Long.compare(two, one);
        }

        return 0;
    }

    private static Integer compare(Date one, Date two, Sort.Direction direction)
    {
        switch (direction)
        {
            case ASC:
                return one.compareTo(two);
            case DESC:
                return two.compareTo(one);
        }

        return 0;
    }
}
