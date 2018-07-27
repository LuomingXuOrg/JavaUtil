/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : ListPageHelperUtil.java
 * CreateTime: 2018/07/22 18:43:55
 * LastModifiedDate : 18-7-22 下午6:36
 */

package Util;

import Model.PageModel;
import Model.PageRequest;
import Exception.SortException;

import java.util.*;

public class ListPageHelperUtil
{
    //这边设置了最大的size和page, 可以修改
    private final static Integer maxSize = 50;
    private final static Integer maxPage = 50;

    /**
     * 对传来的List进行分页的操作
     *
     * @param pageRequest 对分页的需求类
     * @param paramLists  需要分页的List
     * @param <T>         List的Model
     * @return PageModel
     */
    public static <T> PageModel<T> doPage(PageRequest pageRequest, List<T> paramLists)
    {
        //内部需要的类
        List<T> pagedLists = new ArrayList<>();
        PageModel<T> pageModel = new PageModel<>();

        //对paramLists进行判断
        if (paramLists == null || paramLists.size() < 1)
        {
            System.err.println("No data in lists. ");
            pageModel.setEmpty(true);
            return pageModel;
        }
        //对pageRequest进行判断
        if (pageRequest == null || (pageRequest.getSize() == null | pageRequest.getPage() == null))
        {
            System.err.println("No data in pageRequest. ");
            pageModel.setEmpty(true);
            return pageModel;
        }

        //sort
        try
        {
            SortUtil.doSort(pageRequest.getSort(), paramLists);
        }
        catch (SortException e)
        {
            e.printStackTrace();
        }

        Integer size = pageRequest.getSize();
        Integer page = pageRequest.getPage() + 1;

        //若是传来的数据不对, 则默认为20, 1
        if ((size < 1 | size > maxSize) || (page > maxPage))
        {
            size = 20;
            page = 1;
        }

        //设置总数据数
        pageModel.setTotalElements(paramLists.size());
        //页内数据默认大小
        pageModel.setSize(size);
        //当前页数
        pageModel.setNumber(page - 1);

        //需要的数量大于总的数据量
        if (size >= paramLists.size())
        {
            //且page > 1时, 就没有数据
            if (page > 1)
            {
                pageModel.setEmpty(true);
                return pageModel;
            }

            pageModel.setNumberOfElements_Content_TotalPages(paramLists.size(), paramLists, 1);
            return pageModel;
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
            System.err.println(String.format("We count \'%s\' data in paramLists", count));
            if (count == 0)
            {
                pageModel.setEmpty(true);
                return pageModel;
            }
            else
            {
                pageModel.setNumberOfElements_Content_TotalPages(count, pagedLists, paramLists.size() / size);
            }
            return pageModel;
        }

        pageModel.setNumberOfElements_Content_TotalPages(count, pagedLists, paramLists.size() / size);

        return pageModel;
    }
}
