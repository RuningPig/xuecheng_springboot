package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 抛出异常
 */
public class ExceptionCast {

    /**
     * 使用此静态方法抛出自定义异常
     * @param resultCode
     */
    public static void cast(ResultCode resultCode){
        throw new CustomerException(resultCode);
    }

}
