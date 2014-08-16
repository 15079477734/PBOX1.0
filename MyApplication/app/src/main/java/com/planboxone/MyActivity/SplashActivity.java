//欢迎界面
//判断是不是第一次启动，根据情况启动对应的activity;
//若是第一次启动，先加载GuideActivity，再加载mainactivity，若是第二次启动，直接加载mainactivity；
package com.planboxone.MyActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.planboxone.R;

public class SplashActivity extends Activity {
    public final static int MAIN_ACTIVITY = 1000;
    public final static int GUIDE_ACTIVITY = 1001;
    SharedPreferences sharedPreferences;
    EditText et_firstpage_inputpsd;
    Button btn_enterapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Boolean mIsFirstEnter = IsFirstEnter(SplashActivity.this, SplashActivity.this.getClass().getName());

        if (mIsFirstEnter) {
            mHandler.sendEmptyMessageDelayed(GUIDE_ACTIVITY, 2000);//5000是延迟的时间5秒

        } else {
            mHandler.sendEmptyMessageDelayed(MAIN_ACTIVITY, 2000);
        }
    }

    //判断是不是第一次启动的函数
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

    private Boolean IsFirstEnter(Context context, String name) {
        // TODO Auto-generated method stub
        if (context == null || name == null || "".equalsIgnoreCase(name)) {
            return false;
        }//显示引导界面
        String mResultString = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(KEY_GUIDE_ACTIVITY, "");
        if (mResultString.equalsIgnoreCase("false")) {
            return false;
        } else {
            return true;//是第一次启动
        }


    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GUIDE_ACTIVITY:
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, GuideActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                    break;
                case MAIN_ACTIVITY:

                    sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                    Boolean isHasPass = sharedPreferences.getBoolean("isHas", false);

                    if (!isHasPass) {
                        Intent mIntent = new Intent();
                        mIntent.setClass(SplashActivity.this, MainActivity.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        showInputPsdAty();
                    }
                    break;

            }
            super.handleMessage(msg);
        }

        ;
    };

    private void showInputPsdAty() {
        // TODO Auto-generated method stub
        setContentView(R.layout.psd_check);
        et_firstpage_inputpsd = (EditText) findViewById(R.id.et_firstpage_inputpsd);
        btn_enterapp = (Button) findViewById(R.id.btn_enterapp);
        btn_enterapp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String psd_input = et_firstpage_inputpsd.getText().toString();
                final String psd = sharedPreferences.getString("pass", "");

                // TODO Auto-generated method stub
                if (psd_input.trim().equals(psd)) {
                    Log.e("tag", "    真实密码：   " + psd);
                    Log.e("tag", "   输入的密码       " + psd_input);

                    Intent mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this, MainActivity.class);
                    startActivity(mIntent);
                    SplashActivity.this.finish();

                } else {
                    Log.e("tag", "    真实密码：   " + psd);
                    Log.e("tag", "   输入的密码       " + psd_input);
                    Toast.makeText(SplashActivity.this, "密码错误，重新输入", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
