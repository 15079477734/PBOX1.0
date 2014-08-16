//没有设置密码时，要设置密码
package com.planboxone.Test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.planboxone.MyActivity.BaseActivity;
import com.planboxone.MyActivity.MainActivity;
import com.planboxone.MyActivity.SettingActivity;
import com.planboxone.R;

public class OpenPsdActivity extends BaseActivity {
    EditText et_pass;
    Button btn_savepass;
    Boolean isHasPassBoolean;
    Handler mHandler;
    EditText et_pass_second;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        et_pass = (EditText) findViewById(R.id.et_pass);
        btn_savepass = (Button) findViewById(R.id.btn_savepsd);
        et_pass_second = (EditText) findViewById(R.id.et_pass_second);

        btn_savepass.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                savePass();
                OpenPsdActivity.this.finish();


            }

            private void savePass() {
                // TODO Auto-generated method stub
                String passString = et_pass.getText().toString();
                String passString2 = et_pass_second.getText().toString();
                if (passString.trim().isEmpty() || passString2.trim().isEmpty()) {
                    Toast.makeText(OpenPsdActivity.this, "俩次密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!passString.equals(passString2)) {
                    Toast.makeText(OpenPsdActivity.this, "俩次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    Editor editor = sharedPreferences.edit();
                    editor.putString("pass", passString);
                    editor.putBoolean("isHas", true);
                    editor.putBoolean("isOpen", true);
                    editor.commit();
//
                    Toast.makeText(OpenPsdActivity.this, "设置成功", Toast.LENGTH_SHORT).show();

                    mHandler = new Handler() {
                        public void dispatchMessage(android.os.Message msg) {
                            switch (msg.what) {
                                case 1:
                                    finish();
                                    break;
                                default:
                                    break;
                            }
                        }

                        ;
                    };
                    mHandler.sendEmptyMessageDelayed(1, 0);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("isOpen", false);
        editor.commit();
        finish();

    }
}
