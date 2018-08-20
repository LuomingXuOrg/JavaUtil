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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class SortUtil
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    //返回的字段的类名
    private static final String FIELD_CLASS = "fieldClass";
    //返回的字段
    private static final String FIELD_OBJECT = "fieldObject";

    //定义了一般需要进行排序的类名
    private static final String INTEGER_CLASS = "java.lang.Integer";
    private static final String LONG_CLASS = "java.lang.Long";
    private static final String DOUBLE_CLASS = "java.lang.Double";
    private static final String DATE_CLASS = "java.util.Date";

    /**
     * 给外部调用的排序的方法
     *
     * @param sort
     * @param paramLists
     * @param <T>
     * @throws SortException
     */
    public static <T> void doSort(Sort sort, List<T> paramLists) throws SortException
    {
        //判断是否选择了需要排序的fieldName
        if (sort.getFieldName() == null)
        {
            throw new SortException("You did not choose one field which you want to sort by it! ");
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
                System.exit(1); //无法进行比较, 退出程序
            }
            return 0;
        });
    }

    /**
     * 给doSort方法调用的比较方法
     *
     * @param paramOne
     * @param paramTwo
     * @param fieldName
     * @param direction
     * @return
     * @throws Exception
     */
    private static Integer compare(Object paramOne, Object paramTwo, String fieldName, Sort.Direction direction) throws Exception
    {
        //如果两个类的类名不一样, 抛出异常
        if (!paramOne.getClass().getName().equals(paramTwo.getClass().getName()))
        {
            throw new SortException("The two params is not in one class!");
        }

        Map<String, Object> mapOne = getFieldByField(paramOne, fieldName);
        Map<String, Object> mapTwo = getFieldByField(paramTwo, fieldName);

        //判断获取的类名是哪个类
        if (mapOne.get(FIELD_CLASS).equals(INTEGER_CLASS))
        {
            return compare(Integer.parseInt(mapOne.get(FIELD_OBJECT).toString()), Integer.parseInt(mapTwo.get(FIELD_OBJECT).toString()), direction);
        }
        if (mapOne.get(FIELD_CLASS).equals(DOUBLE_CLASS))
        {
            return compare(Double.valueOf(mapOne.get(FIELD_OBJECT).toString()), Double.valueOf(mapTwo.get(FIELD_OBJECT).toString()), direction);
        }
        if (mapOne.get(FIELD_CLASS).equals(LONG_CLASS))
        {
            return compare(Long.valueOf(mapOne.get(FIELD_OBJECT).toString()), Long.valueOf(mapTwo.get(FIELD_OBJECT).toString()), direction);
        }
        if (mapOne.get(FIELD_CLASS).equals(DATE_CLASS))
        {
            return compare(DATE_FORMAT.parse(DATE_FORMAT.format(mapOne.get(FIELD_OBJECT))), DATE_FORMAT.parse(DATE_FORMAT.format(mapTwo.get(FIELD_OBJECT))), direction);
        }
        else
        {
            throw new SortException("Can not solve this attribute's type!");
        }
    }

    /**
     * 直接通过反射来获取这个字段的值和类名--比较快
     *
     * @param param
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     * @throws SortException
     */
    private static Map<String, Object> getFieldByField(Object param, String fieldName) throws IllegalAccessException, SortException
    {
        Map<String, Object> map = new HashMap<>();

        Class c = param.getClass();
        Field[] fields = c.getDeclaredFields();

        for (Field field : fields)
        {
            field.setAccessible(true);

            if (field.getName().equals(fieldName))
            {
                Object obj = field.get(param);

                if (obj == null)
                {
                    throw new NullPointerException("Such field is null!");
                }

                //存入字段类名
                map.put(FIELD_CLASS, obj.getClass().getName());
                //存入字段
                map.put(FIELD_OBJECT, obj);

                return map;
            }
        }

        throw new SortException(String.format("\"%s\"--No such field in this param!", fieldName));
    }

    //通过类里面的get方法来获取这个字段
    private static Map<String, Object> doFieldGetMethod(Object param, String fieldName) throws InvocationTargetException, IllegalAccessException, NullPointerException, SortException
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

                //存入字段类名
                map.put(FIELD_CLASS, obj.getClass().getName());
                //存入字段
                map.put(FIELD_OBJECT, obj);

                return map; //找到get方法直接return, 不再循环
            }
        }

        throw new SortException(String.format("\"%s\"--No such field in this param!", fieldName));
    }

    //对不同的类型的字段调用不同的方法

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
