package com.mobgen.droidcon.offline.demos.offline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.mobgen.droidcon.offline.DemosApplication;
import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.repository.PostRepository;
import com.mobgen.droidcon.offline.sdk.repository.RepositoryException;
import com.mobgen.droidcon.offline.sdk.sync.loaders.PostLoader;
import com.mobgen.droidcon.offline.shared.adapters.PostAdapter;
import com.mobgen.droidcon.offline.shared.ui.BasePostActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OfflinePostActivity extends BasePostActivity implements LoaderManager.LoaderCallbacks<List<Post>>,PostAdapter.PostListener {

    private PostRepository mRepository;
    private Executor mExecutor;

    public static void start(@NonNull Context context){
        Intent intent = new Intent(context, OfflinePostActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_offline_post_list);
        mRepository = DemosApplication.instance().demoSdk().postRepository();
        mExecutor = Executors.newSingleThreadExecutor();
        startLoading();
        PostLoader.init(getSupportLoaderManager(), this);
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
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mRepository.localCreate(post);
                } catch (RepositoryException e) {
                    showError();
                }
            }
        });
    }

    @Override
    public void onDeleted(@NonNull final Post post, int position) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mRepository.localDelete(post);
                    onRefresh();
                } catch (RepositoryException e) {
                    showError();
                }
            }
        });
    }

    public void showError(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_load_posts), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
