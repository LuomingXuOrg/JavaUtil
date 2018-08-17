/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : ConverterHelper.java
 * CreateTime: 2018/08/14 10:44:58
 * LastModifiedDate : 18-8-15 下午12:56
 */

package Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

public class ConverterHelper
{
    public static <T> T convert(T destin, Object source)
    {
        if (source == null) { return null; }

        Class clazz = source.getClass();
        Field[] fieldsSource = clazz.getDeclaredFields();
        clazz = destin.getClass();
        Field[] fieldsDestin = clazz.getDeclaredFields();

        for (Field fieldSource : fieldsSource)
        {
            fieldSource.setAccessible(true);

            try
            {
                for (Field fieldDestin : fieldsDestin)
                {
                    fieldDestin.setAccessible(true);

                    if (fieldDestin.getName().equals(fieldSource.getName()))
                    {
                        if (fieldDestin.getType().equals(fieldSource.getType()))
                        {
                            fieldDestin.set(destin, fieldSource.get(source));
                            break;
                        }
                        else
                        {
                            fieldDestin.set(destin, typeConvert(fieldDestin.getType(), fieldSource.get(source)));
                            break;
                        }
                    }
                }
            }
            catch (Exception e) { e.printStackTrace(); }
        }

        return destin;
    }

    public static <S, T> List<T> convertList(T destin, List<S> source)
    {
        if (source.size() == 0) { return null; }

        List<T> lists = new LinkedList<>();
        Class clazz = destin.getClass();

        try
        {
            for (S item : source)
            {
                T temp = (T) clazz.newInstance();
                lists.add(convert(temp, item));
            }
        }
        catch (Exception e) { e.printStackTrace(); }

        return lists;
    }

    private static <T> Object typeConvert(Class<T> destinClass, Object sourceObj) throws Exception
    {
        try
        {
            if (destinClass.equals(Integer.class))
            {
                Method method = sourceObj.getClass().getMethod("intValue");
                return method.invoke(sourceObj);
            }
            if (destinClass.equals(Double.class))
            {
                Method method = sourceObj.getClass().getMethod("doubleValue");
                return method.invoke(sourceObj);
            }
            if (destinClass.equals(Long.class))
            {
                Method method = sourceObj.getClass().getMethod("longValue");
                return method.invoke(sourceObj);
            }
            if (destinClass.equals(Float.class))
            {
                Method method = sourceObj.getClass().getMethod("floatValue");
                return method.invoke(sourceObj);
            }
            if (destinClass.equals(Short.class))
            {
                Method method = sourceObj.getClass().getMethod("shortValue");
                return method.invoke(sourceObj);
            }
            if (destinClass.equals(BigDecimal.class))
            {
                if (sourceObj.getClass().equals(Double.class))
                {
                    Method method = destinClass.getMethod("valueOf", double.class);
                    return method.invoke(null, sourceObj);
                }
                if (sourceObj.getClass().equals(Long.class))
                {
                    Method method = destinClass.getMethod("valueOf", long.class);
                    return method.invoke(null, sourceObj);
                }
            }

            throw new Exception(String.format("Can not convert \'%s\' to \'%s\'", sourceObj.getClass(), destinClass));
        }
        catch (Exception e)
        {
            throw new Exception(String.format("Can not convert \'%s\' to \'%s\'", sourceObj.getClass(), destinClass));
        }
    }
}
