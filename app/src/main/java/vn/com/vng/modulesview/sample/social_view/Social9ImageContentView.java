package vn.com.vng.modulesview.sample.social_view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import java.util.List;

import vn.com.vng.modulesview.modules_view.ImageModule;

/**
 * Created by HungNQ on 15/09/2017.
 */

public class Social9ImageContentView extends SocialImageContentView {
    public Social9ImageContentView(Context context) {
        this(context, null);
    }

    public Social9ImageContentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Social9ImageContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Social9ImageContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getImagesContentCount() {
        return 9;
    }

    @Override
    protected int configModulesOnMeasure(List<ImageModule> imageModules) {
        super.configModulesOnMeasure(imageModules);
        int widthSize = getMeasuredWidth();

        ImageModule img1 = imageModules.get(0);
        ImageModule img2 = imageModules.get(1);
        ImageModule img3 = imageModules.get(2);
        ImageModule img4 = imageModules.get(3);
        ImageModule img5 = imageModules.get(4);
        ImageModule img6 = imageModules.get(5);
        ImageModule img7 = imageModules.get(6);
        ImageModule img8 = imageModules.get(7);
        ImageModule img9 = imageModules.get(8);


        int temp = widthSize / 3;

        img1.setBounds(0,                   0,                   temp - dp(1),          temp - dp(1));
        img2.setBounds(temp + dp(1),        0,                   temp + temp - dp(1),   temp - dp(1));
        img3.setBounds(temp + temp + dp(1), 0,                   widthSize,             temp - dp(1));

        img4.setBounds(0,                   temp + dp(1),        temp - dp(1),          temp + temp - dp(1));
        img5.setBounds(temp + dp(1),        temp + dp(1),        temp + temp - dp(1),   temp + temp - dp(1));
        img6.setBounds(temp + temp + dp(1), temp + dp(1),        widthSize,             temp + temp - dp(1));


        img7.setBounds(0,                   temp + temp + dp(1), temp - dp(1),          widthSize);
        img8.setBounds(temp + dp(1),        temp + temp + dp(1), temp + temp - dp(1),   widthSize);
        img9.setBounds(temp + temp + dp(1), temp + temp + dp(1), widthSize,             widthSize);


        for (ImageModule imageModule : imageModules) {
            imageModule.configModule();
        }
        return widthSize;
    }
}
