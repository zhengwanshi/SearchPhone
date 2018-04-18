package com.example.searchphone.mvp.impl;

import android.util.Log;

import com.example.searchphone.business.HttpUntil;
import com.example.searchphone.model.Phone;
import com.example.searchphone.mvp.MvpMainView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengyg on 2018/1/30.
 */

public class Mainpresenter extends BasePresenter
{
   MvpMainView mMvpMainView;
    Phone mPhone;
    public Phone getPhoneInfo(){
        return mPhone;
    }
    String mUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    public Mainpresenter(MvpMainView mainView){
        mMvpMainView = mainView;
    }
    public void searchPhoneInfo(String phone){
        if (phone.length()!=11){
            mMvpMainView.showToast("请输入正确的手机号");
            return;
        }
        mMvpMainView.showLoading();
        sendHttp(phone);
    }
    private void sendHttp(String phone){
        Map<String,String> map = new HashMap<String,String>();
        map.put("tel",phone);
        HttpUntil httpUntil = new HttpUntil(new HttpUntil.HttpRespone() {
            @Override
            public void onSuccess(Object object) {
                String json = object.toString();
                Log.d("Jsonhhhhhhhhhhhh",json);
                int index = json.indexOf("{");
                json = json.substring(index,json.length());
               /* try {
                    //原生解析Json方法
                    mPhone = parseModelWithOrgJson(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                //Gson
                mPhone=parseModelWithGson(json);
                //fastJSon
              //  parseModelWithFastJson(json);
                mMvpMainView.hidenLoading();
                mMvpMainView.updateView();
            }

            @Override
            public void onFail(String error) {
                mMvpMainView.showToast(error);
                mMvpMainView.hidenLoading();
            }
        });
        httpUntil.sendGetHttp(mUrl,map);
    }
    private Phone parseModelWithOrgJson(String json) throws JSONException {
        Phone phone = new Phone();
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(json);
            String value = jsonObject.getString("telString");
            phone.setTelString(value);

             value = jsonObject.getString("province");
            phone.setProvince(value);
            value = jsonObject.getString("catName");
            phone.setCatName(value);
            value = jsonObject.getString("carrier");
            phone.setCarrier(value);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return phone;
    }
    private Phone parseModelWithGson(String json){
            Gson gson = new Gson();
        Phone phone = gson.fromJson(json,Phone.class);
        return phone;
    }
    private Phone parseModelWithFastJson(String json){
        Phone phone = com.alibaba.fastjson.JSONObject.parseObject(json,Phone.class);
        return phone;
    }
}
