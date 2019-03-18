package com.example.i_to_sky.functionproject.view.longpic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.i_to_sky.functionproject.utils.ScreenUtil;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by weiyupei on 2018/12/28.
 */

public class LongPicView extends View {

    private static final String TAG = "LongPicView";

    private GestureDetector mGestureDetector;
    private ScrollerCompat mScroller;

    private volatile Rect mRect = new Rect();
    private BitmapRegionDecoder mDecoder;
    private BitmapFactory.Options mOptions;

    private static final int mDefault = 100;
    private int mImageWidth;
    private int mImageHeight;

    public LongPicView(Context context) {
        this(context, null);
    }

    public LongPicView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LongPicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFocusable(true);
        setWillNotDraw(false);
        mScroller = ScrollerCompat.create(getContext(), null);
        mGestureDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure");
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefault, mDefault);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefault, heightMeasureSpec);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSpec, mDefault);
        }

        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mImageWidth > getMeasuredWidth() ? getMeasuredWidth() : mImageWidth;
        mRect.bottom = mImageHeight > getMeasuredHeight() ? getMeasuredHeight() : mImageHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
        if (mDecoder == null) {
            return;
        }
        if (mOptions == null) {
            mOptions = new BitmapFactory.Options();
            mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        }
        Bitmap bm = mDecoder.decodeRegion(mRect, mOptions);
        int left = 0;
        if (mImageWidth < ScreenUtil.getScreenWidth()) {
            left = ScreenUtil.getScreenWidth() / 2 - mImageWidth / 2;
        }
        canvas.drawBitmap(bm, left, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    public void setBitmap(String filePath) {
        Log.d(TAG, "setBitmap");
        InputStream inputStream1 = null;
        InputStream inputStream2 = null;
        try {
            inputStream1 = new FileInputStream(filePath);
            inputStream2 = new FileInputStream(filePath);
            mDecoder = BitmapRegionDecoder.newInstance(inputStream1, false);
            BitmapFactory.Options tempOptions = new BitmapFactory.Options();
            tempOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream2, null, tempOptions);
            mImageWidth = tempOptions.outWidth;
            mImageHeight = tempOptions.outHeight;
            requestLayout();
        } catch (Exception e) {
            Log.d(TAG, "setBitmap error");
            e.printStackTrace();
        } finally {
            Log.d(TAG, "setBitmap finally");
            try {
                if (inputStream1 != null) {
                    inputStream1.close();
                }
                if (inputStream2 != null) {
                    inputStream2.close();
                }
            } catch (Exception e) {
                Log.d(TAG, "inputStream close error");
                e.printStackTrace();
            }
        }
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "GestureDetector.SimpleOnGestureListener onDown");
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d(TAG, "GestureDetector.SimpleOnGestureListener onSingleTapConfirmed");
            if (!isEnabled()) {
                return false;
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG, "GestureDetector.SimpleOnGestureListener onScroll： distanceX = " + distanceX + " distanceY = " + distanceY);
            if (!isEnabled()) {
                return false;
            }
            //如果当前位于图片顶部并且是向上滑动
            if (mRect.top <= 0 && distanceY < 0) {
                return true;
            }
            //如果当前位于图片底部并且是向下滑动
            if (mRect.bottom >= mImageHeight && distanceY > 0) {
                return true;
            }
            scrollView(distanceX, distanceY);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "GestureDetector.SimpleOnGestureListener onLongPress");
            if (!isEnabled()) {
                return;
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "GestureDetector.SimpleOnGestureListener onFling");
            if (!isEnabled()) {
                return false;
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG, "GestureDetector.SimpleOnGestureListener onDoubleTap");
            if (!isEnabled()) {
                return false;
            }
            return true;
        }
    };

    private void scrollView(float distanceX, float distanceY) {
        int x = (int) distanceX;
        int y = (int) distanceY;
        if (y > 0) {
            //向下滑动
            if (mRect.bottom + y > mImageHeight) {
                mRect.offset(0, mImageHeight - mRect.bottom);
            } else {
                mRect.offset(0, y);
            }
        } else if (y < 0) {
            //向上滑动
            if (mRect.top + y < 0) {
                mRect.offset(0, -mRect.top);
            } else {
                mRect.offset(0, y);
            }
        }
        postInvalidate();
    }
}
