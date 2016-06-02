package com.fr.marcoucou.placereminder.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marc on 15/03/2016.
 */


public class Places implements Parcelable {
    private long placeId;
    private String title;
    private String adresse;
    private Double latitude;
    private Double longitude;
    private String comment;



    private PlaceCategory category;
    private Bitmap placeImage;

    public Places() {
    }

    public Bitmap getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(Bitmap placeImage) {
        this.placeImage = placeImage;
    }

    public PlaceCategory getCategory(){return category;}

    public String getTitle(){
        return title;
    }

    public String getAdresse(){
        return adresse;
    }

    public long getId(){
        return placeId;
    }

    public void setPlaceId(long id){
        this.placeId = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAdresse(String adresse){
        this.adresse = adresse;
    }
    public void setCategory(PlaceCategory category) {
        this.category = category;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(adresse);
        dest.writeLong(placeId);
        dest.writeValue(category);
        placeImage.writeToParcel(dest,flags);
    }
}
