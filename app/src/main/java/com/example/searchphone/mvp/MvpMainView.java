package com.example.searchphone.mvp;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by zhengyg on 2018/1/30.
 */

public interface MvpMainView extends MvpLoadingView {

    void showToast(String msg);
    void updateView();


}
