package com.mobgen.droidcon.offline.sdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.mobgen.droidcon.offline.sdk.base.AutoValueTypeAdapterFactory;
import com.mobgen.droidcon.offline.sdk.base.DatabaseException;
import com.mobgen.droidcon.offline.sdk.base.DatabaseManager;
import com.mobgen.droidcon.offline.sdk.base.DemoDatabaseHelper;
import com.mobgen.droidcon.offline.sdk.base.LogInterceptor;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.repository.DataStore;
import com.mobgen.droidcon.offline.sdk.repository.PostRepository;
import com.mobgen.droidcon.offline.sdk.repository.RepositoryException;
import com.mobgen.droidcon.offline.sdk.repository.local.CommentDAO;
import com.mobgen.droidcon.offline.sdk.repository.local.PostDAO;
import com.mobgen.droidcon.offline.sdk.repository.remote.PostService;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemoSdkImpl implements DataStore {

    private Context mContext;
    private DataStore mPostRepository;
    private Retrofit mRetrofit;
    private DatabaseManager mDatabaseManager;

    DemoSdkImpl(@NonNull Context context) {
        mContext = context;
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

        init();

    }

    private void init(){
        PostService service = mRetrofit.create(PostService.class);
        PostDAO postDao = new PostDAO(mDatabaseManager);
        CommentDAO commentDao = new CommentDAO(mDatabaseManager);
        mPostRepository = new PostRepository(mContext, service, postDao, commentDao);
    }

    @Override
    public void remoteCreate(@NonNull Post post) throws DatabaseException, IOException {
        mPostRepository.remoteCreate(post);
    }

    @Override
    public void remoteDelete(@NonNull Post post) throws IOException, DatabaseException {
        mPostRepository.remoteDelete(post);
    }

    @Override
    public void remoteCreate(@NonNull Comment comment) throws DatabaseException, IOException {
        mPostRepository.remoteCreate(comment);
    }

    @Override
    public void remoteDelete(@NonNull Comment comment) throws IOException, DatabaseException {
        mPostRepository.remoteDelete(comment);
    }

    @Override
    public List<Post> posts() {
        return mPostRepository.posts();
    }

    @Override
    public void localDelete(@NonNull Post post) throws RepositoryException {
        mPostRepository.localDelete(post);
    }

    @Override
    public void localCreate(@NonNull Post post) throws RepositoryException {
        mPostRepository.localCreate(post);
    }

    @Override
    public List<Post> localPendingPosts() throws DatabaseException {
        return mPostRepository.localPendingPosts();
    }

    @Override
    public List<Comment> comments(@NonNull Post post) {
        return mPostRepository.comments(post);
    }

    @Override
    public List<Comment> localPendingComments() throws DatabaseException {
        return mPostRepository.localPendingComments();
    }

    @Override
    public void localDelete(@NonNull Comment comment) throws RepositoryException {
        mPostRepository.localDelete(comment);
    }

    @Override
    public void localCreate(@NonNull Comment comment) throws RepositoryException {
        mPostRepository.localCreate(comment);
    }
}
