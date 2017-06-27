package com.mobgen.droidcon.offline.presentation.modules.postdetail;

import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.repository.DataStore;
import com.mobgen.droidcon.offline.sdk.repository.RepositoryException;

import java.util.concurrent.Executor;

/**
 * Created by NoaD on 26/06/2017.
 */

public class PostDetailPresenter implements PostDetailContract.Presenter {

    private Executor mExecutor;
    private DataStore mRepository;
    private PostDetailContract.View view;

    public PostDetailPresenter(PostDetailContract.View view,
                               DataStore mRepository,
                               Executor mExecutor){

        this.view = view;
        this.mRepository = mRepository;
        this.mExecutor = mExecutor;

        this.view.setPresenter(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void createComment(final Comment comment) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mRepository.localCreate(comment);
                } catch (RepositoryException e) {
                    view.showError();
                }
            }
        });
    }

    @Override
    public void deleteComment(final Comment comment) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mRepository.localDelete(comment);
                } catch (RepositoryException e) {
                    view.showError();
                }
            }
        });
    }
}
