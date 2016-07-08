package com.mobgen.droidcon.offline.sdk.repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.mobgen.droidcon.offline.sdk.base.DatabaseManager;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.repository.local.CommentDAO;
import com.mobgen.droidcon.offline.sdk.repository.local.PostDAO;
import com.mobgen.droidcon.offline.sdk.repository.remote.PostService;
import com.mobgen.droidcon.offline.sdk.sync.SyncService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

public class PostRepository {

    private Context mContext;
    private PostService mPostService;
    private CommentDAO mCommentDao;
    private PostDAO mPostDao;

    public PostRepository(@NonNull Context context,
                          @NonNull PostService postService,
                          @NonNull PostDAO postDAO,
                          @NonNull CommentDAO commentDAO) {
        mContext = context;
        mPostService = postService;
        mCommentDao = commentDAO;
        mPostDao = postDAO;
    }

    @WorkerThread
    public List<Post> posts() {
        List<Post> posts;
        try {
            Response<List<Post>> postsResponse = mPostService.posts().execute();
            //Consume response
            if (postsResponse.isSuccessful()) {
                posts = postsResponse.body();
                mPostDao.deleteAll();
                mPostDao.save(posts);
            }
            posts = mPostDao.posts();
        } catch (IOException e) {
            posts = mPostDao.posts();
        } catch (DatabaseManager.DatabaseException e) {
            posts = null;
        }
        return posts;
    }

    @WorkerThread
    public void localDelete(@NonNull Post post) throws RepositoryException {
        try {
            long now = new Date().getTime();
            if (post.isNew() && post.isStoredLocally()) {
                mPostDao.delete(post.internalId());
                SyncService.notifyChange(mContext);
            } else {
                mPostDao.save(Post.builder(post)
                        .deletedAt(now)
                        .updatedAt(now)
                        .needsSync(true).build());
                SyncService.triggerSync(mContext);
            }
        } catch (DatabaseManager.DatabaseException e) {
            throw new RepositoryException("Repository: Error saving post", e);
        }
    }

    @WorkerThread
    public void localCreate(@NonNull Post post) throws RepositoryException {
        try {
            mPostDao.save(post);
            SyncService.triggerSync(mContext);
        } catch (DatabaseManager.DatabaseException e) {
            throw new RepositoryException("Repository: Error saving post", e);
        }
    }

    @WorkerThread
    public List<Post> localPendingPosts() throws DatabaseManager.DatabaseException {
        return mPostDao.postsPendingToSync();
    }

    @WorkerThread
    public void remoteCreate(@NonNull Post post) throws DatabaseManager.DatabaseException, IOException {
        //Remove the internal remoteId
        Response<Post> postResponse = mPostService.create(Post.builder(post)
                .internalId(null)
                .build()).execute();
        if (postResponse.isSuccessful()) {
            //Add the internal remoteId again
            mPostDao.save(Post.builder(postResponse.body())
                    .internalId(post.internalId())
                    .build());
        }
    }

    @WorkerThread
    public void remoteDelete(@NonNull Post post) throws IOException, DatabaseManager.DatabaseException {
        if (mPostService.deletePost(post.id()).execute().isSuccessful() && post.isStoredLocally()) {
            mPostDao.delete(post.internalId());
        }
    }


    @WorkerThread
    public List<Comment> comments(@NonNull Post post) {
        List<Comment> comments;
        try {
            if (!post.isNew()) {
                Response<List<Comment>> commentsResponse = mPostService.comments(post.id()).execute();
                //Consume response
                if (commentsResponse.isSuccessful()) {
                    comments = commentsResponse.body();
                    mCommentDao.delete(post.internalId());
                    mCommentDao.save(post, comments);
                }
            }
            comments = mCommentDao.comments(post.internalId());
        } catch (IOException e) {
            comments = mCommentDao.comments(post.internalId());
        } catch (DatabaseManager.DatabaseException e) {
            comments = null;
        }
        return comments;
    }

    @WorkerThread
    public List<Comment> localPendingComments() throws DatabaseManager.DatabaseException {
        return mCommentDao.commentsPendingToSync();
    }

    @WorkerThread
    public void localDelete(@NonNull Comment comment) throws RepositoryException {
        try {
            if (comment.isNew() && comment.isStoredLocally()) {
                mCommentDao.delete(comment.internalId());
                SyncService.notifyChange(mContext);
            } else {
                long now = new Date().getTime();
                mCommentDao.save(Comment.builder(comment)
                        .deletedAt(now)
                        .updatedAt(now)
                        .needsSync(true).build());
                SyncService.triggerSync(mContext);
            }
        } catch (DatabaseManager.DatabaseException e) {
            throw new RepositoryException("Repository: Error saving post", e);
        }
    }

    @WorkerThread
    public void localCreate(@NonNull Comment comment) throws RepositoryException {
        try {
            mCommentDao.save(comment);
            SyncService.triggerSync(mContext);
        } catch (DatabaseManager.DatabaseException e) {
            throw new RepositoryException("Repository: Error saving post", e);
        }
    }

    @WorkerThread
    public void remoteCreate(@NonNull Comment comment) throws DatabaseManager.DatabaseException, IOException {
        //Remove the internal remoteId
        Response<Comment> commentResponse = mPostService.create(Comment.builder(comment)
                .internalId(null)
                .internalPostId(null)
                .build()).execute();
        if (commentResponse.isSuccessful()) {
            //Add the internal remoteId again
            mCommentDao.save(Comment.builder(commentResponse.body())
                    .internalId(comment.internalId())
                    .build());
        }
    }

    @WorkerThread
    public void remoteDelete(@NonNull Comment comment) throws IOException, DatabaseManager.DatabaseException {
        if (mPostService.deleteComment(comment.id()).execute().isSuccessful() && comment.isStoredLocally()) {
            mCommentDao.delete(comment.internalId());
        }
    }

    public PostService postService() {
        return mPostService;
    }
}
