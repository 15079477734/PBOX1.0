package com.planboxone.MyActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.planboxone.R;
import com.planboxone.Test.SetPsdActivity;


public class SettingActivity extends BaseActivity {
    private final static String TAG = "MySettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_my_setting);
        (findViewById(R.id.lv_passward)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, SetPsdActivity.class);
                intent.putExtra("setting", true);
                SettingActivity.this.startActivity(intent);
            }
        });
        (findViewById(R.id.lv_about_us)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AboutUsActivity.class);
                SettingActivity.this.startActivity(intent);
            }
        });
        (findViewById(R.id.lv_reminder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        (findViewById(R.id.lv_net)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        (findViewById(R.id.lv_backup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        (findViewById(R.id.lv_update)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this, "你使用的版本已是最新 ", Toast.LENGTH_SHORT).show();
            }
        });
        (findViewById(R.id.lv_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent localIntent1 = new Intent("android.intent.action.SEND");
                localIntent1.setType("text/plain");
                localIntent1.putExtra("android.intent.extra.SUBJECT", "你好");
                localIntent1.putExtra("android.intent.extra.TEXT", "微计划是记录生活中重要的日子的小工具。还在为女友突然问你们相恋了多久而瞠目结舌吗？还在为关键时刻忘记女友的生日而发愁吗？那么这个小工具正是你需要的。/\n");
                startActivity(Intent.createChooser(localIntent1, "分享给好友"));
            }
        });
        (findViewById(R.id.lv_feedback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent localIntent2 = new Intent("android.intent.action.SENDTO");
                localIntent2.setData(Uri.parse("mailto:462679107.com"));
                localIntent2.putExtra("android.intent.extra.SUBJECT", "我觉得你们的软件还有一些地方可以改进");
                startActivity(Intent.createChooser(localIntent2, ""));
            }
        });
    }




}
