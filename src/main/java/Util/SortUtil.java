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
import jdk.nashorn.internal.ir.ReturnNode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class SortUtil
{
    //返回的字段的类名
    private static final String FIELD_CLASS = "fieldClass";
    //返回的字段的值
    private static final String FIELD_OBJECT = "fieldObject";

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
     * @param paramOne  entity
     * @param paramTwo  entity
     * @param fieldName field name which is used to compare
     * @param direction compare direction
     * @return compare result
     * @throws Exception
     */
    private static Integer compare(Object paramOne, Object paramTwo, String fieldName, Sort.Direction direction) throws Exception
    {
        //如果两个类的类名不一样, 抛出异常
        if (!paramOne.getClass().getName().equals(paramTwo.getClass().getName()))
        {
            throw new SortException("The two params is not in one class!");
        }

        return compare(getFieldByField(paramOne, fieldName), getFieldByField(paramTwo, fieldName), direction);
    }

    /**
     * 直接通过反射来获取这个字段的值和类名--比较快
     *
     * @param param     entity
     * @param fieldName field name which you need its value
     * @return field value
     * @throws IllegalAccessException
     * @throws SortException
     */
    private static Object getFieldByField(Object param, String fieldName) throws Exception
    {
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

                return obj;
            }
        }

        throw new SortException(String.format("\"%s\"--No such field in this param!", fieldName));
    }

    /**
     * 通过类里面的get方法来获取这个字段
     *
     * @param param
     * @param fieldName
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NullPointerException
     * @throws SortException
     */
    private static Map<String, Object> doFieldGetMethod(Object param, String fieldName) throws Exception
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

    /**
     * compare two object which could be Integer, Double,
     * Long, Short, Float, Date
     *
     * @param one
     * @param two
     * @param direction
     * @return
     * @throws Exception
     */
    private static Integer compare(Object one, Object two, Sort.Direction direction) throws Exception
    {
        Method method;
        Class clazz = one.getClass();
        if (clazz.equals(Integer.class) || clazz.equals(Double.class)
                || clazz.equals(Long.class) || clazz.equals(Short.class)
                || clazz.equals(Float.class) || clazz.equals(Date.class))
        {
            method = one.getClass().getMethod("compareTo", one.getClass());
            return compare(method, one, two, direction);
        }

        return 0;
    }

    /**
     * do method
     *
     * @param method "compareTo" method
     * @param one
     * @param two
     * @param direction
     * @return
     * @throws Exception
     */
    private static Integer compare(Method method, Object one, Object two, Sort.Direction direction) throws Exception
    {
        switch (direction)
        {
            case ASC:
                return (Integer) method.invoke(one, two);
            case DESC:
                return (Integer) method.invoke(two, one);
        }

        return 0;
    }
}
