package com.missmess.demo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.missmess.autorollpager.AutoRollImagePager;
import com.missmess.autorollpager.AutoRollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoRollImageFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    private AutoRollImagePager arp;
    private CheckBox cb_roll;
    private CheckBox cb_loop;
    private CheckBox cb_title;
    private CheckBox cb_click;
    private CheckBox cb_dot;
    private SeekBar sb_dot_interval;
    private SeekBar sb_roll_interval;
    private List<String> titles;
    private TextView tv_dot;
    private TextView tv_roll;
    private SwitchCompat switch_direction;
    private Spinner sp_dot_gravity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_auto_roll_image, container, false);

        init(content);
        initData();
        return content;
    }

    private void init(View content) {
        arp = (AutoRollImagePager) content.findViewById(R.id.arp);
        cb_roll = (CheckBox) content.findViewById(R.id.cb_roll);
        cb_loop = (CheckBox) content.findViewById(R.id.cb_loop);
        cb_title = (CheckBox) content.findViewById(R.id.cb_title);
        sp_dot_gravity = (Spinner) content.findViewById(R.id.sp_dot_gravity);
        cb_click = (CheckBox) content.findViewById(R.id.cb_click);
        cb_dot = (CheckBox) content.findViewById(R.id.cb_dot);
        sb_dot_interval = (SeekBar) content.findViewById(R.id.sb_dot_interval);
        sb_roll_interval = (SeekBar) content.findViewById(R.id.sb_roll_interval);
        tv_dot = (TextView) content.findViewById(R.id.tv_dot);
        tv_roll = (TextView) content.findViewById(R.id.tv_roll);
        switch_direction = (SwitchCompat) content.findViewById(R.id.switch_direction);

        cb_roll.setOnCheckedChangeListener(this);
        cb_loop.setOnCheckedChangeListener(this);
        cb_title.setOnCheckedChangeListener(this);
        cb_dot.setOnCheckedChangeListener(this);
        cb_click.setOnCheckedChangeListener(this);
        sb_dot_interval.setOnSeekBarChangeListener(this);
        sb_roll_interval.setOnSeekBarChangeListener(this);
        switch_direction.setOnCheckedChangeListener(this);

        ArrayList<Map<String, String>> data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, String> map = new HashMap<>();
            String val = "";
            if (i == 0)
                val = "居中";
            else if (i == 1)
                val = "最左";
            else if (i == 2)
                val = "最右";
            map.put("data", val);
            data.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getContext(), data,
                android.R.layout.simple_list_item_1, new String[]{"data"}, new int[]{android.R.id.text1});
        sp_dot_gravity.setAdapter(adapter);
        sp_dot_gravity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    arp.setDotGravity(AutoRollViewPager.GRAVITY_CENTER);
                } else if (position == 1) {
                    arp.setDotGravity(AutoRollViewPager.GRAVITY_LEFT);
                } else if (position == 2) {
                    arp.setDotGravity(AutoRollViewPager.GRAVITY_RIGHT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData() {
        List<Object> images = new ArrayList<>();
        titles = new ArrayList<>();

        images.add(R.mipmap.pic1);
        images.add(R.mipmap.pic2);
        images.add("http://img3.imgtn.bdimg.com/it/u=509912007,3678988032&fm=21&gp=0.jpg");
        images.add(createADrawable());

        titles.add("本地图片1");
        titles.add("本地图片2");
        titles.add("网络图片");
        titles.add("Drawable图片");

        //设置图片源
        arp.setImageLists(images);
        //设置描述标题
        arp.setTitles(titles);
        //自动滚动（默认true）
        arp.setAutoRoll(true);
        //设置指示点间隔（默认5dp）
        arp.setDotInterval(8);
        //是否无限循环模式（默认false）
        arp.setLoopMode(true);
        //设置自动滚动间隔（默认3秒）
        arp.setRollInterval(3000);
        //设置轮播view点击事件
        arp.setOnPageClickListener(new ClickPager());
        //设置自动滚动方向
        arp.setRollDirection(AutoRollViewPager.DIRECTION_RIGHT);
        //完成配置并显示
        arp.showUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        arp.tearDown();
    }

    private Drawable createADrawable() {
        int width = 800;
        int height = 480;

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(15, Color.RED);
        drawable.setColor(Color.CYAN);
        drawable.setBounds(0, 0, width, height);

        Bitmap imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imgTemp);
        drawable.draw(canvas);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(28f);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度
        textPaint.setColor(Color.BLUE);

        Rect rect = new Rect();
        String text = "这是一个Drawable";
        textPaint.getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text, (width - rect.width()) / 2, (height - rect.height()) / 2, textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return new BitmapDrawable(getResources(), imgTemp);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_roll:
                if(isChecked) {
                    cb_loop.setEnabled(true);
                    sb_roll_interval.setEnabled(true);

                    //close
                    arp.tearDown();
                    //config again
                    arp.setAutoRoll(true);
                    //re-show
                    arp.showUp();
                } else {
                    cb_loop.setEnabled(false);
                    sb_roll_interval.setEnabled(false);

                    arp.tearDown();
                    arp.setAutoRoll(false);
                    arp.showUp();
                }
                break;
            case R.id.cb_loop:
                if(isChecked) {
                    arp.tearDown();
                    arp.setLoopMode(true);
                    arp.showUp();
                } else {
                    arp.tearDown();
                    arp.setLoopMode(false);
                    arp.showUp();
                }
                break;
            case R.id.cb_dot:
                arp.setDotVisiblity(isChecked);
                break;
            case R.id.cb_title:
                if(isChecked) {
                    arp.setTitles(titles);
                    arp.showUp();
                } else {
                    arp.setTitles(null);
                    arp.showUp();
                }
                break;
            case R.id.cb_click:
                if(isChecked) {
                    arp.setOnPageClickListener(new ClickPager());
                } else {
                    arp.setOnPageClickListener(null);
                }
                break;
            case R.id.switch_direction:
                if(isChecked) {
                    arp.setRollDirection(AutoRollViewPager.DIRECTION_RIGHT);
                } else {
                    arp.setRollDirection(AutoRollViewPager.DIRECTION_LEFT);
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_dot_interval:
                int dot = 5 + progress;
                arp.setDotInterval(dot);
                tv_dot.setText(String.format("%dpx", dot));
                arp.showUp();
                break;
            case R.id.sb_roll_interval:
                int roll = 1000 * (progress + 1);
                arp.setRollInterval(roll);
                tv_roll.setText(String.format("%dms", roll));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    class ClickPager implements AutoRollViewPager.OnPagerClick {
        @Override
        public void onClick(int position) {
            Toast.makeText(getActivity(), "点击了轮播图" + position, Toast.LENGTH_SHORT).show();
        }
    }
}
