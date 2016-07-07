package com.mobgen.droidcon.offline.sdk.sync;

import android.app.job.JobParameters;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.DemoSdk;
import com.mobgen.droidcon.offline.sdk.base.DatabaseManager;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.repository.RepositoryException;

import java.io.IOException;
import java.util.List;

public class SyncTask extends AsyncTask<JobParameters, Void, JobParameters> {

    private DemoSdk mSdk;
    private SyncService mSyncService;
    private boolean mNeedsResync;

    public SyncTask(@NonNull DemoSdk sdk, @NonNull SyncService service) {
        mSdk = sdk;
        mSyncService = service;
        mNeedsResync = false;
    }

    @Override
    protected JobParameters doInBackground(JobParameters... params) {
        try {
            syncPosts();
            syncComments();
        } catch (DatabaseManager.DatabaseException | RepositoryException | IOException e) {
            mNeedsResync = true;
        }
        return params[0];
    }

    private void syncComments() throws DatabaseManager.DatabaseException, RepositoryException, IOException {
        List<Comment> comments = mSdk.postRepository().localPendingComments();
        for (Comment comment : comments) {
            if (comment.isNew()) { //New post
                mSdk.postRepository().remoteCreate(comment);
            } else if (comment.isDeleted()) {
                mSdk.postRepository().remoteDelete(comment);
            }
        }
    }

    private void syncPosts() throws DatabaseManager.DatabaseException, RepositoryException, IOException {
        List<Post> posts = mSdk.postRepository().localPendingPosts();
        for (Post post : posts) {
            if (post.isNew()) { //New post
                mSdk.postRepository().remoteCreate(post);
            } else if (post.isDeleted()) {
                mSdk.postRepository().remoteDelete(post);
            }
        }

    }

    @Override
    protected void onPostExecute(JobParameters jobParameters) {
        super.onPostExecute(jobParameters);
        SyncService.notifyChange(mSyncService);
        mSyncService.jobFinished(jobParameters, mNeedsResync);
    }
}
