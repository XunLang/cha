package org.mobiletrain.d1_chabaike.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.mobiletrain.d1_chabaike.R;
import org.mobiletrain.d1_chabaike.beans.TabInfo;
import org.mobiletrain.d1_chabaike.ui.fragment.ContentFragment;

public class HomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ContentAdapter adapter;

    private TabInfo[] tabs=new TabInfo[]{
            new TabInfo("社会热点",6),
            new TabInfo("食品新闻",5),
            new TabInfo("疾病快讯",7),
            new TabInfo("药品新闻",4),
            new TabInfo("生活贴士",3),
            new TabInfo("医疗新闻",2),
            new TabInfo("企业要闻",1),
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    private void initView() {

        tabLayout= (TabLayout) findViewById(R.id.home_tablayout);
        viewPager= (ViewPager) findViewById(R.id.home_vp);

        adapter=new ContentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }
    class ContentAdapter extends FragmentStatePagerAdapter{

        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ContentFragment cf=new ContentFragment();
            Bundle bundle=new Bundle();
            //把分类ID传过去
            bundle.putInt("id",tabs[position].class_id);
            cf.setArguments(bundle);
            return cf;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }


        //获取tab标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position].name;
        }
    }
}
