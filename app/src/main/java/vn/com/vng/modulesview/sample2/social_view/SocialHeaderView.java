package vn.com.vng.modulesview.sample2.social_view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import vn.com.vng.modulesview.modules_view.ImageModule;
import vn.com.vng.modulesview.modules_view.Module;
import vn.com.vng.modulesview.modules_view.ModulesView;
import vn.com.vng.modulesview.modules_view.TextModule;
import vn.com.vng.modulesview.sample.model.SocialModel;

/**
 * Created by HungNQ on 15/09/2017.
 */

public class SocialHeaderView extends ModulesView {

    public SocialHeaderView(Context context) {
        this(context, null);
    }

    public SocialHeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SocialHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //module's properties
    private int mHeaderHeight = dp(56);
    private int mAvaSize = dp(40);
    private int mAvaMargin = dp(8);

    private int mNameTextSize = sp(16);
    private static int mNameTextColor = 0xff050505;
    private int mTimeTextSize = sp(12);
    private static int mTimeTextColor = 0xffaaaaaa;


    //modules
    ImageModule mAvaImgModule;
    TextModule mNameTextModule;
    TextModule mTimeTextModule;
    private Module mTopLine;


    //properties
    Module.OnClickListener mOnAvaClickListener;
    Module.OnClickListener mOnNameClickListener;
    Module.OnClickListener mOnTimeClickListener;
    Module.OnLongClickListener mOnAvaLongClickListener;
    Module.OnLongClickListener mOnNameLongClickListener;
    Module.OnLongClickListener mOnTimeLongClickListener;
    private void init() {
        mAvaImgModule = buildAvaModule();
        addModule(mAvaImgModule);

        mNameTextModule = buildNameModule();
        addModule(mNameTextModule);

        mTimeTextModule = buildTimeModule();
        addModule(mTimeTextModule);

        mTopLine = new Module();
        mTopLine.setBackgroundColor(0xffcccccc);

        addModule(mTopLine);
        staticConfigModules();
    }


    private ImageModule buildAvaModule() {
        ImageModule module = new ImageModule();
        module.setScaleType(ImageModule.ScaleType.CENTER_CROP);
        module.setRoundCorner(ImageModule.ROUND_CIRCLE);

        return module;
    }

    private TextModule buildNameModule() {

        TextModule module = new TextModule();
        module.setTextSize(mNameTextSize);
        module.setTextColor(mNameTextColor);

        return module;
    }

    private TextModule buildTimeModule() {

        TextModule module = new TextModule();
        module.setTextSize(mTimeTextSize);
        module.setTextColor(mTimeTextColor);
        return module;
    }


    private void staticConfigModules() {
        mAvaImgModule.setBounds(mAvaMargin, mAvaMargin, mAvaMargin + mAvaSize, mAvaMargin + mAvaSize);

        mNameTextModule.setBounds(mAvaImgModule.getRight() + mAvaMargin, mAvaImgModule.getTop(), Module.SPECIFIC_LATER, Module.SPECIFIC_LATER);
        mNameTextModule.configModule();
        mTimeTextModule.setBounds(mNameTextModule.getLeft(), mNameTextModule.getBottom(), Module.SPECIFIC_LATER, Module.SPECIFIC_LATER);
        mTimeTextModule.configModule();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = configModulesOnMeasure();
        setMeasureDimension(getMeasuredWidth(), height);
    }

    private int configModulesOnMeasure() {
        mTopLine.setBounds(0,0,getMeasuredWidth(), 1);
        return mHeaderHeight + 1;
    }

    public void bindHeader(SocialModel model) {
        if(model != null) {
            mAvaImgModule.setBitmap(model.getAvatar());
            mNameTextModule.setText(model.getName());
            mTimeTextModule.setText(model.getTime());
        }else{
            mAvaImgModule.setBitmap(null);
            mNameTextModule.setText(null);
            mTimeTextModule.setText(null);
        }
        //static
        reConfig();
    }

    private void reConfig() {
        mAvaImgModule.configModule();

        mNameTextModule.setBounds(mNameTextModule.getLeft(), mNameTextModule.getTop(), Module.SPECIFIC_LATER, Module.SPECIFIC_LATER);
        mNameTextModule.configModule();

        mTimeTextModule.setBounds(mTimeTextModule.getLeft(), mTimeTextModule.getTop(), Module.SPECIFIC_LATER, Module.SPECIFIC_LATER);
        mTimeTextModule.configModule();
    }


    //-----------------listener------------

    public void setOnAvaClickListener(Module.OnClickListener onAvaClickListener) {
        mOnAvaClickListener = onAvaClickListener;
        mAvaImgModule.setOnClickListener(mOnAvaClickListener);
    }


    public void setOnNameClickListener(Module.OnClickListener onNameClickListener) {
        mOnNameClickListener = onNameClickListener;
        mNameTextModule.setOnClickListener(mOnNameClickListener);
    }

    public void setOnTimeClickListener(Module.OnClickListener onTimeClickListener) {
        mOnTimeClickListener = onTimeClickListener;
        mTimeTextModule.setOnClickListener(mOnTimeClickListener);
    }

    public void setOnAvaLongClickListener(Module.OnLongClickListener onAvaLongClickListener) {
        mOnAvaLongClickListener = onAvaLongClickListener;
        mAvaImgModule.setOnLongClickListener(mOnAvaLongClickListener);
    }

    public void setOnNameLongClickListener(Module.OnLongClickListener onNameLongClickListener) {
        mOnNameLongClickListener = onNameLongClickListener;
        mNameTextModule.setOnLongClickListener(mOnNameLongClickListener);
    }

    public void setOnTimeLongClickListener(Module.OnLongClickListener onTimeLongClickListener) {
        mOnTimeLongClickListener = onTimeLongClickListener;
        mTimeTextModule.setOnLongClickListener(mOnTimeLongClickListener);
    }
}