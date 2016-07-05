package com.mobgen.droidcon.offline.shared;

import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.shared.models.Comment;
import com.mobgen.droidcon.offline.shared.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface PostService {

    @NonNull
    @GET("/posts")
    Call<List<Post>> posts();

    @NonNull
    @GET("/posts/{id}")
    Call<Post> post(@NonNull @Query("id") Integer id);

    @NonNull
    @PUT("/posts")
    Call<Post> create(@Body @NonNull Post post);

    @NonNull
    @PUT("/posts")
    Call<Post> update(@Body @NonNull Post post);

    @NonNull
    @DELETE("/posts")
    Call<Void> delete(Post post);

    @NonNull
    @GET("/comments?postId={postId}")
    Call<List<Comment>> comments(@NonNull @Query("postId") Integer postId);

    @NonNull
    @POST("/comments")
    Call<Comment> create(@Body @NonNull Comment comment);

    @NonNull
    @PUT("/comments")
    Call<Comment> update(@Body @NonNull Comment comment);

    @NonNull
    @DELETE("/comments")
    Call<Void> delete(Comment comment);
}