package org.mobiletrain.httplib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2016/6/22.
 */
public class BitmapRequest extends Request<Bitmap>{
    public BitmapRequest(String url, Method method, Callback callback) {
        super(url, method, callback);
    }

    @Override
    public void dispatchContent(byte[] content) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);
        onResponse(bitmap);
    }
}
