package com.singularity.archdesignhub.utils;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.singularity.archdesignhub.R;

/**
 * Created by Frederick on 5/13/2015.
 * Class to load the app wide settings for image loader. avoid the need to repeat the config in all affected classes
 */
public class DefaultImageLoader {
    private static DisplayImageOptions options;
    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    private static DefaultImageLoader loader;

    private DefaultImageLoader() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ca_image)
                            //.showImageForEmptyUri(R.drawable.w_empty)
                    .showImageOnFail(R.drawable.ca_archdesign_blur)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new FadeInBitmapDisplayer(200))
                    .build();
    }

    public static DefaultImageLoader getInstance() {
        if (loader == null)
            loader = new DefaultImageLoader();
        return loader;
    }

    public void loadImage(String uri, ImageView imageView) {
        imageLoader.displayImage(uri, imageView, options);


    }

}
