package org.mobiletrain.d1_chabaike.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import org.mobiletrain.d1_chabaike.R;

import org.mobiletrain.d1_chabaike.app.ConstantKey;
import org.mobiletrain.d1_chabaike.utils.Pref_Utils;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //loading页面停留三秒
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent intent=new Intent();
               intent.setClass(LoadingActivity.this,WelcomeActivity.class);

               if (!getFirstOpenFlag()){
                   intent.setClass(LoadingActivity.this,HomeActivity.class);
               }
               startActivity(intent);
               finish();
           }
       },3000);
    }
    public boolean getFirstOpenFlag(){
        return Pref_Utils.getBoolean(this, ConstantKey.PRE_KEY_FIRST_OPEN,true);
    }
}
