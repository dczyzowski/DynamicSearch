package pl.agh.dynamicsearch.models;

import java.util.ArrayList;

public class Product {

    private String mType;
    private String mName;
    private double mPrice;

    public Product(){
        this.mName = "";
        this.mType = "";
        this.mPrice = 0;
    }

    public Product(String mType, String mName, double mPrice){
        this.mType = mType;
        this.mName = mName;
        this.mPrice = mPrice;
    }

    public double getmPrice() {
        return mPrice;
    }

    public String getmName() {
        return mName;
    }

    public String getmType() {
        return mType;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }
}
