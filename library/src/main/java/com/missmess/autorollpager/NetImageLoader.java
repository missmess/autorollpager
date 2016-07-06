package com.missmess.autorollpager;

import android.content.Context;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.missmess.library.R;

/**
 * 网络图片加载器（可以根据需要更换）
 *
 * @author wl
 * @since 2016/07/05 15:06
 */
public class NetImageLoader {
    /**
     * 图片下载器
     */
    private BitmapUtils bitmapUtils;

    public NetImageLoader(Context context) {
        bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDefaultLoadingImage(R.drawable.zhanwei_1);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.zhanwei_2);
        bitmapUtils.configDefaultReadTimeout(30 * 1000);
        bitmapUtils.configDefaultConnectTimeout(30 * 1000);
    }

    public void loadImage(ImageView imageView, String url) {
        bitmapUtils.display(imageView, url);
    }
}
