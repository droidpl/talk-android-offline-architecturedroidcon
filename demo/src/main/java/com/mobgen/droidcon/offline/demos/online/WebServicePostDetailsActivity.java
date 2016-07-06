package com.mobgen.droidcon.offline.demos.online;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.mobgen.droidcon.offline.DemosApplication;
import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.server.PostService;
import com.mobgen.droidcon.offline.shared.adapters.CommentAdapter;
import com.mobgen.droidcon.offline.shared.ui.BasePostDetailsActivity;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServicePostDetailsActivity extends BasePostDetailsActivity implements CommentAdapter.CommentListener {

    private CommentAdapter mCommentAdapter;
    private PostService mPostService;

    public static void start(@NonNull Context context, @NonNull Post post) {
        context.startActivity(getIntent(context, post, WebServicePostDetailsActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title_web_service_post_details));
        mPostService = DemosApplication.instance().demoSdk().postService();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        reloadComments();
    }

    @Override
    public void onRefresh() {
        reloadComments();
    }

    private void reloadComments() {
        startLoading();
        mPostService.comments(mCurrentPost.id()).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                mCommentAdapter = new CommentAdapter(mCurrentPost, response.body(), WebServicePostDetailsActivity.this);
                setAdapter(mCommentAdapter);
                stopLoading();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_load_comments), Snackbar.LENGTH_LONG).show();
                stopLoading();
            }
        });
    }

    @Override
    public void onRemoveComment(@NonNull Comment comment) {
        startLoading();
        mPostService.deleteComment(comment.id()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                stopLoading();
                reloadComments();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_load_comments), Snackbar.LENGTH_LONG).show();
                stopLoading();
            }
        });
    }

    @Override
    public void onCommentCreated(@NonNull Comment comment) {
        startLoading();
        mPostService.create(comment).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                stopLoading();
                reloadComments();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_load_comments), Snackbar.LENGTH_LONG).show();
                stopLoading();
            }
        });
    }
}
