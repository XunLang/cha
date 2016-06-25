package org.mobiletrain.httplib;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/22.
 */
abstract public class Request<T> {
    private String url;
    private Method method;
    private Callback callback;

    //构造方法
    public Request(String url, Method method ,Callback callback) {
        this.url = url;
        this.method = method;
        this.callback = callback;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    //枚举
    public enum Method{
        POST,GET;
    }

    //回调方法
    public Callback getCallback(){
        return callback;
    }
    public void onError(Exception e){
        callback.onError(e);
    }
    protected void onResponse(T res){
        callback.onResponse(res);
    }
    //接口
    public interface Callback<T>{
        //抽象方法
        void onError(Exception e);
        void onResponse(T response);
    }

    //这个方法是什么作用？
    public HashMap<String,String> getPostParams(){
        return null;
    }
    //抽象方法
    abstract public void dispatchContent(byte[] content);
}
