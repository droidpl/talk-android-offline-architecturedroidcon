package com.mobgen.droidcon.offline.shared.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mobgen.droidcon.offline.R;
import com.mobgen.droidcon.offline.demos.offline.OfflinePostActivity;
import com.mobgen.droidcon.offline.demos.online.OnlinePostActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_selector);
        ButterKnife.bind(this);
        setTitle(R.string.title_demo_selector);
    }

    @OnClick(R.id.bt_web_service)
    public void onWebServiceClick() {
        OnlinePostActivity.start(this);
    }

    @OnClick(R.id.bt_loader_job_scheduler)
    public void onLoadersClick() {
        OfflinePostActivity.start(this);
    }
}
