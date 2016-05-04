package com.fr.marcoucou.placereminder.model;

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
}
