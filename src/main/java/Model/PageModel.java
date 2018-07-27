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

    public PageModel(Integer totalPages, Integer totalElements, Integer pageOfElements, Integer size, Integer page, List<T> content, boolean empty)
    {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageOfElements = pageOfElements;
        this.size = size;
        this.page = page;
        this.content = content;
        this.empty = empty;
    }

    /**
     * 总页数--从1开始
     */
    private Integer totalPages;
    /**
     * 总数据数
     */
    private Integer totalElements;//
    /**
     * 当前页数据数
     */
    private Integer pageOfElements;
    /**
     * 页内数据默认大小
     */
    private Integer size;//
    /**
     * 当前页码--从1开始
     */
    private Integer page;
    /**
     * 包含的list数据
     */
    private List<T> content;
    /**
     * 是否为空, 默认为false
     */
    private boolean empty = false;

    public void setPageOfElements_Content_TotalPages(Integer pageOfElements, List<T> content, Integer totalPages)
    {
        this.pageOfElements = pageOfElements;
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

    public Integer getPageOfElements()
    {
        return pageOfElements;
    }

    public void setPageOfElements(Integer pageOfElements)
    {
        this.pageOfElements = pageOfElements;
    }

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
        StringBuilder temp = new StringBuilder("\ncontent[");

        if (content != null && content.size() > 0)
        {
            for (Object item : content)
            {
                temp.append(item.toString()).append("\n");
            }
        }
        else
        {
            temp.append("null");
        }

        temp.append("]");

        return "PageModel{" +
                "totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", pageOfElements=" + pageOfElements +
                ", size=" + size +
                ", page=" + page +
                ", empty=" + empty +
                temp +
                '}';
    }
}
