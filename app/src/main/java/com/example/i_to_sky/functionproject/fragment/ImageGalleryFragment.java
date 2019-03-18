package com.example.i_to_sky.functionproject.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.i_to_sky.functionproject.R;
import com.example.i_to_sky.functionproject.activity.ImageGalleryActivity;
import com.example.i_to_sky.functionproject.adapter.FolderAdapter;
import com.example.i_to_sky.functionproject.fragment.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageGalleryFragment extends BaseFragment {


    @BindView(R.id.image_recycler)
    RecyclerView mImageRecyclerView;

    private FolderAdapter mAdapter;
    private ArrayList<String> mImageUrlList;

    public static ImageGalleryFragment createFragment() {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        return fragment;
    }

    public ImageGalleryFragment() {
    }

    @Override
    protected int getFragmentViewId() {
        return R.layout.fragment_image_gallery;
    }

    @Override
    protected void initView() {
        if (getActivity() != null) {
            mImageUrlList = ((ImageGalleryActivity) getActivity()).getImageList();
        } else {
            mImageUrlList = new ArrayList<>();
        }
        mAdapter = new FolderAdapter(getContext(), mImageUrlList, FolderAdapter.COLUMN_NUMBER_3);
        mImageRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), FolderAdapter.COLUMN_NUMBER_3));
        mImageRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void handleBundle(Bundle bundle) {

    }

    public boolean isCanCreateLongPic() {
        return mAdapter.getSelectImageList() != null && mAdapter.getSelectImageList().size() > 0;
    }

    public ArrayList<String> getSelectImageList() {
        return mAdapter.getSelectImageList();
    }

}
