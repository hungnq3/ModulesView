package vn.com.vng.modulesview.modules_view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.fbui.textlayoutbuilder.TextLayoutBuilder;

/**
 * Created by HungNQ on 08/09/2017.
 */

public class TextModule extends Module {

    public static final int DEFAULT_TEXT_COLOR = 0x8a000000;
    public static final int DEFAULT_TEXT_SIZE_IN_SP = 14;

    //stuff
    private TextLayoutBuilder mTextLayoutBuilder;
    private Layout mTextLayout;
    private RectF mClipRect = new RectF();

    //properties
    private CharSequence mText;
    private int mTextSize;
    private int mTextColor;
    private Layout.Alignment mAlignment;
    private int mMaxLines;
    private boolean mSingleLine;
    private TextUtils.TruncateAt mEllipsize;
    private Typeface mTypeFace;


    public TextModule() {
        init();
    }

    private void init() {

        //init default properties
        buildDefaultProperties();

        //build LayoutBuilder with default properties
        mTextLayoutBuilder = new TextLayoutBuilder()
                .setShouldCacheLayout(true)
                .setText(mText)
                .setTextColor(mTextColor)
                .setAlignment(mAlignment)
                .setEllipsize(mEllipsize)
                .setMaxLines(mMaxLines)
                .setSingleLine(mSingleLine)
                .setTextSize(mTextSize)
                .setTypeface(mTypeFace);
    }

    private void buildDefaultProperties() {
        mText = "";
        mTextColor = DEFAULT_TEXT_COLOR;
        mMaxLines = Integer.MAX_VALUE;
        mSingleLine = false;
        mAlignment = Layout.Alignment.ALIGN_NORMAL;
        mEllipsize = null;
    }

    //----------------------Properties' getter & setter region---------------
    public CharSequence getText() {
        return mText;
    }

    public void setText(CharSequence text) {
        mText = makeSureTextValid(text);
        mTextLayoutBuilder.setText(mText);
    }

    private CharSequence makeSureTextValid(CharSequence text) {
        return text == null ? "" : text;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        mTextLayoutBuilder.setTextSize(mTextSize);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextLayoutBuilder.setTextColor(mTextColor);
    }

    public int getMaxLines() {
        return mMaxLines;
    }

    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines;
        mTextLayoutBuilder.setMaxLines(mMaxLines);
    }

    public boolean isSingleLine() {
        return mSingleLine;
    }

    public void setSingleLine(boolean singleLine) {
        mSingleLine = singleLine;
        mTextLayoutBuilder.setSingleLine(mSingleLine);
    }

    public Layout.Alignment getAlignment() {
        return mAlignment;
    }

    public void setAlignment(Layout.Alignment alignment) {
        mAlignment = alignment;
        mTextLayoutBuilder.setAlignment(mAlignment);
    }

    public TextUtils.TruncateAt getEllipsize() {
        return mEllipsize;
    }

    public void setEllipsize(TextUtils.TruncateAt ellipsize) {
        mEllipsize = ellipsize;
        mTextLayoutBuilder.setEllipsize(mEllipsize);
    }

    public Typeface getTypeFace() {
        return mTypeFace;
    }

    public void setTypeFace(Typeface typeFace) {
        mTypeFace = typeFace;
        mTextLayoutBuilder.setTypeface(mTypeFace);
    }

    public Layout getTextLayout() {
        return mTextLayout;
    }

    //-----------------endregion--------------------------------------------


    @Override
    public void configModule(boolean changed) {
        super.configModule(changed);
        if (changed) {
            buildTextLayout();
            configClipBounds();
        }
    }

    private void buildTextLayout() {

        int width = Math.max(mRight - mLeft - mPaddingLeft - mPaddingRight, 0);
        //build a layout to calculate text width and height
        mTextLayout = buildTextLayout(width);

//        int textWidth;
//        if (mTextLayout.getLineCount() <= 1) {
//            textWidth = (int) mTextLayout.getLineWidth(0);
//        } else
//            textWidth = width;

//        if (mBottom == BOUND_UNKNOWN) {
//            int textHeight = mTextLayout.getLineBottom(mTextLayout.getLineCount() - 1) - mTextLayout.getLineTop(0);
//            mBottom = mTop + textHeight + mPaddingTop + mPaddingBottom;
//            setBounds(mLeft, mTop, mRight, mBottom);
//        }
    }

    private void configClipBounds() {
        int width = mRight - mBottom;
        int height = mBottom - mTop;
        if(width >0 && height >0){
            width = width - mPaddingLeft - mPaddingRight;
            height = height - mPaddingBottom - mPaddingTop;
            mClipRect.set(0,0,width,height);
        }else{        //unnecessary
            mClipRect.setEmpty();
        }
    }


    @Override
    protected void onSetBound(boolean layoutChanged, int left, int top, int right, int bottom) {
        Log.d("text", "onSetBound: " + left + " - " + top + " - " + right + " - " + bottom);
        super.onSetBound(layoutChanged, left, top, right, bottom);
    }


    /**
     * build a {@link Layout} based on text width and element's properties set
     *
     * @return {@link Layout}
     */
    private Layout buildTextLayout(int width) {
        //default text size
        if (mTextSize == 0 && mContext != null) {
            mTextSize = (int) (DEFAULT_TEXT_SIZE_IN_SP * mContext.getResources().getDisplayMetrics().scaledDensity);
            mTextLayoutBuilder.setTextSize(mTextSize);
        }
        return mTextLayoutBuilder
                .setWidth(width)
                .build();
    }

    @Override
    protected void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();

        //translate if needed
        int translateLeft = mLeft + mPaddingLeft;
        int translateTop = mTop + mPaddingTop;
        if (translateLeft > 0 || translateTop > 0)
            canvas.translate(translateLeft, translateTop);

        //clip drawing region
        if(!mClipRect.isEmpty())
            canvas.clipRect(mClipRect);

        if (mTextLayout != null)
            mTextLayout.draw(canvas);

        canvas.restore();
    }


}
