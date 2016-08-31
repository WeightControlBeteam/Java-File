package com.example.tuananh.manhinhchinh;

/**
 * Created by TUANANH on 19-May-16.
 */
public class Buttons {
    private String name;
    private int imageID;

    public Buttons(String name, int imageID) {
        this.name = name;
        this.imageID = imageID;
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

    @Override
    public String toString() {
        return name;
    }
}
