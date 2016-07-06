package com.mobgen.droidcon.offline.shared.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mobgen.droidcon.offline.base.AutoGson;

@AutoValue
@AutoGson(autoClass = AutoValue_Comment.class)
public abstract class Comment {

    @NonNull
    public abstract Integer postId();

    @Nullable
    public abstract Integer id();

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

    @NonNull
    public static Builder builder() {
        return new AutoValue_Comment.Builder();
    }

    @NonNull
    public static Builder builder(@NonNull Comment comment) {
        return builder()
                .postId(comment.postId())
                .id(comment.id())
                .name(comment.name())
                .email(comment.email())
                .body(comment.body())
                .createdAt(comment.createdAt())
                .updatedAt(comment.updatedAt())
                .deletedAt(comment.deletedAt());
    }

    @AutoValue.Builder
    public static abstract class Builder {

        @NonNull
        public abstract Builder postId(Integer postId);

        @NonNull
        public abstract Builder id(@Nullable Integer id);

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
        public abstract Comment build();
    }
}
