package com.example.i_to_sky.functionproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.i_to_sky.functionproject.R;
import com.example.i_to_sky.functionproject.activity.base.BaseActivity;
import com.example.i_to_sky.functionproject.view.recodevideo.ReadyRecordVideoView;

import butterknife.OnClick;

public class RecordVideoActivity extends BaseActivity {

    private ReadyRecordVideoView mRecordVideoView;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, RecordVideoActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void handleIntent(Intent intent) {

    }

    @OnClick(R.id.record_video)
    public void onRecordVideoClick() {
        if (mRecordVideoView == null) {
            mRecordVideoView = new ReadyRecordVideoView(this);
        }
        mRecordVideoView.show();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
