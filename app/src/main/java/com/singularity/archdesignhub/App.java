package com.singularity.archdesignhub;

import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Frederick on 4/21/2015.
 */
public class App extends MultiDexApplication {
    private static Typeface latoBold = null, latoHairline = null, latoItalic = null, latoLight = null, latoRegular = null, robotoSlabRegular = null, robotoSlabLight = null, robotoThin = null;
    private static Context context;


    public static Typeface getLatoBold() {
        if (latoBold == null)
            latoBold = Typeface.createFromAsset(context.getAssets(),
                    "lato-bold.ttf");
        return latoBold;
    }

    public static Typeface getLatoHairline() {
        if (latoHairline == null)
            latoHairline = Typeface.createFromAsset(context.getAssets(),
                    "lato-hairline.ttf");
        return latoHairline;
    }

    public static Typeface getLatoItalic() {
        if (latoItalic == null)
            latoItalic = Typeface.createFromAsset(context.getAssets(),
                    "lato-italic.ttf");
        return latoItalic;
    }

    public static Typeface getLatoLight() {
        if (latoLight == null)
            latoLight = Typeface.createFromAsset(context.getAssets(),
                    "lato-light.ttf");
        return latoLight;
    }

    public static Typeface getLatoRegular() {
        if (latoRegular == null)
            latoRegular = Typeface.createFromAsset(context.getAssets(),
                    "lato-regular.ttf");
        return latoRegular;
    }

    public static Typeface getRobotoSlabRegular() {
        if (robotoSlabRegular == null)
            robotoSlabRegular = Typeface.createFromAsset(context.getAssets(),
                    "robotoslab-regular.ttf");
        return robotoSlabRegular;
    }

    public static Typeface getRobotoSlabLight() {
        if (robotoSlabLight == null)
            robotoSlabLight = Typeface.createFromAsset(context.getAssets(),
                    "robotoslab-light.ttf");
        return robotoSlabLight;
    }

    public static Typeface getRobotoThin() {
        if (robotoThin == null)
            robotoThin = Typeface.createFromAsset(context.getAssets(),
                    "roboto-thin.ttf");
        return robotoThin;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initUIL();
    }

    private void initUIL() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);
    }


}
