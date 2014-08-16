package com.planboxone.MyActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.library.util.Calculator;
import com.library.util.DatabaseManage;
import com.library.util.MemoryCache;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.planboxone.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class ContentFragment extends Fragment {
    private final static String TAG = "ContentFragment";
    private DatabaseManage databaseManage;
    private ListView listView;
    private ArrayList<String> ids;
    private List<Map<String, String>> planData;
    private String dbName;
    private int newsType = 0;

    public ContentFragment() {
    }

    public void setType(int newsType) {
        this.newsType = newsType;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //A计划与B计划的的判定

        dbName = "AP";
        switch (newsType) {
            case 0:
                dbName = "AP";
                break;
            case 1:
                dbName = "BP";
                break;
        }
        Log.e(TAG, dbName);

        //数据库处理

        databaseManage = new DatabaseManage(getActivity(), dbName);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.activity_my_list, null);
        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_tip, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(view1).create();
                alertDialog.setCanceledOnTouchOutside(true);


                (view1.findViewById(R.id.btn_delete)).setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deletePlan(position);
                        alertDialog.dismiss();
                    }
                });
                (view1.findViewById(R.id.btn_edit)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        editPlan(position);
                    }
                });
                (view1.findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addPlanProgress(position);

                    }
                });
                (view1.findViewById(R.id.btn_progress)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        refresh();
                    }
                });
                alertDialog.show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    public void deletePlan(int position) {
        if (databaseManage.deleteData("_id = ?", new String[]{ids.get(position)})) {
            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            refresh();
        }
    }

    public void editPlan(int position) {
        Map<String, String> str = databaseManage.findData("_id = ?", new String[]{ids.get(position)});
        Intent intent = new Intent(getActivity(), WritePlanActivity.class);
        intent.putExtra("_id", str.get("_id"));
        intent.putExtra("dbName", dbName);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.zoin);
    }

    public void addPlanProgress(int position) {
        ContentValues values = new ContentValues();
        int progress = Integer.valueOf(databaseManage.findData("_id = ?", new String[]{ids.get(position)}).get("progress")) + 25;
        if (progress > 100) progress = 0;
        values.put("progress", progress);
        databaseManage.updateData(values, "_id = ?", new String[]{ids.get(position)});
    }

    public void refresh() {
        sortData();
        BaseAdapter mAdapter = new MyAdapter(getActivity(), getItems(), planData);
        AnimationAdapter animAdapter = new ScaleInAnimationAdapter(mAdapter);
        animAdapter.setAbsListView(listView);
        listView.setAdapter(animAdapter);
    }

    public void sortData() {
        List<Map<String, String>> data2 = databaseManage.listData();
        planData = new ArrayList<Map<String, String>>();


        for (Map<String, String> m : data2) {
            String str = m.get("progress");
            if (m.get("top").equals("1"))
                m.put("type", "6");
            else if (str.equals("100"))
                m.put("type", "5");
            else {
                int a = Calculator.calculate(m.get("date"));
                if (a > 20)
                    m.put("type", "4");
                else if (a > 10)
                    m.put("type", "3");
                else if (a > 5)
                    m.put("type", "2");
                else
                    m.put("type", "1");
            }
            planData.add(m);
        }


        Collections.sort(planData, new MapComparator());


        ids = new ArrayList<String>();
        for (Map<String, String> m : planData) {
            ids.add(m.get("_id"));
        }

    }

    public ArrayList<Integer> getItems() {
        ArrayList<Integer> items = new ArrayList<Integer>();
        for (int i = 0; i < planData.size(); i++) {
            items.add(i);
        }
        return items;
    }

    final class MyAdapter extends ArrayAdapter<Integer> {
        private final Context mContext;
        private List<Map<String, String>> mData;


        public MyAdapter(Context context, ArrayList<Integer> items, List<Map<String, String>> list) {
            super(items);
            mContext = context;
            this.mData = list;
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View contentView, ViewGroup parent) {
            ViewHolder holder;
            if (contentView == null) {
                holder = new ViewHolder();
                contentView = LayoutInflater.from(getActivity()).inflate(R.layout.plantwo, null);
                holder.title = (TextView) contentView.findViewById(R.id.tv_event_title);
                holder.time = (TextView) contentView.findViewById(R.id.tv_event_time);
                holder.head = (ImageView) contentView.findViewById(R.id.lv_event_head);
                holder.progress = (TextView) contentView.findViewById(R.id.tv_event_progress);
                holder.background = (LinearLayout) contentView.findViewById(R.id.ll_event_background);
                holder.due = (TextView) contentView.findViewById(R.id.tv_event_due);
                contentView.setTag(holder);
            } else {
                holder = (ViewHolder) contentView.getTag();
            }
            String dueTime = mData.get(position).get("date");
            String title = mData.get(position).get("title");
            int day = Calculator.calculate(dueTime);

            holder.progress.setText(mData.get(position).get("progress") + "%");
            holder.due.setText(dueTime);
            if (day < 0) {
                holder.title.setText(title + "已经");
            } else {
                holder.title.setText(title + "还剩");
            }
            holder.time.setText(String.valueOf(Math.abs(day)) + "天");

            int imageResId;
            int t = Integer.valueOf(mData.get(position).get("type"));
            switch (t) {
                case 1:
                    imageResId = R.drawable.cover_bg4;
                    holder.background.setBackgroundResource(R.drawable.listitem_red);
                    break;
                case 2:
                    imageResId = R.drawable.cover_bg5;
                    holder.background.setBackgroundResource(R.drawable.listitem_yellow);
                    break;
                case 3:
                    imageResId = R.drawable.cover_bg6;
                    holder.background.setBackgroundResource(R.drawable.listitem_blue);
                    break;
                case 4:
                    imageResId = R.drawable.cover_bg3;
                    holder.background.setBackgroundResource(R.drawable.listitem_green);
                    break;
                default:
                    imageResId = R.drawable.cover_bg1;
                    holder.background.setBackgroundResource(R.drawable.listitem_white);
                    break;
            }
            holder.background.setPadding(6, 8, 8, 8);

            MemoryCache memoryCache = new MemoryCache();
            Bitmap bitmap = memoryCache.getBitmapFromMemCache(imageResId);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
                memoryCache.addBitmapToMemoryCache(imageResId, bitmap);
            }
            holder.head.setImageBitmap(bitmap);
            return contentView;
        }
    }

    final class ViewHolder {
        public TextView progress;
        public LinearLayout background;
        public TextView title;
        public TextView time;
        public ImageView head;
        public TextView due;
    }

    final class MapComparator implements Comparator<Map<String, String>> {
        @Override
        public int compare(Map<String, String> o1, Map<String, String> o2) {
            String b1 = o1.get("type");
            String b2 = o2.get("type");
            return b1.compareTo(b2);
        }
    }
}
