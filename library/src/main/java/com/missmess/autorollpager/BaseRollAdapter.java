package com.missmess.autorollpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 提供 {@link AutoRollViewPager} 显示的adapter基类
 *
 * @author wl
 * @since 2016/03/31 14:09
 */
public abstract class BaseRollAdapter extends PagerAdapter {
    protected Context context;
    private boolean loopMode;

    public BaseRollAdapter(Context context) {
        this.context = context;
    }

    public void setLoopMode(boolean loopMode) {
        this.loopMode = loopMode;
    }

    @Override
    public int getCount() {
        if(loopMode) {
            return getRealCount() + 2;
        } else {
            return getRealCount();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //实际位置
        int realPos = getRealPos(position);
        View view = getView(container, realPos);
        container.addView(view);
        return view;
    }

    /**
     * 获得需要第一个显示的item位置
     * @return
     */
    protected int getFirstPos() {
        if(loopMode) {
            return (getRealCount() == 0 ? 0 : 1);
        } else {
            return 0;
        }
    }

    protected int getRealPos(int position) {
        if(loopMode) {
            if (position == 0) {
                return getRealCount() - 1;
            } else if (position == getCount() - 1) {
                return 0;
            } else {
                return position - 1;
            }
        } else {
            return position;
        }
    }

    /**
     * 每一个position的view
     * @param container container
     * @param position position
     * @return view
     */
    public abstract View getView(ViewGroup container, int position);

    /**
     * 滑动view的总数
     * @return int
     */
    public abstract int getRealCount();
}
