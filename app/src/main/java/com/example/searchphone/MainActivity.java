package com.example.searchphone;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searchphone.model.Phone;
import com.example.searchphone.mvp.MvpMainView;
import com.example.searchphone.mvp.impl.Mainpresenter;

public class MainActivity extends AppCompatActivity implements MvpMainView,View.OnClickListener {

    private EditText inputPhone;
    private Button btnSearch;
    private TextView resultPhone;
    private TextView resultProvince;
    private TextView resultType;
    private TextView resultCarrier;
    Mainpresenter mainpresenter;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
         mainpresenter = new Mainpresenter(this);
        mainpresenter.attach(this);
    }
    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-30 22:25:18 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        inputPhone = (EditText)findViewById( R.id.input_phone );
        btnSearch = (Button)findViewById( R.id.btn_search );
        resultPhone = (TextView)findViewById( R.id.result_phone );
        resultProvince = (TextView)findViewById( R.id.result_province );
        resultType = (TextView)findViewById( R.id.result_type );
        resultCarrier = (TextView)findViewById( R.id.result_carrier );

        btnSearch.setOnClickListener( MainActivity.this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-01-30 22:25:18 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        mainpresenter.searchPhoneInfo(inputPhone.getText().toString());
    }


    @Override
    public void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateView() {
        Phone phone = mainpresenter.getPhoneInfo();
        if (phone!=null) {
            Toast.makeText(this,"hehehehe",Toast.LENGTH_SHORT).show();
            resultPhone.setText("手机号码：" + phone.getTelString());
            resultProvince.setText("省份：" + phone.getProvince());
            resultType.setText("运营商：" + phone.getCatName());
            resultCarrier.setText("归属运营商：" + phone.getCarrier());
        }
    }

    @Override
    public void showLoading() {
        if (progressDialog==null){
            progressDialog= ProgressDialog.show(this,"","正在加载。。。",true,false);

        }else if (progressDialog.isShowing()){
            progressDialog.setTitle("");
            progressDialog.setMessage("正在加载。。。");
        }progressDialog.show();
    }

    @Override
    public void hidenLoading() {
        if (progressDialog!=null&&progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
