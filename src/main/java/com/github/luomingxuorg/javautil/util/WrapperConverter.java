/*
 *  Copyright 2018-2019 LuomingXu
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
 * File Name : WrapperConverter.java
 * Repo: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javautil.util;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将{@link BeanCopier#create(Class, Class, boolean)}的copier存储起来, 以减少创建的耗时
 */
@Slf4j
public class WrapperConverter
{
    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    private static final Map<String, ConstructorAccess> CONSTRUCTOR_ACCESS_CACHE = new ConcurrentHashMap<>();

    /**
     * 拷贝对象
     *
     * @param source       源对象
     * @param target       目标对象
     * @param useConverter 是否使用自定义的拷贝接口
     * @param converter    自定义拷贝接口
     */
    public static void copy(Object source, Object target, @NonNull boolean useConverter, Converter converter)
    {
        if (useConverter && converter == null)
        {
            log.error("Converter can not be NULL when useConverter is true");
            return;
        }

        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass(), useConverter);
        copier.copy(source, target, converter);
    }

    /**
     * 拷贝list
     *
     * @param sourceList   源list
     * @param targetClass  目标Class
     * @param useConverter 是否使用自定义的拷贝接口
     * @param converter    自定义拷贝接口
     * @param <S>          源
     * @param <T>          目标
     * @return 目标list
     */
    public static <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass, @NonNull boolean useConverter, Converter converter)
    {
        if (sourceList == null || sourceList.isEmpty())
        {
            return Collections.emptyList();
        }

        if (useConverter && converter == null)
        {
            log.error("Converter can not be NULL when useConverter is true");
            return Collections.emptyList();
        }

        ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
        BeanCopier copier = getBeanCopier(sourceList.get(0).getClass(), targetClass, useConverter);

        List<T> resultList = new LinkedList<>();

        try
        {
            for (S source : sourceList)
            {
                T target;

                target = constructorAccess.newInstance();
                copier.copy(source, target, converter);
                resultList.add(target);
            }
        }
        catch (Exception e)
        {
            log.error("Copy list {} failed", targetClass);
            e.printStackTrace();
            return Collections.emptyList();
        }

        return resultList;
    }

    private static BeanCopier getBeanCopier(Class sourceClass, Class targetClass, boolean useConverter)
    {
        String beanKey = sourceClass.getName() +
                "-" +
                targetClass.getName() +
                "-" +
                useConverter;

        BeanCopier copier = BEAN_COPIER_CACHE.get(beanKey);
        if (copier == null)
        {
            copier = BeanCopier.create(sourceClass, targetClass, useConverter);
            BEAN_COPIER_CACHE.put(beanKey, copier);

            return copier;
        }

        return copier;
    }

    @SuppressWarnings("unchecked")
    private static <T> ConstructorAccess<T> getConstructorAccess(Class<T> targetClass)
    {
        ConstructorAccess<T> constructorAccess = CONSTRUCTOR_ACCESS_CACHE.get(targetClass.getName());
        if (constructorAccess != null)
        {
            return constructorAccess;
        }

        try
        {
            constructorAccess = ConstructorAccess.get(targetClass);
            //对创建construct进行检验, 通过再存入cache中
            constructorAccess.newInstance();
            CONSTRUCTOR_ACCESS_CACHE.put(targetClass.getName(), constructorAccess);
        }
        catch (Exception e)
        {
            log.error("Create new instance of {} failed, message: {}", targetClass, e.getClass().getName());
            e.printStackTrace();
            throw e;
        }

        return constructorAccess;
    }
}
