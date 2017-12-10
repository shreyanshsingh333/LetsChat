package com.example.shreyansh.letschat;

/**
 * Created by Shreyansh on 12/9/2017.
 */

public class Friends {
    private String mName;
    private String mId;
    public Friends() {
    }

    Friends(String id,String name){
        this.mId = id;
        this.mName = name;
    }

    public String getmName(){return mName;}
    public String getmId(){return mId;}
}
