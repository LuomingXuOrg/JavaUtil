/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : PageModel.java
 * CreateTime: 2018/07/22 18:44:22
 * LastModifiedDate : 18-7-22 下午6:33
 */

package Model;

import java.util.List;

public class PageModel<T>
{
    public PageModel() {}

    public PageModel(Integer totalPages, Integer totalElements, Integer numberOfElements, Integer size, Integer number, List<T> content, boolean empty)
    {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.numberOfElements = numberOfElements;
        this.size = size;
        this.number = number;
        this.content = content;
        this.empty = empty;
    }

    /**
     * 总页数
     */
    private Integer totalPages;
    /**
     * 总数据数
     */
    private Integer totalElements;//
    /**
     * 当前页数据数
     */
    private Integer numberOfElements;
    /**
     * 页内数据默认大小
     */
    private Integer size;//
    /**
     * 当前页码
     */
    private Integer number;
    /**
     * 包含的list数据
     */
    private List<T> content;
    /**
     * 是否为空
     */
    private boolean empty = false;

    public void setNumberOfElements_Content_TotalPages(Integer numberOfElements, List<T> content, Integer totalPages)
    {
        this.numberOfElements = numberOfElements;
        this.content = content;
        this.totalPages = totalPages;
    }

    public Integer getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages)
    {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements()
    {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements)
    {
        this.totalElements = totalElements;
    }

    public Integer getNumberOfElements()
    {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements)
    {
        this.numberOfElements = numberOfElements;
    }

    public Integer getSize()
    {
        return size;
    }

    public void setSize(Integer size)
    {
        this.size = size;
    }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }

    public List<T> getContent()
    {
        if (content == null)
        {
            return null;
        }
        return content;
    }

    public void setContent(List<T> content)
    {
        this.content = content;
    }

    public boolean isEmpty()
    {
        return empty;
    }

    public void setEmpty(boolean empty)
    {
        this.empty = empty;
    }

    @Override
    public String toString()
    {
        return "PageModel{" +
                "totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", numberOfElements=" + numberOfElements +
                ", size=" + size +
                ", number=" + number +
                ", content=" + content +
                ", empty=" + empty +
                '}';
    }
}
