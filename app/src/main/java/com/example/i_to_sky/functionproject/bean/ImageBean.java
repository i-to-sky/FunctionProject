package com.example.i_to_sky.functionproject.bean;

import java.util.List;

/**
 * Created by weiyupei on 2018/11/27.
 */

public class ImageBean {

    /**
     * 文件夹名
     */
    private String mFolderName;
    /**
     * 文件夹中的所有图片的url
     */
    private List<String> mImageUrlList;

    public String getTopImagePath() {
        if (mImageUrlList != null && mImageUrlList.size() > 0) {
            return mImageUrlList.get(0);
        }
        return "";
    }

    public String getFolderName() {
        return mFolderName;
    }

    public void setFolderName(String folderName) {
        this.mFolderName = folderName;
    }

    public int getImageCounts() {
        if (mImageUrlList != null && mImageUrlList.size() > 0) {
            return mImageUrlList.size();
        }
        return 0;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        mImageUrlList = imageUrlList;
    }

    public List<String> getImageUrlList() {
        return mImageUrlList;
    }

}
