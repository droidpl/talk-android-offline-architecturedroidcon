package com.mobgen.droidcon.offline.sdk.base;

/**
 * Created by dalgarins on 02/04/2017.
 */

public class DatabaseException extends RuntimeException {
    public DatabaseException(String detailMessage) {
        super(detailMessage);
    }

    public DatabaseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
