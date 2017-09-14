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

public class Social3ImgModulesView extends SocialModulesView {
    public Social3ImgModulesView(Context context) {
        super(context);
    }

    public Social3ImgModulesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Social3ImgModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Social3ImgModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected int getImageContentCount() {
        return 3;
    }

    @Override
    protected int configImagesContentOnMeasure(int top, int widthSize) {
        //init image region

        ImageModule img1 = imgModules.get(0);
        ImageModule img2 = imgModules.get(1);
        ImageModule img3 = imgModules.get(2);


        //1 left - 2 right
        int temp1 = (int) (widthSize * 2f / 3);
        int temp2 = (int) (widthSize / 2f);
        img1.setBounds(0, top, temp1 - dp(1), widthSize + top);
        img2.setBounds(temp1 + dp(1), top, widthSize, temp2 + top - dp(1));
        img3.setBounds(temp1 + dp(1), temp2 + top + dp(1), widthSize, widthSize + top);

        for (ImageModule imgModule : imgModules) {
            imgModule.configModule();
        }
        return widthSize;
    }
}
