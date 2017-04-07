package com.mobgen.droidcon.offline.sdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.repository.DataStore;

public class DemoSdk {

    private static DataStore sInstance;

    public static synchronized DataStore init(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new DemoSdkImpl(context.getApplicationContext());
        }
        return sInstance;
    }

    public static DataStore instance() {
        return sInstance;
    }
}
