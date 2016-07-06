package com.mobgen.droidcon.offline;

import android.app.Application;
import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.DemoSdk;

public class DemosApplication extends Application {

    private static DemosApplication sInstance;
    private DemoSdk mDemoSdk;

    public static DemosApplication instance() {
        return sInstance;
    }

    @NonNull
    public DemoSdk demoSdk() {
        return mDemoSdk;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mDemoSdk = DemoSdk.Factory.init(this);
    }
}
