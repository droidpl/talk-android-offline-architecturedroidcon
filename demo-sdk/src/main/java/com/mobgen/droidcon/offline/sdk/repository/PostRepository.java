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
    public List<Post> posts() throws RepositoryException {
        List<Post> posts;
        try {
            Response<List<Post>> postsResponse = mPostService.posts().execute();
            //Consume response
            if (postsResponse.isSuccessful()) {
                posts = postsResponse.body();
                mPostDao.deleteAll();
                mPostDao.save(posts);
            } else {
                posts = mPostDao.posts();
            }
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
            if(post.isNew()){
                mPostDao.delete(post.id());
                SyncService.notifyChange(mContext);
            }else {
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
        Response<Post> postResponse = mPostService.create(post).execute();
        if (postResponse.isSuccessful()) {
            mPostDao.save(postResponse.body());
        }
    }

    @WorkerThread
    public void remoteDelete(@NonNull Post post) throws IOException, DatabaseManager.DatabaseException {
        if (mPostService.deletePost(post.id()).execute().isSuccessful()) {
            mPostDao.delete(post.id());
        }
    }


    @WorkerThread
    public List<Comment> comments(@NonNull Post post) {
        List<Comment> comments;
        try {
            Response<List<Comment>> commentsResponse = mPostService.comments(post.id()).execute();
            //Consume response
            if (commentsResponse.isSuccessful()) {
                comments = commentsResponse.body();
                mCommentDao.delete(post.id());
                mCommentDao.save(comments);
            } else {
                comments = mCommentDao.comments(post.id());
            }
        } catch (IOException e) {
            comments = mCommentDao.comments(post.id());
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
            if(comment.isNew()){
                mCommentDao.delete(comment.id());
                SyncService.notifyChange(mContext);
            }else {
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
        Response<Comment> postResponse = mPostService.create(comment).execute();
        if (postResponse.isSuccessful()) {
            mCommentDao.save(postResponse.body());
        }
    }

    @WorkerThread
    public void remoteDelete(@NonNull Comment comment) throws IOException, DatabaseManager.DatabaseException {
        if (mPostService.deletePost(comment.id()).execute().isSuccessful()) {
            mCommentDao.delete(comment.id());
        }
    }

    public PostService postService() {
        return mPostService;
    }
}
