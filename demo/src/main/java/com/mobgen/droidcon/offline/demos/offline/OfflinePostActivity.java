package com.mobgen.droidcon.offline.demos.offline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.mobgen.droidcon.offline.DemosApplication;
import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.presentation.modules.post.PostContract;
import com.mobgen.droidcon.offline.presentation.modules.post.PostPresenter;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.repository.DataStore;
import com.mobgen.droidcon.offline.shared.adapters.PostAdapter;
import com.mobgen.droidcon.offline.shared.loaders.PostLoader;
import com.mobgen.droidcon.offline.shared.ui.BasePostActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OfflinePostActivity extends BasePostActivity
        implements LoaderManager.LoaderCallbacks<List<Post>>
        , PostAdapter.PostListener
        , PostContract.View {

    private DataStore mRepository;
    private Executor mExecutor;
    private PostContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
    }

    @Override
    public void onRefresh() {
        startLoading();
        PostLoader.getLoader(getSupportLoaderManager()).forceLoad();
    }

    @Override
    public Loader<List<Post>> onCreateLoader(int id, Bundle args) {
        return new PostLoader(this, DemosApplication.instance().demoSdk());
    }

    @Override
    public void onLoadFinished(Loader<List<Post>> loader, List<Post> data) {
        stopLoading();
        setAdapter(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Post>> loader) {
        //Do nothing
    }

    @Override
    public void onSelected(@NonNull Post post) {
        OfflinePostDetailsActivity.start(this, post);
    }

    @Override
    public void onPostCreated(@NonNull final Post post) {
        startLoading();
        presenter.createPost(post);
    }

    @Override
    public void onDeleted(@NonNull final Post post, int position) {
        startLoading();
        presenter.deletePost(post);
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

        setTitle(R.string.title_offline_post_list);
        mRepository = DemosApplication.instance().demoSdk();
        mExecutor = Executors.newSingleThreadExecutor();
        startLoading();
        PostLoader.init(getSupportLoaderManager(), this);

        new PostPresenter(this, mRepository, mExecutor);
    }

    @Override
    public void setPresenter(PostContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
