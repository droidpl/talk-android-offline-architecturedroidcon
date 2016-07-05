package com.mobgen.droidcon.offline.shared.models;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mobgen.droidcon.offline.base.AutoGson;

@AutoValue
@AutoGson(autoClass = AutoValue_Post.class)
public abstract class Post implements Parcelable {

    @Nullable
    public abstract Integer id();
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

    @AutoValue.Builder
    public static abstract class Builder {
        @NonNull
        public abstract Builder id(@Nullable Integer id);
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
        public abstract Post build();
    }
}
