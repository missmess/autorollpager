package com.missmess.autorollpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 轮播图控件，支持本地图片和网络图片资源，支持图片标题
 *
 * @author wl
 * @since 2016/1/4 18:03
 */
public class BaseRollPager extends ViewPager {
    private final String TAG = "BaseRollPager";
    private final int AUTOROLL_DELAY = 3000; // ms
    private final int MSG = 0x001;

    private AutoRollViewPager.OnPagerClick mPagerClick;
    private OnPageChangeListener mChangeListener;
    private boolean autoroll = true;
    /** 这个RollPager配置完成了 */
    private boolean settled = false;
    private int mInterval = AUTOROLL_DELAY;
    private BaseRollAdapter mAdapter;
    private int mDirection = AutoRollViewPager.DIRECTION_RIGHT;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG) {
                int next;
                if(mDirection == AutoRollViewPager.DIRECTION_RIGHT) {
                    next = getCurrentItem() + 1;
                } else {
                    next = getCurrentItem() - 1;
                }
                if(!mLoopMode)
                    next %= mAdapter.getRealCount();
//                Log.e(TAG, "next=" + next);
                setCurrentItem(next, true);
                rollInternal();
            }
        }
    };
    private float downY;
    private float downX;
    private boolean mLoopMode;

    public BaseRollPager(Context context) {
        super(context);
        addOnPageChangeListener(new MyListener());
    }

    public void setOnPageClickListener(AutoRollViewPager.OnPagerClick pagerClick) {
        this.mPagerClick = pagerClick;
    }

    public void setRollAdapter(BaseRollAdapter adapter) {
        this.mAdapter = adapter;
        //根据adapter刷新loopmode的值
        setLoopMode(mLoopMode);
    }

    protected BaseRollAdapter getRollAdapter() {
        return this.mAdapter;
    }

    public void setAutoRoll(boolean autoroll) {
        this.autoroll = autoroll;
    }

    public void setDirection(int direction) {
        this.mDirection = direction;
    }

    public void setLoopMode(boolean loopMode) {
        boolean loop = loopMode;
        if(mAdapter != null) {
            //如果item条目不大于1，无限循环模式没有意义
            loop = mAdapter.getRealCount() > 1 && loopMode;
            mAdapter.setLoopMode(loop);
        }
        this.mLoopMode = loop;
    }

    protected boolean isLoopMode() {
        return mLoopMode;
    }

    public void setRollInterval(int interval) {
        mInterval = interval;
    }

    /**
     * 配置完成，启动RollPage
     */
    public void setUp() {
        if (mAdapter == null) {
            throw new IllegalStateException("没有设置adapter！");
        }

        this.setAdapter(mAdapter);

        setCurrentItem(mAdapter.getFirstPos());

        settled = true;
        rollInternal();
    }

    private void rollInternal() {
        if(settled) {
            if (autoroll && !handler.hasMessages(MSG))
                handler.sendEmptyMessageDelayed(MSG, mInterval);
        }
    }

    /**
     * rollpager tear down.
     */
    public void tearDown() {
        pauseRoll();
        settled = false;
    }

    private void pauseRoll() {
        handler.removeMessages(MSG);
    }

    /**
     * RollPager是否配置完成了
     */
    public boolean isSettled() {
        return settled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // 当拦截触摸事件到达此位置的时候，返回true，
        // 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent--DOWN::" + ev.getX());

                pauseRoll();

                downX = ev.getX();
                downY = ev.getY();
                requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent--MOVE::" + ev.getX());

                float moveX = ev.getX();
                float moveY = ev.getY();

                //竖向滑动更多，则不再请求不拦截。
                if (Math.abs(moveX - downX) < Math.abs(moveY - downY)) {
                    requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent--UP::" + ev.getX());

                //点击事件触发条件
                if (ev.getEventTime() - ev.getDownTime() < 500
                        && Math.abs(downX - ev.getX()) < 10
                        && Math.abs(downY - ev.getY()) < 10) {
                    int pos = mAdapter.getRealPos(getCurrentItem());
                    Log.i(TAG, "clicked page::" + pos);

                    if (mPagerClick != null) {
                        mPagerClick.onClick(pos);
                        rollInternal();
                        return true;
                    }
                }
                rollInternal();
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "onTouchEvent--CANCEL::" + ev.getX() + "//" + ev.getY());
                rollInternal();
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mChangeListener = listener;
    }

    private class MyListener implements OnPageChangeListener {
        // 是否已经提前触发了OnPageSelected事件，防止临界位置处重复调用onPageSelected
        private boolean alreadyTriggerOnPageSelected;

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mLoopMode && state == SCROLL_STATE_IDLE) {
                //滑动到临界位置时，重新设置位置
                if (getCurrentItem() == 0) {
                    setCurrentItem(mAdapter.getCount() - 2, false);
                } else if (getCurrentItem()  == mAdapter.getCount() - 1) {
                    setCurrentItem(1, false);
                }
            }

            if(mChangeListener != null)
                mChangeListener.onPageScrollStateChanged(state);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(mChangeListener != null)
                mChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            if(mLoopMode) {
                if (position == 0) {
                    if(mChangeListener != null)
                        mChangeListener.onPageSelected(mAdapter.getRealPos(position));
                    alreadyTriggerOnPageSelected = true;
                } else if (position == mAdapter.getCount() - 1) {
                    if(mChangeListener != null)
                        mChangeListener.onPageSelected(mAdapter.getRealPos(position));
                    alreadyTriggerOnPageSelected = true;
                } else {
                    if (!alreadyTriggerOnPageSelected) {
                        if(mChangeListener != null)
                            mChangeListener.onPageSelected(mAdapter.getRealPos(position));
                    }
                    alreadyTriggerOnPageSelected = false;
                }
            } else {
                if(mChangeListener != null)
                    mChangeListener.onPageSelected(position);
            }
        }

    }
}
