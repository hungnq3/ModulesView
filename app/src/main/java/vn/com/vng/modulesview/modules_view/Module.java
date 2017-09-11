package vn.com.vng.modulesview.modules_view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ViewGroup;

/**
 * Created by HungNQ on 08/09/2017.
 */

public class Module {

    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    public static final int BOUND_UNKNOWN = -1;

    protected Context mContext;
    protected ModulesView mParent;
    protected boolean mIgnoreConfigFromParent;


    protected int mLeft, mTop, mRight, mBottom;
    protected int mWidth, mHeight;

    protected int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

    public ModulesView getParent() {
        return mParent;
    }

    void setParent(ModulesView parent) {
        mParent = parent;
        mContext = mParent != null ? mParent.getContext() : null;
    }

    public int getLeft() {
        return mLeft;
    }

    public int getTop() {
        return mTop;
    }

    public int getRight() {
        return mRight;
    }

    public int getBottom() {
        return mBottom;
    }


    final public void setBounds(int left, int top, int right, int bottom) {
        boolean changed = checkLayoutChanged(mLeft, mTop, mRight, mBottom, left, top, right, bottom);
        mLeft = left;
        mRight = right;
        mBottom = bottom;
        mTop = top;
        mWidth = mRight - mLeft;
        mHeight = mBottom - mTop;

        onSetBound(changed, mLeft, mTop, mRight, mBottom);
    }

    protected void onSetBound(boolean layoutChanged, int left, int top, int right, int bottom) {

    }

    public boolean isIgnoreConfigFromParent() {
        return mIgnoreConfigFromParent;
    }

    public void setIgnoreConfigFromParent(boolean ignoreConfigFromParent) {
        mIgnoreConfigFromParent = ignoreConfigFromParent;
    }

    public void configModule(boolean changed){
    }


    private boolean checkLayoutChanged(int oldLeft, int oldTop, int oldRight, int oldBottom, int newLeft, int newTop, int newRight, int newBottom) {
        return ((oldRight - oldLeft) != (newRight - newLeft)) || ((oldTop - oldBottom) != (newTop - newBottom));
    }


    protected void draw(Canvas canvas) {

    }

}
