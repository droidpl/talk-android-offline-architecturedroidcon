package com.mobgen.droidcon.offline.sdk.sync.loaders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public abstract class BaseLoader<T> extends AsyncTaskLoader<T> {

    private T mData;

    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(T data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        //Keep reference to avoid garbage collection
        T oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData);
        }

        // Begin monitoring the underlying data source.
        registerListener();

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected final void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mData != null) {
            releaseResources(mData);
            setData(null);
        }

        unregisterListener();
    }

    @Override
    public final void onCanceled(T data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    /**
     * Unregisters the listener but you must make sure it is already registered.
     */
    public abstract void unregisterListener();

    /**
     * Registers a new observer. You must make sure it is not registered yet since
     * the registration can be called multiple times.
     */
    public abstract void registerListener();

    public final void setData(@Nullable T data) {
        mData = data;
    }

    public T getData() {
        return mData;
    }

    private void releaseResources(T data) {
        //Override it if there is something you must release
    }
}
