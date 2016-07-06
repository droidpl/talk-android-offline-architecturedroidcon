package com.mobgen.droidcon.offline.sdk.repository;

import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.server.PostService;

public class PostRepository {

    private PostService mPostService;

    public PostRepository(@NonNull PostService postService) {
        mPostService = postService;
    }

    public PostService postService() {
        return mPostService;
    }
}
