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

import lombok.Data;

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
    private Integer page = 1;

    private Sort sort;
}
