package com.youxin.yxlib.supper.http.response;

import java.io.Serializable;

/**
 * Created by wb.zhuzichu18 on 2018/9/6.
 */
public class BaseResponse<T> implements Serializable {
    public int code;
    public T data;
    public String msg;
    public String requestId;

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
