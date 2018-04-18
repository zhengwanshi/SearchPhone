package com.example.searchphone.business;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhengyg on 2018/1/30.
 */

public class HttpUntil {
    String mUrl;
    Map<String,String>mParam;
    HttpRespone mHttpRespone;
    private final OkHttpClient client =new OkHttpClient();
    Handler myHandler = new Handler(Looper.getMainLooper());

    public interface HttpRespone{
        void onSuccess(Object object);
        void onFail(String error);
    }

    public HttpUntil(HttpRespone respone){
        mHttpRespone=respone;
    }
    public void sendPostHttp(String url, Map<String,String>param){
        sendHttp(url,param,true);
    }
    public void sendGetHttp(String url,Map<String,String >param){
        sendHttp(url,param,false);
    }
    private void sendHttp(String url,Map<String,String>param,boolean isPost){
        mUrl = url;
        mParam = param;
        run(isPost);
    }
    private void run(boolean isPost){

      final Request request=  createRequest(isPost);
        //创建请求队列
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mHttpRespone!=null){
                        myHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mHttpRespone.onFail("请求错误");
                            }
                        });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (mHttpRespone==null)return;
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!response.isSuccessful()){
                            mHttpRespone.onFail("请求失败:code"+response);
                        }else
                        {
                            try {
                                mHttpRespone.onSuccess(response.body().string());

                            } catch (IOException e) {
                                e.printStackTrace();
                                mHttpRespone.onFail("结果转换失败");
                            }
                        }
                    }
                });
            }
        });
    }
    private Request createRequest(boolean isPost){
        Request request;
        if (isPost){
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
            requestBodyBuilder.setType(MultipartBody.FORM);
            //遍历map请求参数
            Iterator<Map.Entry<String,String>> iterator= mParam.entrySet().iterator();
            while ((iterator.hasNext())){
                Map.Entry<String,String>entry=iterator.next();
                requestBodyBuilder.addFormDataPart(entry.getKey(),entry.getValue());

            }
            request = new okhttp3.Request.Builder().url(mUrl).post(requestBodyBuilder.build()).build();

        }else
        {
            String urlStr = mUrl+"?"+MapParamToString(mParam);
            Log.d("urlStrllllllll",urlStr);
            request = new Request.Builder().url(urlStr).build();

        }
        return request;
    }
    private String MapParamToString(Map<String,String>param){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String,String>>iterator=param.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,String>entry = iterator.next();
            stringBuilder.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        String str= stringBuilder.toString().substring(0,stringBuilder.length()-1);
        return str;
    }
}
