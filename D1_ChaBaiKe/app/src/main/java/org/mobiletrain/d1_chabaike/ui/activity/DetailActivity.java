package org.mobiletrain.d1_chabaike.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobiletrain.d1_chabaike.R;
import org.mobiletrain.d1_chabaike.beans.Info;
import org.mobiletrain.httplib.Request;
import org.mobiletrain.httplib.StringRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{


    private long id;
    private List<Info> list;
    private TextView desc;
    private TextView message;
    private TextView keyword;
    private TextView time;
    private Info info;
    private int item_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        list=new ArrayList<>();
        Intent intent=getIntent();
        id = intent.getLongExtra("id", 0);
        item_position = intent.getIntExtra("position", 0);
        System.out.println("DetailActivity:id"+ id);
        initData();
        initView();

    }

    private void initData() {
        getDetailFromNetwork();
    }

    private void getDetailFromNetwork() {
        String url="http://www.tngou.net/api/info/show?id="+id;
        StringRequest request=new StringRequest(url, Request.Method.GET, new Request.Callback<String>() {
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    //下载下来的就是一个object对象
                    List<Info> listInfo = parseJson2List(object);
                    list.addAll(listInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 解析json
     * @param object
     * @return
     */
    private List<Info> parseJson2List(JSONObject object) {
        list=new ArrayList<>();
        info = new Info();
        info.setMessage(object.optString("message"));
        info.setDescription(object.optString("description"));
        info.setRcount(object.optInt("rcount"));
        info.setImg(object.optString("img"));
        long time = object.optLong("time");
        //格式化日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String str = format.format(time);
        info.setTime(str);
        info.setId(object.optInt("id"));
        list.add(info);
        return list;
    }

    private void initView() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);

        desc = ((TextView) findViewById(R.id.detail_desc));
        keyword = ((TextView) findViewById(R.id.detail_keyword));
        message = ((TextView) findViewById(R.id.detail_message));
        time = ((TextView) findViewById(R.id.detail_time));
        System.out.println(list);
        Info info = list.get(1);
        String message1 = this.info.getMessage();
        message.setText(message1);
        System.out.println("message"+message1);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
    class DetailAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            getItem(position);
            return null;
        }
    }

}
