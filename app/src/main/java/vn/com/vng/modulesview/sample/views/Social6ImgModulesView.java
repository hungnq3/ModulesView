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

public class Social6ImgModulesView extends SocialModulesView {
    public Social6ImgModulesView(Context context) {
        super(context);
    }

    public Social6ImgModulesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Social6ImgModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Social6ImgModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getImageContentCount() {
        return 6;
    }


    @Override
    protected int configImagesContentOnMeasure(int top, int widthSize) {
        //init image region

        ImageModule img1 = imgModules.get(0);
        ImageModule img2 = imgModules.get(1);
        ImageModule img3 = imgModules.get(2);
        ImageModule img4 = imgModules.get(3);
        ImageModule img5 = imgModules.get(4);
        ImageModule img6 = imgModules.get(5);


        //square
//        int temp = widthSize/ 2;
//        img1.setBounds(0, top, temp - dp(1), top+temp-dp(1));
//        img2.setBounds(temp + dp(1), top, widthSize, top + temp - dp(1));
//        img3.setBounds(0, temp + top + dp(1), temp - dp(1), widthSize + top);
//        img4.setBounds(temp + dp(1), temp + top + dp(1), widthSize, top+ widthSize);

        int temp1 = (int) (widthSize * 2f / 3);
        int temp2 =  widthSize / 3;
        img1.setBounds(0,               top,                          temp1 - dp(1),        top + temp1 - dp(1) );
        img2.setBounds(0,               temp1 + top + dp(1),          temp2 - dp(1),        top + widthSize);
        img3.setBounds(temp2 + dp(1),   temp1 + top + dp(1),          temp2 +temp2 - dp(1), top + widthSize);
        img4.setBounds(temp1 + dp(1),   top,                          widthSize,            top + temp2 - dp(1));
        img5.setBounds(temp1 + dp(1),   top + temp2 + dp(1),          widthSize,            top + temp2 + temp2- dp(1));
        img6.setBounds(temp1 + dp(1),   top + temp2 +temp2 + dp(1),   widthSize,            top + widthSize);


        for (ImageModule imgModule : imgModules) {
            imgModule.configModule();
        }

        return widthSize;
    }
}