package com.anenn.network;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 网络接口配置
 * Created by anenn on 3/26/16.
 */
public class BaseApi {

    // 网络请求对象
    private static OkHttpClient mHttpClient;
    // Retrofit API 接口对象
    private static Retrofit mRetrofit;
    // 主机地址
    private static String mEndPoint;

    /**
     * 在进入应用时进行初始化, 建议在 Application 中进行.
     *
     * @param context 应用上下文
     */
    public static void init(Context context, String endPoint) {
        mEndPoint = endPoint;

        if (mHttpClient == null) {
            // Http UserAgent
            final String userAgent = HttpParams.getUserAgent(context);
            // 缓存路径
//            final File cacheFile = new File(context.getCacheDir(), HttpParams.CACHE_FILE_NAME);
            // 缓存对象
//            final Cache cache = new Cache(cacheFile, HttpParams.CACHE_SIZE);
            final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(HttpParams.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .readTimeout(HttpParams.READ_TIME_OUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(HttpParams.WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
//                    .cache(cache)
                    .addNetworkInterceptor(new NetworkInterceptor(userAgent));
            // 在 Debug 模式下开启网络请求日志记录, Release 模式下则关闭
//            ApplicationInfo info = context.getApplicationInfo();
//            if ((info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
//                final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                httpClientBuilder.addInterceptor(httpLoggingInterceptor);
//            }

            mHttpClient = httpClientBuilder.build();
        }
    }

    /**
     * 获取 Retrofit 对象
     *
     * @return retrofit
     */
    protected static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            synchronized (Retrofit.class) {
                if (mRetrofit == null) {
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(mEndPoint)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(mHttpClient != null ? mHttpClient : new OkHttpClient())
                            .build();
                }
            }
        }

        return mRetrofit;
    }

    private static Gson buildGson() {
        return new GsonBuilder()
//                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
//                .excludeFieldsWithoutExposeAnnotation()
//                .serializeNulls()
                .registerTypeAdapter(int.class, IntegerDefault0Adapter.class)
                .registerTypeAdapter(Integer.class, IntegerDefault0Adapter.class)
                .create();

//        ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {
//
//            @Override
//            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
//                return false;
//            }
//
//            @Override
//            public boolean shouldSkipClass(Class<?> clazz) {
//                return clazz == Field.class || clazz == Method.class;
//            }
//        };
//
//        return new GsonBuilder()
//                .addSerializationExclusionStrategy(exclusionStrategy)
//                .addDeserializationExclusionStrategy(exclusionStrategy)
//                .create();
    }
}
