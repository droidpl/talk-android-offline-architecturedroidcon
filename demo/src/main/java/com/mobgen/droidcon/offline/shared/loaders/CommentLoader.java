package com.mobgen.droidcon.offline.shared.loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.mobgen.droidcon.offline.sdk.DemoSdk;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.sync.SyncService;

import java.util.List;

public class CommentLoader extends BaseLoader<List<Comment>> {

    private static final int COMMENT_LOADER_ID = 1;
    private DemoSdk mDemoSdk;
    private BroadcastReceiver mObserver;
    private Post mPost;

    public static void init(@NonNull LoaderManager supportLoaderManager, @NonNull LoaderManager.LoaderCallbacks<List<Comment>> callback) {
        supportLoaderManager.initLoader(COMMENT_LOADER_ID, null, callback);
    }

    public static Loader loader(LoaderManager supportLoaderManager) {
        return supportLoaderManager.getLoader(COMMENT_LOADER_ID);
    }

    public CommentLoader(@NonNull Context context, @NonNull DemoSdk demoSdk, @NonNull Post post) {
        super(context);
        mDemoSdk = demoSdk;
        mPost = post;
    }

    @Override
    public List<Comment> loadInBackground() {
        setData(mDemoSdk.postRepository().comments(mPost));
        return getData();
    }

    @Override
    public void registerListener() {
        if (mObserver == null) {
            mObserver = SyncService.listenForUpdates(this);
        }
    }

    @Override
    public void unregisterListener() {
        if (mObserver != null) {
            SyncService.removeUpdateListener(this, mObserver);
        }
    }
}
