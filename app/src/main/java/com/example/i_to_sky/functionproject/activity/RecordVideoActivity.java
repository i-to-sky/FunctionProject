package com.example.i_to_sky.functionproject.activity;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;

import com.example.i_to_sky.functionproject.R;
import com.example.i_to_sky.functionproject.activity.base.BaseActivity;
import com.example.i_to_sky.functionproject.utils.ToastUtil;
import com.example.i_to_sky.functionproject.view.recodevideo.ReadyRecordVideoView;

import butterknife.OnClick;

public class RecordVideoActivity extends BaseActivity {

    private static final int RECORD_VIDEO_REQUEST_CODE = 1;

    private ReadyRecordVideoView mRecordVideoView;
    private MediaProjectionManager mMediaProjectionManager;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORD_VIDEO_REQUEST_CODE && resultCode == RESULT_OK) {
            MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection  != null) {

            }
        }
    }

    private void recordStart() {
        if (mMediaProjectionManager == null) {
            mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        }
        //发送Intent申请录屏权限
        Intent intent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(intent, RECORD_VIDEO_REQUEST_CODE);
        //申请录屏权限的时，会自动跳回app中，体验不好，因此在此处调用skipLauncher()方法，视觉上没有跳入感
//        skipLauncher();
    }

    private void recordStop() {

    }

    private void recordClose() {

    }

    @OnClick(R.id.record_video)
    public void onRecordVideoClick() {
        //录屏相关的api在android5.0之后才开放
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ToastUtil.showToastShort(R.string.cannot_record_video_hint);
            return;
        }
        if (mRecordVideoView == null) {
            mRecordVideoView = new ReadyRecordVideoView(this);
            mRecordVideoView.setOnViewClickListner(new ReadyRecordVideoView.OnViewClickListener() {
                @Override
                public void onRecordStartClick() {
                    recordStart();
                }

                @Override
                public void onRecordCloseClick() {
                    recordClose();
                }

                @Override
                public void onRecordStopClick() {
                    recordStop();
                }
            });
        }
        mRecordVideoView.show();
        skipLauncher();
    }

    //跳转到桌面
    private void skipLauncher() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
