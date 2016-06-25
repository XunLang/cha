package org.mobiletrain.d1_chabaike.beans;

/**
 * Created by Administrator on 2016/6/22.
 */
public class TabInfo {
    public String name;
    //分类ID
    public int class_id;

    public TabInfo(String name, int class_id) {
        this.name = name;
        this.class_id = class_id;
    }
}
