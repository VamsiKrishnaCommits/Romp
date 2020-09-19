package com.app.romp.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.romp.Auth;
import com.app.romp.R;

public class Main8Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        final Intent inte=new Intent(Main8Activity.this, Auth.class);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(inte);
                finish();
            }
        }, 1000);
    }
}
