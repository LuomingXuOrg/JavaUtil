/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : PageRequest.java
 * CreateTime: 2018/07/22 18:45:16
 * LastModifiedDate : 18-7-22 下午6:35
 */

package Model;

import Annotation.ListSortDefault;

public class PageRequest
{
    private Integer size = 10;
    private Integer page = 0;

    @ListSortDefault
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
