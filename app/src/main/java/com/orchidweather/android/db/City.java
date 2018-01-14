package com.orchidweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by hailie on 2018/1/13.
 */

public class City extends DataSupport {
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getCityName(){
        return cityName;

    }
    public void setCityName(String CityName){
        this.cityName=CityName;

    }
    public int getCityCode(){
        return cityCode;

    }
    public void setCityCode(int code){
        this.cityCode=code;

    }

    public int getProviceId(){
        return provinceId;

    }
    public void setProviceId(int id){
        this.provinceId=id;

    }
}