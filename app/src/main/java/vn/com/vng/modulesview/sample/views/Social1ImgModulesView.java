package vn.com.vng.modulesview.sample.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import vn.com.vng.modulesview.modules_view.ImageModule;

/**
 * Created by HungNQ on 13/09/2017.
 */

public class Social1ImgModulesView extends SocialModulesView {
    public Social1ImgModulesView(Context context) {
        super(context);
    }

    public Social1ImgModulesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Social1ImgModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Social1ImgModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected int getImageContentCount() {
        return 1;
    }

    @Override
    protected int configImagesContentOnMeasure(int top, int widthSize) {
        //init image region

        ImageModule img1 = imgModules.get(0);
        img1.setBounds(0, top, widthSize, widthSize + top);
        img1.configModule();

        return widthSize;
    }



}
