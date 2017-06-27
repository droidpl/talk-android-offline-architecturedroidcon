package com.mobgen.droidcon.offline;

import android.app.Application;
import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.DemoSdk;
import com.mobgen.droidcon.offline.sdk.repository.DataStore;

public class DemosApplication extends Application {

    private static DemosApplication sInstance;
    private DataStore mDemoSdk;

    public static DemosApplication instance() {
        return sInstance;
    }

    @NonNull
    public DataStore demoSdk() {
        return mDemoSdk;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mDemoSdk = DemoSdk.init(this);
    }
}
