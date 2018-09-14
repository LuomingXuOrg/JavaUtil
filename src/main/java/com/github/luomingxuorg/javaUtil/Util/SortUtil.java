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
 * File Name : SortUtil.java
 * Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javaUtil.Util;

import com.github.luomingxuorg.javaUtil.Entity.Sort;
import com.github.luomingxuorg.javaUtil.Exception.SortException;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 对 {@link List}{@code <T>} 按照T中的某一个字段名进行排序
 * <br>
 * <font color="red">支持的字段类型: </font>
 * <li>{@link Short}</li>
 * <li>{@link Integer}</li>
 * <li>{@link Long}</li>
 * <li>{@link Float}</li>
 * <li>{@link Double}</li>
 * <li>{@link BigDecimal}</li>
 * <li>{@link Date}</li>
 */
public class SortUtil
{
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

        return compare(FieldUtil.getObjectByFieldName(paramOne, fieldName), FieldUtil.getObjectByFieldName(paramTwo, fieldName), direction);
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
    @SuppressWarnings("unchecked")
    private static Integer compare(Object one, Object two, Sort.Direction direction) throws Exception
    {
        Method method;
        Class clazz = one.getClass();
        if (clazz.equals(Integer.class) || clazz.equals(Double.class)
                || clazz.equals(Long.class) || clazz.equals(Short.class)
                || clazz.equals(Float.class) || clazz.equals(Date.class))
        {
            method = clazz.getMethod("compareTo", one.getClass());
            return compare(method, one, two, direction);
        }
        //special handle on BigDecimal class
        else if (clazz.equals(BigDecimal.class))
        {
            method = BigDecimal.class.getMethod("doubleValue");
            Double temp0 = (Double) method.invoke(one);
            Double temp1 = (Double) method.invoke(two);
            method = Double.class.getMethod("compareTo", Double.class);
            return compare(method, temp0, temp1, direction);
        }
        else { throw new Exception("Can not compare the two param's class. ");}
    }

    /**
     * do method
     *
     * @param method    "compareTo" method
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
