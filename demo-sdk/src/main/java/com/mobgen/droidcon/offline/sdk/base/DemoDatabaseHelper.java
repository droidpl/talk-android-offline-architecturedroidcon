package com.mobgen.droidcon.offline.sdk.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.model.db.CommentModel;
import com.mobgen.droidcon.offline.sdk.model.db.PostModel;


public class DemoDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "demo.db";

    public DemoDatabaseHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PostModel.CREATE_TABLE);
        db.execSQL(CommentModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //No upgrading scripts
    }
}
