package com.mobgen.droidcon.offline.sdk.models;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mobgen.droidcon.offline.sdk.base.AutoGson;
import com.mobgen.droidcon.offline.sdk.model.db.CommentModel;
import com.mobgen.droidcon.offline.sdk.models.db.CommentDb;

@AutoValue
@AutoGson(autoClass = AutoValue_Comment.class)
public abstract class Comment {

    @Nullable
    public abstract Long internalId();

    @Nullable
    public abstract Long id();

    @NonNull
    public abstract Long postId();

    @NonNull
    public abstract String name();

    @NonNull
    public abstract String email();

    @NonNull
    public abstract String body();

    @NonNull
    public abstract Long createdAt();

    @Nullable
    public abstract Long updatedAt();

    @Nullable
    public abstract Long deletedAt();

    public abstract boolean needsSync();

    @NonNull
    public ContentValues marshal() {
        CommentModel.Marshal marshal = CommentDb.FACTORY.marshal()
                ._remoteId(id())
                ._postId(postId())
                ._name(name())
                ._body(body())
                ._email(email())
                ._createdAt(createdAt())
                ._updatedAt(updatedAt())
                ._deletedAt(deletedAt())
                ._needsSync(needsSync());
        Long internalId = internalId();
        if(internalId != null){
            marshal._id(internalId);
        }
        return marshal.asContentValues();
    }

    @NonNull
    public static Builder builder() {
        return new AutoValue_Comment.Builder();
    }

    @NonNull
    public static Builder builder(@NonNull Comment comment) {
        return builder()
                .internalId(comment.internalId())
                .id(comment.id())
                .postId(comment.postId())
                .name(comment.name())
                .email(comment.email())
                .body(comment.body())
                .createdAt(comment.createdAt())
                .updatedAt(comment.updatedAt())
                .deletedAt(comment.deletedAt())
                .needsSync(comment.needsSync());
    }

    public boolean isNew() {
        return id() == null;
    }

    public boolean isDeleted() {
        return deletedAt() != null;
    }

    @AutoValue.Builder
    public static abstract class Builder {

        @NonNull
        public abstract Builder internalId(@Nullable Long id);

        @NonNull
        public abstract Builder id(@Nullable Long id);

        @NonNull
        public abstract Builder postId(@NonNull Long postId);

        @NonNull
        public abstract Builder name(@NonNull String name);

        @NonNull
        public abstract Builder email(@NonNull String email);

        @NonNull
        public abstract Builder body(@NonNull String body);

        @NonNull
        public abstract Builder createdAt(@NonNull Long createdAt);

        @NonNull
        public abstract Builder updatedAt(@Nullable Long updatedAt);

        @NonNull
        public abstract Builder deletedAt(@Nullable Long deletedAt);

        @NonNull
        public abstract Builder needsSync(@Nullable boolean needsSync);

        @NonNull
        public abstract Comment build();
    }
}
