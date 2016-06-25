package org.mobiletrain.d1_chabaike.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.mobiletrain.d1_chabaike.R;
import org.mobiletrain.d1_chabaike.app.ConstantKey;
import org.mobiletrain.d1_chabaike.utils.Pref_Utils;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout ll_container;
    private WelcomeAdapter adapter;
    private int[] imgId = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};
    private ImageView[] imageViews = new ImageView[imgId.length];
    //圆点的前一个下标
    private int pre_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        viewPager = (ViewPager) findViewById(R.id.welcome_vp);
        initView();
        adapter=new WelcomeAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(adapter);
    }

    private void initView() {

        ll_container = (LinearLayout) findViewById(R.id.welcome_ll);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10, 10);

        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView iv = null;
        //创建一个view，添加到ll_container中
        View view = null;
        for (int i = 0; i < imgId.length; i++) {
            iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(vlp);

            imageViews[i] = iv;
            imageViews[i].setImageResource(imgId[i]);

            //当到第三页时，点击调转到HomeActivity
            if (i == imgId.length - 1) {
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(WelcomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                        //点击返回时，不会进入到欢迎页面
                        finish();
                    }
                });
            }
            view = new View(this);
            lp.leftMargin = 10;
            if (i == 0) {
                view.setBackgroundResource(R.drawable.page_now);
            } else {
                view.setBackgroundResource(R.drawable.page);
            }
            //设置参数
            view.setLayoutParams(lp);
            //向ll_container容器中添加view
            ll_container.addView(view);
        }

        viewPager.setAdapter(new WelcomeAdapter());
    }

    class WelcomeAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        @Override
        public int getCount() {
            return imgId.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews[position]);
            return imageViews[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //当没有删除这句时，滑到第三页时会出现异常
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            ll_container.getChildAt(position).setBackgroundResource(R.drawable.page_now);
            ll_container.getChildAt(pre_index).setBackgroundResource(R.drawable.page);
            pre_index = position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获得焦点后，将PRE_KEY_FIRST_OPEN设置为false 下次再打开软件，就不会出现欢迎页
        Pref_Utils.putBoolean(this, ConstantKey.PRE_KEY_FIRST_OPEN,false);
    }
}
