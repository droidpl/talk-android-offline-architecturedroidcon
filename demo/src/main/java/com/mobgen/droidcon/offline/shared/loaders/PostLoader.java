package com.mobgen.droidcon.offline.shared.loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.mobgen.droidcon.offline.sdk.DemoSdk;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.sync.SyncService;

import java.util.List;

public class PostLoader extends BaseLoader<List<Post>> {

    private static final int POST_LOADER_ID = 1;
    private DemoSdk mDemoSdk;
    private BroadcastReceiver mObserver;

    public static void init(@NonNull LoaderManager supportLoaderManager, @NonNull LoaderManager.LoaderCallbacks<List<Post>> callback) {
        supportLoaderManager.initLoader(POST_LOADER_ID, null, callback);
    }

    @NonNull
    public static Loader getLoader(@NonNull LoaderManager supportLoaderManager) {
        return supportLoaderManager.getLoader(POST_LOADER_ID);
    }

    public PostLoader(@NonNull Context context, DemoSdk demoSdk) {
        super(context);
        mDemoSdk = demoSdk;
    }

    @Override
    public List<Post> loadInBackground() {
        setData(mDemoSdk.postRepository().posts());
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