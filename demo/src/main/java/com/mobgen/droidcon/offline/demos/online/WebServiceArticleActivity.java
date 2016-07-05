package com.mobgen.droidcon.offline.demos.online;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.base.DemosApplication;
import com.mobgen.droidcon.offline.shared.PostService;
import com.mobgen.droidcon.offline.shared.adapters.PostAdapter;
import com.mobgen.droidcon.offline.shared.models.Post;
import com.mobgen.droidcon.offline.shared.ui.BaseArticleActivity;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServiceArticleActivity extends BaseArticleActivity {

    private PostService mPostService;
    private PostAdapter mAdapter;
    private Call<List<Post>> mOnGoingCall;
    private Call<Void> mDeletedCall;

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, WebServiceArticleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostService = DemosApplication.instance()
                .client()
                .create(PostService.class);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        reloadArticles();
    }

    @Override
    public void onRefresh() {
        reloadArticles();
    }

    private void reloadArticles() {
        mOnGoingCall = mPostService.posts();
        startLoading();
        mOnGoingCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                mAdapter = new PostAdapter(WebServiceArticleActivity.this, response.body(), WebServiceArticleActivity.this);
                setAdapter(mAdapter);
                stopLoading();
                mOnGoingCall = null;
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView(), getString(R.string.error_load_comments), Snackbar.LENGTH_LONG).show();
                stopLoading();
                mOnGoingCall = null;
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
                        reloadArticles();
                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView(), getString(R.string.error_load_comments), Snackbar.LENGTH_LONG).show();
                stopLoading();
                mDeletedCall = null;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOnGoingCall != null) {
            mOnGoingCall.cancel();
        }
        if (mDeletedCall != null) {
            mDeletedCall.cancel();
        }
    }
}
