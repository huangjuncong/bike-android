package com.coder520.mamabike.internet;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.coder520.mamabike.AppConfig;
import com.coder520.mamabike.MamaApplication;
import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.ResponseEntityVo;
import com.coder520.mamabike.manager.UserManager;
import com.coder520.mamabike.utils.CommonUtils;
import com.coder520.mamabike.utils.DeviceInfoUtils;
import com.coder520.mamabike.utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cloan on 2017/5/11.
 */

public class InternetRequestWorker {
    private static final String TAG = InternetRequestWorker.class.getSimpleName();
    private static final String NO_LOGIN_REQUEST = "NO-Login-Request";
    private static final String THIRED_REQUEST = "Thread-Request";
    private static final String SIGN_KEY = "sign";
    private static final String SESSION_KEY = "JSESSIONID";
    public static final String MARK_AS_THIRED_REQUEST = THIRED_REQUEST + ": true";
    //不需要session访问的接口需要加这个请求头
    public static final String MARK_AS_NO_LOGIN_REQUEST = NO_LOGIN_REQUEST + ": true";
    public static final String BASE_URL = AppConfig.API_BASE_URL;
    private static final int INTERNET_CACHE_SIZE = 10 * 1024 * 1024; //10M
    private static InternetRequestWorker sInstance = null;
    /**
     * 用户token
     **/
    public static final String REQUEST_TOKEN_KEY = "user-token";
    public static final String REQUEST_VERSION_KEY = "version";

    private Retrofit mRetrofit = null;

    public interface RequestCallBack {
        void onSuccess(ResponseEntityVo data);
        void onFailed(String errorMessage);
    }

    public interface FileUploadRequestCallBack extends RequestCallBack {
        void onProgressChanged(long totle, long progress);
    }


    private InternetRequestWorker() {
    }

    public void init(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Cache cache = new Cache(context.getExternalCacheDir(),
                INTERNET_CACHE_SIZE);
        httpClient.cache(cache)
                .addInterceptor(getInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
//                .sslSocketFactory(getSSLCertifcation(context))
                .hostnameVerifier(
                        new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        });
        if (AppConfig.DEBUG) {
            httpClient.addInterceptor(logging);
        }
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .addConverterFactory(JsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    private static final String CLIENT_P12 = "client.p12";
    private final static String PROTOCOL_TYPE = "TLS";
    private final static String CERTIFICATE_FORMAT = "X509";
    private final static String CLIENT_KEY_TYPE = "PKCS12";

    public SSLSocketFactory getSSLCertifcation(Context context) {
        //TODO ssh 证书密码
        String password = "";
        SSLSocketFactory sslSocketFactory = null;
        try {
            // 服务器端需要验证的客户端证书，其实就是客户端的keystore
            KeyStore keyStore = KeyStore.getInstance(CLIENT_KEY_TYPE);// 客户端信任的服务器端证书
            InputStream ksIn = context.getAssets().open(CLIENT_P12);
            keyStore.load(ksIn, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, password.toCharArray());
            ksIn.close();
            TrustManagerFactory trustManagerFactory
                    = TrustManagerFactory.getInstance(CERTIFICATE_FORMAT);
            trustManagerFactory.init(keyStore);

            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            sslContext.init(keyManagerFactory.getKeyManagers(),
                    trustManagerFactory.getTrustManagers(), null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }


    /**
     * 为请求添加sign相关的查询参数
     * 1. 添加 url 中的请求参数
     * 2. 添加 body 中的请求参数
     * 最后将URL中添加sign相关字段然后添加到url的查询条件中,最后返回
     *
     * @param request
     * @return
     */
    private Request appendSignQueryParameter(Request request) {
        Map<String, Object> signMap = new HashMap<>();
        URL url = request.url().url();
        //获取请求url中的查询条件
        try {
            String urlQueryStr = url.getQuery();
            Map<String, String> urlQuery = CommonUtils.urlQueryToMap(urlQueryStr);
            if (urlQuery != null) {
                signMap.putAll(urlQuery);
            }
        } catch (Exception e) {
            Log.e(TAG, "error here...");
            e.printStackTrace();
        }
        //添加body中的查询条件
        try {
            if (request.body() != null && request.body().contentLength() > 0) {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                String bodyQueryStr = buffer.readUtf8();
                Map<String, String> map = new HashMap<>(1);
                Map<String, String> bodyQuery = new Gson().fromJson(bodyQueryStr, map.getClass());
                if (bodyQuery != null) {
                    signMap.putAll(bodyQuery);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "error here...");
            e.printStackTrace();
        }
        if (signMap.size() > 0) {
            String sign = NetworkUtils.sign(signMap);
            URL newUrl = null;
            try {
                newUrl = NetworkUtils.appendUri(url, SIGN_KEY + "=" + sign);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (newUrl != null) {
                request = request.newBuilder().url(newUrl).build();
            }
        }
        return request;
    }

    private Request appendSessionParameter(Request request) {
        if (!UserManager.getInstance().isLogin()) {
            return request;
        }
        String sessionId = UserManager.getInstance().getUserToken();
        URL newUrl = null;
        return request.newBuilder().addHeader(REQUEST_TOKEN_KEY, sessionId)
                .addHeader(REQUEST_VERSION_KEY, "0.1").build();
    }

    private Request removeHead(Request request, String head) {
        Headers heads = request.headers().newBuilder().removeAll(head).build();
        return request.newBuilder().headers(heads).build();
    }

    /**
     * 非即时性很强的接口我们希望在没有网络的时候也可以使用.
     * 所以如果是非nostore标签的网络访问在没有网络的时候我们同样允许访问
     *
     * @return
     */
    public Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                String noLoginRequest = request.header(NO_LOGIN_REQUEST);
                if (!"true".equals(noLoginRequest)) {
                    request = appendSessionParameter(request);
                }
                //移除自定义的请求头, loginRequest , 这个请求头仅仅用于标记这个请求是否需要登录
                request = removeHead(request, NO_LOGIN_REQUEST);

//                String noSignNeeded = request.header(THIRED_REQUEST);
//                if (!"true".equals(noSignNeeded)) {
//                    //为所有请求添加sign字段
//                    request = appendSignQueryParameter(request, needSession);
//                }
//                request = removeHead(request, THIRED_REQUEST);

                request = appendSignQueryParameter(request);
                boolean noStore = request.cacheControl().noStore();
                if (!DeviceInfoUtils.isNetworkAvailable(MamaApplication.getApplication())
                        && !noStore) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }

                return chain.proceed(request);
            }

        };
    }

    public static InternetRequestWorker getInstance() {
        if (sInstance == null) {
            sInstance = new InternetRequestWorker();
        }
        return sInstance;
    }

    /**
     * 创建Call对象
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> tClass) {
        return mRetrofit.create(tClass);
    }

    /**
     * 异步网络调用
     *
     * @param requestCall
     * @param callback
     * @param <T>最终需要解析的数据类型
     */
    public <T> void asyncNetwork(final Call<ResponseEntityVo<T>> requestCall,
                                 final DefaultResponseDispatcher callback) {
        Call<ResponseEntityVo<T>> call;
        if (requestCall.isExecuted()) {
            call = requestCall.clone();
        } else {
            call = requestCall;
        }
        call.enqueue(new Callback<ResponseEntityVo<T>>() {
            @Override
            public void onResponse(Call<ResponseEntityVo<T>> call,
                                   retrofit2.Response<ResponseEntityVo<T>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailed(MamaApplication.getApplication()
                            .getString(R.string.message_network_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseEntityVo<T>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailed(MamaApplication.getApplication()
                        .getString(R.string.message_network_error));
            }
        });
    }

    /**
     * 异步网络调用, 返回的数据为byte数组
     *
     * @param requestCall
     * @param callback
     */
    public void asyncNetworkBackWithByteArray(final Call<ResponseBody> requestCall,
                                              final RequestCallBack callback) {
        Call<ResponseBody> call;
        if (requestCall.isExecuted()) {
            call = requestCall.clone();
        } else {
            call = requestCall;
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        ResponseEntityVo<byte[]> responseEntit = new ResponseEntityVo<>();
                        //TODO response status here
//                        responseEntit.setStatus(ResponseEntityVo.RESULT_OK);
                        responseEntit.setData(response.body().bytes());
                        callback.onSuccess(responseEntit);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailed(MamaApplication.getApplication()
                        .getString(R.string.message_network_error));
            }
        });
    }

    public void asyncNetwork(final Call requestCall, Callback callBack) {
        Call call;
        if (requestCall.isExecuted()) {
            call = requestCall.clone();
        } else {
            call = requestCall;
        }
        call.enqueue(callBack);
    }


    private void uploadFileBody(Call call,
                                RequestBody requestBody,
                                final InternetRequestWorker.FileUploadRequestCallBack callback) {
        //对RequestBody进行封装, 提供文件上传进程监听功能
        FileRequestBody fileRequestBody = new FileRequestBody(requestBody, callback);
        call.enqueue(new Callback<ResponseEntityVo<Object>>() {

            @Override
            public void onResponse(Call<ResponseEntityVo<Object>> call,
                                   retrofit2.Response<ResponseEntityVo<Object>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailed(MamaApplication.getApplication()
                            .getString(R.string.message_network_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseEntityVo<Object>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailed(MamaApplication.getApplication()
                        .getString(R.string.message_network_error));
            }
        });
    }

    /**
     * 文件上传接口
     *
     * @param call
     * @param file
     * @param callback
     */
    public void asyncUploadFile(Call call, File file,
                                InternetRequestWorker.FileUploadRequestCallBack callback) {
        //创建RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        uploadFileBody(call, requestBody, callback);
    }

    /**
     * 文件流上传接口
     *
     * @param call
     * @param inputStream 文件流
     * @param size        文件流大小
     * @param callback
     */
    public void asyncUploadFileStream(Call call, final InputStream inputStream, final long size,
                                      InternetRequestWorker.FileUploadRequestCallBack callback) {
        //创建RequestBody
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("multipart/form-data");
            }

            @Override
            public long contentLength() throws IOException {
                return size;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
        uploadFileBody(call, requestBody, callback);
    }
}
