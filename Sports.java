package com.example.tuananh.manhinhchinh;

import java.io.Serializable;

/**
 * Created by TUANANH on 17-Aug-16.
 */
public class Sports implements Serializable{
    private String name;
    private int imageID;
    private double calories;
    private int imageBodyID;
    private boolean enable;
    private double constSport;

    public Sports(String name, int imageID, double calories, int imageBodyID, boolean enable, double constSport) {
        this.name = name;
        this.imageID = imageID;
        this.calories = calories;
        this.imageBodyID = imageBodyID;
        this.enable = enable;
        this.constSport = constSport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public int getImageBodyID() {
        return imageBodyID;
    }

    public void setImageBodyID(int imageBodyID) {
        this.imageBodyID = imageBodyID;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public double getConstSport() {
        return constSport;
    }

    public void setConstSport(double constSport) {
        this.constSport = constSport;
    }
}
