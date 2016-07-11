package com.mobgen.droidcon.offline.sdk.sync;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;

import com.mobgen.droidcon.offline.sdk.DemoSdk;

public class SyncService extends JobService {

    private static final int SYNC_SERVICE_ID = 1;
    private static final String CHANGE_SYNC_INTENT_ACTION = "sync_service_change";
    private SyncTask mRunningSyncTask;

    public static void triggerSync(@NonNull Context context) {
        SyncService.notifyChange(context);
        ComponentName component = new ComponentName(context, SyncService.class);
        JobInfo info = new JobInfo.Builder(SYNC_SERVICE_ID, component)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(info);
    }

    public static BroadcastReceiver listenForUpdates(@NonNull Loader loader) {
        IntentFilter filter = new IntentFilter(CHANGE_SYNC_INTENT_ACTION);
        SyncServiceReceiver receiver = new SyncServiceReceiver(loader);
        LocalBroadcastManager.getInstance(loader.getContext()).registerReceiver(receiver, filter);
        return receiver;
    }

    public static void removeUpdateListener(@NonNull Loader loader, @NonNull BroadcastReceiver mObserver) {
        LocalBroadcastManager.getInstance(loader.getContext()).unregisterReceiver(mObserver);
    }

    public static void notifyChange(@NonNull Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(getCompletionIntent());
    }

    public static Intent getCompletionIntent() {
        return new Intent(CHANGE_SYNC_INTENT_ACTION);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        DemoSdk sdk = DemoSdk.Factory.instance();
        boolean willExecute = true;
        //Make sure it is initialized
        if (sdk != null) {
            mRunningSyncTask = new SyncTask(sdk, this);
            mRunningSyncTask.execute(params);
        } else {
            willExecute = false;
        }
        return willExecute;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        boolean reschedule = false;
        if (mRunningSyncTask != null) {
            mRunningSyncTask.cancel(true);
            reschedule = true;
        }
        return reschedule;
    }

    private static class SyncServiceReceiver extends BroadcastReceiver {

        private Loader mLoader;

        private SyncServiceReceiver(@NonNull Loader loader) {
            mLoader = loader;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            mLoader.onContentChanged(); //Resync
        }
    }
}
