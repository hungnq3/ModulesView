package vn.com.vng.modulesview.temp;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import vn.com.vng.modulesview.Application;
import vn.com.vng.modulesview.MainActivity;
import vn.com.vng.modulesview.R;
import vn.com.vng.modulesview.modules_view.ImageModule;
import vn.com.vng.modulesview.modules_view.Module;
import vn.com.vng.modulesview.modules_view.ModulesView;
import vn.com.vng.modulesview.modules_view.TextModule;

/**
 * Created by HungNQ on 12/09/2017.
 */

public class DemoViewFactory {


    public static ModulesView buildDemoView(final Context context) {

        Drawable drawableimg = ContextCompat.getDrawable(context, R.drawable.img);
        Drawable drawableLike = ContextCompat.getDrawable(context, R.drawable.ic_heart);
        Drawable drawableComment = ContextCompat.getDrawable(context, R.drawable.ic_comment);
        Drawable drawableMore = ContextCompat.getDrawable(context, R.drawable.ic_more);



        final ModulesView view = new ModulesView(context);

        final ImageModule imgAva = buildImageModule(drawableimg, ImageModule.CENTER_CROP, ImageModule.ROUND_CIRCLE);
        final TextModule textName = buildTextModule("Cú vọ", sp(16), 0xff050505, Typeface.DEFAULT);
        final TextModule textTime = buildTextModule("20:27 Hôm qua", sp(12), 0xffaaaaaa, Typeface.DEFAULT);

        final TextModule textContent = buildTextModule("Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", sp(14), 0xff222222, Typeface.DEFAULT);
        final ImageModule img1 = buildImageModule(drawableimg, ImageModule.CENTER_CROP, ImageModule.ROUND_NONE);
        final ImageModule img2 = buildImageModule(drawableimg, ImageModule.CENTER_CROP, ImageModule.ROUND_NONE);
        final ImageModule img3 = buildImageModule(drawableimg, ImageModule.CENTER_CROP, ImageModule.ROUND_NONE);


        final ImageModule imgLike = buildImageModule(drawableLike, ImageModule.FIT_CENTER, ImageModule.ROUND_NONE);
        final TextModule textLike = buildTextModule("0", dp(14), 0xff222222, Typeface.DEFAULT);
        final TextModule textComment = buildTextModule("0", dp(14), 0xff222222, Typeface.DEFAULT);
        final ImageModule imgComment = buildImageModule(drawableComment, ImageModule.FIT_CENTER, ImageModule.ROUND_NONE);
        final ImageModule imgMore = buildImageModule(drawableMore, ImageModule.FIT_CENTER, ImageModule.ROUND_NONE);


        //add header
        view.addModule(imgAva);
        view.addModule(textName);
        view.addModule(textTime);

        //add content
        view.addModule(textContent);

//        add image
        view.addModule(img1);
        view.addModule(img2);
        view.addModule(img3);

//        add footer
        view.addModule(imgLike);
        view.addModule(textLike);
        view.addModule(imgComment);
        view.addModule(textComment);
        view.addModule(imgMore);



        //config a few properties
        textContent.setPadding(dp(8), dp(4), dp(8), dp(4));
        imgLike.setPadding(dp(6));
        textLike.setPadding(dp(1),dp(4),dp(4),dp(4));
        textComment.setPadding(dp(4));
        imgComment.setPadding(dp(2));
        imgMore.setPadding(dp(8), dp(4), dp(12), dp(4));


        //declare modules' size
        final int headerHeight = dp(56);
        final int avaSize = dp(40);
        final int avaMargin = dp(8);

        final int footerHeight = dp(40);
        final int footerMarginTop = dp(8);


        final int likeTextWidth = dp(56);
        final int likeImgSize = dp(32);

        final int commentImgSize = dp(24);
        final int commentTextWidth = dp(56);
        final int moreImgSize = dp(38);



        imgAva.setBounds(avaMargin, avaMargin, avaMargin + avaSize, avaMargin + avaSize);
        imgAva.configModule();

        final int textNameMargin = dp(8);
        textName.setBounds(avaMargin * 2 + avaSize, textNameMargin, Module.SPECIFIC_LATER, Module.SPECIFIC_LATER);

        view.setOnMeasureListener(new ModulesView.OnMeasureListener() {
            @Override
            public void onMeasure(ModulesView view, int withMeasureSpec, int heightMeasureSpec) {

                //init view size
                int widthSize = View.MeasureSpec.getSize(withMeasureSpec);

                //init header region
                textName.configModule();

                textTime.configModule();

                //init content
                textContent.configModule();
                int contentHeight = textContent.getBoundBottom() - textContent.getBoundTop();

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

                view.setMeasureDimension(widthSize, widthSize + headerHeight+ contentHeight + footerHeight + footerMarginTop);
            }
        });


        //test event


        //test click listener
        Module.OnClickListener testClickListener = new Module.OnClickListener() {
            @Override
            public void onClick(Module module) {
                if (module == imgAva)
                    Toast.makeText(context, "CLICK AVA", Toast.LENGTH_SHORT).show();
                else if (module == textName)
                    Toast.makeText(context, "CLICK NAME", Toast.LENGTH_SHORT).show();
                else if (module == textTime)
                    Toast.makeText(context, "CLICK TIME", Toast.LENGTH_SHORT).show();
                else if (module == img1)
                    Toast.makeText(context, "CLICK IMG1", Toast.LENGTH_SHORT).show();
                else if (module == img2)
                    Toast.makeText(context, "CLICK IMG2", Toast.LENGTH_SHORT).show();
                else if (module == img3)
                    Toast.makeText(context, "CLICK IMG3", Toast.LENGTH_SHORT).show();
                else if (module == imgLike || module == textLike)
                    Toast.makeText(context, "CLICK LIKE", Toast.LENGTH_SHORT).show();
                else if (module == imgComment || module == textComment)
                    Toast.makeText(context, "CLICK COMMENT", Toast.LENGTH_SHORT).show();
                else if (module == imgMore)
                    Toast.makeText(context, "CLICK MORE", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(context, "LONG CLICK", Toast.LENGTH_SHORT).show();

            }
        });

        //test background
//        textTime.setBackgroundColor(0xffdddddd);
//        textName.setBackgroundColor(0xffcccccc);
//        imgAva.setBackgroundColor(0xffbbbbbb);
//
//        imgLike.setBackgroundColor(0xffcccccc);
//        textLike.setBackgroundColor(0xffcccccc);
//
//        imgComment.setBackgroundColor(0xffdddddd);
//        textComment.setBackgroundColor(0xffdddddd);
//
//        imgMore.setBackgroundColor(0xffbbbbbb);
        return view;
    }


    private static int sp(int sp) {
        return (int) (Application.self.getResources().getDisplayMetrics().scaledDensity * sp);

    }

    private static int dp(int dp) {
        return (int) (Application.self.getResources().getDisplayMetrics().density * dp);
    }


    private static ImageModule buildImageModule(Drawable drawable, @ImageModule.ScaleType  int scaleType, int corner) {
        ImageModule img = new ImageModule();
        img.setScaleType(scaleType);
        img.setImageDrawable(drawable);
        img.setRoundCorner(corner);
        return img;
    }

    private static TextModule buildTextModule(String text, int textSize, int color, Typeface typeface) {
        TextModule textModule = new TextModule();
        textModule.setText(text);
        textModule.setTextSize(textSize);
        textModule.setTextColor(color);
        textModule.setTypeFace(typeface);
        return textModule;
    }

}
