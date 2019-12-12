/*
 *  Copyright 2018-2019 LuomingXuOrg
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
 *  File Name : FieldUtil.java
 *  Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javautil.util;

import com.github.luomingxuorg.javautil.exception.SortException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * 通过反射获取类的字段值
 */
public class FieldUtil
{

    /**
     * 直接通过反射来获取这个字段的值和类名--比较快
     *
     * @param param     entity
     * @param fieldName field name which you need its value
     * @return field value
     * @throws IllegalAccessException
     * @throws SortException
     */
    public static Object getObjectByFieldName(Object param, String fieldName) throws Exception
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
    public static Object doFieldGetMethod(Object param, String fieldName) throws Exception
    {
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

                return obj; //找到get方法直接return, 不再循环
            }
        }

        throw new SortException(String.format("\"%s\"--No such field in this param!", fieldName));
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
    public static <T> Object typeConvert(Class<T> destinClass, Object sourceObj) throws Exception
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

            throw new Exception(String.format("Can not convert '%s' to '%s'", sourceObj.getClass(), destinClass));
        }
        catch (Exception e)
        {
            throw new Exception(String.format("Can not convert '%s' to '%s'", sourceObj.getClass(), destinClass));
        }
    }
}
