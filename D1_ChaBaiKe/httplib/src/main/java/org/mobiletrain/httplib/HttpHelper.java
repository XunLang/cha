package org.mobiletrain.httplib;

/**
 * Created by Administrator on 2016/6/21.
 */
public class HttpHelper {
    private static HttpHelper Instance;
    private RequestQueue mQueue;

    //懒汉式
    private static HttpHelper getInstance(){
        if (Instance==null){
            synchronized (HttpHelper.class){
                if (Instance==null){
                    Instance=new HttpHelper();
                }
            }
        }
        return Instance;
    }

    private HttpHelper(){
        mQueue=new RequestQueue();
    }

    //添加到队列中
    public static void addRequest(Request request){
        getInstance().mQueue.addRequest(request);
    }
}
