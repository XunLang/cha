package org.mobiletrain.httplib;

import java.nio.charset.Charset;

/**
 * Created by Administrator on 2016/6/22.
 */
public class StringRequest extends Request<String> {
    public StringRequest(String url, Method method, Callback callback) {
        super(url, method, callback);
    }

    @Override
    public void dispatchContent(byte[] content) {
       onResponse(new String(content, Charset.defaultCharset()));//这里把字节数组，变成字符串
    }
}
