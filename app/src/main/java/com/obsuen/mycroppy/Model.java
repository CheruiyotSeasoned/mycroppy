package com.obsuen.mycroppy;

public class Model {
    String disease;
    int    disease_image;
    public Model(int img,String disease){
        this.disease= disease;
        disease_image=img;
    };

    public String getDisease() {
        return disease;
    }

    public int getDisease_image() {
        return disease_image;
    }
}
