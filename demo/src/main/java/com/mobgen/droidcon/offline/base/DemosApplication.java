package com.mobgen.droidcon.offline.base;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.mobgen.droidcon.offline.BuildConfig;
import com.mobgen.droidcon.offline.shared.utils.LogInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemosApplication extends Application {

    private static DemosApplication sInstance;
    private Retrofit mRetrofit;

    public static DemosApplication instance() {
        return sInstance;
    }

    @NonNull
    public Retrofit client() {
        return mRetrofit;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //Initialize retrofit
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().registerTypeAdapterFactory(new AutoValueTypeAdapterFactory()).create()
                ))
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new LogInterceptor())
                        .build())
                .baseUrl(BuildConfig.SERVER_URL)
                .build();
    }
}
