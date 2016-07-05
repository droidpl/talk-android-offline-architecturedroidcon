package com.mobgen.droidcon.offline.demos.online;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.base.DemosApplication;
import com.mobgen.droidcon.offline.shared.PostService;
import com.mobgen.droidcon.offline.shared.adapters.PostAdapter;
import com.mobgen.droidcon.offline.shared.models.Post;
import com.mobgen.droidcon.offline.shared.utils.NetworkMonitor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServiceArticleActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, PostAdapter.PostListener, NetworkMonitor.ConnectivityListener {

    private PostService mPostService;
    private Call<List<Post>> mOnGoingCall;
    private PostAdapter mAdapter;
    private NetworkMonitor mNetworkMonitor;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.srl_list)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.ll_offline)
    View mNotAvailableInternet;

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, WebServiceArticleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_reload);
        setTitle(getString(R.string.web_service_sample));
        ButterKnife.bind(this);
        mPostService = DemosApplication.instance()
                .client()
                .create(PostService.class);

        mNetworkMonitor = NetworkMonitor.register(this, this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefresh.setOnRefreshListener(this);
        reloadArticles();
    }

    @Override
    public void onRefresh() {
        reloadArticles();
    }

    private void reloadArticles() {
        mOnGoingCall = mPostService.posts();
        startLoading();
        mOnGoingCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                mAdapter = new PostAdapter(WebServiceArticleActivity.this, response.body(), WebServiceArticleActivity.this);
                mRecyclerView.swapAdapter(mAdapter, true);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView(), getString(R.string.error_load_comments), Snackbar.LENGTH_LONG).show();
                stopLoading();
            }
        });
    }

    private void startLoading() {
        //Avoid the first refresh bug for swipe refresh view
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
            }
        });
    }

    private void stopLoading() {
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOnGoingCall != null) {
            mOnGoingCall.cancel();
        }

        //Unregister monitor
        if (mNetworkMonitor != null) {
            mNetworkMonitor.unsubscribe(this);
            mNetworkMonitor = null;
        }
    }

    @Override
    public void onPostSelected(@NonNull Post post) {
        //Post clicked go to next screen
        WebServiceCommentActivity.start(this, post);
    }

    @Override
    public void onConnectionChanged(boolean available) {
        if (available) {
            mNotAvailableInternet.setVisibility(View.GONE);
        } else {
            mNotAvailableInternet.setVisibility(View.VISIBLE);
        }
    }
}
