package com.missmess.autorollpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.missmess.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图控件
 *
 * @author wl
 * @since 2016/1/4 18:03
 */
public class AutoRollViewPager extends RelativeLayout {
    /** 左 */
    public static final int DIRECTION_LEFT = 0;
    /** 右 */
    public static final int DIRECTION_RIGHT = 1;
    /** 默认 点阵中点之间的间距 */
    private static final int DEFAULT_DOT_MARGIN = 5; // dp
    /** 默认 title一栏的高度 */
    private static final int DEFAULT_TITLE_HEIGHT = 24; // dp
    private TextView mTv;
    private LinearLayout mLl;
    private BaseRollPager rollPager;
    private List<View> dots;
    private  List<String> titles;
    private int dot_interval = 0;
    private Drawable mNormalDot;
    private Drawable mFocusDot;
    private int dotGravity;
    private boolean showDot;
    public static final int GRAVITY_CENTER = 0;
    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_RIGHT = 2;

    public AutoRollViewPager(Context context) {
        this(context, null);
    }

    public AutoRollViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoRollViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoRollViewPager);
        mFocusDot = typedArray.getDrawable(R.styleable.AutoRollViewPager_dot_focus);
        mNormalDot = typedArray.getDrawable(R.styleable.AutoRollViewPager_dot_normal);
        showDot = typedArray.getBoolean(R.styleable.AutoRollViewPager_showDot, true);
        dotGravity = typedArray.getInt(R.styleable.AutoRollViewPager_dotGravity, GRAVITY_CENTER);
        if(mFocusDot == null)
            mFocusDot = getResources().getDrawable(R.drawable.dot_focus);
        if(mNormalDot == null)
            mNormalDot = getResources().getDrawable(R.drawable.dot_normal);
        typedArray.recycle();

        init();
    }

    private void init() {
        addPager();
        addTitleView();
        addDotGroupView();

        dot_interval = dip2px(getContext(), DEFAULT_DOT_MARGIN);
    }

    private void addPager() {
        rollPager = new BaseRollPager(getContext());
        rollPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int realPos = convertRealPos(position);
                if (titles != null) {
                    String title = titles.get(realPos);
                    mTv.setText(title != null ? title : "");
                }
                if(dots == null)
                    return;
                for (int i = 0; i < dots.size(); i++) {
                    if (i == realPos)
                        dots.get(i).setBackgroundDrawable(mFocusDot);
                    else
                        dots.get(i).setBackgroundDrawable(mNormalDot);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        this.addView(rollPager, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    private void addTitleView() {
        mTv = new TextView(getContext());
        mTv.setTextColor(0xFF333333);
        int pad = dip2px(getContext(), 4);
        mTv.setPadding(pad, 0, pad, 0);
        mTv.setBackgroundColor(0xAABDBDBD);
        mTv.setTextSize(12);
        mTv.setVisibility(View.GONE);
        mTv.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), DEFAULT_TITLE_HEIGHT));
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(mTv, lps);
    }

    private void addDotGroupView() {
        mLl = new LinearLayout(getContext());
        mLl.setOrientation(LinearLayout.HORIZONTAL);
        mLl.setGravity(Gravity.CENTER);
        int padLR = dip2px(getContext(), DEFAULT_DOT_MARGIN);
        mLl.setPadding(padLR, 0, padLR, 0);

        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), DEFAULT_TITLE_HEIGHT));
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(mLl, lps);
    }

    /**
     * 设置轮播view的 描述标题
     * @param titles 标题内容
     */
    public void setTitles(List<String> titles) {
        if(titles != null) {
            mTv.setVisibility(View.VISIBLE);
            this.titles = titles;
        } else {
            mTv.setVisibility(View.GONE);
            this.titles = null;
        }
    }

    /**
     * 在配置完成后，调用此方法来启动显示轮播view
     */
    public void showUp() {
        rollPager.setUp();

        setupTitle();
        renderDotGroup();
    }

    /**
     * close the pager
     */
    public void tearDown() {
        rollPager.tearDown();
    }

    /**
     * 设置当前位置的标题
     */
    protected void setupTitle() {
        int pos = convertRealPos(rollPager.getCurrentItem());
        if (titles != null) {
            String title = titles.get(pos);
            mTv.setText(title != null ? title : "");
        }
    }

    /**
     * 生成指示点阵
     */
    protected void renderDotGroup() {
        if (dots == null) {
            dots = new ArrayList<>();
        }
        mLl.removeAllViews();
        dots.clear();

        int length = rollPager.getRollAdapter().getRealCount();
        for (int i = 0; i < length; i++) {
            View view = new View(getContext());
            if (i == convertRealPos(rollPager.getCurrentItem())) {
                view.setBackgroundDrawable(mFocusDot);
            } else {
                view.setBackgroundDrawable(mNormalDot);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    dip2px(getContext(), 6), dip2px(
                    getContext(), 6));
            view.setLayoutParams(layoutParams);
            layoutParams.setMargins(dot_interval, 0, dot_interval, 0);
            mLl.addView(view);
            dots.add(view);
        }

        applyDotVisibility();
        applyDotGravity();
    }

    private void applyDotVisibility() {
        mLl.setVisibility(showDot ? View.VISIBLE : View.GONE);
    }

    private void applyDotGravity() {
        switch (dotGravity) {
            case GRAVITY_CENTER:
                mLl.setGravity(Gravity.CENTER);
                mTv.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                break;
            case GRAVITY_LEFT:
                mLl.setHorizontalGravity(Gravity.START);
                mTv.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                break;
            case GRAVITY_RIGHT:
                mLl.setHorizontalGravity(Gravity.END);
                mTv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                break;
        }
    }

    private int convertRealPos(int virtualPos) {
        return rollPager.getRollAdapter().getRealPos(virtualPos);
    }

    /**
     * 设置指示点间距
     * @param px 像素值
     */
    public void setDotInterval(int px) {
        dot_interval = px;
    }

    /**
     * 自动滑动时，滑动的方向。
     * @param direction 方向。{@link #DIRECTION_LEFT} 、 {@link #DIRECTION_RIGHT}
     */
    public void setRollDirection(int direction) {
        rollPager.setDirection(direction);
    }

    public void setDotVisiblity(boolean show) {
        showDot = show;
        applyDotVisibility();
    }

    public void setDotGravity(int gravity) {
        this.dotGravity = gravity;
        applyDotGravity();
    }

    /**
     * 设置是否自动滚动
     *
     * @param autoroll true-滚动
     */
    public void setAutoRoll(boolean autoroll) {
        rollPager.setAutoRoll(autoroll);
    }

    /**
     * 是否是无限循环模式
     * @param loopMode true-无限循环
     */
    public void setLoopMode(boolean loopMode) {
        rollPager.setLoopMode(loopMode);
    }

    /**
     * 设置自动滑动的间隔时间，毫秒
     * @param interval time
     */
    public void setRollInterval(int interval) {
        rollPager.setRollInterval(interval);
    }

    /**
     * RollPager是否配置完成了
     */
    public boolean isSettled() {
        return rollPager.isSettled();
    }

    /**
     * 设置轮播view的点击事件
     */
    public void setOnPageClickListener(OnPagerClick pagerClick) {
        rollPager.setOnPageClickListener(pagerClick);
    }

    /**
     * 提供 轮播view的 内容adapter
     */
    public void setRollAdapter(BaseRollAdapter adapter) {
        rollPager.setRollAdapter(adapter);
    }

    public void setOnTouchListener(OnTouchListener listener) {
        rollPager.setOnTouchListener(listener);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        rollPager.setOnPageChangeListener(listener);
    }

    /**
     * 轮播view被点击的回调接口
     */
    public interface OnPagerClick {
        /**
         * 某个位置的轮播view被点击了
         *
         * @param position pos
         */
        void onClick(int position);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
