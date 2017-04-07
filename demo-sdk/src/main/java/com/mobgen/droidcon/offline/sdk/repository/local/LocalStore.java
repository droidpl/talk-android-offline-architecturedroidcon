package com.mobgen.droidcon.offline.sdk.repository.local;

import android.support.annotation.NonNull;

import com.mobgen.droidcon.offline.sdk.base.DatabaseException;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.repository.RepositoryException;

import java.util.List;

/**
 * Created by NoaD on 06/04/2017.
 */

public interface LocalStore {

    List<Post> posts();
    void localDelete(@NonNull Post post) throws RepositoryException;
    void localCreate(@NonNull Post post) throws RepositoryException;
    List<Post> localPendingPosts() throws DatabaseException;

    List<Comment> comments(@NonNull Post post);
    List<Comment> localPendingComments() throws DatabaseException;
    void localDelete(@NonNull Comment comment) throws RepositoryException;
    void localCreate(@NonNull Comment comment) throws RepositoryException;
}
