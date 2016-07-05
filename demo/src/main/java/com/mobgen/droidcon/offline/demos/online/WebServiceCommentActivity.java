package com.mobgen.droidcon.offline.demos.online;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mobgen.droidcon.offline.shared.models.Post;

public class WebServiceCommentActivity extends AppCompatActivity {

    private static final String BUNDLE_POST_ARGUMENT = "post_argument";

    private Post mCurrentPost;

    public static void start(@NonNull Context context, @NonNull Post post) {
        Intent intent = new Intent(context, WebServiceCommentActivity.class);
        intent.putExtra(BUNDLE_POST_ARGUMENT, post);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null || !intent.getExtras().containsKey(BUNDLE_POST_ARGUMENT)) {
            throw new IllegalArgumentException("This activity requires a post to be started. Use the start method");
        }
        mCurrentPost = intent.getParcelableExtra(BUNDLE_POST_ARGUMENT);
    }
}
