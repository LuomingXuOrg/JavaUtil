/*
 *           Copyright 2018-2020 LuomingXu
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
 * File Name : SortException.java
 * Repo: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javautil.exception;

public class SortException extends Exception
{
    public SortException()
    {
        super();
    }

    public SortException(String message)
    {
        super(message);
    }

    // 用指定的详细信息和原因构造一个新的异常
    public SortException(String message, Throwable cause)
    {
        super(message, cause);
    }

    // 用指定原因构造一个新的异常
    public SortException(Throwable cause)
    {
        super(cause);
    }
}
