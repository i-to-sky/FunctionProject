package com.example.i_to_sky.functionproject.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.i_to_sky.functionproject.R;
import com.example.i_to_sky.functionproject.activity.base.BaseActivity;
import com.example.i_to_sky.functionproject.adapter.GroupAdapter;
import com.example.i_to_sky.functionproject.bean.ImageBean;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by weiyupei on 2019/3/4.
 */

public class ImageFolderActivity extends BaseActivity {

    private static final int REQUEST_PERMISSION = 0;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, ImageFolderActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initAction() {
        if (!hasSelfPermission()) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        } else {
            getImages();
        }
    }

    @Override
    protected void handleIntent(Intent intent) {

    }

    private boolean hasSelfPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void getImages() {
        GetImageUrlTask task = new GetImageUrlTask(mProgressBar, mRecyclerView);
        task.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImages();
            }
        }
    }

    static class GetImageUrlTask extends AsyncTask<Void, Void, Map<String, List<String>>> {

        WeakReference<ProgressBar> mProgressBarWeakRef;
        WeakReference<RecyclerView> mRecyclerViewWeakRef;
        GroupAdapter mAdapter;

        public GetImageUrlTask(ProgressBar progressBar, RecyclerView recyclerView) {
            mProgressBarWeakRef = new WeakReference<ProgressBar>(progressBar);
            mRecyclerViewWeakRef = new WeakReference<RecyclerView>(recyclerView);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBarWeakRef.get().setVisibility(View.VISIBLE);
        }

        @Override
        protected Map<String, List<String>> doInBackground(Void... voids) {
            Cursor cursor = getImagesCursor();
            if (cursor == null) {
                return null;
            }
            Map<String, List<String>> imageMap = new HashMap<>();
            while (cursor.moveToNext()) {
                //获取图片的路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                //获取图片的父路径名
                String parentName = new File(path).getParentFile().getName();
                //根据父路径名将图片放入到mGroupMap中
                if (!imageMap.containsKey(parentName)) {
                    List<String> childList = new ArrayList<>();
                    childList.add(path);
                    imageMap.put(parentName, childList);
                } else {
                    imageMap.get(parentName).add(path);
                }
            }
            cursor.close();
            return imageMap;
        }

        @Override
        protected void onPostExecute(Map<String, List<String>> imageMap) {
            super.onPostExecute(imageMap);
            if (imageMap == null || imageMap.size() == 0) {
                return;
            }
            mProgressBarWeakRef.get().setVisibility(View.GONE);
            mRecyclerViewWeakRef.get().setVisibility(View.VISIBLE);
            mAdapter = new GroupAdapter(mRecyclerViewWeakRef.get().getContext(), subGroupImage(imageMap));
            mRecyclerViewWeakRef.get().setLayoutManager(new GridLayoutManager(mRecyclerViewWeakRef.get().getContext(), 2));
            mRecyclerViewWeakRef.get().setAdapter(mAdapter);
        }

        private Cursor getImagesCursor() {
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = mRecyclerViewWeakRef.get().getContext().getContentResolver();
            Cursor cursor = contentResolver.query(imageUri,
                    null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png"},
                    MediaStore.Images.Media.DATE_MODIFIED);
            return cursor;
        }

        private List<ImageBean> subGroupImage(Map<String, List<String>> imageMap) {
            List<ImageBean> list = new ArrayList<>();
            Iterator<Map.Entry<String, List<String>>> iterator = imageMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> entry = iterator.next();
                ImageBean imageBean = new ImageBean();
                String key = entry.getKey();
                List<String> value = entry.getValue();
                imageBean.setFolderName(key);
                imageBean.setImageUrlList(value);
                list.add(imageBean);
            }
            return list;
        }

    }

}
