package vn.com.vng.modulesview.sample.views;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import vn.com.vng.modulesview.Application;
import vn.com.vng.modulesview.R;
import vn.com.vng.modulesview.modules_view.ImageModule;
import vn.com.vng.modulesview.modules_view.Module;
import vn.com.vng.modulesview.modules_view.ModulesView;
import vn.com.vng.modulesview.modules_view.TextModule;
import vn.com.vng.modulesview.sample.model.SocialModel;

/**
 * Created by HungNQ on 12/09/2017.
 */

public class SocialModulesView extends ModulesView {
    public SocialModulesView(Context context) {
        super(context);
    }

    public SocialModulesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SocialModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //stuff
    ImageModule imgAva;
    TextModule textName;
    TextModule textTime;

    TextModule textContent;
    ImageModule img1;
    ImageModule img2;
    ImageModule img3;

    ImageModule imgLike;
    TextModule textLike;
    TextModule textComment;
    ImageModule imgComment;
    ImageModule imgMore;


    //Modules' size
     int headerHeight;
     int avaSize;
     int avaMargin;

     int footerHeight;
     int footerMarginTop;


     int likeTextWidth;
     int likeImgSize;

     int commentImgSize;
     int commentTextWidth;
     int moreImgSize;
     int textNameMargin;


    //Model
    private SocialModel mModel;


    @Override
    protected void init() {
        super.init();

        initSize();

        initModules();

        configModules();

        configEvents();
    }


    private void initSize() {
        headerHeight = dp(56);
        avaSize = dp(40);
        avaMargin = dp(8);

        footerHeight = dp(40);
        footerMarginTop = dp(8);


        likeTextWidth = dp(56);
        likeImgSize = dp(32);

        commentImgSize = dp(24);
        commentTextWidth = dp(56);
        moreImgSize = dp(38);
        textNameMargin = dp(8);
    }


    private void initModules() {
        imgAva = new ImageModule();
        imgAva.setRoundCorner(ImageModule.ROUND_CIRCLE);
        imgAva.setScaleType(ImageModule.ScaleType.CENTER_CROP);

        textName = new TextModule();
        textName.setTextSize(sp(16));
        textName.setTextColor(0xff050505);

        textTime = new TextModule();
        textTime.setTextSize(sp(12));
        textTime.setTextColor(0xffaaaaaa);

        textContent = new TextModule();
        textContent.setTextSize(sp(14));
        textContent.setTextColor(0xff222222);
        textContent.setPadding(dp(8), dp(4), dp(8), dp(4));

        img1 = new ImageModule();
        img1.setScaleType(ImageModule.ScaleType.CENTER_CROP);

        img2 = new ImageModule();
        img2.setScaleType(ImageModule.ScaleType.CENTER_CROP);

        img3 = new ImageModule();
        img3.setScaleType(ImageModule.ScaleType.CENTER_CROP);


        imgLike = new ImageModule();
        imgLike.setScaleType(ImageModule.ScaleType.FIT_CENTER);
        imgLike.setPadding(dp(6));
        imgLike.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart));

        textLike = new TextModule();
        textLike.setTextSize(sp(14));
        textLike.setTextColor(0xff222222);
        textLike.setPadding(dp(1), dp(4), dp(4), dp(4));

        textComment = new TextModule();
        textComment.setTextSize(sp(14));
        textComment.setTextColor(0xff222222);
        textComment.setPadding(dp(4));

        imgComment = new ImageModule();
        imgComment.setScaleType(ImageModule.ScaleType.FIT_CENTER);
        imgComment.setPadding(dp(2));
        imgComment.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_comment));

        imgMore = new ImageModule();
        imgMore.setScaleType(ImageModule.ScaleType.FIT_CENTER);
        imgMore.setPadding(dp(8), dp(4), dp(12), dp(4));
        imgMore.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_more));

        //add header
        addModule(imgAva);
        addModule(textName);
        addModule(textTime);

        //add content
        addModule(textContent);

        addModule(img1);
        addModule(img2);
        addModule(img3);

//        add footer
        addModule(imgLike);
        addModule(textLike);
        addModule(imgComment);
        addModule(textComment);
        addModule(imgMore);
    }

    private void configModules() {
        imgAva.setBounds(avaMargin, avaMargin, avaMargin + avaSize, avaMargin + avaSize);
        imgAva.configModule();

        //config on runtime
        setOnMeasureListener(new ModulesView.OnMeasureListener() {
            @Override
            public void onMeasure(ModulesView view, int withMeasureSpec, int heightMeasureSpec) {

                //init view size
                int widthSize = View.MeasureSpec.getSize(withMeasureSpec);


                //init header region
                textName.setBounds(avaMargin * 2 + avaSize, textNameMargin, widthSize - textNameMargin, Module.SPECIFIC_LATER);
                textName.configModule();

                textTime.setBounds(textName.getLeft(), textName.getBottom(), widthSize - textNameMargin, Module.SPECIFIC_LATER);
                textTime.configModule();

                //init content
                textContent.setBounds(0, headerHeight, widthSize, Module.SPECIFIC_LATER);
                textContent.configModule();
                int contentHeight = textContent.getBottom() - textContent.getTop();

                //init image region
                int imgRegionTop = headerHeight + contentHeight;

                int temp1 = (int) (widthSize * 2f / 3);
                int temp2 = (int) (widthSize / 2f);
                img1.setBounds(0, imgRegionTop, temp1 - dp(1), widthSize + imgRegionTop);
                img2.setBounds(temp1 + dp(1), imgRegionTop, widthSize, temp2 + imgRegionTop - dp(1));
                img3.setBounds(temp1 + dp(1), temp2 + imgRegionTop + dp(1), widthSize, widthSize + imgRegionTop);

                img1.configModule();
                img2.configModule();
                img3.configModule();

                //init footer region
                int footerTop = headerHeight + contentHeight + widthSize + footerMarginTop;

                imgLike.setBounds(0, footerTop, likeImgSize, footerTop + footerHeight);

                int textLikeLeft = likeImgSize;
                textLike.setBounds(textLikeLeft, footerTop, textLikeLeft + likeTextWidth, footerTop + footerHeight);
                textLike.configModule();
                int textLikeHeight = textLike.getTextLayout().getHeight() + textLike.getPaddingTop() + textLike.getPaddingBottom();
                int textLikeMarginTop = Math.max((footerHeight - textLikeHeight) / 2, 0);
                textLike.setBounds(textLikeLeft, footerTop + textLikeMarginTop, textLikeLeft + likeTextWidth, footerTop + textLikeMarginTop + footerHeight);

                int imgCommentLeft = likeImgSize + likeTextWidth;
                imgComment.setBounds(imgCommentLeft, footerTop, imgCommentLeft + commentImgSize, footerTop + footerHeight);

                int textCommentLeft = imgCommentLeft + commentImgSize;
                textComment.setBounds(textCommentLeft, footerTop + textLikeMarginTop, textCommentLeft + commentTextWidth, footerTop + textLikeMarginTop + footerHeight);

                imgMore.setBounds(widthSize - moreImgSize, footerTop, widthSize, footerTop + footerHeight);

                imgLike.configModule();
                textLike.configModule();
                textComment.configModule();
                imgComment.configModule();
                imgMore.configModule();

                view.setMeasureDimension(widthSize, widthSize + headerHeight + contentHeight + footerHeight + footerMarginTop);
            }
        });
    }



    private void configEvents() {

        //test click listener
        Module.OnClickListener testClickListener = new Module.OnClickListener() {
            @Override
            public void onClick(Module module) {
                if (module == imgAva)
                    Toast.makeText(getContext(), "CLICK AVA", Toast.LENGTH_SHORT).show();
                else if (module == textName)
                    Toast.makeText(getContext(), "CLICK NAME", Toast.LENGTH_SHORT).show();
                else if (module == textTime)
                    Toast.makeText(getContext(), "CLICK TIME", Toast.LENGTH_SHORT).show();
                else if (module == img1)
                    Toast.makeText(getContext(), "CLICK IMG1", Toast.LENGTH_SHORT).show();
                else if (module == img2)
                    Toast.makeText(getContext(), "CLICK IMG2", Toast.LENGTH_SHORT).show();
                else if (module == img3)
                    Toast.makeText(getContext(), "CLICK IMG3", Toast.LENGTH_SHORT).show();
                else if (module == imgLike || module == textLike)
                    Toast.makeText(getContext(), "CLICK LIKE", Toast.LENGTH_SHORT).show();
                else if (module == imgComment || module == textComment)
                    Toast.makeText(getContext(), "CLICK COMMENT", Toast.LENGTH_SHORT).show();
                else if (module == imgMore)
                    Toast.makeText(getContext(), "CLICK MORE", Toast.LENGTH_SHORT).show();

            }
        };

        imgAva.setOnClickListener(testClickListener);
        textName.setOnClickListener(testClickListener);
        textTime.setOnClickListener(testClickListener);
        img1.setOnClickListener(testClickListener);
        img2.setOnClickListener(testClickListener);
        img3.setOnClickListener(testClickListener);
        imgLike.setOnClickListener(testClickListener);
        textLike.setOnClickListener(testClickListener);
        imgComment.setOnClickListener(testClickListener);
        textComment.setOnClickListener(testClickListener);
        imgMore.setOnClickListener(testClickListener);


        //test long click
        img1.setOnLongClickListener(new Module.OnLongClickListener() {
            @Override
            public void onLongClick(Module module) {
                Toast.makeText(getContext(), "LONG CLICK", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private int sp(int sp) {
        return (int) (Application.self.getResources().getDisplayMetrics().scaledDensity * sp);

    }

    private int dp(int dp) {
        return (int) (Application.self.getResources().getDisplayMetrics().density * dp);
    }


    private ImageModule buildImageModule(Drawable drawable, ImageModule.ScaleType scaleType, int corner) {
        ImageModule img = new ImageModule();
        img.setScaleType(scaleType);
        img.setImageDrawable(drawable);
        img.setRoundCorner(corner);
        return img;
    }

    private TextModule buildTextModule(String text, int textSize, int color, Typeface typeface) {
        TextModule textModule = new TextModule();
        textModule.setText(text);
        textModule.setTextSize(textSize);
        textModule.setTextColor(color);
        textModule.setTypeFace(typeface);
        return textModule;
    }

    public SocialModel getModel() {
        return mModel;
    }

    public void setModel(SocialModel model) {
        if (mModel != model) {
            mModel = model;
            if (mModel != null) {
                imgAva.setBitmap(mModel.getAvatar());
                textName.setText(mModel.getName());
                textTime.setText(mModel.getTime());
                textContent.setText(mModel.getContent());

                if (mModel.getImages() != null) {
                    img1.setBitmap(mModel.getImages().size() > 0 ? mModel.getImages().get(0) : null);
                    img2.setBitmap(mModel.getImages().size() > 1 ? mModel.getImages().get(1) : null);
                    img3.setBitmap(mModel.getImages().size() > 2 ? mModel.getImages().get(2) : null);
                }

                textLike.setText(String.valueOf(mModel.getLikeCount()));
                textComment.setText(String.valueOf(mModel.getCommentCount()));
            } else {
                imgAva.setBitmap(null);
                textName.setText(null);
                textTime.setText(null);
                textContent.setText(null);

                img1.setBitmap(null);
                img2.setBitmap(null);
                img3.setBitmap(null);

                textLike.setText(null);
                textComment.setText(null);
            }

            //config
            imgAva.configModule();
            textName.configModule();
            textTime.configModule();
            textContent.configModule();
            img1.configModule();
            img2.configModule();
            img3.configModule();
            textLike.configModule();
            textComment.configModule();
        }
    }


}
