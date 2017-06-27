package com.mobgen.droidcon.offline.presentation.modules.post;

import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.sdk.repository.DataStore;
import com.mobgen.droidcon.offline.sdk.repository.RepositoryException;

import java.util.concurrent.Executor;

/**
 * Created by NoaD on 26/06/2017.
 */

public class PostPresenter implements PostContract.Presenter{

    private Executor mExecutor;
    private DataStore mRepository;
    private PostContract.View view;

    public PostPresenter(PostContract.View view,
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
    public void createPost(final Post post) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mRepository.localCreate(post);
                } catch (RepositoryException e) {
                    view.showError();
                }
            }
        });
    }

    @Override
    public void deletePost(final Post post) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mRepository.localDelete(post);
                } catch (RepositoryException e) {
                    view.showError();
                }
            }
        });
    }
}
