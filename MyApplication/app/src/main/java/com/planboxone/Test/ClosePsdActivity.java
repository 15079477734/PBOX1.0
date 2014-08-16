package com.planboxone.Test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.planboxone.MyActivity.BaseActivity;
import com.planboxone.MyActivity.MainActivity;
import com.planboxone.R;


public class ClosePsdActivity extends BaseActivity {
    EditText et_check_psd;
    Button btn_closepsd;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        et_check_psd = (EditText) findViewById(R.id.et_check_psd);
        btn_closepsd = (Button) findViewById(R.id.btn_closepsd);

        btn_closepsd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                closePsd();

            }
        });

    }

    protected void closePsd() {
        // TODO Auto-generated method stub
        String writeString = et_check_psd.getText().toString();
        String true_pass = sharedPreferences.getString("pass", "");
        // TODO Auto-generated method stub
        if (writeString.trim().equals(true_pass)) {
            Editor editor = sharedPreferences.edit();
            editor.putBoolean("isHas", false);
            editor.putBoolean("isOpen", false);
            editor.commit();

            ClosePsdActivity.this.finish();
        }//密码正确，取消本地密码
        else {
            Toast.makeText(ClosePsdActivity.this, "密码错误，重新输入", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("isOpen", true);
        editor.commit();
        Intent mIntent = new Intent();
        mIntent.setClass(ClosePsdActivity.this, MainActivity.class);
        startActivity(mIntent);
    }//重写返回键，防止密码不正确时按自带返回键switch按钮没有改变状态
}
