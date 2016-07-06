package com.mobgen.droidcon.offline.sdk.models.dbmodels;

import com.google.auto.value.AutoValue;
import com.mobgen.droidcon.offline.sdk.model.dbmodels.CommentModel;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class CommentDb implements CommentModel {

    public static final Factory<CommentDb> FACTORY = new Factory<>(new CommentModel.Creator<CommentDb>() {
        @Override
        public CommentDb create(long id, long postId, String name, String body, String email, long createdAt, Long updatedAt, Long deletedAt, boolean needsSync) {
            return new AutoValue_CommentDb(id, postId, name, body, email, createdAt, updatedAt, deletedAt, needsSync);
        }
    });

    public static final RowMapper<CommentDb> COMMENTS_POST_MAPPER = FACTORY.selectCommentsPostMapper();

    public static final RowMapper<CommentDb> SYNC_POSTS_MAPPER = FACTORY.selectSyncCommentsMapper();
}
