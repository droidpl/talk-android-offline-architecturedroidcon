package com.mobgen.droidcon.offline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mobgen.droidcon.offline.demos.online.WebServicePostActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_selector);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_demo_selector));
    }

    @OnClick(R.id.bt_web_service)
    public void onWebServiceClick() {
        WebServicePostActivity.start(this);
    }

    @OnClick(R.id.bt_repository)
    public void onRepositoryClick() {

    }

    @OnClick(R.id.bt_loader_job_scheduler)
    public void onLoadersClick() {

    }
}
