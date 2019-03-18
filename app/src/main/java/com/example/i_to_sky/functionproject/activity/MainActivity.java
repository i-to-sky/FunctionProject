package com.example.i_to_sky.functionproject.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.i_to_sky.functionproject.R;
import com.example.i_to_sky.functionproject.activity.base.BaseActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    @OnClick(R.id.generate_longpic_function)
    public void onGenerateLongpicClick() {
        Intent intent = ImageFolderActivity.createIntent(this);
        startActivity(intent);
    }

    @OnClick(R.id.record_video_function)
    public void onRecordVideoClick() {
        Intent intent = RecordVideoActivity.createIntent(this);
        startActivity(intent);
    }

}
