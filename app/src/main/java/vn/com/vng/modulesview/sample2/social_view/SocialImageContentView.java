package vn.com.vng.modulesview.sample2.social_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import vn.com.vng.modulesview.R;
import vn.com.vng.modulesview.modules_view.ImageModule;
import vn.com.vng.modulesview.modules_view.Module;
import vn.com.vng.modulesview.modules_view.ModulesView;
import vn.com.vng.modulesview.sample.model.SocialModel;

/**
 * Created by HungNQ on 15/09/2017.
 */

public class SocialImageContentView extends ModulesView {
    private List<ImageModule> mImageModules;

    public SocialImageContentView(Context context) {
        this(context, null);
    }

    public SocialImageContentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SocialImageContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialImageContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Module.OnClickListener mOnImageClickListener;
    Module.OnLongClickListener mOnImageLongClickListener;

    private void init() {
        mImageModules = buildImageModules(getImagesContentCount());
        addModules(mImageModules);
    }


    protected List<ImageModule> buildImageModules(int n) {
        List<ImageModule> list;
        list = new LinkedList<>();
        for (int i = 0; i < n; ++i) {
            ImageModule imgModule = new ImageModule();
            imgModule.setScaleType(ImageModule.CENTER_CROP);
            list.add(imgModule);
        }

        return list;
    }

    public int getImagesContentCount() {
        return 0;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = configModulesOnMeasure(mImageModules);
        setMeasureDimension(getMeasuredWidth(), height);
    }

    protected int configModulesOnMeasure(List<ImageModule> imageModules) {
        return 0;
    }

//    public void bindModel(SocialModel model) {
//        if (model != null && model.getImages() != null) {
//            Iterator<ImageModule> imgIterator = mImageModules.iterator();
//            Iterator<Bitmap> bitmapIterator = model.getImages().iterator();
//            while (imgIterator.hasNext()) {
//                ImageModule imgModule = imgIterator.next();
//                imgModule.setBitmap(bitmapIterator.hasNext() ? bitmapIterator.next() : null);
//                imgModule.configModule();
//
//            }
//        } else {
//            for (ImageModule imgModule : mImageModules) {
//                imgModule.setBitmap(null);
//                imgModule.configModule();
//            }
//        }
//    }

    public void bindImage(int position, Bitmap bitmap){
        if(position <0 || position >= getImagesContentCount())
            return;
        ImageModule imageModule = mImageModules.get(position);
        imageModule.setBitmap(bitmap);
        imageModule.configModule();

//        imageModule.invalidate();
    }


    //-----------------listener------------


    public void setOnImageClickListener(Module.OnClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
        for (ImageModule imageModule : mImageModules) {
            imageModule.setOnClickListener(mOnImageClickListener);
        }
    }

    public void setOnImageLongClickListener(Module.OnLongClickListener onImageLongClickListener) {
        mOnImageLongClickListener = onImageLongClickListener;
        for (ImageModule imageModule : mImageModules) {
            imageModule.setOnLongClickListener(mOnImageLongClickListener);
        }
    }
}
