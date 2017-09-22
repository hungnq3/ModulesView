package vn.com.vng.modulesview.sample2.adapter.holder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import vn.com.vng.modulesview.R;
import vn.com.vng.modulesview.modules_view.ImageModule;
import vn.com.vng.modulesview.modules_view.Module;
import vn.com.vng.modulesview.sample2.adapter.view_item.BaseViewItem;
import vn.com.vng.modulesview.sample2.adapter.view_item.SocialImageContextViewItem;
import vn.com.vng.modulesview.sample2.social_view.SocialImageContentView;

/**
 * Created by HungNQ on 15/09/2017.
 */

public class SocialImageContentViewHolder extends BaseViewHolder {
    SocialImageContentView mSocialImageContentView;

    public SocialImageContentViewHolder(SocialImageContentView itemView) {
        super(itemView);
        mSocialImageContentView = itemView;
        init();
    }

    private void init() {

    }


    @Override
    public void onBind(final BaseViewItem item) {
        super.onBind(item);
        if (item instanceof SocialImageContextViewItem) {
            for (int i = 0, count = mSocialImageContentView.getImagesContentCount(); i < count; ++i) {
                mSocialImageContentView.bindImage(i, null);
                final int finalI = i;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSocialImageContentView.bindImage(finalI, (((SocialImageContextViewItem) item).getSocialModel()).getImages().get(finalI));
                        mSocialImageContentView.invalidate();
                    }
                }, 200*i);
            }
        }
    }
}
