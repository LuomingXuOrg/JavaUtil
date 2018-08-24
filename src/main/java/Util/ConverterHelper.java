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
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

public class ConverterHelper
{
    public static <T> T convert(T destin, Object source)
    {
        if (source == null) { return null; }

        Class clazz = source.getClass();
        Field[] fieldsSource = getAllFields(clazz);
        clazz = destin.getClass();
        Field[] fieldsDestin = getAllFields(clazz);

        for (Field fieldSource : fieldsSource)
        {
            //如果这个field是静态的, 不与目标类型进行匹配
            if (isStatic(fieldSource))
            { continue; }

            fieldSource.setAccessible(true);

            try
            {
                for (Field fieldDestin : fieldsDestin)
                {
                    //如果这个field是静态的, 不匹配
                    if (isStatic(fieldDestin))
                    { break; }

                    fieldDestin.setAccessible(true);

                    if (fieldDestin.getName().equals(fieldSource.getName()))
                    {
                        if (fieldDestin.getType().equals(fieldSource.getType()))
                        {
                            fieldDestin.set(destin, fieldSource.get(source));
                        }
                        else
                        {
                            //同名属性, 不同类型, 尝试进行类型转化
                            try
                            {
                                fieldDestin.set(destin, typeConvert(fieldDestin.getType(), fieldSource.get(source)));
                            }
                            catch (Exception e)
                            {
                                //转化失败的话, 设值为null
                                fieldDestin.set(destin, null);
                            }
                        }

                        break;
                    }
                }
            }
            catch (Exception e) { e.printStackTrace(); }
        }

        return destin;
    }

    @SuppressWarnings("unchecked")
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

    /**
     * 获取类的所有属性, 包含父类的属性
     *
     * @param clazz 需要获取Field[]的类
     * @return Field[]
     */
    private static Field[] getAllFields(Class<?> clazz)
    {
        List<Field> fieldList = new ArrayList<>();

        for (; clazz != Object.class; clazz = clazz.getSuperclass())
        {
            try
            {
                fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            }
            catch (Exception e)
            {
                //这里什么都不能抛出去.
                //如果这里的异常打印或者往外抛，则就不会进入
            }
        }

        Field[] temp = new Field[fieldList.size()];
        fieldList.toArray(temp);

        return temp;
    }

    /**
     * 在转化的时候可能需要的一些类型转化的方法
     *
     * @param destinClass 目标类型
     * @param sourceObj   需要转化的对象
     * @param <T>         目标类型
     * @return 转化之后的field的对象
     * @throws Exception 无法进行类型转化异常
     */
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
                    /*
                     * 通过方法名, 和这个方法需要的参数类型,
                     * 来获取方法
                     */
                    Method method = destinClass.getMethod("valueOf", double.class);
                    /*
                     * 调用obj这个类下面的这个方法,
                     * 由于这个是静态的, 所以不要new一个对象出来
                     * 后面的Object...就是这个方法的参数
                     */
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

    private static Boolean isStatic(Field field)
    {
        /*
         * Modifier这个类就是用来判断public, private, abstract之类的信息的
         */
        return Modifier.isStatic(field.getModifiers());
    }
}
