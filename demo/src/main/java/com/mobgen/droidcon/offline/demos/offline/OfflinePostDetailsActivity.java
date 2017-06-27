package com.mobgen.droidcon.offline.demos.offline;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.mobgen.droidcon.offline.DemosApplication;
import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.presentation.modules.postdetail.PostDetailContract;
import com.mobgen.droidcon.offline.presentation.modules.postdetail.PostDetailPresenter;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.repository.DataStore;
import com.mobgen.droidcon.offline.sdk.repository.PostRepository;
import com.mobgen.droidcon.offline.sdk.repository.RepositoryException;
import com.mobgen.droidcon.offline.shared.loaders.CommentLoader;
import com.mobgen.droidcon.offline.shared.ui.BasePostDetailsActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OfflinePostDetailsActivity extends BasePostDetailsActivity
        implements LoaderManager.LoaderCallbacks<List<Comment>>
        , PostDetailContract.View {

    private Executor mExecutor;
    private DataStore mRepository;
    private PostDetailContract.Presenter presenter;

    public static void start(@NonNull Context context, @NonNull Post post) {
        context.startActivity(getIntent(context, post, OfflinePostDetailsActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
    }

    @Override
    public void onRefresh() {
        startLoading();
        CommentLoader.loader(getSupportLoaderManager()).forceLoad();
    }

    @Override
    public Loader<List<Comment>> onCreateLoader(int id, Bundle args) {
        return new CommentLoader(this, DemosApplication.instance().demoSdk(), mCurrentPost);
    }

    @Override
    public void onLoadFinished(Loader<List<Comment>> loader, List<Comment> data) {
        stopLoading();
        setAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Comment>> loader) {
        // Do nothing here
    }

    @Override
    public void onCommentCreated(@NonNull final Comment comment) {
        startLoading();
        presenter.createComment(comment);
    }

    @Override
    public void onRemoveComment(@NonNull final Comment comment) {
        startLoading();
        presenter.deleteComment(comment);
    }

    public void showError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_load_posts), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void setUp() {

        setTitle(R.string.title_offline_post_details);
        mRepository = DemosApplication.instance().demoSdk();
        mExecutor = Executors.newSingleThreadExecutor();
        CommentLoader.init(getSupportLoaderManager(), this);

        new PostDetailPresenter(this, mRepository, mExecutor);
    }

    @Override
    public void setPresenter(PostDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
