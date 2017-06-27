package com.mobgen.droidcon.offline.sdk.repository.remote;

import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.base.DatabaseException;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;

import java.io.IOException;

/**
 * Created by NoaD on 05/04/2017.
 */

public interface SyncStore {

    void remoteCreate(@NonNull Post post) throws DatabaseException, IOException;
    void remoteDelete(@NonNull Post post) throws IOException, DatabaseException;

    void remoteCreate(@NonNull Comment comment) throws DatabaseException, IOException;
    void remoteDelete(@NonNull Comment comment) throws IOException, DatabaseException;
}
