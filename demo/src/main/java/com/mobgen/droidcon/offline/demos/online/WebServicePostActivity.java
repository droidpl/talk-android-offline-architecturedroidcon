package com.mobgen.droidcon.offline.demos.online;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.mobgen.droidcon.offline.DemosApplication;
import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.server.PostService;
import com.mobgen.droidcon.offline.shared.adapters.PostAdapter;
import com.mobgen.droidcon.offline.shared.ui.BasePostActivity;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServicePostActivity extends BasePostActivity implements PostAdapter.PostListener {

    private PostService mPostService;
    private PostAdapter mAdapter;
    private Call<Void> mDeletedCall;

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, WebServicePostActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title_web_service_post_list));
        mPostService = DemosApplication.instance().demoSdk().postService();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        reloadPosts();
    }

    @Override
    public void onRefresh() {
        reloadPosts();
    }

    private void reloadPosts() {
        startLoading();
        mPostService.posts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                mAdapter = new PostAdapter(response.body(), WebServicePostActivity.this);
                setAdapter(mAdapter);
                stopLoading();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_load_comments), Snackbar.LENGTH_LONG).show();
                stopLoading();
            }
        });
    }

    @Override
    public void onDeleted(@NonNull final Post post, final int position) {
        mDeletedCall = mPostService.deletePost(post.id());
        startLoading();
        mDeletedCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                stopLoading();
                mDeletedCall = null;
                mAdapter.posts().set(position, Post.builder(post).deletedAt(new Date().getTime()).build());
                mAdapter.notifyItemChanged(position);
                //Reload articles with some delay to show off
                startLoading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reloadPosts();
                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_load_posts), Snackbar.LENGTH_LONG).show();
                stopLoading();
                mDeletedCall = null;
            }
        });
    }

    @Override
    public void onSelected(@NonNull Post post) {
        //Post clicked go to next screen
        WebServicePostDetailsActivity.start(this, post);
    }

    @Override
    public void onPostCreated(@NonNull Post post) {
        startLoading();
        mPostService.create(post).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                reloadPosts();
                stopLoading();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_load_posts), Snackbar.LENGTH_LONG).show();
                stopLoading();
            }
        });
    }
}
