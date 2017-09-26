package vn.com.vng.modulesview.modules_view;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

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
    public void configModule() {
        super.configModule();
        buildTextLayout();
        configClipBounds();
    }

    private void buildTextLayout() {
        int textWidth = Math.max(mRealRight - mRealLeft - mPaddingLeft - mPaddingRight, 0);
        //build a layout to calculate text width and height
        mTextLayout = buildTextLayout(textWidth);

        if (getBottom() == BOUND_WRAP_CONTENT) {
            int textHeight = mTextLayout.getHeight();
            mRealBottom = mRealTop + textHeight + mPaddingTop + mPaddingBottom;
        }

        if (getRight() == BOUND_WRAP_CONTENT) {
            textWidth = mTextLayout.getWidth();
            mRealRight = mRealLeft + textWidth + mPaddingLeft + mPaddingRight;
        }
    }


    private void configClipBounds() {
        int width = mRealRight - mRealLeft;
        int height = mRealBottom - mRealTop;
        mClipRect.set(0, 0, width, height);
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
        if (width > 0)
            mTextLayoutBuilder.setWidth(width);
        else
            mTextLayoutBuilder.setWidth(0, TextLayoutBuilder.MEASURE_MODE_UNSPECIFIED);

        Layout layout = mTextLayoutBuilder.build();

        //fix layout null when text empty
        if (layout == null) {
            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(mTextSize);
            layout = new StaticLayout("", textPaint, width, mAlignment, mTextLayoutBuilder.getTextSpacingMultiplier(), mTextLayoutBuilder.getTextSpacingExtra(), false);
        }
        return layout;
    }

    @Override
    protected void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();

        //translate if needed
        int translateLeft = mRealLeft + mPaddingLeft;
        int translateTop = mRealTop + mPaddingTop;
        if (translateLeft > 0 || translateTop > 0)
            canvas.translate(translateLeft, translateTop);

        //clip drawing region
        canvas.clipRect(mClipRect);

        if (mTextLayout != null)
            mTextLayout.draw(canvas);

        canvas.restore();
    }

}
