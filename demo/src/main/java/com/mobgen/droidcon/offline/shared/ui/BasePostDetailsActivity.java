package com.mobgen.droidcon.offline.shared.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.sdk.models.Comment;
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.shared.adapters.CommentAdapter;
import com.mobgen.droidcon.offline.shared.utils.NetworkMonitor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BasePostDetailsActivity extends AppCompatActivity implements View.OnClickListener, NetworkMonitor.ConnectivityListener, SwipeRefreshLayout.OnRefreshListener, NewCommentDialogFragment.NewCommentListener, CommentAdapter.CommentListener {

    protected static final String BUNDLE_POST_ARGUMENT = "post_argument";
    private static final String DIALOG_CREATE_TAG = "create_post_dialog";

    private NetworkMonitor mNetworkMonitor;
    private CommentAdapter mAdapter;

    @BindView(R.id.rv_list)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.srl_list)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.ll_offline)
    View mNotAvailableInternet;
    @BindView(R.id.fab_add_new_comment)
    FloatingActionButton mFloatingActionButtonNew;

    protected Post mCurrentPost;

    protected static Intent getIntent(Context context, Post post, Class clazz) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(BUNDLE_POST_ARGUMENT, post);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ButterKnife.bind(this);
        mNetworkMonitor = NetworkMonitor.register(this, this);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null || !intent.getExtras().containsKey(BUNDLE_POST_ARGUMENT)) {
            throw new IllegalArgumentException("This activity requires a post to be started. Use the start method");
        }
        mCurrentPost = intent.getParcelableExtra(BUNDLE_POST_ARGUMENT);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefresh.setOnRefreshListener(this);
        mFloatingActionButtonNew.setOnClickListener(this);
        if (savedInstanceState != null) {
            NewCommentDialogFragment fragment = (NewCommentDialogFragment) getFragmentManager().findFragmentByTag(DIALOG_CREATE_TAG);
            if (fragment != null) {
                fragment.setCommentListener(this);
            }
        }
    }

    protected void startLoading() {
        //Avoid the first refresh bug for swipe refresh view
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
            }
        });
    }

    protected void stopLoading() {
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister monitor
        if (mNetworkMonitor != null) {
            mNetworkMonitor.unsubscribe(this);
            mNetworkMonitor = null;
        }
    }

    @Override
    public void onConnectionChanged(boolean available) {
        if (available) {
            mNotAvailableInternet.setVisibility(View.GONE);
        } else {
            mNotAvailableInternet.setVisibility(View.VISIBLE);
        }
    }

    public void setAdapter(@Nullable CommentAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setAdapter(@Nullable List<Comment> comments) {
        if (mAdapter != null) {
            mAdapter.comments(comments);
        } else {
            setAdapter(new CommentAdapter(mCurrentPost, comments, this));
        }
    }

    private void onAddNewPressed() {
        NewCommentDialogFragment dialog = NewCommentDialogFragment.create(mCurrentPost);
        dialog.setCommentListener(this);
        dialog.show(getFragmentManager(), DIALOG_CREATE_TAG);
    }

    @Override
    public void onClick(View view) {
        if (view == mFloatingActionButtonNew) {
            onAddNewPressed();
        }
    }
}
