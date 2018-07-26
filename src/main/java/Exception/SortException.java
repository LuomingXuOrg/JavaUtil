/*
 * Copyright (c) 2018
 * Author : Luoming Xu
 * Project Name : OwnJavaUtil
 * File Name : SortException.java
 * CreateTime: 2018/07/26 17:13:13
 * LastModifiedDate : 18-7-26 下午5:13
 */

package Exception;

public class SortException extends Exception
{
    public SortException(){

        super();
    }

    public SortException(String message){
        super(message);

    }

    // 用指定的详细信息和原因构造一个新的异常
    public SortException(String message, Throwable cause){

        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public SortException(Throwable cause) {

        super(cause);
    }
}
