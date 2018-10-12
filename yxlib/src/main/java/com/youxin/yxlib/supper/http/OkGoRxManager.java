package com.youxin.yxlib.supper.http;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.DeleteRequest;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.HeadRequest;
import com.lzy.okgo.request.OptionsRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;
import com.lzy.okgo.request.TraceRequest;
import com.lzy.okrx2.adapter.CompletableResponse;
import com.lzy.okrx2.adapter.FlowableResponse;
import com.lzy.okrx2.adapter.MaybeResponse;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.lzy.okrx2.adapter.SingleResponse;
import com.youxin.yxlib.supper.http.convert.JsonConvert;
import com.youxin.yxlib.supper.http.response.BaseResponse;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.OkHttpClient;

/**
 * OKgo管理器
 * Created by huangkangfa on 2018/9/21.
 */
public class OkGoRxManager {
    private static Application APPLICATION;

    private final static String HTTPLOGGINGINTERCEPTORNAME = "okgo";

    public static int READTIMEOUT = 6000;                                   //全局的读取超时时间
    public static int WRITETIMEOUT = 6000;                                  //全局的写入超时时间
    public static int CONNECTTIMEOUT = 6000;                                //全局的连接超时时间
    public static CacheMode CACHEMODE = CacheMode.NO_CACHE;                 //全局统一缓存模式，默认不使用缓存
    public static long CACHEENTITY = CacheEntity.CACHE_NEVER_EXPIRE;        //全局统一缓存时间，默认永不过期
    public static int RETRYCOUNT = 1;                                       //全局统一超时重连次数，默认为1次，那么最差的情况会请求2次(一次原始请求，1次重连请求)，不需要可以设置为0

    private OkGoRxManager() {
    }

    /**
     * 初始化okgo管理器
     *
     * @param application
     */
    public static void init(Application application) {
        APPLICATION=application;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(HTTPLOGGINGINTERCEPTORNAME);
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //超时时间设置，默认60秒
        builder.readTimeout(READTIMEOUT, TimeUnit.MILLISECONDS);            //全局的读取超时时间
        builder.writeTimeout(WRITETIMEOUT, TimeUnit.MILLISECONDS);          //全局的写入超时时间
        builder.connectTimeout(CONNECTTIMEOUT, TimeUnit.MILLISECONDS);      //全局的连接超时时间

        builder.cookieJar(new CookieJarImpl(new DBCookieStore(APPLICATION)));              //使用数据库保持cookie，如果cookie不过期，则一直有效

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        //HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        //builder.hostnameVerifier(new SafeHostnameVerifier());
        init(application, builder.build());
    }

    /**
     * 初始化okgo管理器
     *
     * @param application
     * @param mOkHttpClient
     */
    public static void init(Application application, OkHttpClient mOkHttpClient) {
        APPLICATION = application;
        HttpHeaders headers = new HttpHeaders();
        //添加全局头   header不支持中文，不允许有特殊字符
        HttpParams params = new HttpParams();
        //添加全局参数 param支持中文,直接传,不要自己编码

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(APPLICATION)                    //必须调用初始化
                .setOkHttpClient(mOkHttpClient)               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CACHEMODE)                       //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CACHEENTITY)                     //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(RETRYCOUNT)                      //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
    }

    /**
     * options请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Observable
     */
    public static <T> Observable<Response<BaseResponse<T>>> optionsObservable(String url, Map<String, String> headers, Map<String, Object> params) {
        OptionsRequest<BaseResponse<T>> mOptionsRequest = getOptionsRequest(url, headers, params);
        return mOptionsRequest.adapt(new ObservableResponse<BaseResponse<T>>());
    }

    /**
     * options请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Flowable
     */
    public static <T> Flowable<Response<BaseResponse<T>>> optionsFlowable(String url, Map<String, String> headers, Map<String, Object> params) {
        OptionsRequest<BaseResponse<T>> mOptionsRequest = getOptionsRequest(url, headers, params);
        return mOptionsRequest.adapt(new FlowableResponse<BaseResponse<T>>());
    }

    /**
     * options请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Maybe
     */
    public static <T> Maybe<Response<BaseResponse<T>>> optionsMaybe(String url, Map<String, String> headers, Map<String, Object> params) {
        OptionsRequest<BaseResponse<T>> mOptionsRequest = getOptionsRequest(url, headers, params);
        return mOptionsRequest.adapt(new MaybeResponse<BaseResponse<T>>());
    }

    /**
     * options请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Single
     */
    public static <T> Single<Response<BaseResponse<T>>> optionsSingle(String url, Map<String, String> headers, Map<String, Object> params) {
        OptionsRequest<BaseResponse<T>> mOptionsRequest = getOptionsRequest(url, headers, params);
        return mOptionsRequest.adapt(new SingleResponse<BaseResponse<T>>());
    }

    /**
     * options请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Completable
     */
    public static <T> Completable optionsCompletable(String url, Map<String, String> headers, Map<String, Object> params) {
        OptionsRequest<BaseResponse<T>> mOptionsRequest = getOptionsRequest(url, headers, params);
        return mOptionsRequest.adapt(new CompletableResponse<BaseResponse<T>>());
    }

    /**
     * 获取OptionsRequest
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return OptionsRequest
     */
    private static <T> OptionsRequest<BaseResponse<T>> getOptionsRequest(String url, Map<String, String> headers, Map<String, Object> params) {
        checkInit();
        return OkGo.<BaseResponse<T>>options(url)
                .headers(getHttpHeaders(headers))
                .params(getHttpParams(params))
                .converter(new JsonConvert<BaseResponse<T>>());
    }

    /**
     * head请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Observable
     */
    public static <T> Observable<Response<BaseResponse<T>>> headObservable(String url, Map<String, String> headers, Map<String, Object> params) {
        HeadRequest<BaseResponse<T>> mHeadRequest = getHeadRequest(url, headers, params);
        return mHeadRequest.adapt(new ObservableResponse<BaseResponse<T>>());
    }

    /**
     * head请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Flowable
     */
    public static <T> Flowable<Response<BaseResponse<T>>> headFlowable(String url, Map<String, String> headers, Map<String, Object> params) {
        HeadRequest<BaseResponse<T>> mHeadRequest = getHeadRequest(url, headers, params);
        return mHeadRequest.adapt(new FlowableResponse<BaseResponse<T>>());
    }

    /**
     * head请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Maybe
     */
    public static <T> Maybe<Response<BaseResponse<T>>> headMaybe(String url, Map<String, String> headers, Map<String, Object> params) {
        HeadRequest<BaseResponse<T>> mHeadRequest = getHeadRequest(url, headers, params);
        return mHeadRequest.adapt(new MaybeResponse<BaseResponse<T>>());
    }

    /**
     * head请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Single
     */
    public static <T> Single<Response<BaseResponse<T>>> headSingle(String url, Map<String, String> headers, Map<String, Object> params) {
        HeadRequest<BaseResponse<T>> mHeadRequest = getHeadRequest(url, headers, params);
        return mHeadRequest.adapt(new SingleResponse<BaseResponse<T>>());
    }

    /**
     * head请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Completable
     */
    public static <T> Completable headCompletable(String url, Map<String, String> headers, Map<String, Object> params) {
        HeadRequest<BaseResponse<T>> mHeadRequest = getHeadRequest(url, headers, params);
        return mHeadRequest.adapt(new CompletableResponse<BaseResponse<T>>());
    }

    /**
     * 获取HeadRequest
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return 获取HeadRequest
     */
    private static <T> HeadRequest<BaseResponse<T>> getHeadRequest(String url, Map<String, String> headers, Map<String, Object> params) {
        checkInit();
        return OkGo.<BaseResponse<T>>head(url)
                .headers(getHttpHeaders(headers))
                .params(getHttpParams(params))
                .converter(new JsonConvert<BaseResponse<T>>());
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Observable
     */
    public static <T> Observable<Response<BaseResponse<T>>> getObservable(String url, Map<String, String> headers, Map<String, Object> params) {
        GetRequest<BaseResponse<T>> mGetRequest = getGetRequest(url, headers, params);
        return mGetRequest.adapt(new ObservableResponse<BaseResponse<T>>());
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Flowable
     */
    public static <T> Flowable<Response<BaseResponse<T>>> getFlowable(String url, Map<String, String> headers, Map<String, Object> params) {
        GetRequest<BaseResponse<T>> mGetRequest = getGetRequest(url, headers, params);
        return mGetRequest.adapt(new FlowableResponse<BaseResponse<T>>());
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Maybe
     */
    public static <T> Maybe<Response<BaseResponse<T>>> getMaybe(String url, Map<String, String> headers, Map<String, Object> params) {
        GetRequest<BaseResponse<T>> mGetRequest = getGetRequest(url, headers, params);
        return mGetRequest.adapt(new MaybeResponse<BaseResponse<T>>());
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Single
     */
    public static <T> Single<Response<BaseResponse<T>>> getSingle(String url, Map<String, String> headers, Map<String, Object> params) {
        GetRequest<BaseResponse<T>> mGetRequest = getGetRequest(url, headers, params);
        return mGetRequest.adapt(new SingleResponse<BaseResponse<T>>());
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Completable
     */
    public static <T> Completable getCompletable(String url, Map<String, String> headers, Map<String, Object> params) {
        GetRequest<BaseResponse<T>> mGetRequest = getGetRequest(url, headers, params);
        return mGetRequest.adapt(new CompletableResponse<BaseResponse<T>>());
    }

    /**
     * 获取GetRequest
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return GetRequest
     */
    private static <T> GetRequest<BaseResponse<T>> getGetRequest(String url, Map<String, String> headers, Map<String, Object> params) {
        checkInit();
        return OkGo.<BaseResponse<T>>get(url)
                .headers(getHttpHeaders(headers))
                .params(getHttpParams(params))
                .converter(new JsonConvert<BaseResponse<T>>());
    }

    /**
     * post请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Observable
     */
    public static <T> Observable<Response<BaseResponse<T>>> postObservable(String url, Map<String, String> headers, Map<String, Object> params) {
        PostRequest<BaseResponse<T>> mPostRequest = getPostRequest(url, headers, params);
        return mPostRequest.adapt(new ObservableResponse<BaseResponse<T>>());
    }

    /**
     * post请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Flowable
     */
    public static <T> Flowable<Response<BaseResponse<T>>> postFlowable(String url, Map<String, String> headers, Map<String, Object> params) {
        PostRequest<BaseResponse<T>> mPostRequest = getPostRequest(url, headers, params);
        return mPostRequest.adapt(new FlowableResponse<BaseResponse<T>>());
    }

    /**
     * post请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Maybe
     */
    public static <T> Maybe<Response<BaseResponse<T>>> postMaybe(String url, Map<String, String> headers, Map<String, Object> params) {
        PostRequest<BaseResponse<T>> mPostRequest = getPostRequest(url, headers, params);
        return mPostRequest.adapt(new MaybeResponse<BaseResponse<T>>());
    }

    /**
     * post请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Single
     */
    public static <T> Single<Response<BaseResponse<T>>> postSingle(String url, Map<String, String> headers, Map<String, Object> params) {
        PostRequest<BaseResponse<T>> mPostRequest = getPostRequest(url, headers, params);
        return mPostRequest.adapt(new SingleResponse<BaseResponse<T>>());
    }

    /**
     * post请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Completable
     */
    public static <T> Completable postCompletable(String url, Map<String, String> headers, Map<String, Object> params) {
        PostRequest<BaseResponse<T>> mPostRequest = getPostRequest(url, headers, params);
        return mPostRequest.adapt(new CompletableResponse<BaseResponse<T>>());
    }

    /**
     * 获取PostRequest
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return PostRequest
     */
    private static <T> PostRequest<BaseResponse<T>> getPostRequest(String url, Map<String, String> headers, Map<String, Object> params) {
        checkInit();
        return OkGo.<BaseResponse<T>>post(url)
                .headers(getHttpHeaders(headers))
                .params(getHttpParams(params))
                .converter(new JsonConvert<BaseResponse<T>>());
    }

    /**
     * put请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Observable
     */
    public static <T> Observable<Response<BaseResponse<T>>> putObservable(String url, Map<String, String> headers, Map<String, Object> params) {
        PutRequest<BaseResponse<T>> mPutRequest = getPutRequest(url, headers, params);
        return mPutRequest.adapt(new ObservableResponse<BaseResponse<T>>());
    }

    /**
     * put请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Flowable
     */
    public static <T> Flowable<Response<BaseResponse<T>>> putFlowable(String url, Map<String, String> headers, Map<String, Object> params) {
        PutRequest<BaseResponse<T>> mPutRequest = getPutRequest(url, headers, params);
        return mPutRequest.adapt(new FlowableResponse<BaseResponse<T>>());
    }

    /**
     * put请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Maybe
     */
    public static <T> Maybe<Response<BaseResponse<T>>> putMaybe(String url, Map<String, String> headers, Map<String, Object> params) {
        PutRequest<BaseResponse<T>> mPutRequest = getPutRequest(url, headers, params);
        return mPutRequest.adapt(new MaybeResponse<BaseResponse<T>>());
    }

    /**
     * put请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Single
     */
    public static <T> Single<Response<BaseResponse<T>>> putSingle(String url, Map<String, String> headers, Map<String, Object> params) {
        PutRequest<BaseResponse<T>> mPutRequest = getPutRequest(url, headers, params);
        return mPutRequest.adapt(new SingleResponse<BaseResponse<T>>());
    }

    /**
     * put请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Completable
     */
    public static <T> Completable putCompletable(String url, Map<String, String> headers, Map<String, Object> params) {
        PutRequest<BaseResponse<T>> mPutRequest = getPutRequest(url, headers, params);
        return mPutRequest.adapt(new CompletableResponse<BaseResponse<T>>());
    }

    /**
     * 获取PutRequest
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return PutRequest
     */
    private static <T> PutRequest<BaseResponse<T>> getPutRequest(String url, Map<String, String> headers, Map<String, Object> params) {
        checkInit();
        return OkGo.<BaseResponse<T>>put(url)
                .headers(getHttpHeaders(headers))
                .params(getHttpParams(params))
                .converter(new JsonConvert<BaseResponse<T>>());
    }

    /**
     * delete请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Observable
     */
    public static <T> Observable<Response<BaseResponse<T>>> deleteObservable(String url, Map<String, String> headers, Map<String, Object> params) {
        DeleteRequest<BaseResponse<T>> mDeleteRequest = getDeleteRequest(url, headers, params);
        return mDeleteRequest.adapt(new ObservableResponse<BaseResponse<T>>());
    }

    /**
     * delete请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Flowable
     */
    public static <T> Flowable<Response<BaseResponse<T>>> deleteFlowable(String url, Map<String, String> headers, Map<String, Object> params) {
        DeleteRequest<BaseResponse<T>> mDeleteRequest = getDeleteRequest(url, headers, params);
        return mDeleteRequest.adapt(new FlowableResponse<BaseResponse<T>>());
    }

    /**
     * delete请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Maybe
     */
    public static <T> Maybe<Response<BaseResponse<T>>> deleteMaybe(String url, Map<String, String> headers, Map<String, Object> params) {
        DeleteRequest<BaseResponse<T>> mDeleteRequest = getDeleteRequest(url, headers, params);
        return mDeleteRequest.adapt(new MaybeResponse<BaseResponse<T>>());
    }

    /**
     * delete请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Single
     */
    public static <T> Single<Response<BaseResponse<T>>> deleteSingle(String url, Map<String, String> headers, Map<String, Object> params) {
        DeleteRequest<BaseResponse<T>> mDeleteRequest = getDeleteRequest(url, headers, params);
        return mDeleteRequest.adapt(new SingleResponse<BaseResponse<T>>());
    }

    /**
     * delete请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Completable
     */
    public static <T> Completable deleteCompletable(String url, Map<String, String> headers, Map<String, Object> params) {
        DeleteRequest<BaseResponse<T>> mDeleteRequest = getDeleteRequest(url, headers, params);
        return mDeleteRequest.adapt(new CompletableResponse<BaseResponse<T>>());
    }

    /**
     * 获取DeleteRequest
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return DeleteRequest
     */
    private static <T> DeleteRequest<BaseResponse<T>> getDeleteRequest(String url, Map<String, String> headers, Map<String, Object> params) {
        checkInit();
        return OkGo.<BaseResponse<T>>delete(url)
                .headers(getHttpHeaders(headers))
                .params(getHttpParams(params))
                .converter(new JsonConvert<BaseResponse<T>>());
    }

    /**
     * trace请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Observable
     */
    public static <T> Observable<Response<BaseResponse<T>>> traceObservable(String url, Map<String, String> headers, Map<String, Object> params) {
        TraceRequest<BaseResponse<T>> mTraceRequest = getTraceRequest(url, headers, params);
        return mTraceRequest.adapt(new ObservableResponse<BaseResponse<T>>());
    }

    /**
     * trace请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Flowable
     */
    public static <T> Flowable<Response<BaseResponse<T>>> traceFlowable(String url, Map<String, String> headers, Map<String, Object> params) {
        TraceRequest<BaseResponse<T>> mTraceRequest = getTraceRequest(url, headers, params);
        return mTraceRequest.adapt(new FlowableResponse<BaseResponse<T>>());
    }

    /**
     * trace请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Maybe
     */
    public static <T> Maybe<Response<BaseResponse<T>>> traceMaybe(String url, Map<String, String> headers, Map<String, Object> params) {
        TraceRequest<BaseResponse<T>> mTraceRequest = getTraceRequest(url, headers, params);
        return mTraceRequest.adapt(new MaybeResponse<BaseResponse<T>>());
    }

    /**
     * trace请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Single
     */
    public static <T> Single<Response<BaseResponse<T>>> traceSingle(String url, Map<String, String> headers, Map<String, Object> params) {
        TraceRequest<BaseResponse<T>> mTraceRequest = getTraceRequest(url, headers, params);
        return mTraceRequest.adapt(new SingleResponse<BaseResponse<T>>());
    }

    /**
     * trace请求
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return Completable
     */
    public static <T> Completable traceCompletable(String url, Map<String, String> headers, Map<String, Object> params) {
        TraceRequest<BaseResponse<T>> mTraceRequest = getTraceRequest(url, headers, params);
        return mTraceRequest.adapt(new CompletableResponse<BaseResponse<T>>());
    }

    /**
     * 获取TraceRequest
     *
     * @param url
     * @param headers
     * @param params
     * @param <T>
     * @return TraceRequest
     */
    private static <T> TraceRequest<BaseResponse<T>> getTraceRequest(String url, Map<String, String> headers, Map<String, Object> params) {
        checkInit();
        return OkGo.<BaseResponse<T>>trace(url)
                .headers(getHttpHeaders(headers))
                .params(getHttpParams(params))
                .converter(new JsonConvert<BaseResponse<T>>());
    }

    /**
     * map转换成httpParams
     *
     * @param map
     * @return
     */
    private static HttpParams getHttpParams(Map<String, Object> map) {
        HttpParams hp = new HttpParams();
        if (map == null) return hp;
        for (String key : map.keySet()) {
            if (map.get(key) instanceof String) {
                hp.put(key, (String) map.get(key));
            }
            if (map.get(key) instanceof Integer) {
                hp.put(key, (Integer) map.get(key));
            }
            if (map.get(key) instanceof Long) {
                hp.put(key, (Long) map.get(key));
            }
            if (map.get(key) instanceof Float) {
                hp.put(key, (Float) map.get(key));
            }
            if (map.get(key) instanceof Double) {
                hp.put(key, (Double) map.get(key));
            }
            if (map.get(key) instanceof Double) {
                hp.put(key, (Double) map.get(key));
            }
            if (map.get(key) instanceof Character) {
                hp.put(key, (Character) map.get(key));
            }
            if (map.get(key) instanceof Boolean) {
                hp.put(key, (Boolean) map.get(key));
            }
            if (map.get(key) instanceof File) {
                hp.put(key, (File) map.get(key));
            }
        }
        return hp;
    }

    /**
     * map转换成HttpHeaders
     *
     * @param map
     * @return
     */
    private static HttpHeaders getHttpHeaders(Map<String, String> map) {
        HttpHeaders hh = new HttpHeaders();
        if (map == null) return hh;
        for (String key : map.keySet()) {
            hh.put(key, map.get(key));
        }
        return hh;
    }

    /**
     * 校验是否初始化
     */
    private static void checkInit() {
        if (APPLICATION == null) {
            throw new RuntimeException("OkGoManager -> please init OkGoManager first");
        }
    }


}
