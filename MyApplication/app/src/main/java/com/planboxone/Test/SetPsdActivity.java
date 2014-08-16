package com.planboxone.Test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.planboxone.MyActivity.BaseActivity;
import com.planboxone.MyWidget.SlipSwitch;
import com.planboxone.R;


public class SetPsdActivity extends BaseActivity {
    final static private String TAG = "SetPsdActivity";
    RelativeLayout rl_modify;
    Boolean isHas;//是否有密码
    Boolean isOpen;//是否打开密码开关
    Switch aSwitch;
    boolean isSwitchOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpsd);
        getData();
        initView();

    }

    private void initView() {
        aSwitch = (Switch) findViewById(R.id.switch_psd);
        rl_modify = (RelativeLayout) findViewById(R.id.rl_modify_pass);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isSwitchOn) {
                    if (isChecked) {
                        startActivity(new Intent(SetPsdActivity.this, OpenPsdActivity.class));
                    } else {
                        startActivity(new Intent(SetPsdActivity.this, ClosePsdActivity.class));
                    }
                }
                if (!isSwitchOn)
                    isSwitchOn = true;
                Log.e(TAG, "isCheck  =  " + String.valueOf(isChecked));


                Log.e(TAG, "isSwitchOn   =  " + String.valueOf(isChecked));
            }

        });


        rl_modify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isOpen.equals(false)) {
                    Toast.makeText(SetPsdActivity.this, "您还未设置密码，请先设置密码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    startActivity(new Intent(SetPsdActivity.this, ModifyPassActivity.class));
                }

            }
        });
        setSwitch();
        isSwitchOn = true;
    }

    private void setSwitch() {
        // TODO Auto-generated method stub
        if (isOpen.equals(true)) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }
    }

    private void getData() {
        // TODO Auto-generated method stub
        final SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        isHas = sharedPreferences.getBoolean("isHas", false);
        isOpen = sharedPreferences.getBoolean("isOpen", false);
    }
}
