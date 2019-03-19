package com.example.i_to_sky.functionproject.view.recodevideo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.i_to_sky.functionproject.R;
import com.example.i_to_sky.functionproject.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weiyupei on 2019/3/6.
 */

public class ReadyRecordVideoView extends FrameLayout {

    private static final int TIME_INTERVAL = 1000;

    private static final int DEFAULT = 0;
    private static final int READY_RECORD_VIDEO = 1;
    private static final int RECORDING_VIDEO = 2;
    private static final int READY_RECORD_VIDEO_AGAING = 3;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    private float mStartX;
    private float mStartY;
    private float mScreenX;
    private float mScreenY;
    private int mViewStatus = DEFAULT;

    private Handler mHandler = new Handler();
    private int mDuration = -1;

    private OnViewClickListener mOnViewClickListener;

    @BindView(R.id.ready_record_video_layout)
    View mReadyLayout;
    @BindView(R.id.record_video_layout)
    View mRecordingLayout;
    @BindView(R.id.recording_time)
    TextView mRecordingTime;

    public ReadyRecordVideoView(@NonNull Context context) {
        super(context);
        initView();
    }

    public ReadyRecordVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ReadyRecordVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View layoutView = LayoutInflater.from(getContext()).inflate(R.layout.view_layout_ready_record_video, this);
        ButterKnife.bind(layoutView);
        mWindowManager = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }

    @OnClick(R.id.record_video_start)
    public void onRecordStartClick() {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onRecordStartClick();
        }
        mReadyLayout.setVisibility(GONE);
        mRecordingLayout.setVisibility(VISIBLE);
        mViewStatus = RECORDING_VIDEO;
        mHandler.removeCallbacks(mTimeRunnable);
        mHandler.post(mTimeRunnable);
    }

    @OnClick(R.id.record_video_close)
    public void onRecordCloseClick() {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onRecordCloseClick();
        }
        mWindowManager.removeView(this);
        mViewStatus = DEFAULT;
        mDuration = -1;
        mHandler.removeCallbacks(mTimeRunnable);
    }

    @OnClick(R.id.record_video_stop)
    public void onRecordStopClick() {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onRecordStopClick();
        }
        mReadyLayout.setVisibility(VISIBLE);
        mRecordingLayout.setVisibility(GONE);
        mViewStatus = READY_RECORD_VIDEO_AGAING;
        mDuration = -1;
        mHandler.removeCallbacks(mTimeRunnable);
    }

    private Runnable mTimeRunnable = new Runnable() {
        @Override
        public void run() {
            mDuration += 1;
            mRecordingTime.setText(getDuration(mDuration));
            mHandler.postDelayed(mTimeRunnable, TIME_INTERVAL);
        }
    };

    private String getDuration(int duration) {
        String result = "00:00";
        int minute = duration / 60;
        int second = duration % 60;
        String minuteStr = String.valueOf(minute);
        String secondStr = String.valueOf(second);
        if (minute < 10) {
            minuteStr = "0" + minuteStr;
        }
        if (second < 10) {
            secondStr = "0" + secondStr;
        }
        result = minuteStr + ":" + secondStr;
        return result;
    }

    public void show() {
        switch (mViewStatus) {
            case DEFAULT:
                showView();
                mViewStatus = READY_RECORD_VIDEO;
                break;
            case READY_RECORD_VIDEO:
            case READY_RECORD_VIDEO_AGAING:
                ToastUtil.showToastLong(R.string.ready_record_video_hint);
                break;
            case RECORDING_VIDEO:
                ToastUtil.showToastLong(R.string.recording_video_hint);
                break;
        }
    }

    private void showView() {
        if (mLayoutParams == null) {
            mLayoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                /*
                 * api >= 25时，直接禁用了TYPE_TOAST,所以使用了TYPE_APPLICATION_OVERLAY，但是需要去打开app的悬浮窗权限
                 * 本模块主要是为了实现了解录屏的功能，对于app体验上来说，暂时不多做优化
                 * 后期有时间会考虑在这种情况下引导用户跳转到权限设置页面
                 */
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                /*
                 * 因为国产手机定制Android系统的原因，很可能会关闭悬浮穿的权限
                 * 在api < 25的情况下，当type设置成TYPE_TOAST时候，可以避免因为悬浮窗权限关闭，导致不能弹出悬浮穿的问题
                 */
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mLayoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.x = 60;
            mLayoutParams.y = 60;
        }
        mWindowManager.addView(this, mLayoutParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mScreenX = event.getRawX();
                mScreenY = event.getRawY();
                float slideX = mScreenX - mStartX;
                float slideY = mScreenY - mStartY;
                updateViewPosition(slideX, slideY);
                mStartX = mScreenX;
                mStartY = mScreenY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void updateViewPosition(float slideX, float slideY) {
        mLayoutParams.x = mLayoutParams.x - (int) slideX;
        mLayoutParams.y = mLayoutParams.y - (int) slideY;
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }

    public void setOnViewClickListner(OnViewClickListener listener) {
        mOnViewClickListener = listener;
    }

    public interface OnViewClickListener{
        void onRecordStartClick();
        void onRecordCloseClick();
        void onRecordStopClick();
    }

}
