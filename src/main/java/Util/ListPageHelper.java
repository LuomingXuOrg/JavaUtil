/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : ListPageHelper.java
 * CreateTime: 2018/07/22 18:43:55
 * LastModifiedDate : 18-7-22 下午6:36
 */

package Util;

import Model.PageEntity;
import Model.PageRequest;
import Exception.SortException;

import java.util.*;

/**
 * 对list进行分页操作
 */
public class ListPageHelper
{
    //设置最大的size和page, 可以修改
    private static Integer maxSize = 100;
    private static Integer maxPage = 100;

    /**
     * 对传来的List进行分页的操作
     *
     * @param pageRequest 对分页的需求类
     * @param paramLists  需要分页的List
     * @param <T>         List的Model
     * @return PageEntity
     */
    public static <T> PageEntity<T> doPage(PageRequest pageRequest, List<T> paramLists, Integer... param)
    {
        setMaxSizePage(param);

        //内部需要的类
        List<T> pagedLists = new ArrayList<>();
        PageEntity<T> pageEntity = new PageEntity<>();

        //对paramLists进行判断
        if (paramLists == null || paramLists.size() < 1)
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
                SortUtil.doSort(pageRequest.getSort(), paramLists);
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
        pageEntity.setTotalElements(paramLists.size());
        //页内数据默认大小
        pageEntity.setSize(size);
        //当前页码
        pageEntity.setPage(page);

        //需要的数量大于总的数据量
        if (size >= paramLists.size())
        {
            //且page > 1时, 就没有数据
            if (page > 1)
            {
                pageEntity.setEmpty(true);
                return pageEntity;
            }

            pageEntity.setPageOfElements_Content_TotalPages(paramLists.size(), paramLists, 1);
            return pageEntity;
        }

        int count = 0;
        try
        {
            for (int i = size * (page - 1); i < size * page; i++)
            {
                pagedLists.add(paramLists.get(i));
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
                pageEntity.setTotalPages((paramLists.size() / size) + 1);
                return pageEntity;
            }
            else
            {
                pageEntity.setPageOfElements_Content_TotalPages(count, pagedLists, (paramLists.size() / size) + 1);
            }
            return pageEntity;
        }

        pageEntity.setPageOfElements_Content_TotalPages(count, pagedLists, (paramLists.size() / size) + 1);

        return pageEntity;
    }

    /**
     * set max size page
     *
     * @param param first is size, second is page
     */
    private static void setMaxSizePage(Integer... param)
    {
        Integer[] temp = param.clone();
        try
        {
            maxSize = temp[0];
            maxPage = temp[1];
        }
        catch(Exception e) { /*try set value*/}
    }
}
