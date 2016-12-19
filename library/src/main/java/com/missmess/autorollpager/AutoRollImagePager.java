package com.missmess.autorollpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.missmess.library.R;

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
    private int mLoadRes;
    private int mErrorRes;

    public AutoRollImagePager(Context context) {
        this(context, null);
    }

    public AutoRollImagePager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoRollImagePager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoRollImagePager);
        mLoadRes = typedArray.getResourceId(R.styleable.AutoRollImagePager_placeholder_load, R.drawable.zhanwei_1);
        mErrorRes = typedArray.getResourceId(R.styleable.AutoRollImagePager_placeholder_error, R.drawable.zhanwei_2);
        typedArray.recycle();

        init(context);
    }

    private void init(Context context) {
        adapter = new RollImageAdapter(context, mLoadRes, mErrorRes);
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