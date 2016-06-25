package org.mobiletrain.d1_chabaike.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.mobiletrain.d1_chabaike.R;
import org.mobiletrain.d1_chabaike.beans.Info;
import org.mobiletrain.httplib.BitmapRequest;
import org.mobiletrain.httplib.HttpHelper;
import org.mobiletrain.httplib.Request;

import java.util.List;

/**
 * Created by Administrator on 2016/6/22.
 */
public class InfoListAdapter extends BaseAdapter {
    private static final String TAG = InfoListAdapter.class.getSimpleName();
    private List<Info> infos;
    public InfoListAdapter(List<Info> infos) {
        this.infos=infos;
    }

    //数据源

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Info getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder=null;
        if (convertView==null){
            //先填充布局
            convertView=View.inflate(parent.getContext(),R.layout.item_content_lv,null);
            vHolder=new ViewHolder();
            //找到控件
            vHolder.content_item_desc= (TextView) convertView.findViewById(R.id.content_item_desc);
            vHolder.content_item_rcount= (TextView) convertView.findViewById(R.id.content_item_rcount);
            vHolder.content_item_time= (TextView) convertView.findViewById(R.id.content_item_time);
            vHolder.content_item_iv= (ImageView) convertView.findViewById(R.id.content_item_iv);

            convertView.setTag(vHolder);
        }
        vHolder = (ViewHolder) convertView.getTag();
        //更新UI
        //infos=new ArrayList<>();
        //Info info = infos.get(position);
        Info info=getItem(position);

        vHolder.content_item_desc.setText(info.getDescription());
        vHolder.content_item_rcount.setText(""+info.getRcount());
        vHolder.content_item_time.setText(info.getTime());

        //先设置一个默认的图片
        vHolder.content_item_iv.setImageResource(R.drawable.ic_launcher);
        //下载图片 ，然后在设置
        loadImage(vHolder.content_item_iv,"http://tnfs.tngou.net/image"+info.getImg()+"_100x100");
        return convertView;
    }

    //下载图片
    private void loadImage(final ImageView content_item_iv, final String url) {
        Log.d(TAG, "loadImage() returned: url=" +url);

        content_item_iv.setTag(url);
        BitmapRequest br = new BitmapRequest(url, Request.Method.GET, new Request.Callback<Bitmap>(){

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Bitmap response) {
                if (content_item_iv != null && response != null && ((String)content_item_iv.getTag()).equals(url)){
                    System.out.println("image:"+response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            content_item_iv.setImageBitmap(response);
                            System.out.println("setImageBitmap:"+response);
                        }
                    });
                }
            }

        } );
        HttpHelper.addRequest(br);
    }

    //控件封装类
    class ViewHolder {
        TextView content_item_desc;
        TextView content_item_rcount;
        TextView content_item_time;
        ImageView content_item_iv;
    }
}
