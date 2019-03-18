package com.example.i_to_sky.functionproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.i_to_sky.functionproject.R;
import com.example.i_to_sky.functionproject.utils.ScreenUtil;
import com.example.i_to_sky.functionproject.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weiyupei on 2018/11/30.
 */

public class FolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int COLUMN_NUMBER_3 = 3;

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<String> mImageUrlList = new ArrayList<>();
    private ArrayList<String> mSelectImageList = new ArrayList<>();
    private int mColumnNumber;

    public FolderAdapter(Context context, ArrayList<String> imageUrlList, int columnNumber) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mImageUrlList = imageUrlList;
        mColumnNumber = columnNumber;
    }

    public void updateGalleryList(ArrayList<String> imageUrlList) {
        mImageUrlList = imageUrlList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_image_folder, null);
        ImageHolder imageHolder = new ImageHolder(itemView);
        return imageHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageHolder) {
            ImageHolder imageHolder = (ImageHolder) holder;
            int imageHeight = ScreenUtil.getScreenWidth() / mColumnNumber;
            ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageHeight);
            imageHolder.mFolderImage.setLayoutParams(layoutParams);
            imageHolder.mImagePosition = position;
            String imageUrl = getItem(position);
            Glide.with(mContext).load(imageUrl).into(imageHolder.mFolderImage);
        }
    }

    public ArrayList<String> getSelectImageList() {
        return mSelectImageList;
    }

    private String getItem(int position) {
        if (mImageUrlList == null) {
            return "";
        }
        return mImageUrlList.get(position);
    }

    @Override
    public int getItemCount() {
        if (mImageUrlList == null) {
            return 0;
        }
        return mImageUrlList.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        public int mImagePosition;

        @BindView(R.id.folder_image)
        public ImageView mFolderImage;
        @BindView(R.id.folder_image_number)
        public TextView mFolderTextNumber;
        @BindView(R.id.image_shadow_layout)
        public FrameLayout mImageShadowLayout;

        public ImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.folder_item_layout)
        public void onClick(View view) {
            String imageUrl = getItem(mImagePosition);
            if (!mSelectImageList.contains(imageUrl)) {
                mImageShadowLayout.setVisibility(View.VISIBLE);
                mFolderTextNumber.setText(String.valueOf(mSelectImageList.size() + 1));
                mSelectImageList.add(imageUrl);
            } else {
                int selectSize = mSelectImageList.size();
                if (mSelectImageList.get(selectSize - 1).equals(getItem(mImagePosition))) {
                    mImageShadowLayout.setVisibility(View.GONE);
                    mSelectImageList.remove(selectSize - 1);
                } else {
                    ToastUtil.showToastShort(R.string.image_folder_cancel_image_select);
                }
            }

        }

    }

}
