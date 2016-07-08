package com.mobgen.droidcon.offline.sdk.models.db;

import com.google.auto.value.AutoValue;
import com.mobgen.droidcon.offline.sdk.model.db.PostModel;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class PostDb implements PostModel {

    public static final PostModel.Factory<PostDb> FACTORY = new PostModel.Factory<>(new PostModel.Creator<PostDb>() {
        @Override
        public PostDb create(long id, Long remoteId, String title, String body, long createdAt, Long updatedAt, Long deletedAt, boolean needsSync) {
            return new AutoValue_PostDb(id, remoteId, title, body, createdAt, updatedAt, deletedAt, needsSync);
        }
    });

    public Post asModel() {
        return Post.builder()
                .internalId(_id())
                .id(_remoteId())
                .title(_title())
                .body(_body())
                .createdAt(_createdAt())
                .updatedAt(_updatedAt())
                .deletedAt(_deletedAt())
                .needsSync(_needsSync())
                .build();
    }

    public static final RowMapper<PostDb> POST_SYNC_MAPPER = FACTORY.selectSyncPostsMapper();
    public static final RowMapper<PostDb> ALL_POSTS_MAPPER = FACTORY.selectSyncPostsMapper();

}
