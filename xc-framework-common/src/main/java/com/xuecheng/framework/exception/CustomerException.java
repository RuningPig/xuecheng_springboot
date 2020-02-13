package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 可预知异常处理，自定义异常类
 */
public class CustomerException extends RuntimeException {

    private ResultCode resultCode;

    public CustomerException(ResultCode resultCode){
        super("错误代码：" + resultCode.code() + "错误信息：" + resultCode.message());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode(){
        return this.resultCode;
    }
}
