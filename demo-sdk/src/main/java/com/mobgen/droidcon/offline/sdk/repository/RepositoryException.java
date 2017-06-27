package com.mobgen.droidcon.offline.sdk.repository;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String detailMessage) {
        super(detailMessage);
    }

    public RepositoryException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
