package com.mobgen.droidcon.offline.sdk.database;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.model.dbmodels.CommentModel;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.models.dbmodels.CommentDb;

public class PostDAO {

    private DatabaseManager mDatabaseManager;

    public void save(Post post){

    }

    public void save(final Comment comment) throws Exception {
        mDatabaseManager.transaction(new DatabaseManager.Transaction() {
            @Override
            public void onTransaction(@NonNull SQLiteDatabase database) throws Exception {
                database.insert(CommentModel.TABLE_NAME, null, comment.marshal());
            }
        });
    }
}
