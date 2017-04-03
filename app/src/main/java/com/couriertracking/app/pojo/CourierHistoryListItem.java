package com.couriertracking.app.pojo;

/**
 * Created by Relsell Global on 13/10/15.
 */
public class CourierHistoryListItem {

    private String mImgSrc;
    private String mCourierName;
    private String mCourierTrackNo;
    private String mLastSearchedDate;
    private String mCourierUrl;

    public CourierHistoryListItem(String mImgSrc, String mCourierName, String mCourierTrackNo,String mCourierUrl, String mLastSearchedDate) {
        this.mImgSrc = mImgSrc;
        this.mCourierName = mCourierName;
        this.mCourierTrackNo = mCourierTrackNo;
        this.mLastSearchedDate = mLastSearchedDate;
        this.mCourierUrl = mCourierUrl;
    }

    public String getmCourierUrl() {
        return mCourierUrl;
    }

    public void setmCourierUrl(String mCourierUrl) {
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

    public String getmCourierTrackNo() {
        return mCourierTrackNo;
    }

    public void setmCourierTrackNo(String mCourierTrackNo) {
        this.mCourierTrackNo = mCourierTrackNo;
    }

    public String getmLastSearchedDate() {
        return mLastSearchedDate;
    }

    public void setmLastSearchedDate(String mLastSearchedDate) {
        this.mLastSearchedDate = mLastSearchedDate;
    }
}
