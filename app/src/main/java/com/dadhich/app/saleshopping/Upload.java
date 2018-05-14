package com.dadhich.app.saleshopping;

/**
 * Created by DeLL on 10/05/2018.
 */

public class Upload {
    private String Mname;
    private String Pname;
    private String Price;
    private String mImageUrl;
    private String mKey;
    public Upload() {
        //empty constuctor
    }

    public Upload(String proname, String manuname, String price, String imageUrl) {
        if (proname.trim().equals("") || manuname.trim().equals("")) {
            proname = "No Name";
            manuname = "DEFAULT";

        }

        Pname = proname;
        Mname = manuname;
        Price = price;
        mImageUrl = imageUrl;


    }

    public String getName() {
        return Pname;

    }

    public void setName(String manuname) {
        Pname = manuname;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }


    public void setImageUrl(String imageUrl)

    {
        mImageUrl = imageUrl;
    }

    public String getMname() {
        return Mname;
    }

    public void setMname(String manuname) {
        Mname = manuname;

    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
    public String getmKey(){
        return mKey;
    }
    public void setmKey(String Key){
        mKey = Key;
    }
}
