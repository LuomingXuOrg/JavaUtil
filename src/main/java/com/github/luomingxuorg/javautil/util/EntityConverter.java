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
 *  File Name : EntityConverter.java
 *  Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javautil.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * do, entity, dto. 转化<br>
 * 会同时转化父类里面的字段
 * 不转化static的字段
 *
 * //@deprecated 建议使用cglib https://github.com/cglib/cglib
 * 重出江湖, cglib在直接由父类对子类进行设值的地方不行
 */
public class EntityConverter
{
    public static <T> T convert(T destin, Object source)
    {
        if (source == null) { return destin; }

        Field[] fieldsSource = getAllFields(source.getClass());
        Field[] fieldsDestin = getAllFields(destin.getClass());

        Map<String, Field> destMap = Arrays.stream(fieldsDestin)
                .filter(item-> !isStatic(item))
                .collect(Collectors.toMap(Field::getName, item -> item, (old, fresh) -> old));

        for (Field fieldSource : fieldsSource)
        {
            fieldSource.setAccessible(true);

            //如果这个field是静态的, 不与目标类型进行匹配
            if (isStatic(fieldSource))
            { continue; }

            try
            {
                Object sourceValue = fieldSource.get(source);
                if (sourceValue == null)
                { continue; }

                Field fieldDestin = destMap.get(fieldSource.getName());
                if (fieldDestin == null)
                { continue; }

                fieldDestin.setAccessible(true);

                if (fieldDestin.getType().equals(fieldSource.getType()))
                {
                    fieldDestin.set(destin, sourceValue);
                }
                else
                {
                    //同名属性, 不同类型, 尝试进行类型转化
                    try
                    {
                        fieldDestin.set(destin, FieldUtil.typeConvert(fieldDestin.getType(), sourceValue));
                    }
                    catch (Exception e)
                    {
                        //转化失败的话, 设值为null
                        fieldDestin.set(destin, null);
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
        if (source == null || source.size() < 1) { return null; }

        List<T> lists = new ArrayList<>(source.size());
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

    private static Boolean isStatic(Field field)
    {
        /*
         * Modifier这个类就是用来判断public, private, abstract之类的信息的
         */
        return Modifier.isStatic(field.getModifiers());
    }
}
