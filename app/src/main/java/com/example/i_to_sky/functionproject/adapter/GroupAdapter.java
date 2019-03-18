package com.example.i_to_sky.functionproject.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.i_to_sky.functionproject.R;
import com.example.i_to_sky.functionproject.activity.ImageGalleryActivity;
import com.example.i_to_sky.functionproject.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by weiyupei on 2018/11/27.
 */

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ImageBean> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public GroupAdapter(Context context, List<ImageBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_image_group, null);
        ImageHolder imageHolder = new ImageHolder(itemView);
        return imageHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageHolder) {
            ImageHolder imageHolder = (ImageHolder) holder;
            final ImageBean item = getItem(position);
            imageHolder.mTextViewTitle.setText(item.getFolderName());
            imageHolder.mTextViewCounts.setText(String.valueOf(item.getImageCounts()));
            Glide.with(mContext)
                    .load(item.getTopImagePath())
                    .into(imageHolder.mImageView);
            imageHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(ImageGalleryActivity.createIntent(mContext, (ArrayList<String>) item.getImageUrlList()));
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private ImageBean getItem(int position) {
        return mList.get(position);
    }


    public static class ImageHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.group_image)
        public ImageView mImageView;
        @BindView(R.id.group_title)
        public TextView mTextViewTitle;
        @BindView(R.id.group_count)
        public TextView mTextViewCounts;
        @BindView(R.id.card_view)
        public CardView mCardView;

        public ImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
