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
 * File Name : PageRequest.java
 * Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javaUtil.Entity;

import com.github.luomingxuorg.javaUtil.Annotation.SortDefault;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * 定义分页需求的类
 */
@Data
public class PageRequest
{
    public PageRequest() {}

    public PageRequest(Integer size, Integer page, Sort sort)
    {
        this.size = size;
        this.page = page;
        this.sort = sort;
    }

    //一页里面的数据的大小
    private Integer size = 10;

    //第几页
    private Integer page = 0;

    @SortDefault(fieldName = "123", Direction = Sort.Direction.DESC)
    private Sort sort;

    public Sort getSort()
    {
        if (sort == null)
        {
            try
            {
                Field field = this.getClass().getDeclaredField("sort");
                SortDefault temp = field.getAnnotation(SortDefault.class);
                if (temp != null)
                {
                    sort = new Sort(temp.fieldName(), temp.Direction());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return sort;
    }

    @Override
    public String toString()
    {
        return "PageRequest{" +
                "size=" + size +
                ", page=" + page +
                ", sort=" + sort +
                '}';
    }
}
