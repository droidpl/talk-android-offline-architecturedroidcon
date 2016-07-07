package com.mobgen.droidcon.offline.sdk.repository.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.mobgen.droidcon.offline.sdk.base.DatabaseManager;
import com.mobgen.droidcon.offline.sdk.model.db.CommentModel;
import com.mobgen.droidcon.offline.sdk.model.db.PostModel;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.db.CommentDb;

import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    private DatabaseManager mDatabaseManager;

    public CommentDAO(DatabaseManager databaseManager) {
        mDatabaseManager = databaseManager;
    }

    @WorkerThread
    public List<Comment> comments(final long postId) {
        final List<Comment> comments = new ArrayList<>();
        try {
            mDatabaseManager.transaction(new DatabaseManager.Transaction() {
                @Override
                public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                    try (Cursor cursor = database.rawQuery(CommentModel.SELECTCOMMENTSPOST, new String[]{String.valueOf(postId)})) {
                        while (cursor.moveToNext()) {
                            comments.add(CommentDb.COMMENTS_POST_MAPPER.map(cursor).asModel());
                        }
                    }
                }
            });
        } catch (DatabaseManager.DatabaseException e) {
            Log.e("Database", "Error in the database", e);
        }
        return comments;
    }

    @WorkerThread
    public void save(@NonNull final Comment comment) throws DatabaseManager.DatabaseException {
        mDatabaseManager.transaction(new DatabaseManager.Transaction() {
            @Override
            public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                database.insertWithOnConflict(CommentModel.TABLE_NAME, null, comment.marshal(), SQLiteDatabase.CONFLICT_REPLACE);
            }
        });
    }

    @WorkerThread
    public void save(@NonNull final List<Comment> comments) throws DatabaseManager.DatabaseException {
        mDatabaseManager.transaction(new DatabaseManager.Transaction() {
            @Override
            public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                //Could be more efficient by reusing the statement, but what the hell
                for (Comment comment : comments) {
                    database.insertWithOnConflict(CommentModel.TABLE_NAME, null, comment.marshal(), SQLiteDatabase.CONFLICT_REPLACE);
                }
            }
        });
    }

    @NonNull
    @WorkerThread
    public List<Comment> commentsPendingToSync() throws DatabaseManager.DatabaseException {
        final List<Comment> comments = new ArrayList<>();
        mDatabaseManager.transaction(new DatabaseManager.Transaction() {
            @Override
            public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                try (Cursor cursor = database.rawQuery(CommentModel.SELECTSYNCCOMMENTS, null)) {
                    while(cursor.moveToNext()){
                        comments.add(CommentDb.SYNC_POSTS_MAPPER.map(cursor).asModel());
                    }
                }
            }
        });
        return comments;
    }

    @WorkerThread
    public void delete(final long commentId) throws DatabaseManager.DatabaseException {
        mDatabaseManager.transaction(new DatabaseManager.Transaction() {
            @Override
            public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                database.execSQL(PostModel.DELETEPOST, new Long[]{commentId});
            }
        });
    }
}
