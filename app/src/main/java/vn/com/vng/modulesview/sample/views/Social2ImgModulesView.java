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

public class Social2ImgModulesView extends SocialModulesView {
    public Social2ImgModulesView(Context context) {
        super(context);
    }

    public Social2ImgModulesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Social2ImgModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Social2ImgModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected int getImageContentCount() {
        return 2;
    }


    @Override
    protected int configImagesContentOnMeasure(int top, int widthSize) {
        //init image region

        ImageModule img1 = imgModules.get(0);
        ImageModule img2 = imgModules.get(1);

        //1 left - 1 right
        int temp = (int) (widthSize / 2f);
        img1.setBounds(0, top, temp - dp(1),  top +widthSize);
        img2.setBounds(temp+dp(1),  top, widthSize, top + widthSize);

        for (ImageModule imgModule : imgModules) {
            imgModule.configModule();
        }

        return widthSize;
    }
}
