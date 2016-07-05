package com.mobgen.droidcon.offline.shared.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.demos.online.WebServiceCommentActivity;
import com.mobgen.droidcon.offline.shared.adapters.PostAdapter;
import com.mobgen.droidcon.offline.shared.models.Post;
import com.mobgen.droidcon.offline.shared.utils.NetworkMonitor;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseArticleActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, PostAdapter.PostListener, NetworkMonitor.ConnectivityListener {

    private NetworkMonitor mNetworkMonitor;

    @BindView(R.id.rv_list)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.srl_list)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.ll_offline)
    View mNotAvailableInternet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_reload);
        setTitle(getString(R.string.web_service_sample));
        ButterKnife.bind(this);
        mNetworkMonitor = NetworkMonitor.register(this, this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefresh.setOnRefreshListener(this);
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
        mSwipeRefresh.setRefreshing(false);
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

    public void setAdapter(@NonNull PostAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onSelected(@NonNull Post post) {
        //Post clicked go to next screen
        WebServiceCommentActivity.start(this, post);
    }
}
