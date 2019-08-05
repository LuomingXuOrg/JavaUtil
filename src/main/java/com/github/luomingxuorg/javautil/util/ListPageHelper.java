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
 * File Name : ListPageHelper.java
 * Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javautil.util;

import com.github.luomingxuorg.javautil.entity.PageEntity;
import com.github.luomingxuorg.javautil.entity.PageRequest;
import com.github.luomingxuorg.javautil.exception.SortException;

import java.util.ArrayList;
import java.util.List;

/**
 * 对list进行分页操作
 */
public class ListPageHelper
{
    //设置最大的size和page, 可以修改
    @Deprecated
    private static Integer maxSize = 100;
    @Deprecated
    private static Integer maxPage = 100;

    /**
     * 采用list.subList()来进行分页, 简单的测试了一下, 好像速度没有太大的区别2333
     * 就先这样把...
     *
     * @param pageRequest 对分页的需求类
     * @param paramList  需要分页的List
     * @param <T>         List的Model
     * @return PageEntity
     */
    public static <T> PageEntity<T> doPage(PageRequest pageRequest, List<T> paramList)
    {
        //内部需要的类
        PageEntity<T> pageEntity = new PageEntity<>();

        //对paramLists进行判断
        if (paramList == null || paramList.size() < 1)
        {
            System.err.println("No data in lists. ");
            pageEntity.setEmpty(true);
            return pageEntity;
        }
        //对pageRequest进行判断
        if (pageRequest == null || (pageRequest.getSize() == null | pageRequest.getPage() == null))
        {
            System.err.println("No data in pageRequest. ");
            pageEntity.setEmpty(true);
            return pageEntity;
        }

        Integer size = pageRequest.getSize();
        Integer page = pageRequest.getPage();
        Integer listSize = paramList.size();

        //设置总数据数
        pageEntity.setTotalElements(Long.valueOf(listSize));
        //页内数据默认大小
        pageEntity.setSize(size);
        //当前页码
        pageEntity.setPage(page);
        //总页数
        pageEntity.setTotalPages((int) Math.ceil(listSize.doubleValue() / size.doubleValue()));

        //进行分页
        Integer startIndex = (page - 1) * size;
        Integer endIndex = startIndex + size;

        Integer pageElements = size;

        if (startIndex > listSize)
        {
            pageEntity.setEmpty(true);
            pageEntity.setPageOfElements(0);
            return pageEntity;
        }

        if (endIndex > listSize)
        {
            endIndex = listSize;
            pageElements = endIndex - startIndex;
        }

        if (startIndex.equals(endIndex))
        {
            pageEntity.setEmpty(true);
            pageEntity.setPageOfElements(0);
            return pageEntity;
        }

        pageEntity.setPageOfElements(pageElements);

        //sort
        if (pageRequest.getSort() != null)
        {
            try
            {
                SortUtil.doSort(pageRequest.getSort(), paramList);
            }
            catch (SortException e)
            {
                e.printStackTrace();
            }
        }

        pageEntity.setContent(paramList.subList(startIndex, endIndex));

        return pageEntity;
    }

    /**
     * 对传来的List进行分页的操作
     *
     * @param pageRequest 对分页的需求类
     * @param paramList  需要分页的List
     * @param <T>         List的Model
     * @return PageEntity
     */
    @Deprecated
    public static <T> PageEntity<T> doPage(PageRequest pageRequest, List<T> paramList, Integer... param)
    {
        setMaxSizePage(param);

        //内部需要的类
        List<T> pagedLists = new ArrayList<>();
        PageEntity<T> pageEntity = new PageEntity<>();

        //对paramLists进行判断
        if (paramList == null || paramList.size() < 1)
        {
            System.err.println("No data in lists. ");
            pageEntity.setEmpty(true);
            return pageEntity;
        }
        //对pageRequest进行判断
        if (pageRequest == null || (pageRequest.getSize() == null | pageRequest.getPage() == null))
        {
            System.err.println("No data in pageRequest. ");
            pageEntity.setEmpty(true);
            return pageEntity;
        }

        //sort
        if (pageRequest.getSort() != null)
        {
            try
            {
                SortUtil.doSort(pageRequest.getSort(), paramList);
            }
            catch (SortException e)
            {
                e.printStackTrace();
            }
        }

        Integer size = pageRequest.getSize();
        Integer page = pageRequest.getPage();

        //若是传来的数据不对, 则默认为20, 1
        if ((size < 1 | size > maxSize) || (page < 1 | page > maxPage))
        {
            size = 20;
            page = 1;
        }

        //设置总数据数
        pageEntity.setTotalElements((long) paramList.size());
        //页内数据默认大小
        pageEntity.setSize(size);
        //当前页码
        pageEntity.setPage(page);

        //需要的数量大于总的数据量
        if (size >= paramList.size())
        {
            //且page > 1时, 就没有数据
            if (page > 1)
            {
                pageEntity.setEmpty(true);
                return pageEntity;
            }

            pageEntity.setPageOfElements_Content_TotalPages(paramList.size(), paramList, 1);
            return pageEntity;
        }

        int count = 0;
        try
        {
            for (int i = size * (page - 1); i < size * page; i++)
            {
                pagedLists.add(paramList.get(i));
                //添加成功之后再计数
                count++;
            }
        }
        catch (Exception e)
        {
            System.err.println("List index out of range. ");
            System.err.println(String.format("We count \'%s\' data in this page.", count));
            if (count == 0)
            {
                pageEntity.setEmpty(true);
                pageEntity.setTotalPages((paramList.size() / size) + 1);
                return pageEntity;
            }
            else
            {
                pageEntity.setPageOfElements_Content_TotalPages(count, pagedLists, (paramList.size() / size) + 1);
            }
            return pageEntity;
        }

        pageEntity.setPageOfElements_Content_TotalPages(count, pagedLists, (paramList.size() / size) + 1);

        return pageEntity;
    }

    /**
     * set max size page
     *
     * @param param first is size, second is page
     */
    @Deprecated
    private static void setMaxSizePage(Integer... param)
    {
        Integer[] temp = param.clone();
        try
        {
            maxSize = temp[0];
            maxPage = temp[1];
        }
        catch (Exception e)
        { /*try set value*/}
    }
}
