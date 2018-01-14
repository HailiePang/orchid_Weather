package com.orchidweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by hailie on 2018/1/13.
 */

public class County extends DataSupport {
    private int id;
    private String CountyName;
    private int weatherId;
    private int cityId;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getCountyName(){
        return CountyName;
    }
    public void setCountyName(String CountyName){
        this.CountyName=CountyName;
    }
    public int getWeatherId(){
        return weatherId;
    }
    public void setWeatherId(int id){
        this.weatherId=id;
    }
    public void setCityId(int id){
        this.cityId=id;
    }
    public int getCityId(){
        return cityId;
    }
}