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

public class Social3ImageContentView extends SocialImageContentView {
    public Social3ImageContentView(Context context) {
        this(context, null);
    }

    public Social3ImageContentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Social3ImageContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Social3ImageContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public int getImagesContentCount() {
        return 3;
    }

    @Override
    protected int configModulesOnMeasure(List<ImageModule> imageModules) {
        super.configModulesOnMeasure(imageModules);
        int widthSize = getMeasuredWidth();

        ImageModule img1 = imageModules.get(0);
        ImageModule img2 = imageModules.get(1);
        ImageModule img3 = imageModules.get(2);

        //1 left - 2 right
        int temp1 = (int) (widthSize * 2f / 3);
        int temp2 = (int) (widthSize / 2f);
        img1.setBounds(0, 0, temp1 - dp(1), widthSize );
        img2.setBounds(temp1 + dp(1), 0, widthSize, temp2  - dp(1));
        img3.setBounds(temp1 + dp(1), temp2 + dp(1), widthSize, widthSize );


        for (ImageModule imageModule : imageModules) {
            imageModule.configModule();
        }
        return widthSize;
    }
}
