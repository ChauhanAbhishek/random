package com.rapido.youtube_rapido.app.di.modules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapido.youtube_rapido.BuildConfig;
import com.rapido.youtube_rapido.app.di.annotations.ApplicationScope;
import com.rapido.youtube_rapido.app.service.ApiService;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by adityachauhan on 23/06/18.
 *
 */

@Module(includes = ApplicationContextModule.class)
public class NetworkModule {

    @ApplicationScope
    @Provides
    Interceptor httpLoggingInterceptor(Context context) {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> {
//            if(BuildConfig.DEBUG) {
//                Log.d("Network",message);
//            }
//        });
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//
//        return interceptor;

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                Log.d("cnrr","intercepted");
                if (isNetworkAvailable(context)) {
                    int maxAge = 60; // read from cache for 1 minute
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
    }

    @ApplicationScope
    @Provides
    Cache cache(File file) {
        return new Cache(file,10*1000*1000);
    }

    @ApplicationScope
    @Provides
    File file(Context context) {
        return new File(context.getCacheDir(),"okhttp_cache");
    }

    @ApplicationScope
    @Provides
    OkHttpClient okHttpClient(Cache cache,Interceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @ApplicationScope
    ApiService apiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @ApplicationScope
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    @Provides
    @ApplicationScope
    Retrofit retrofit(OkHttpClient okHttpClient,ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @ApplicationScope
    public Picasso picasso(Context context, OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
