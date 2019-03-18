package com.example.i_to_sky.functionproject.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.i_to_sky.functionproject.R;
import com.example.i_to_sky.functionproject.activity.ImageGalleryActivity;
import com.example.i_to_sky.functionproject.fragment.base.BaseFragment;
import com.example.i_to_sky.functionproject.task.longpic.LongPicGenerateTask;
import com.example.i_to_sky.functionproject.view.longpic.LongPicView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;

import static com.example.i_to_sky.functionproject.activity.ImageGalleryActivity.KEY_SELECT_IMAGE_LIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class LongPicFragment extends BaseFragment {

    private ArrayList<String> mSelectImageList;

    @BindView(R.id.long_pic)
    LongPicView mLargeImage;
    @BindView(R.id.long_pic_generate_progress)
    ProgressBar mProgress;
    @BindView(R.id.generate_progress_hint)
    TextView mProgressHit;
    @BindView(R.id.generate_progress_layout)
    LinearLayout mProgressLayout;

    public static LongPicFragment createFragment() {
        LongPicFragment fragment = new LongPicFragment();
        return fragment;
    }

    public LongPicFragment() {
    }

    @Override
    protected int getFragmentViewId() {
        return R.layout.fragment_long_pic;
    }

    @Override
    protected void initView() {
        generateLongPic();
    }

    public void generateLongPic() {
        if (getActivity() != null) {
            mSelectImageList = ((ImageGalleryActivity) getActivity()).getSelectImageList();
        } else {
            mSelectImageList = new ArrayList<>();
        }
        LongPicGenerateTask task = new LongPicGenerateTask(mSelectImageList);
        task.setOnGenerateLongPicListener(new LongPicGenerateTask.OnGenerateLongPicListener() {
            @Override
            public void onStart() {
                mProgressLayout.setVisibility(View.VISIBLE);
                mLargeImage.setVisibility(View.GONE);
            }

            @Override
            public void onProgress(int progress) {
                mProgressHit.setText(String.format(getResources().getString(R.string.generate_long_pic_progress), progress));
                mProgress.setProgress(progress);
            }

            @Override
            public void onSuccess(String filePath, int imageWidth) {
                mProgressLayout.setVisibility(View.GONE);
                mLargeImage.setVisibility(View.VISIBLE);
                readFileFromSdcard(filePath, imageWidth);

            }
        });
        task.execute();
    }

    public void readFileFromSdcard(String filePath, int imageWidth) {
        try {
            File sdcardFile = new File(filePath);
            if (!sdcardFile.exists()) {
                return;
            }
            mLargeImage.setBitmap(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleBundle(Bundle bundle) {
        mSelectImageList = bundle.getStringArrayList(KEY_SELECT_IMAGE_LIST);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            generateLongPic();
        }
    }
}
