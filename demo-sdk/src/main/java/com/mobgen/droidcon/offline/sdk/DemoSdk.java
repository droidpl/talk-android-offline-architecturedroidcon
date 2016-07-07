package com.mobgen.droidcon.offline.sdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.repository.PostRepository;
import com.mobgen.droidcon.offline.sdk.repository.remote.PostService;

public interface DemoSdk {
    PostRepository postRepository();

    PostService postService();

    class Factory {

        private static DemoSdk sInstance;

        public static synchronized DemoSdk init(@NonNull Context context) {
            if (sInstance == null) {
                sInstance = new DemoSdkImpl(context.getApplicationContext());
            }
            return sInstance;
        }

        public static DemoSdk instance() {
            return sInstance;
        }
    }
}
