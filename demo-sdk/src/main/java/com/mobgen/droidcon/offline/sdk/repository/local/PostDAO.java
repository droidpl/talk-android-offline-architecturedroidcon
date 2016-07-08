package com.mobgen.droidcon.offline.sdk.repository.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.mobgen.droidcon.offline.sdk.base.DatabaseManager;
import com.mobgen.droidcon.offline.sdk.model.db.CommentModel;
import com.mobgen.droidcon.offline.sdk.model.db.PostModel;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.models.db.PostDb;

import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    private DatabaseManager mDatabaseManager;

    public PostDAO(DatabaseManager databaseManager) {
        mDatabaseManager = databaseManager;
    }

    @WorkerThread
    public void save(@NonNull final Post post) throws DatabaseManager.DatabaseException {
        mDatabaseManager.transaction(new DatabaseManager.Transaction() {
            @Override
            public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                database.insertWithOnConflict(PostModel.TABLE_NAME, null, post.marshal(), SQLiteDatabase.CONFLICT_REPLACE);
            }
        });
    }

    @WorkerThread
    public void save(@NonNull final List<Post> posts) throws DatabaseManager.DatabaseException {
        mDatabaseManager.transaction(new DatabaseManager.Transaction() {
            @Override
            public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                for (Post post : posts) {
                    database.insertWithOnConflict(PostModel.TABLE_NAME, null, post.marshal(), SQLiteDatabase.CONFLICT_REPLACE);
                }
            }
        });
    }

    @WorkerThread
    public List<Post> posts() {
        final List<Post> posts = new ArrayList<>();
        try {
            mDatabaseManager.transaction(new DatabaseManager.Transaction() {
                @Override
                public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                    try (Cursor cursor = database.rawQuery(PostModel.SELECTPOSTS, null)) {
                        while (cursor.moveToNext()) {
                            posts.add(PostDb.ALL_POSTS_MAPPER.map(cursor).asModel());
                        }
                    }
                }
            });
        } catch (DatabaseManager.DatabaseException e) {
            Log.e("Database", "Error in the database", e);
        }
        return posts;
    }

    @WorkerThread
    @NonNull
    public List<Post> postsPendingToSync() throws DatabaseManager.DatabaseException {
        final List<Post> posts = new ArrayList<>();
        mDatabaseManager.transaction(new DatabaseManager.Transaction() {
            @Override
            public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                try (Cursor cursor = database.rawQuery(PostModel.SELECTSYNCPOSTS, null)) {
                    while (cursor.moveToNext()) {
                        posts.add(PostDb.POST_SYNC_MAPPER.map(cursor).asModel());
                    }
                }
            }
        });
        return posts;
    }

    @WorkerThread
    public void delete(@Nullable final Long internalId) throws DatabaseManager.DatabaseException {
        if(internalId != null) {
            mDatabaseManager.transaction(new DatabaseManager.Transaction() {
                @Override
                public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                    database.execSQL(CommentModel.DELETECOMMENTSBYPOST, new Long[]{internalId});
                    database.execSQL(PostModel.DELETEPOST, new Long[]{internalId});
                }
            });
        }
    }

    @WorkerThread
    public void deleteAll() throws DatabaseManager.DatabaseException {
        mDatabaseManager.transaction(new DatabaseManager.Transaction() {
            @Override
            public void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseManager.DatabaseException {
                database.execSQL(PostModel.DELETEALL);
            }
        });
    }
}
