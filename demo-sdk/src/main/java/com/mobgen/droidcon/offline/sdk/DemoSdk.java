package com.mobgen.droidcon.offline.sdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.repository.PostRepository;
import com.mobgen.droidcon.offline.sdk.server.PostService;

public interface DemoSdk {
    PostRepository postRepository();

    PostService postService();

    class Factory {
        public static DemoSdk init(@NonNull Context context) {
            return new DemoSdkImpl(context.getApplicationContext());
        }
    }
}
