package vn.com.vng.modulesview.sample.social_view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import vn.com.vng.modulesview.R;
import vn.com.vng.modulesview.modules_view.ImageModule;
import vn.com.vng.modulesview.modules_view.Module;
import vn.com.vng.modulesview.modules_view.ModulesView;
import vn.com.vng.modulesview.modules_view.TextModule;
import vn.com.vng.modulesview.sample.model.SocialModel;

/**
 * Created by HungNQ on 15/09/2017.
 */

public class SocialFooterView extends ModulesView {



    public SocialFooterView(Context context) {
        this(context, null);
    }

    public SocialFooterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SocialFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    //modules' properties
    private int mFooterHeight = dp(40);
    private int mFooterMarginTop = dp(12);
    private int mLikeTextWidth = dp(56);
    private int mLikeImgSize = dp(42);

    private int mCommentImgSize = dp(24);
    private int mCommentTextWidth = dp(56);
    private int mMoreImgSize = dp(42);

    private int mLeftMargin = dp(16);
    private int mRightMargin = dp(16);

    private int mBottomSeparatorSize = dp(12);

    //modules
    private ImageModule mImgLike;
    private TextModule mTextLike;
    private TextModule mTextComment;
    private ImageModule mImgComment;
    private ImageModule mImgMore;
    private Module mTopLine;
    private Module mBottomLine;
    private Module mBottomSeparator;

    private Module.OnClickListener mOnLikeClickListener;
    private Module.OnClickListener mOnCommentClickListener;
    private Module.OnClickListener mOnMoreClickListener;

    private Module.OnLongClickListener mOnLikeLongClickListener;
    private Module.OnLongClickListener mOnCommentLongClickListener;
    private Module.OnLongClickListener mOnMoreLongClickListener;

    private void init() {
        //build modules
        mImgLike = buildLikeImgModule();
        mTextLike = buildLikeTextModule();
        mImgComment = buildCommentImgModule();
        mTextComment = buildCommentTextModule();
        mImgMore = buildMoreImgModule();

        mTopLine = new Module();
        mTopLine.setBackgroundColor(0xffcccccc);
        mBottomLine = new Module();
        mBottomLine.setBackgroundColor(0xffcccccc);
        mBottomSeparator = new Module();
        mBottomSeparator.setBackgroundColor(0xffe4e5e5);

        //add modules
        addModule(mImgLike);
        addModule(mTextLike);
        addModule(mImgComment);
        addModule(mTextComment);
        addModule(mImgMore);
        addModule(mTopLine);
        addModule(mBottomLine);
        addModule(mBottomSeparator);

        //static config
        staticConfigModules();
    }


    private ImageModule buildLikeImgModule() {
        ImageModule module = new ImageModule();
        module.setScaleType(ImageModule.FIT_CENTER);
        module.setPadding(mLeftMargin,dp(6),dp(6),dp(6));
        module.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart));

        return module;
    }

    private TextModule buildLikeTextModule() {
        TextModule module = new TextModule();
        module.setTextSize(sp(14));
        module.setTextColor(0xff222222);
        module.setPadding(dp(1), dp(4), dp(4), dp(4));
        return module;
    }

    private ImageModule buildCommentImgModule() {
        ImageModule module;
        module = new ImageModule();
        module.setScaleType(ImageModule.FIT_CENTER);
        module.setPadding(dp(2));
        module.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_comment));
        return module;
    }

    private TextModule buildCommentTextModule() {
        TextModule module = new TextModule();
        module.setTextSize(sp(14));
        module.setTextColor(0xff222222);
        module.setPadding(dp(4));
        return module;
    }


    private ImageModule buildMoreImgModule() {
        ImageModule module = new ImageModule();
        module.setScaleType(ImageModule.FIT_CENTER);
        module.setPadding(dp(8), dp(4), mLeftMargin, dp(4));
        module.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_more));
        return module;
    }


    private void staticConfigModules() {

        mImgLike.setBounds(0, mFooterMarginTop, mLikeImgSize, mFooterMarginTop + mFooterHeight);

        int textLikeLeft = mImgLike.getRealRight();
        mTextLike.setBounds(textLikeLeft, mFooterMarginTop, textLikeLeft + mLikeTextWidth, mFooterMarginTop + mFooterHeight);
        mTextLike.configModule();
        //re bound text like to vertical center
        int textLikeHeight = mTextLike.getTextLayout().getHeight() + mTextLike.getPaddingTop() + mTextLike.getPaddingBottom();
        int textLikeMarginTop = Math.max((mFooterHeight - textLikeHeight) / 2, 0);
        mTextLike.setBounds(textLikeLeft, mFooterMarginTop + textLikeMarginTop, textLikeLeft + mLikeTextWidth, mFooterMarginTop + textLikeMarginTop + mFooterHeight);

        int imgCommentLeft = mTextLike.getRealRight();
        mImgComment.setBounds(imgCommentLeft, mFooterMarginTop, imgCommentLeft + mCommentImgSize, mFooterMarginTop + mFooterHeight);

        int textCommentLeft = mImgComment.getRealRight();
        mTextComment.setBounds(textCommentLeft, mFooterMarginTop + textLikeMarginTop, textCommentLeft + mCommentTextWidth, mFooterMarginTop + textLikeMarginTop + mFooterHeight);

        mImgLike.configModule();
        mTextLike.configModule();
        mTextComment.configModule();
        mImgComment.configModule();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = configModulesOnMeasure();
        setMeasureDimension(getMeasuredWidth(), height);



    }

    private int configModulesOnMeasure() {
        int widthSize = getMeasuredWidth();
        mImgMore.setBounds(widthSize - mMoreImgSize, mFooterMarginTop, widthSize, mFooterMarginTop + mFooterHeight);
        mImgMore.configModule();

        int height =  mFooterMarginTop + mFooterHeight + mBottomSeparatorSize;
        mTopLine.setBounds(mLeftMargin, mFooterMarginTop, widthSize - mRightMargin, mFooterMarginTop + 1);

        mBottomLine.setBounds(0, mFooterMarginTop + mFooterHeight, widthSize, mFooterMarginTop + mFooterHeight + 1);
        mBottomSeparator.setBounds(0, mFooterMarginTop + mFooterHeight+1, widthSize, height);

        return height;
    }

    public void bindModel(SocialModel model) {
        if (model != null) {
            mTextLike.setText(String.valueOf(model.getLikeCount()));
            mTextComment.setText(String.valueOf(model.getCommentCount()));
        } else {
            mTextLike.setText("0");
            mTextComment.setText("0");
        }
        mTextLike.configModule();
        mTextComment.configModule();
    }



    //-----------------listener------------

    public void setOnLikeClickListener(Module.OnClickListener onLikeClickListener) {
        mOnLikeClickListener = onLikeClickListener;
        mImgLike.setOnClickListener(mOnLikeClickListener);
        mTextLike.setOnClickListener(mOnLikeClickListener);
    }

    public void setOnCommentClickListener(Module.OnClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
        mImgComment.setOnClickListener(mOnCommentClickListener);
        mTextComment.setOnClickListener(mOnCommentClickListener);
    }

    public void setOnMoreClickListener(Module.OnClickListener onMoreClickListener) {
        mOnMoreClickListener = onMoreClickListener;
        mImgMore.setOnClickListener(mOnMoreClickListener);
    }

    public void setOnLikeLongClickListener(Module.OnLongClickListener onLikeLongClickListener) {
        mOnLikeLongClickListener = onLikeLongClickListener;
        mImgLike.setOnLongClickListener(mOnLikeLongClickListener);
        mTextLike.setOnLongClickListener(mOnLikeLongClickListener);
    }

    public void setOnCommentLongClickListener(Module.OnLongClickListener onCommentLongClickListener) {
        mOnCommentLongClickListener = onCommentLongClickListener;
        mImgComment.setOnLongClickListener(mOnCommentLongClickListener);
        mTextComment.setOnLongClickListener(mOnCommentLongClickListener);
    }

    public void setOnMoreLongClickListener(Module.OnLongClickListener onMoreLongClickListener) {
        mOnMoreLongClickListener = onMoreLongClickListener;
        mImgMore.setOnLongClickListener(mOnMoreLongClickListener);
    }
}
