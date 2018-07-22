/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : PageRequest.java
 * CreateTime: 2018/07/22 18:45:16
 * LastModifiedDate : 18-7-22 下午6:35
 */

package Model;

public class PageRequest
{
    private Integer size;
    private Integer page;

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

    @Override
    public String toString()
    {
        return "PageRequest{" +
                "size=" + size +
                ", page=" + page +
                '}';
    }
}
