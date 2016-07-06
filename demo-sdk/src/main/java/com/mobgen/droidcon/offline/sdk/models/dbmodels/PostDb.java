package com.mobgen.droidcon.offline.sdk.models.dbmodels;

import com.google.auto.value.AutoValue;
import com.mobgen.droidcon.offline.sdk.model.dbmodels.PostModel;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class PostDb implements PostModel {

    public static final PostModel.Factory<PostDb> FACTORY = new PostModel.Factory<>(new PostModel.Creator<PostDb>() {
        @Override
        public PostDb create(long id, String title, String body, long createdAt, Long updatedAt, Long deletedAt, boolean needsSync) {
            return new AutoValue_PostDb(id, title, body, createdAt, updatedAt, deletedAt, needsSync);
        }
    });

    public static final RowMapper<PostDb> POST_SYNC_MAPPER = FACTORY.selectSyncPostsMapper();

}
