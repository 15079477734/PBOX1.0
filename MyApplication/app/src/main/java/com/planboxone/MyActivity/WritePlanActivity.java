package com.planboxone.MyActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.library.googledatetimepicker.date.DatePickerDialog;
import com.library.util.DatabaseManage;
import com.library.util.GetDate;
import com.planboxone.R;

import java.util.Calendar;
import java.util.Map;

import static com.library.util.Format.pad;
import static com.library.util.GetDate.getWeek;

public class WritePlanActivity extends BaseActivity {
    private final static String TAG = "MyWritePlanActivity";
    private DatabaseManage mDatabaseManage;
    private ContentValues mDatavalues;
    private ViewWriteHolder mViewWriteHolder;
    private String mTitle, mDate, mCategory, mTop, mNote, mTime;
    private String eoc = null;
    private final Calendar mCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_plan);
        init();
        viewWrite();
    }

    public void init() {
        String str = null;
        String dbName = null;
        Intent intent = getIntent();

        try {
            str = intent.getStringExtra("_id");
            dbName = intent.getStringExtra("dbName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == str) {
            initViewWrite();
        } else {
            initEditView(str, dbName);
        }


    }

    public void initViewWrite() {
        mTitle = "";
        mCategory = "工作";
        mDate = GetDate.getDate();
        mTop = "0";
        mNote = "";
        eoc = "0";
        mTime = "A计划";

    }

    public void initEditView(String str, String dbName) {
        mDatabaseManage = new DatabaseManage(WritePlanActivity.this, dbName);
        eoc = str;
        Map<String, String> map = mDatabaseManage.findData("_id = ?", new String[]{str});
        mTitle = map.get("title");
        mCategory = map.get("category");
        mDate = map.get("date");
        mTop = map.get("top");
        mNote = map.get("note");
        mTime = map.get("time");
    }

    public void findView() {
        mViewWriteHolder = new ViewWriteHolder();
        mViewWriteHolder.date_textview = (TextView) findViewById(R.id.date_textview);
        mViewWriteHolder.category_textview = (TextView) findViewById(R.id.category_textview);
        mViewWriteHolder.time_textview = (TextView) findViewById(R.id.time_textview);
        mViewWriteHolder.note_edittext = (EditText) findViewById(R.id.note_edittext);
        mViewWriteHolder.title_edittext = (EditText) findViewById(R.id.title_edittext);
        mViewWriteHolder.top_checkBox = (CheckBox) findViewById(R.id.top_checkbox);
        mViewWriteHolder.confirm_button = (Button) findViewById(R.id.confirm_button);
        mViewWriteHolder.cancel_button = (Button) findViewById(R.id.cancel);
        mViewWriteHolder.class_layout = (RelativeLayout) findViewById(R.id.class_relative);
        mViewWriteHolder.title_layout = (RelativeLayout) findViewById(R.id.title_relative);
        mViewWriteHolder.date_layout = (RelativeLayout) findViewById(R.id.date_relative);
        mViewWriteHolder.note_layout = (RelativeLayout) findViewById(R.id.note_relative);
        mViewWriteHolder.time_layout = (RelativeLayout) findViewById(R.id.time_relative);
    }

    public void viewWrite() {
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

                String str = pad(year) + "-" + pad(month + 1) + "-" + pad(day) + " " + getWeek(year, month, day);
                mViewWriteHolder.date_textview.setText(str);
                mDatavalues.put("date", str);
            }

        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatavalues = new ContentValues();
        findView();
        mDatavalues.put("title", mTitle);
        mDatavalues.put("date", mDate);
        mDatavalues.put("note", mNote);
        mDatavalues.put("category", mCategory);
        mDatavalues.put("time", mTime);
        mDatavalues.put("top", mTop);
        if (mTop.equals("1"))
            mViewWriteHolder.top_checkBox.setChecked(true);
        else
            mViewWriteHolder.top_checkBox.setChecked(false);

        mViewWriteHolder.time_textview.setText(mTime);
        mViewWriteHolder.category_textview.setText(mCategory);
        mViewWriteHolder.date_textview.setText(mDate);
        mViewWriteHolder.title_edittext.setText(mTitle);

        mViewWriteHolder.date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = "";
                datePickerDialog.show(WritePlanActivity.this.getFragmentManager(), tag);
            }
        });
        mViewWriteHolder.class_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(WritePlanActivity.this);
                final View view1 = inflater.inflate(R.layout.dialog_class, null);
                final RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.class_radiogroup);
                final AlertDialog alertDialog = new AlertDialog.Builder(WritePlanActivity.this).setTitle("分类选择").setView(view1).create();
                alertDialog.show();
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup arg0, int arg1) {
                        int radioButtonId = arg0.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) view1.findViewById(radioButtonId);
                        String str = rb.getText().toString();
                        mViewWriteHolder.category_textview.setText(str);
                        mDatavalues.put("category", str);
                        alertDialog.dismiss();
                    }
                });
            }
        });
        mViewWriteHolder.top_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewWriteHolder.top_checkBox.isChecked()) ;
            }
        });
        mViewWriteHolder.time_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(WritePlanActivity.this);
                final View view1 = inflater.inflate(R.layout.dialog_time, null);
                RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.time_radiogroup);
                final AlertDialog alertDialog = new AlertDialog.Builder(WritePlanActivity.this).setView(view1).setTitle("时间选择 ").create();
                alertDialog.show();
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup arg0, int arg1) {
                        int radioButtonId = arg0.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) view1.findViewById(radioButtonId);
                        String str = rb.getText().toString();
                        String dbName = "AP";
                        if (str.equals("A计划")) dbName = "AP";
                        if (str.equals("B计划")) dbName = "BP";
                        if (str.equals("C计划")) dbName = "CP";
                        mDatabaseManage = new DatabaseManage(WritePlanActivity.this, dbName);
                        mViewWriteHolder.time_textview.setText(str);
                        mDatavalues.put("time", str);
                        alertDialog.dismiss();
                    }
                });
            }
        });
        mViewWriteHolder.confirm_button.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View view) {
                                                                   String title = mViewWriteHolder.title_edittext.getText().toString();
                                                                   if (mViewWriteHolder.top_checkBox.isChecked())
                                                                       mDatavalues.put("top", "1");
                                                                   else
                                                                       mDatavalues.put("top", "0");
                                                                   if (title.equals(""))
                                                                       Toast.makeText(WritePlanActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                                                                   else {
                                                                       mDatavalues.put("title", title);
                                                                       mDatavalues.put("note", mViewWriteHolder.note_edittext.getText().toString());
                                                                       if (eoc.equals("0")) {
                                                                           mDatabaseManage.addData(mDatavalues);
                                                                           Toast.makeText(WritePlanActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                                                                           finish();
                                                                       } else {
                                                                           Log.e("123", mDatavalues.toString());
                                                                           if (mDatabaseManage.updateData(mDatavalues, "_id=?", new String[]{eoc}))
                                                                               Toast.makeText(WritePlanActivity.this, "更新成功", Toast.LENGTH_LONG).show();
                                                                           finish(); /*  Intent intent = new Intent(MyWritePlanActivity.this, MyListPlanActivity.class); startActivity(intent);*/
                                                                       }
                                                                   }
                                                               }
                                                           }
        );
        mViewWriteHolder.cancel_button.setOnClickListener(new View.OnClickListener()

                                                          {
                                                              @Override
                                                              public void onClick(View view) {
                                                                  finish();
                                                              }
                                                          }
        );


    }
}

final class ViewWriteHolder {
    public EditText title_edittext;
    public TextView date_textview, category_textview, note_edittext, time_textview;
    public CheckBox top_checkBox;
    public Button confirm_button, cancel_button;
    public RelativeLayout title_layout, note_layout, date_layout, class_layout, time_layout;

}

