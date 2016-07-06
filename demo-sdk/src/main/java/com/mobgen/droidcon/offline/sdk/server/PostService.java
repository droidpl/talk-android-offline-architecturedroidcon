package com.mobgen.droidcon.offline.sdk.server;

import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostService {

    @NonNull
    @GET("/posts")
    Call<List<Post>> posts();

    @NonNull
    @GET("/posts/{id}")
    Call<Post> post(@Query("id") long id);

    @NonNull
    @POST("/posts")
    Call<Post> create(@NonNull @Body Post post);

    @NonNull
    @PUT("/posts")
    Call<Post> update(@NonNull @Body Post post);

    @NonNull
    @DELETE("/posts/{id}")
    Call<Void> deletePost(@Path("id") long id);

    @NonNull
    @GET("/comments")
    Call<List<Comment>> comments(@Query("postId") long postId);

    @NonNull
    @POST("/comments")
    Call<Comment> create(@NonNull @Body Comment comment);

    @NonNull
    @PUT("/comments")
    Call<Comment> update(@NonNull @Body Comment comment);

    @NonNull
    @DELETE("/comments/{id}")
    Call<Void> deleteComment(@Path("id") long id);
}