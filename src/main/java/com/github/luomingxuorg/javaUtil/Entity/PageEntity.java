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
 * File Name : PageEntity.java
 * Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javaUtil.Entity;

import java.util.List;

public class PageEntity<T>
{
    public PageEntity() {}

    public PageEntity(Integer totalPages, Integer totalElements, Integer pageOfElements, Integer size, Integer page, List<T> content, boolean empty)
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
    private Integer totalElements;
    /**
     * 当前页数据数
     */
    private Integer pageOfElements;
    /**
     * 页内数据默认大小
     */
    private Integer size;
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

        return "PageEntity{" +
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
