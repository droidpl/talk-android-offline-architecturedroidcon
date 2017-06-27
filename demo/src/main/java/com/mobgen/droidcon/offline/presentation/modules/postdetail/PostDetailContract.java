package com.mobgen.droidcon.offline.presentation.modules.postdetail;

import com.mobgen.droidcon.offline.presentation.BasePresenter;
import com.mobgen.droidcon.offline.presentation.BaseView;
import com.mobgen.droidcon.offline.sdk.models.Comment;

/**
 * Created by NoaD on 26/06/2017.
 */

public interface PostDetailContract {

    interface View extends BaseView<Presenter> {
        void showError();
    }

    interface Presenter extends BasePresenter{
        void createComment(Comment comment);
        void deleteComment(Comment comment);
    }
}
