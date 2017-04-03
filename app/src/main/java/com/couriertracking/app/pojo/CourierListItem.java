package com.couriertracking.app.pojo;

/**
 * Created by Relsell Global on 13/10/15.
 */
public class CourierListItem {

    private String mImgSrc;
    private String mCourierName;
    private String mCourierUrl;

    public CourierListItem(String mImgSrc, String mCourierName, String mCourierUrl) {
        this.mImgSrc = mImgSrc;
        this.mCourierName = mCourierName;
        this.mCourierUrl = mCourierUrl;
    }

    public String getmImgSrc() {
        return mImgSrc;
    }

    public void setmImgSrc(String mImgSrc) {
        this.mImgSrc = mImgSrc;
    }

    public String getmCourierName() {
        return mCourierName;
    }

    public void setmCourierName(String mCourierName) {
        this.mCourierName = mCourierName;
    }

    public String getmCourierUrl() {
        return mCourierUrl;
    }

    public void setmCourierUrl(String mCourierUrl) {
        this.mCourierUrl = mCourierUrl;
    }
}
