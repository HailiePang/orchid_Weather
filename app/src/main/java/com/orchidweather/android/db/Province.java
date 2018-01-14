package com.orchidweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by hailie on 2018/1/13.
 */

public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int provinceCode;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getProvinceName(){
        return provinceName;
    }
    public void setProvinceName(String provinceName){
        this.provinceName=provinceName;
    }
    public int getProvinceCode(){
        return provinceCode;
    }
    public void setProviceCode(int code){
        this.provinceCode=code;
    }
}
