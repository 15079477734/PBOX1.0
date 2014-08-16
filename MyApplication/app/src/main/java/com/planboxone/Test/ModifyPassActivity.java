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

public class ModifyPassActivity extends BaseActivity {
    EditText et_modifypsd;
    EditText et_modifypsd_second;
    Button btn_savepsd_modify;
    SharedPreferences sharedPreferences;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        Intent intent = new Intent();
        flag = intent.getBooleanExtra("setting", false);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        et_modifypsd = (EditText) findViewById(R.id.et_modifypsd);
        et_modifypsd_second = (EditText) findViewById(R.id.et_modifypsd_second);
        btn_savepsd_modify = (Button) findViewById(R.id.btn_savepsd_modify);
        btn_savepsd_modify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                savePsd();

            }

            protected void savePsd() {
                // TODO Auto-generated method stub
                String writePassString = et_modifypsd.getText().toString();
                String setPassString = et_modifypsd_second.getText().toString();
                String first_pass = sharedPreferences.getString("pass", "");

                if (writePassString.trim().equals(first_pass)) {

                    setPass();
                } else {
                    Toast.makeText(ModifyPassActivity.this, "对不起，原始密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                }
            }


            private void setPass() {
                // TODO Auto-generated method stub
                String setPassString = et_modifypsd_second.getText().toString();
                if (setPassString.isEmpty()) {
                    Toast.makeText(ModifyPassActivity.this, "对不起，密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Editor editor = sharedPreferences.edit();
                    editor.putString("pass", setPassString);
                    editor.putBoolean("isHas", true);
                    editor.commit();
                    Toast.makeText(ModifyPassActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                    if (flag)
                        finish();
                    else {
                        Intent mIntent = new Intent();
                        mIntent.setClass(ModifyPassActivity.this, MainActivity.class);
                        startActivity(mIntent);
                        finish();
                    }
                }

            }
        });
    }
}
