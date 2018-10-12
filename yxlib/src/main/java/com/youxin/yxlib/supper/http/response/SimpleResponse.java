package com.youxin.yxlib.supper.http.response;

import java.io.Serializable;

/**
 * Created by wb.zhuzichu18 on 2018/9/6.
 */
public class SimpleResponse implements Serializable {
    public int code;
    public String msg;
    public String requestId;

    public BaseResponse toBaseResponse() {
        BaseResponse baseResponse = new BaseResponse<>();
        baseResponse.code = code;
        baseResponse.msg = msg;
        baseResponse.requestId = requestId;
        return baseResponse;
    }
}
