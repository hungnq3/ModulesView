package vn.com.vng.modulesview.sample.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
        this(context, null);
    }

    public SocialModulesView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SocialModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //stuff
    ImageModule imgAva;
    TextModule textName;
    TextModule textTime;

    TextModule textContent;
    List<ImageModule> imgModules;


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


    private final Module.OnClickListener testClickListener = new Module.OnClickListener() {
        @Override
        public void onClick(Module module) {
            if (module == imgAva)
                Toast.makeText(getContext(), "CLICK AVA", Toast.LENGTH_SHORT).show();
            else if (module == textName)
                Toast.makeText(getContext(), "CLICK NAME", Toast.LENGTH_SHORT).show();
            else if (module == textTime)
                Toast.makeText(getContext(), "CLICK TIME", Toast.LENGTH_SHORT).show();
            else if (module == textContent)
                Toast.makeText(getContext(), "CLICK CONTENT", Toast.LENGTH_SHORT).show();
            else if (module == imgLike || module == textLike)
                Toast.makeText(getContext(), "CLICK LIKE", Toast.LENGTH_SHORT).show();
            else if (module == imgComment || module == textComment)
                Toast.makeText(getContext(), "CLICK COMMENT", Toast.LENGTH_SHORT).show();
            else if (module == imgMore)
                Toast.makeText(getContext(), "CLICK MORE", Toast.LENGTH_SHORT).show();

            else if (module instanceof ImageModule)
                Toast.makeText(getContext(), "CLICK IMG", Toast.LENGTH_SHORT).show();
        }
    };

    final Module.OnLongClickListener testLongClickListener = new Module.OnLongClickListener() {
        @Override
        public void onLongClick(Module module) {
            Toast.makeText(getContext(), "LONG CLICK", Toast.LENGTH_SHORT).show();
        }
    };


    private void init() {

        //init
        initHeader();

        initTextContent();

        initImagesContent();

        initFooter();

        //setup
        configModules();
    }


    protected void initHeader() {
        //size
        headerHeight = dp(56);
        avaSize = dp(40);
        avaMargin = dp(8);


        //init
        imgAva = new ImageModule();
        imgAva.setRoundCorner(ImageModule.ROUND_CIRCLE);
        imgAva.setScaleType(ImageModule.CENTER_CROP);

        textName = new TextModule();
        textName.setTextSize(sp(16));
        textName.setTextColor(0xff050505);

        textTime = new TextModule();
        textTime.setTextSize(sp(12));
        textTime.setTextColor(0xffaaaaaa);


        //add header
        addModule(imgAva);
        addModule(textName);
        addModule(textTime);

        imgAva.setBounds(avaMargin, avaMargin, avaMargin + avaSize, avaMargin + avaSize);
        imgAva.configModule();

        //test event
        imgAva.setOnClickListener(testClickListener);
        textName.setOnClickListener(testClickListener);
        textTime.setOnClickListener(testClickListener);


    }

    protected void initTextContent() {

        textContent = new TextModule();
        textContent.setTextSize(sp(14));
        textContent.setTextColor(0xff222222);
        textContent.setPadding(dp(8), dp(4), dp(8), dp(4));


        //add content
        addModule(textContent);


        //test event
        textContent.setOnClickListener(testClickListener);
    }

    protected void initImagesContent() {
        imgModules = createImageModules(getImageContentCount());
        addModules(imgModules);
    }

    protected int getImageContentCount() {
        return 0;
    }


    private List<ImageModule> createImageModules(int n) {
        List<ImageModule> list;
        list = new LinkedList<>();
        for (int i = 0; i < n; ++i) {
            ImageModule imgModule = new ImageModule();
            imgModule.setScaleType(ImageModule.CENTER_CROP);

            list.add(imgModule);

            //test event
            imgModule.setOnClickListener(testClickListener);
            imgModule.setOnLongClickListener(testLongClickListener);
        }

        return list;
    }

    protected void initFooter() {
        //size
        footerHeight = dp(40);
        footerMarginTop = dp(8);
        likeTextWidth = dp(56);
        likeImgSize = dp(32);

        commentImgSize = dp(24);
        commentTextWidth = dp(56);
        moreImgSize = dp(38);
        textNameMargin = dp(8);


        //init
        imgLike = new ImageModule();
        imgLike.setScaleType(ImageModule.FIT_CENTER);
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
        imgComment.setScaleType(ImageModule.FIT_CENTER);
        imgComment.setPadding(dp(2));
        imgComment.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_comment));

        imgMore = new ImageModule();
        imgMore.setScaleType(ImageModule.FIT_CENTER);
        imgMore.setPadding(dp(8), dp(4), dp(12), dp(4));
        imgMore.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_more));

//        add footer
        addModule(imgLike);
        addModule(textLike);
        addModule(imgComment);
        addModule(textComment);
        addModule(imgMore);


        //test event
        imgLike.setOnClickListener(testClickListener);
        textLike.setOnClickListener(testClickListener);
        imgComment.setOnClickListener(testClickListener);
        textComment.setOnClickListener(testClickListener);
        imgMore.setOnClickListener(testClickListener);
    }


    protected void configModules() {
        //config on runtime
        setOnMeasureListener(new ModulesView.OnMeasureListener() {
            @Override
            public void onMeasure(ModulesView view, int withMeasureSpec, int heightMeasureSpec) {
                //init view size
                int widthSize = View.MeasureSpec.getSize(withMeasureSpec);

                int height = 0;
                //config header region
                height += configHeaderOnMeasure(height, widthSize);
                //config text region
                height += configTextContentOnMeasure(height, widthSize);
                //config images region
                height += configImagesContentOnMeasure(height, widthSize);
                //config footer region
                height += configFooterOnMeasure(height, widthSize);

                view.setMeasureDimension(widthSize, height);
            }
        });
    }


    protected int configHeaderOnMeasure(int top, int widthSize) {
        textName.setBounds(avaMargin * 2 + avaSize, top + textNameMargin, Module.SPECIFIC_LATER, Module.SPECIFIC_LATER);
        textName.configModule();

        textTime.setBounds(textName.getBoundLeft(), top + textName.getBoundBottom(), Module.SPECIFIC_LATER, Module.SPECIFIC_LATER);
        textTime.configModule();

        return headerHeight;
    }

    protected int configTextContentOnMeasure(int top, int widthSize) {
        textContent.setBounds(0, top, widthSize, Module.SPECIFIC_LATER);
        textContent.configModule();

        if (TextUtils.isEmpty(textContent.getText()))
            return 0;

        return textContent.getBoundBottom() - textContent.getBoundTop();
    }


    protected int configImagesContentOnMeasure(int top, int widthSize) {
        return 0;
    }


    protected int configFooterOnMeasure(int top, int widthSize) {

        int footerTop = top + footerMarginTop;

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

        return footerHeight + footerMarginTop;
    }


    public SocialModel getModel() {
        return mModel;
    }

    public void setModel(SocialModel model) {

        if (mModel != model) {
            mModel = model;
            int oldTextContentHeight = getTextContentHeight();
            if (mModel != null) {
                bindHeader(mModel);

                bindTextContent(mModel);

                bindImagesContent(mModel);

                bindFooter(mModel);

            } else {
                bindNoData();

            }
        }
    }

    private void bindNoData() {
        imgAva.setBitmap(null);
        textName.setText(null);
        textTime.setText(null);
        textContent.setText(null);

        for (ImageModule imgModule : imgModules) {
            imgModule.setBitmap(null);
        }

        textLike.setText(null);
        textComment.setText(null);
    }


    protected void bindHeader(SocialModel model) {
        imgAva.setBitmap(model.getAvatar());
        //static config
        imgAva.configModule();

        textName.setText(model.getName());
        textTime.setText(model.getTime());
    }


    protected void bindTextContent(SocialModel model) {
        textContent.setText(model.getContent());
    }


    protected void bindImagesContent(SocialModel model) {
        if (model.getImages() != null) {
            Iterator<ImageModule> imgIterator = imgModules.iterator();
            Iterator<Bitmap> bitmapIterator = model.getImages().iterator();
            while (imgIterator.hasNext()) {
                imgIterator.next().setBitmap(bitmapIterator.hasNext() ? bitmapIterator.next() : null);
            }
        } else {
            for (ImageModule imgModule : imgModules) {
                imgModule.setBitmap(null);
            }
        }
    }

    private void bindFooter(SocialModel model) {
        textLike.setText(String.valueOf(mModel.getLikeCount()));
        textComment.setText(String.valueOf(mModel.getCommentCount()));
    }

    private int getTextContentHeight() {
        if (textContent != null && !TextUtils.isEmpty(textContent.getText()))
            return textContent.getBoundBottom() - textContent.getBoundTop();
        return 0;
    }

}
