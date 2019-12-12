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
 * File Name : SingleConverter.java
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

/**
 * 没有缓存{@link BeanCopier#create(Class, Class, boolean)}
 */
@Slf4j
public class SingleConverter
{
    public static void copy(Object source, Object target, @NonNull boolean useConverter, Converter converter)
    {
        if (useConverter && converter == null)
        {
            log.error("Converter can not be NULL when useConverter is true");
            return;
        }

        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), useConverter);
        copier.copy(source, target, converter);
    }

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

        ConstructorAccess<T> constructorAccess = ConstructorAccess.get(targetClass);
        BeanCopier copier = BeanCopier.create(sourceList.get(0).getClass(), targetClass, useConverter);

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
}
