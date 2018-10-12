package com.youxin.yxlib.supper.http.exception;


import com.youxin.yxlib.supper.http.response.BaseResponse;

/**
 * Created by wb.zhuzichu18 on 2018/9/10.
 */
public class ErrorException extends Exception {
    BaseResponse errorResponse;

    public ErrorException(BaseResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public BaseResponse getResponse() {
        return errorResponse;
    }

    public void setResponse(BaseResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
