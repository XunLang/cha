package org.mobiletrain.httplib;

import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

/**
 * Created by Administrator on 2016/6/21.
 */
public class NetworkDispatcher extends Thread{

    private static final String TAG = NetworkDispatcher.class.getSimpleName();
    private  BlockingDeque<Request> mQueue;
    public boolean flag=true;

    //通过构造方法，把阻塞请求队列传过来
    public NetworkDispatcher(BlockingDeque<Request> queue) {
        this.mQueue=queue;
    }

    @Override
    public void run() {
        //子线程，访问网络
        //如果当前线程的标记是可运行的而且当前线程没有被打断，从请求队列里面取出请求进行网络访问
        if (flag && !isInterrupted()) {
            try {
                //从请求队列中取出一个请求
                Request req = mQueue.take();

                try {
                    //返回的是String类型数据
                    byte[] result = getNetworkResponse(req);
                    if (result != null) {
                        //返回的结果不为空，回调正常的返回结果
                        System.out.println("come here"+result);
                        req.dispatchContent(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    req.onError(e);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                flag=false;
            }
        }
    }

    private byte[] getNetworkResponse(Request request) throws Exception {
        if (TextUtils.isEmpty(request.getUrl())){
            throw new Exception("url is null");
        }
        if (request.getMethod()== Request.Method.GET){
            return getResponseByGET(request);
        }
        if (request.getMethod()== Request.Method.POST){
            return getResponseByPOST(request);
        }
        return null;
    }

    //post提交
    private byte[] getResponseByPOST(Request request) throws Exception {
        URL url=null;
        InputStream is=null;
        ByteArrayOutputStream bos=null;

        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setReadTimeout(5000);

        //设置上传的参数的字节长度
        String str=getPostEncodeString(request);
        byte[] bytes=null;
        if (str!=null){
            bytes=str.getBytes();
            conn.setRequestProperty("bytes-length",""+bytes.length);
        }
        OutputStream os = conn.getOutputStream();
        if (bytes!=null){
            os.write(bytes,0,bytes.length);
            os.flush();
        }

        int code = conn.getResponseCode();
        if (code!=200){
            Log.d(TAG, "getResponseByPOST: return:response code error code ="+code);
            throw new IllegalArgumentException("code error");
        }

        is=conn.getInputStream();
        bos=new ByteArrayOutputStream();
        int len=-1;
        byte[] buf=new byte[1024];
        while ((len=is.read())!=-1){
            bos.write(buf,0,len);
            bos.flush();
        }
        byte[] result = bos.toByteArray();
        is.close();
        os.close();
        return result;
    }

    //get提交
    private byte[] getResponseByGET(Request request) throws Exception{
        URL url=null;
        InputStream is=null;
        ByteArrayOutputStream bos=null;
        HttpURLConnection connection=null;

        url=new URL(request.getUrl());
        System.out.println("url:"+request.getUrl());
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(5000);
        int code = connection.getResponseCode();

        if (code==200){
            is = connection.getInputStream();
            bos=new ByteArrayOutputStream();
            int len=-1;
            byte[] bytes=new byte[1024];
            while ((len=is.read(bytes))!=-1){
                bos.write(bytes,0,len);
            }
            Log.d(TAG, "getResponseByGET() returned: " + connection.getRequestProperty("content-type"));
            //转成字节数组
            byte[] result = bos.toByteArray();
            is.close();
            return result;
        }else {
            Log.d(TAG, "getResponseByGET: return:response code error code="+code);
            throw new IllegalArgumentException("code error");
        }

    }

    //这个方法看不太懂
    private String getPostEncodeString(Request request) {
        HashMap<String, String> postParams = request.getPostParams();
        StringBuffer buf=new StringBuffer();
        if (postParams!=null){
            //迭代
            Iterator<Map.Entry<String,String>> iterator=postParams.entrySet().iterator();
            int i=0;
            while (iterator.hasNext()){
                if (i>0){
                    buf.append("&");
                }
                Map.Entry<String,String> value=iterator.next();
                String str = value.getKey() + "=" + value.getValue();
                buf.append(str);
                i++;
            }
            //return buf.toString();
            return buf.toString();
        }
        return null;
    }
}
