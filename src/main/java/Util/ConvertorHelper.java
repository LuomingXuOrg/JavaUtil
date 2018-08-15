/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : ConvertorHelper.java
 * CreateTime: 2018/08/14 10:44:58
 * LastModifiedDate : 18-8-15 下午12:56
 */

package Util;

import java.lang.reflect.Field;
import java.util.*;

public class ConvertorHelper
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
                        fieldDestin.set(destin, fieldSource.get(source));
                        break;
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
}
