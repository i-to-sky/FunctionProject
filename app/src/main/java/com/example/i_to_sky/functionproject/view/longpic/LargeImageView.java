package com.example.i_to_sky.functionproject.view.longpic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.i_to_sky.functionproject.utils.ScreenUtil;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by weiyupei on 2018/12/22.
 */

public class LargeImageView extends View {

    private static final String TAG = "LargeImageView";

    private BitmapRegionDecoder mDecoder;

    private int mImageWidth;

    private int mImageHeight;

    private int mMinWidth;

    private int mDefault = 100;

    private volatile Rect mRect = new Rect();

    private MoveGestureDetector mDetector;

    private static final BitmapFactory.Options mOptions = new BitmapFactory.Options();

    static {
        mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setWillNotDraw(false);
        init();
    }

    public void setImageBitmap(String filePath) {
        Log.d(TAG, "setInputStream");
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
            Log.d(TAG, "setInputStream error");
            e.printStackTrace();
        } finally {
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

    public void init() {
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector() {

            @Override
            public boolean onMove(MoveGestureDetector detector) {
                int moveX = (int) detector.getMoveX();
                int moveY = (int) detector.getMoveY();

                if (mImageWidth > getWidth()) {
                    mRect.offset(-moveX, 0);
                    checkWidth();
                    invalidate();
                } else {
                    mRect.offset(0, -moveY);
                    checkHeight();
                    invalidate();
                }
                return true;
            }
        });
    }

    private void checkWidth() {

        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.right > imageWidth) {
            rect.right = imageWidth;
            rect.left = imageWidth - getWidth();
        }

        if (rect.left < 0) {
            rect.left = 0;
            rect.right = getWidth();
        }
    }


    private void checkHeight() {

        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.bottom > imageHeight) {
            rect.bottom = imageHeight;
            rect.top = imageHeight - getHeight();
        }

        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = imageHeight;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent");
        mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
        if (mDecoder == null) {
            return;
        }
        Bitmap bm = mDecoder.decodeRegion(mRect, mOptions);
        int left = 0;
        if (mMinWidth < ScreenUtil.getScreenWidth()) {
            left = ScreenUtil.getScreenWidth() / 2 - mMinWidth / 2;
        }
        canvas.drawBitmap(bm, left, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure");
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefault, mDefault);
        } else if(widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mDefault, heightMeasureSpec);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSpec, mDefault);
        }

        mRect.left = 0;
        mRect.top = 0;
        int right = mImageWidth > getMeasuredWidth() ? getMeasuredWidth() : mImageWidth;
        mRect.right = right;
        mMinWidth = right;
        int bottom = mImageHeight > getMeasuredHeight() ? getMeasuredHeight() : mImageHeight;
        mRect.bottom = bottom;

    }

}
