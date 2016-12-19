package com.missmess.autorollpager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 提供本地图片和网络图片显示的adapter
 *
 * @author wl
 * @since 2016/03/31 10:07
 */
public class RollImageAdapter extends BaseRollAdapter {
    private final NetImageLoader imageLoader;
    private List<?> images;

    public RollImageAdapter(Context context, int loadRes, int errorRes) {
        super(context);
        imageLoader = new NetImageLoader(context, loadRes, errorRes);
    }

    public void setImageList(List<?> images) {
        this.images = images;
    }

    @Override
    public int getRealCount() {
        if (images != null)
            return images.size();
        else
            return 0;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView viewpager_item = new ImageView(context);
        viewpager_item.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Object image = images.get(position);

        if (image instanceof String) {
            String url = (String) image;
            imageLoader.loadImage(viewpager_item, url);
        } else if(image instanceof Integer) {
            int resource = (int) image;
            viewpager_item.setImageResource(resource);
        } else if(image instanceof Drawable) {
            Drawable drawable = (Drawable) image;
            viewpager_item.setImageDrawable(drawable);
        } else {
            throw new IllegalArgumentException("不能识别该类型");
        }

        return viewpager_item;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}