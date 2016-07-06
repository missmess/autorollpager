package com.missmess.demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.missmess.autorollpager.AutoRollViewPager;
import com.missmess.autorollpager.BaseRollAdapter;

public class AutoRollViewActivity extends AppCompatActivity {

    private AutoRollViewPager arp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_roll_view);

        init();
        initData();
    }

    private void init() {
        arp = (AutoRollViewPager) findViewById(R.id.arp);
    }

    private void initData() {
        arp.setRollAdapter(new MyRollAdapter(this));
        arp.setLoopMode(false);
        arp.setRollInterval(5000);
        arp.showUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arp.tearDown();
    }

    public void onClickButton(View v) {
        switch (v.getId()) {
            case R.id.agree:
                Toast.makeText(AutoRollViewActivity.this, "smart choice", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reject:
                Toast.makeText(AutoRollViewActivity.this, "WTF!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class MyRollAdapter extends BaseRollAdapter {

        public MyRollAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            View content = null;
            switch (position) {
                case 0:
                    content = View.inflate(context, R.layout.item_roll_view_1, null);
                    break;
                case 1:
                    content = View.inflate(context, R.layout.item_roll_view_2, null);
                    break;
                case 2:
                    content = View.inflate(context, R.layout.item_roll_view_3, null);
                    break;
                case 3:
                    content = View.inflate(context, R.layout.item_roll_view_4, null);
                    break;
            }

            return content;
        }

        @Override
        public int getRealCount() {
            return 4;
        }
    }
}
