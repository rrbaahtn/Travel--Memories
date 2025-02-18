package com.rabiahatunsoylemez.travelmemoriesapp;
public class Travel {
    private int id;
    private String city;
    private String note;


    private String mapLink;
    private boolean isFavorite;

    public Travel(int id, String city, String note, String mapLink, boolean isFavorite) {
        this.id = id;
        this.city = city;
        this.note = note;
        this.mapLink = mapLink;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getNote() {
        return note;
    }

    public String getMapLink() {
        return mapLink;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
}