package org.mobiletrain.d1_chabaike.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobiletrain.d1_chabaike.R;
import org.mobiletrain.d1_chabaike.adapter.InfoListAdapter;
import org.mobiletrain.d1_chabaike.beans.Info;
import org.mobiletrain.d1_chabaike.ui.activity.DetailActivity;
import org.mobiletrain.httplib.HttpHelper;
import org.mobiletrain.httplib.Request;
import org.mobiletrain.httplib.StringRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ContentFragment extends Fragment {

    private static final String TAG = ContentFragment.class.getSimpleName();
    private ListView listView;
    //InfoListAdapter的引用
    private InfoListAdapter adapter;
    private int class_id;
    private PtrClassicFrameLayout mRefreshView;
    private List<Info> list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        class_id = bundle.getInt("id");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable final Bundle bundle) {
        View view = inflater.inflate(R.layout.frag_content, container, false);
        initView(view);

        if (bundle != null) {
            Parcelable[] ps = bundle.getParcelableArray("cache");
            Info[] ins = (Info[]) bundle.getParcelableArray("cache");
            if (ins != null && ins.length != 0) {
                infos = Arrays.asList(ins);
                adapter = new InfoListAdapter(infos);
                listView.setAdapter(adapter);
            } else {
                getDataFromNetwork();
            }
        } else {
            getDataFromNetwork();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String url_detail="http://www.tngou.net/api/info/show?id=6541"+;
                Intent intent=new Intent(getContext(), DetailActivity.class);
                Info info = list.get(position);
                //拿到每项的Id传到detailactivity
                long detail_id = info.getId();
                System.out.println("id:"+detail_id);
                Bundle bundle1=new Bundle();
                bundle1.putLong("id",detail_id);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        //listView= (ListView) view.findViewById(R.id.frag_content_lv);
        //infoListAdapter=new InfoListAdapter();
        //listView.setAdapter(infoListAdapter);
        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.frag_content_lv);
        mRefreshView = (PtrClassicFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
        mRefreshView.setResistance(1.7f);
        mRefreshView.setRatioOfHeaderHeightToRefresh(1.2f);
        mRefreshView.setDurationToClose(200);
        mRefreshView.setDurationToCloseHeader(1000);
        // default is false
        mRefreshView.setPullToRefresh(true);
        // default is true
        mRefreshView.setKeepHeaderWhenRefresh(true);

        mRefreshView.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getDataFromNetwork();
            }
        });

    }

    private List<Info> infos = new ArrayList<>();

    //下载数据
    private void getDataFromNetwork() {
        String url = "http://www.tngou.net/api/info/list?id=" + class_id;
        StringRequest request = new StringRequest(url, Request.Method.GET, new Request.Callback<String>() {

            @Override
            public void onError(Exception e) {
                    e.printStackTrace();
            }

            //这是返回来的结果 response
            @Override
            public void onResponse(String response) {
                try {
                    //解析json
                    JSONObject object=new JSONObject(response);
                    //返回的是Info对象的集合
                    List<Info> listInfo = parseJson2List(object);
                    if (listInfo != null) {
                        infos.clear();
                        infos.addAll(listInfo);
                        if (adapter == null) {
                            //把infos传到InfoListAdapter
                            adapter = new InfoListAdapter(infos);
                            System.out.println(adapter);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(adapter);
                                }
                            });

                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshView.refreshComplete();
                    }
                });
            }
        });
        //添加到请求中
        HttpHelper.addRequest(request);
    }

    private List<Info> parseJson2List(JSONObject object) {
        if (object == null) return null;
        list = null;
        try {
            //解析json是看到array要遍历
            JSONArray array = object.getJSONArray("tngou");
            if (array.length() == 0 || array == null) return null;
            list = new ArrayList<>();
            Info info = null;
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                info=new Info();
                info.setDescription(obj.optString("description"));
                info.setRcount(obj.optInt("rcount"));
                info.setImg(obj.optString("img"));
                long time = obj.optLong("time");
                //格式化日期
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String str = format.format(time);
                info.setTime(str);
                info.setId(obj.optInt("id"));
                list.add(info);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //fragment销毁时调用
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (infos == null || infos.size() == 0) return;
        Info[] parce = new Info[infos.size()];
        infos.toArray(parce);
        outState.putParcelableArray("cache",parce);
    }

}
