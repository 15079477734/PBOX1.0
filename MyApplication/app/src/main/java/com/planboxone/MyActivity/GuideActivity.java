//引导界面的activity
package com.planboxone.MyActivity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.planboxone.R;

public class GuideActivity extends Activity {

    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private ViewPager mviewPager;
    /**
     * 装分页现实的View(导航页面)的数组
     */
    public ArrayList<View> pageViews;
    private ImageView imageView;
    /**
     * 装小圆点的View的数组
     */
    private ImageView[] imageViews;
    /**
     * 装N个导航页面的linearlayout
     */
    private ViewGroup viewPics;

    /**
     * 装圆点数组的linearlayout
     */
    private ViewGroup viewPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater mInflater = getLayoutInflater();
//向pageviews数组中加载各个布局，若多个导航页，从此处添加
        pageViews = new ArrayList<View>();
        pageViews.add(mInflater.inflate(R.layout.viewpager_page1, null));
        pageViews.add(mInflater.inflate(R.layout.viewpager_page2, null));
        pageViews.add(mInflater.inflate(R.layout.viewpager_page3, null));
        pageViews.add(mInflater.inflate(R.layout.viewpager_page4, null));
//实例化
        //创建装小圆点的imageviews，大小是页面的多少
        imageViews = new ImageView[pageViews.size()];
        //从指定的xml文件加载视图
        viewPics = (ViewGroup) mInflater.inflate(R.layout.activity_guide, null);
        //实例化小圆点和viewpager(图片）的linearlayout
        viewPoints = (ViewGroup) viewPics.findViewById(R.id.viewGroup);
        mviewPager = (ViewPager) viewPics.findViewById(R.id.guidePages);
        //添加小圆点
        for (int i = 0; i < pageViews.size(); i++) {
            imageView = new ImageView(GuideActivity.this);
            imageView.setLayoutParams(new LayoutParams(20, 20));
            imageView.setPadding(20, 0, 20, 0);
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.black_chess);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.white_chess);
            }//选中时显示黑棋，未选中显示白棋
            viewPoints.addView(imageViews[i]);

        }
        setContentView(viewPics);
        mviewPager.setAdapter(new GuidePageAdapter());

        mviewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                for (int i = 0; i < imageViews.length; i++) {
                    imageViews[arg0].setBackgroundResource(R.drawable.black_chess);
                    if (i != arg0) {
                        imageViews[i].setBackgroundResource(R.drawable.white_chess);
                    }
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    class GuidePageAdapter extends PagerAdapter {
        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView(pageViews.get(position));
        }

        @Override
        public void finishUpdate(View container) {
            // TODO Auto-generated method stub
            //super.finishUpdate(container);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pageViews.size();
        }

        @Override
        public Object instantiateItem(View container, int position) {
            // TODO Auto-generated method stub
            ((ViewPager) container).addView(pageViews.get(position));
            if (position == pageViews.size() - 1) {
                Button btnButton = (Button) findViewById(R.id.btn);
                btnButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub

                        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFERENCES_NAME, 0);
                        Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_GUIDE_ACTIVITY, "false");
                        editor.commit();
                        Intent mIntent = new Intent();
                        mIntent.setClass(GuideActivity.this, MainActivity.class);
                        GuideActivity.this.startActivity(mIntent);
                        GuideActivity.this.finish();

                    }
                });
            }
            return pageViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }
    }

}

