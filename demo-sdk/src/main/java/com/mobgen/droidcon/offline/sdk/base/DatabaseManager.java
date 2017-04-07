package com.mobgen.droidcon.offline.sdk.base;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseManager {

    private static AtomicInteger sOpenCount = new AtomicInteger();
    private static DemoDatabaseHelper sDatabaseHelper;
    private static DatabaseManager sDatabaseManager;
    private static SQLiteDatabase sDatabase;

    private DatabaseManager() {
        //Do nothing, just protect the constructor
    }

    @NonNull
    public static synchronized DatabaseManager initialize(DemoDatabaseHelper helper) {
        if (sDatabaseManager == null) {
            sDatabaseManager = new DatabaseManager();
        }
        sDatabaseHelper = helper;
        return sDatabaseManager;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (sOpenCount.incrementAndGet() == 1) {
            sDatabase = sDatabaseHelper.getWritableDatabase();
        }
        return sDatabase;
    }

    public synchronized void closeDatabase() {
        if (sOpenCount.decrementAndGet() == 0) {
            sDatabase.close();
        }
    }

    public void transaction(@NonNull Transaction transaction) throws DatabaseException {
        SQLiteDatabase database = openDatabase();
        database.beginTransaction();
        try {
            transaction.onTransaction(database);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Database error", e.getMessage(), e);
            throw new DatabaseException("Error while performing the transaction", e);
        } finally {
            database.endTransaction();
            closeDatabase();
        }

    }

    public interface Transaction {
        void onTransaction(@NonNull SQLiteDatabase database) throws DatabaseException;
    }
}