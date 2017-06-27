package com.mobgen.droidcon.offline.presentation.modules.post;

import com.mobgen.droidcon.offline.presentation.BasePresenter;
import com.mobgen.droidcon.offline.presentation.BaseView;
import com.mobgen.droidcon.offline.sdk.models.Post;

/**
 * Created by NoaD on 26/06/2017.
 */

public interface PostContract {

    interface View extends BaseView<Presenter> {
        void showError();
    }

    interface Presenter extends BasePresenter {

        void createPost(Post post);
        void deletePost(Post post);

    }

}
