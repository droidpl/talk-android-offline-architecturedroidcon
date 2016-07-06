package com.mobgen.droidcon.offline.sdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.mobgen.droidcon.offline.sdk.database.DatabaseManager;
import com.mobgen.droidcon.offline.sdk.database.DemoDatabaseHelper;
import com.mobgen.droidcon.offline.sdk.repository.PostRepository;
import com.mobgen.droidcon.offline.sdk.server.PostService;
import com.mobgen.droidcon.offline.sdk.utils.AutoValueTypeAdapterFactory;
import com.mobgen.droidcon.offline.sdk.utils.LogInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemoSdkImpl implements DemoSdk {

    private PostRepository mPostRepository;
    private Retrofit mRetrofit;
    private DatabaseManager mDatabaseManager;

    DemoSdkImpl(@NonNull Context context){
        //Initialize retrofit
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .registerTypeAdapterFactory(new AutoValueTypeAdapterFactory())
                                .create()
                ))
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new LogInterceptor())
                        .build())
                .baseUrl(BuildConfig.SERVER_URL)
                .build();
        mDatabaseManager = DatabaseManager.initialize(new DemoDatabaseHelper(context));
    }

    @NonNull
    @Override
    public PostRepository postRepository(){
        if(mPostRepository == null){
            PostService service = mRetrofit.create(PostService.class);
            mPostRepository = new PostRepository(service);
        }
        return mPostRepository;
    }

    @NonNull
    @Override
    public PostService postService(){
        return postRepository().postService();
    }
}
