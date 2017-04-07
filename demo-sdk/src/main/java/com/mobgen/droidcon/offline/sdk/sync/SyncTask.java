package com.mobgen.droidcon.offline.sdk.sync;

import android.app.job.JobParameters;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.DemoSdk;
import com.mobgen.droidcon.offline.sdk.base.DatabaseException;
import com.mobgen.droidcon.offline.sdk.base.DatabaseManager;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.repository.DataStore;
import com.mobgen.droidcon.offline.sdk.repository.RepositoryException;

import java.io.IOException;
import java.util.List;

public class SyncTask extends AsyncTask<JobParameters, Void, JobParameters> {

    private DataStore mSdk;
    private SyncService mSyncService;
    private boolean mNeedsResync;

    public SyncTask(@NonNull DataStore sdk, @NonNull SyncService service) {
        mSdk = sdk;
        mSyncService = service;
        mNeedsResync = false;
    }

    @Override
    protected JobParameters doInBackground(JobParameters... params) {
        try {
            syncPosts();
            syncComments();
        } catch (DatabaseException | RepositoryException | IOException e) {
            mNeedsResync = true;
        }
        return params[0];
    }

    private void syncComments() throws DatabaseException, RepositoryException, IOException {
        List<Comment> comments = mSdk.localPendingComments();
        for (Comment comment : comments) {
            if (comment.isNew()) { //New post
                mSdk.remoteCreate(comment);
            } else if (comment.isDeleted()) {
                mSdk.remoteDelete(comment);
            }
        }
    }

    private void syncPosts() throws DatabaseException, RepositoryException, IOException {
        List<Post> posts = mSdk.localPendingPosts();
        for (Post post : posts) {
            if (post.isNew()) { //New post
                mSdk.remoteCreate(post);
            } else if (post.isDeleted()) {
                mSdk.remoteDelete(post);
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
