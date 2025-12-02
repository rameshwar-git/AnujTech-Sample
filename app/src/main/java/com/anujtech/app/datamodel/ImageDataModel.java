package com.anujtech.app.datamodel;

public class ImageDataModel {
    public ImageDataModel() {
    }
    private String filename,imageUri;
    private String imageUrl;
    private String mob1;
    private String mob2;
    private String mDate;
    private String mTimeStamp;

    public ImageDataModel(String filename, String imageUrl,String mob1, String mob2, String mDate, String mTimeStamp) {
        this.filename = filename;
        this.imageUrl = imageUrl;
        this.mob1 = mob1;
        this.mob2 = mob2;
        this.mDate = mDate;
        this.mTimeStamp = mTimeStamp;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getMob1() {
        return mob1;
    }
    public void setMob1(String mob1) {
        this.mob1 = mob1;
    }
    public String getMob2() {
        return mob2;
    }
    public void setMob2(String mob2) {
        this.mob2 = mob2;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getmDate() {
        return mDate;
    }
    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
    public String getmTimeStamp() {
        return mTimeStamp;
    }
    public void setmTimeStamp(String mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }
}
