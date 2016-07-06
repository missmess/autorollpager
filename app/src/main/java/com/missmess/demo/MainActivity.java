package com.missmess.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickButton(View v) {
        switch (v.getId()) {
            case R.id.rollimage:
                startActivity(new Intent(this, AutoRollImageActivity.class));
                break;
            case R.id.rollview:
                startActivity(new Intent(this, AutoRollViewActivity.class));
                break;
        }
    }
}
