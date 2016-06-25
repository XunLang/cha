package org.mobiletrain.httplib;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Administrator on 2016/6/22.
 */
public class RequestQueue {

    //阻塞的请求队列
    private BlockingDeque<Request> requestQueue=new LinkedBlockingDeque<>();
    //最大线程数 3
    private int MAX_THREAD=3;
    //后台线程的引用
    private NetworkDispatcher[] worker=new NetworkDispatcher[MAX_THREAD];

    public RequestQueue(){
        initNetworkDispatcher();
    }

    private void initNetworkDispatcher() {
        for (int i = 0; i < MAX_THREAD; i++) {
            //创建访问网络的线程类
            worker[i]=new NetworkDispatcher(requestQueue);
            worker[i].start();
        }
    }

    //把请求添加到请求队列中 同步synchroized
    public synchronized void addRequest(Request request){
        requestQueue.add(request);
    }
    public void stop(){
        for (int i = 0; i < worker.length; i++) {
            //NetworkDispatcher 中public boolean flag=ture; 注意public,刚才写成private，所以有错误
            worker[i].flag=false;
            worker[i].interrupt();
        }
    }
}
