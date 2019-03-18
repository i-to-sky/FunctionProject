package com.example.i_to_sky.functionproject.task.longpic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.example.i_to_sky.functionproject.application.AppApplication;
import com.example.i_to_sky.functionproject.utils.ImageUtil;
import com.example.i_to_sky.functionproject.utils.ScreenUtil;
import com.example.i_to_sky.functionproject.utils.TimeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by weiyupei on 2018/12/2.
 */

public class LongPicGenerateTask extends AsyncTask<Void, Integer, String> {

    private static final String LONG_PIC_SUFFIX = ".jpg";

    private static int OUTPUT_WIDTH = ScreenUtil.getScreenWidth() / 2;
    private static final int MARGIN = 2;

    private ArrayList<String> mImageUrlList;
    private String mLongPicName;
    private OnGenerateLongPicListener mOnGenerateLongPicListener;

    public LongPicGenerateTask(ArrayList<String> imageUrlList) {
        this(imageUrlList, 0, "");
    }

    public LongPicGenerateTask(ArrayList<String> imageUrlList, int outputWidth) {
        this(imageUrlList, outputWidth, "");
    }

    public LongPicGenerateTask(ArrayList<String> imageUrlList, String longPicName) {
        this(imageUrlList, 0, longPicName);
    }

    public LongPicGenerateTask(ArrayList<String> imageUrlList, int outputWidth, String longPicName) {
        mImageUrlList = imageUrlList;
        if (outputWidth > 0) {
            OUTPUT_WIDTH = outputWidth;
        }
        if (!TextUtils.isEmpty(longPicName)) {
            mLongPicName = longPicName;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mOnGenerateLongPicListener != null) {
            mOnGenerateLongPicListener.onStart();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        int totalHeight = getImageTotalHeight(mImageUrlList);
        Bitmap longPic = Bitmap.createBitmap(OUTPUT_WIDTH, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(longPic);
        int offset = 0;
        for (int i = 0; i < mImageUrlList.size(); i++) {
            offset += drawImage(canvas, mImageUrlList.get(i), offset);
            int progress = (int) (((float) (i + 1) / (float) mImageUrlList.size()) * 100);
            publishProgress(progress);
        }
        return saveBitmapToSdcard(longPic, mLongPicName);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mOnGenerateLongPicListener != null) {
            mOnGenerateLongPicListener.onProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(String filePath) {
        super.onPostExecute(filePath);
        if (mOnGenerateLongPicListener != null) {
            mOnGenerateLongPicListener.onSuccess(filePath, OUTPUT_WIDTH);
        }
    }

    private int getImageTotalHeight(ArrayList<String> imageUrlList) {
        int height = 0;
        for (String path : imageUrlList) {
            height += getImageHeight(path);
        }
        return height;
    }

    private int getImageHeight(String path) {
        if (path != null) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    options.inScaled = false;
                    BitmapFactory.decodeStream(new FileInputStream(path), null, options);
                    int height = 0;
                    if (options.outWidth != 0) {
                        height = options.outHeight * OUTPUT_WIDTH / options.outWidth;
                    }
                    return height;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private int drawImage(Canvas canvas, String imagePath, int offset) {
        if (canvas == null || TextUtils.isEmpty(imagePath)) {
            return 0;
        }
        Bitmap bitmap = getBitmap(imagePath);
        int finalHeight = (int) ((float) bitmap.getHeight() / (float) bitmap.getWidth() * OUTPUT_WIDTH);
        Rect finalRect = new Rect(MARGIN, offset + MARGIN, OUTPUT_WIDTH - MARGIN, offset + finalHeight);
        if (!bitmap.isRecycled()) {
            canvas.drawBitmap(bitmap, null, finalRect, null);
        }
        bitmap.recycle();
        return finalHeight;
    }

    private Bitmap getBitmap(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = ImageUtil.calculateInSampleSize(options, OUTPUT_WIDTH, (int) ((float) options.outHeight / (float) options.outWidth * (float) OUTPUT_WIDTH));
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    private String saveBitmapToSdcard(Bitmap bitmap, String fileName) {
        String finalFileName = TimeUtil.millis2String(System.currentTimeMillis());
        if (!TextUtils.isEmpty(fileName)) {
            finalFileName = fileName;
        }
        finalFileName += LONG_PIC_SUFFIX;
        try {
            String filePath = getFilePath(finalFileName);
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().exists();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            bitmap.recycle();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getFilePath(String fileName) {
        return AppApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + fileName;
    }

    public void setOnGenerateLongPicListener(OnGenerateLongPicListener onGenerateLongPicListener) {
        mOnGenerateLongPicListener = onGenerateLongPicListener;
    }

    public interface OnGenerateLongPicListener {

        void onStart();

        void onProgress(int progress);

        void onSuccess(String filePath, int imageWidth);
    }

}
