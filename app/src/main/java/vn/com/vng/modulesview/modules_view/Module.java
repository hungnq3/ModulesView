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

    private Context mContext;
    private ModulesView mParent;

    private int mLeft, mTop, mRight, mBottom;

    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;
    private int Gravity;

    private WhenParentMeasure mWhenParentMeasure;
    private WhenParentLayout mWhenParentLayout;

    public ModulesView getParent() {
        return mParent;
    }

    void setParent(ModulesView parent) {
        mParent = parent;
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


    public void setBounds(int left, int top, int right, int bottom) {
        boolean changed = checkLayoutChanged(mLeft, mTop, mRight, mBottom, left, top, right, bottom);
        mLeft = left;
        mRight = right;
        mBottom = bottom;
        mTop = top;

        onSetBound(changed, mLeft, mTop, mRight, mBottom);
    }

    protected void onSetBound(boolean layoutChanged, int left, int top, int right, int bottom) {


    }

    private boolean checkLayoutChanged(int oldLeft, int oldTop, int oldRight, int oldBottom, int newLeft, int newTop, int newRight, int newBottom) {
        return ((oldRight - oldLeft) != (newRight - newLeft)) || ((oldTop - oldBottom) != (newTop - newBottom));
    }


    public WhenParentMeasure getWhenParentMeasure() {
        return mWhenParentMeasure;
    }

    public void setWhenParentMeasure(WhenParentMeasure whenParentMeasure) {
        mWhenParentMeasure = whenParentMeasure;
    }

    public WhenParentLayout getWhenParentLayout() {
        return mWhenParentLayout;
    }

    public void setWhenParentLayout(WhenParentLayout whenParentLayout) {
        mWhenParentLayout = whenParentLayout;
    }

    protected void draw(Canvas canvas) {

    }


    public static interface WhenParentLayout {
        void whenParentLayout(ModulesView parent, boolean changed, int width, int height);
    }

    public static interface WhenParentMeasure {
        void whenParentMeasure(ModulesView parent, int widthMeasureSpec, int heightMeasureSpec);
    }

}
