package com.mobgen.droidcon.offline.sdk.models;

import android.content.ContentValues;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mobgen.droidcon.offline.sdk.models.dbmodels.PostDb;
import com.mobgen.droidcon.offline.sdk.utils.AutoGson;

@AutoValue
@AutoGson(autoClass = AutoValue_Post.class)
public abstract class Post implements Parcelable {

    @Nullable
    public abstract Long id();

    @NonNull
    public abstract String title();

    @NonNull
    public abstract String body();

    @NonNull
    public abstract Long createdAt();

    @Nullable
    public abstract Long updatedAt();

    @Nullable
    public abstract Long deletedAt();

    public abstract boolean needsSync();

    public ContentValues marshal() {
        return PostDb.FACTORY.marshal()
                ._id(id())
                ._title(title())
                ._body(body())
                ._createdAt(createdAt())
                ._updatedAt(updatedAt())
                ._deletedAt(deletedAt())
                ._needsSync(needsSync())
                .asContentValues();
    }

    @NonNull
    public static Builder builder() {
        return new AutoValue_Post.Builder();
    }

    @NonNull
    public static Builder builder(@NonNull Post post) {
        return builder()
                .id(post.id())
                .title(post.title())
                .body(post.body())
                .createdAt(post.createdAt())
                .updatedAt(post.updatedAt())
                .deletedAt(post.deletedAt())
                .needsSync(post.needsSync());
    }

    @AutoValue.Builder
    public static abstract class Builder {

        @NonNull
        public abstract Builder id(@Nullable Long id);

        @NonNull
        public abstract Builder title(@NonNull String title);

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
        public abstract Post build();
    }
}
