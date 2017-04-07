package com.mobgen.droidcon.offline.sdk.repository;

import com.mobgen.droidcon.offline.sdk.repository.local.LocalStore;
import com.mobgen.droidcon.offline.sdk.repository.remote.SyncStore;

/**
 * Created by dalgarins on 02/04/2017.
 */

public interface DataStore extends LocalStore, SyncStore {
}
