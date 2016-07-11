package com.mobgen.droidcon.offline.shared.ui;

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
import com.mobgen.droidcon.offline.sdk.models.Post;
import com.mobgen.droidcon.offline.shared.adapters.PostAdapter;
import com.mobgen.droidcon.offline.shared.utils.NetworkMonitor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BasePostActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NetworkMonitor.ConnectivityListener, View.OnClickListener, NewPostDialogFragment.NewPostListener, PostAdapter.PostListener {

    private static final String DIALOG_CREATE_TAG = "create_post_dialog";

    private NetworkMonitor mNetworkMonitor;
    private PostAdapter mAdapter;

    @BindView(R.id.rv_list)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.srl_list)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.ll_offline)
    View mNotAvailableInternet;
    @BindView(R.id.fab_add_new_post)
    FloatingActionButton mFloatingActionButtonNew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        ButterKnife.bind(this);
        mNetworkMonitor = NetworkMonitor.register(this, this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefresh.setOnRefreshListener(this);
        mFloatingActionButtonNew.setOnClickListener(this);
        if (savedInstanceState != null) {
            NewPostDialogFragment fragment = (NewPostDialogFragment) getFragmentManager().findFragmentByTag(DIALOG_CREATE_TAG);
            if (fragment != null) {
                fragment.setPostListener(this);
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

    public void setAdapter(@Nullable PostAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setAdapter(@Nullable List<Post> posts) {
        if (mAdapter != null) {
            mAdapter.posts(posts);
        } else {
            setAdapter(new PostAdapter(posts, this));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mFloatingActionButtonNew) {
            onAddNewPressed();
        }
    }

    private void onAddNewPressed() {
        NewPostDialogFragment dialog = NewPostDialogFragment.create();
        dialog.setPostListener(this);
        dialog.show(getFragmentManager(), DIALOG_CREATE_TAG);
    }
}
