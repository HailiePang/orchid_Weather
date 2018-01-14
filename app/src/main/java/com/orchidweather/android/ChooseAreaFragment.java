package com.orchidweather.android;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.orchidweather.android.db.Province;
import com.orchidweather.android.db.City;
import com.orchidweather.android.db.County;
import com.orchidweather.android.util.HttpUtil;
import com.orchidweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hailie on 2018/1/14.
 */

public class ChooseAreaFragment extends Fragment {
    private Button backButton;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> datalist=new ArrayList<>();
    private int currentLevel;
    public static final int LEVEL_PROVINCE=1;
    public static final int LEVEL_CITY=2;
    public static final int LEVEL_COUNTY=3;
    private Province selectedProvince;
    private List<Province> provinceList;
    private List<City> CityLists;
    private List<County> CountyLists;
    private City selectedCity;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.choose_area,container,false);
        titleText=(TextView) view.findViewById(R.id.title_text);
        backButton=(Button) view.findViewById(R.id.back_button);
        listView=(ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(i);
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    selectedCity=CityLists.get(i);
                    queryCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLevel==LEVEL_COUNTY){
                    queryCities();
                }
                else if(currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList=DataSupport.findAll(Province.class);
        if(provinceList.size()>0) {
            datalist.clear();
            for (Province province : provinceList) {
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }
        else{
            String address="http://guolin.tech/api/china/";
            queryFromServer(address,"province");
        }

    }
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());

        backButton.setVisibility(View.VISIBLE);
        CityLists= DataSupport.where("provinceId=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(CityLists.size()>0){
            datalist.clear();
            for(City city:CityLists){
                datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        CountyLists=DataSupport.where("cityId=?",String.valueOf(selectedCity.getId())).find(County.class);

        if(CountyLists.size()>0) {
            datalist.clear();
            for (County county : CountyLists) {
                datalist.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }
        else{
            int provinceCode=selectedProvince.getProvinceCode();
            int CityCode=selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+CityCode;
            queryFromServer(address,"county");
        }
    }
    private void queryFromServer(String address,final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result =false;
                if("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);

                }else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }
                else if("county".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type))
                                queryProvinces();
                            else if("city".equals(type))
                                queryCities();
                            else if("county".equals(type))
                                queryCounties();

                        }
                    });
                }
            }
        });
    }
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog!=null)
            progressDialog.dismiss();

    }
}
