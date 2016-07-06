package com.missmess.autorollpager;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

/**
 * 轮播图控件，支持本地图片和网络图片资源，支持图片标题
 *
 * @author wl
 * @since 2016/1/4 18:03
 */
public class AutoRollImagePager extends AutoRollViewPager {
    private RollImageAdapter adapter;
    private boolean dataChanged = false;

    public AutoRollImagePager(Context context) {
        super(context);
        init(context);
    }

    public AutoRollImagePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoRollImagePager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        adapter = new RollImageAdapter(context);
    }

    /**
     * 设置要显示的图片集合。
     * 使用String类型代表网络图片地址，使用int类型代表图片资源，使用Drawable代表图片drawable。
     * @param images 只能是String、Integer、Drawable类型的集合
     */
    public void setImageLists(List<?> images) {
        if(adapter != null) {
            adapter.setImageList(images);
            dataChanged = true;
        }
    }

    /**
     * 启动RollPager，或者刷新RollPager
     */
    @Override
    public void showUp() {
        if(!isSettled()) { //没有配置过RollPager，去启动
            setRollAdapter(adapter);
            super.showUp();
        } else { //启动过
            if(dataChanged) { //数据改变了，刷新
                adapter.notifyDataSetChanged();
                setupTitle();
                renderDotGroup();
            }
        }
    }
}