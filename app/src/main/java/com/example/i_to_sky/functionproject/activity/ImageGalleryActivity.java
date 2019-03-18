package com.example.i_to_sky.functionproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.example.i_to_sky.functionproject.R;
import com.example.i_to_sky.functionproject.activity.base.BaseActivity;
import com.example.i_to_sky.functionproject.fragment.ImageGalleryFragment;
import com.example.i_to_sky.functionproject.fragment.LongPicFragment;
import com.example.i_to_sky.functionproject.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ImageGalleryActivity extends BaseActivity {

    private static final int INDEX_IMAGE_GALLERY_FRAGMENT = 0;
    private static final int INDEX_LONG_PIC_FRAGMENT = 1;

    public static final String KEY_IMAGE_LIST = "image_list";
    public static final String KEY_SELECT_IMAGE_LIST = "select_image_list";

    @BindView(R.id.select_image)
    TextView mSelectImageTv;

    private ArrayList<String> mImageUrlList;
    private ImageGalleryFragment mImageGalleryFragment;
    private LongPicFragment mLongPicFragment;

    public static Intent createIntent(Context context, ArrayList<String> imageUrlList) {
        Intent intent = new Intent(context, ImageGalleryActivity.class);
        intent.putStringArrayListExtra(KEY_IMAGE_LIST, imageUrlList);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
    }

    @Override
    protected void initView() {
        updateFragmentVisible(INDEX_IMAGE_GALLERY_FRAGMENT);
    }

    @Override
    protected void initAction() {

    }

    private void updateFragmentVisible(int index) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mImageGalleryFragment == null) {
            mImageGalleryFragment = (ImageGalleryFragment) fragmentManager.findFragmentByTag(ImageGalleryFragment.class.getName());
        }
        if (mLongPicFragment == null) {
            mLongPicFragment = (LongPicFragment) fragmentManager.findFragmentByTag(LongPicFragment.class.getName());
        }

        switch (index) {
            case INDEX_IMAGE_GALLERY_FRAGMENT:
                if (mImageGalleryFragment == null) {
                    mImageGalleryFragment = ImageGalleryFragment.createFragment();
                    fragmentTransaction.add(R.id.fragment_container, mImageGalleryFragment, ImageGalleryFragment.class.getName());
                }
                break;
            case INDEX_LONG_PIC_FRAGMENT:
                if (mLongPicFragment == null) {
                    mLongPicFragment = LongPicFragment.createFragment();
                    fragmentTransaction.add(R.id.fragment_container, mLongPicFragment, LongPicFragment.class.getName());
                }
                break;
        }

        replaceFragment(index, fragmentTransaction);

        if (!fragmentTransaction.isEmpty()) {
            try {
                fragmentTransaction.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void replaceFragment(int index, FragmentTransaction fragmentTransaction) {
        switch (index) {
            case INDEX_IMAGE_GALLERY_FRAGMENT:
                hideFragment(fragmentTransaction, mLongPicFragment);
                showFragment(fragmentTransaction, mImageGalleryFragment);
                break;
            case INDEX_LONG_PIC_FRAGMENT:
                hideFragment(fragmentTransaction, mImageGalleryFragment);
                showFragment(fragmentTransaction, mLongPicFragment);
                break;
        }
    }

    private void showFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if ((fragment != null) && fragment.isHidden()) {
            fragmentTransaction.show(fragment);
        }
    }

    private void hideFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if ((fragment != null) && !fragment.isHidden()) {
            fragmentTransaction.hide(fragment);
        }
    }

    @Override
    protected void handleIntent(Intent intent) {
        mImageUrlList = intent.getStringArrayListExtra(KEY_IMAGE_LIST);
    }

    @OnClick({R.id.back_iv, R.id.make_up_image, R.id.select_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.make_up_image:
                if (mImageGalleryFragment != null && mImageGalleryFragment.isHidden()
                        && mLongPicFragment != null && !mLongPicFragment.isHidden()) {
                    return;
                }
                if (!mImageGalleryFragment.isCanCreateLongPic()) {
                    ToastUtil.showToastShort(R.string.has_not_selected_image);
                    return;
                }
                if (mLongPicFragment == null || (mLongPicFragment.isHidden())) {
                    mSelectImageTv.setText(getResources().getString(R.string.select_image_again));
                    updateFragmentVisible(INDEX_LONG_PIC_FRAGMENT);
                }
                break;
            case R.id.select_image:
                if (mImageGalleryFragment != null && !mImageGalleryFragment.isHidden()
                        && mLongPicFragment != null && mLongPicFragment.isHidden()) {
                    return;
                }
                if (mImageGalleryFragment != null && mImageGalleryFragment.isHidden()) {
                    mSelectImageTv.setText(getResources().getString(R.string.select_image));
                    updateFragmentVisible(INDEX_IMAGE_GALLERY_FRAGMENT);
                }
                break;
        }
    }

    public ArrayList<String> getImageList() {
        return mImageUrlList;
    }

    public ArrayList<String> getSelectImageList() {
        return mImageGalleryFragment.getSelectImageList();
    }

}
