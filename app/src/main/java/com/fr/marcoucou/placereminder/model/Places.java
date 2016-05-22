package com.fr.marcoucou.placereminder.model;

import android.graphics.Bitmap;

/**
 * Created by Marc on 15/03/2016.
 */


public class Places {
    private long placeId;
    private String title;
    private String adresse;
    private Double latitude;
    private Double longitude;
    private String comment;



    private PlaceCategory category;
    private Bitmap placeImage;


    public Bitmap getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(Bitmap placeImage) {
        this.placeImage = placeImage;
    }



    public Places() {
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
}
