package vn.com.vng.modulesview;

import android.content.Context;

import java.io.IOException;

import vn.com.vng.modulesview.image_loader.ImageLoader;
import vn.com.vng.modulesview.image_loader.Util;
import vn.com.vng.modulesview.image_loader.diskcache.DiskCache;

/**
 * Created by HungNQ on 12/09/2017.
 */

public class Application extends android.app.Application {
    public static Context self;
    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        try {
            ImageLoader.getInstance().init(Util.getDiskCacheDir(this, "image_cache"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
