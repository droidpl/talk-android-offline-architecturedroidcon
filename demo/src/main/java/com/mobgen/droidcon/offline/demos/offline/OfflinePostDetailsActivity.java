package com.mobgen.droidcon.offline.demos.offline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.shared.ui.BasePostDetailsActivity;

public class OfflinePostDetailsActivity extends BasePostDetailsActivity {

    public static void start(@NonNull Context context, @NonNull Post post) {
        context.startActivity(getIntent(context, post, OfflinePostDetailsActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_offline_post_details);
    }

    @Override
    public void onCommentCreated(@NonNull Comment comment) {

    }

    @Override
    public void onRefresh() {

    }
}
