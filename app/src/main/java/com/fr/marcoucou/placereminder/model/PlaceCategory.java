package com.fr.marcoucou.placereminder.model;

/**
 * Created by Marc on 21/04/2016.
 */
public class PlaceCategory {

    private String categoryName;
    private int categoryId;

    public PlaceCategory(String name, int categoryId){
        this.categoryName = name;
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }




}
