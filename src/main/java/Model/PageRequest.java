/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : PageRequest.java
 * CreateTime: 2018/07/22 18:45:16
 * LastModifiedDate : 18-7-22 下午6:35
 */

package Model;

import Annotation.SortDefault;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 定义分页需求的类
 */
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

    public Integer getSize()
    {
        return size;
    }

    public void setSize(Integer size)
    {
        this.size = size;
    }

    public Integer getPage()
    {
        return page;
    }

    public void setPage(Integer page)
    {
        this.page = page;
    }

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

    public void setSort(Sort sort)
    {
        this.sort = sort;
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
