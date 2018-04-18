package com.example.searchphone.mvp.impl;

import android.content.Context;

/**
 * Created by zhengyg on 2018/1/30.
 */

public class BasePresenter {
    Context mContext;
    public void attach(Context context){
        mContext = context;
    }
    public void onPause(){}
    public void onResume(){}
    public void onDestroy(){
        mContext=null;
    }
}
