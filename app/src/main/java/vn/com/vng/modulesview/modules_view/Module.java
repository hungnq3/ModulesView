package vn.com.vng.modulesview.modules_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by HungNQ on 08/09/2017.
 */

public class Module {
    private static final long LONG_CLICK_TIME = 500;

    //if a bound (left, top, right, bottom) can not determine , let put SPECIFIC_LATER.
    //Depend on each module, bounds will be defined when attached
    public static final int SPECIFIC_LATER = -1;


    //stuff
    protected Context mContext;
    private ModulesView mParent;

    //properties
    private int mLeft, mTop, mRight, mBottom;
    protected int mBoundLeft, mBoundTop, mBoundRight, mBoundBottom;
    protected int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

    protected Drawable mBackgroundDrawable;

    private OnClickListener mOnClickListener;
    private OnLongClickListener mOnLongClickListener;


    public ModulesView getParent() {
        return mParent;
    }

    void setParent(ModulesView parent) {
        mParent = parent;
        mContext = mParent != null ? mParent.getContext() : null;
    }

    public Context getContext() {
        return mContext;
    }


    //-------------------getter & setter----------

    protected int getLeft() {
        return mLeft;
    }

    protected int getTop() {
        return mTop;
    }

    protected int getRight() {
        return mRight;
    }

    protected int getBottom() {
        return mBottom;
    }

    public int getBoundLeft() {
        return mBoundLeft;
    }

    public int getBoundTop() {
        return mBoundTop;
    }

    public int getBoundRight() {
        return mBoundRight;
    }

    public int getBoundBottom() {
        return mBoundBottom;
    }

    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        mPaddingLeft = paddingLeft;
    }

    public int getPaddingTop() {
        return mPaddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        mPaddingTop = paddingTop;
    }

    public int getPaddingRight() {
        return mPaddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        mPaddingRight = paddingRight;
    }

    public int getPaddingBottom() {
        return mPaddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        mPaddingBottom = paddingBottom;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingTop = top;
        mPaddingRight = right;
        mPaddingBottom = bottom;
    }

    public void setPadding(int padding) {
        mPaddingLeft = mPaddingTop = mPaddingRight = mPaddingBottom = padding;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        clearBackground();
        mBackgroundDrawable = new BitmapDrawable(null, backgroundBitmap);
    }

    public Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    public void setBackgroundDrawable(Drawable backgroundDrawable) {
        clearBackground();
        mBackgroundDrawable = backgroundDrawable;
    }

    public void setBackgroundColor(int backgroundColor) {
        clearBackground();
        mBackgroundDrawable = new ColorDrawable(backgroundColor);
    }

    public void clearBackground() {
        mBackgroundDrawable = null;
    }


    //--------------config region-------------

    final public void setBounds(int left, int top, int right, int bottom) {
        mLeft = left;
        mRight = right;
        mBottom = bottom;
        mTop = top;

        onSetBounds(left, top, right, bottom);
    }

    protected void onSetBounds(int left, int top, int right, int bottom) {
        mBoundLeft = left > 0? left : 0;
        mBoundTop = top > 0? top : 0;
        mBoundRight = right > 0? right : 0;
        mBoundBottom = bottom > 0 ? bottom : 0;
    }

    public void configModule() {
        onSetBounds(mLeft, mTop, mRight, mBottom);
    }

    protected void draw(Canvas canvas) {
        drawBackdround(canvas);
    }

    protected void drawBackdround(Canvas canvas) {
        if (canvas != null && mBackgroundDrawable != null) {
            if (mLeft >= 0 && mTop >= 0 && mRight > 0 && mBottom > 0) {
                mBackgroundDrawable.setBounds(mLeft, mTop, mRight, mBottom);
                mBackgroundDrawable.draw(canvas);
            }
        }
    }

    //handle event

    public boolean clickable() {
        return mOnClickListener != null;
    }

    public boolean longClickable() {
        return mOnLongClickListener != null;
    }

    protected boolean onTouchEvent(MotionEvent e) {
        boolean handled = false;
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                handled = clickable() || longClickable();

                if (longClickable())
                    startWaitingLongClick();
                break;
            }

            case MotionEvent.ACTION_UP: {
                cancelLongClickWaiting();
                handled = clickable();
                if (clickable())
                    performClick();
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                cancelLongClickWaiting();
                break;
            }
        }
        return handled;
    }

    private Handler mLongClickTriggerHandler;
    private Runnable mLongClickTriggerRunnable = new Runnable() {
        @Override
        public void run() {
            performLongClick();

            //trigger cancel touch event to parent
            if (mParent != null)
                mParent.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, getLeft(), getTop(), 0));
        }
    };



    private void startWaitingLongClick() {
        if (mLongClickTriggerHandler == null && mContext == null)
            return;
        cancelLongClickWaiting();
        if (mLongClickTriggerHandler == null)
            mLongClickTriggerHandler = new Handler();
        mLongClickTriggerHandler.removeCallbacks(mLongClickTriggerRunnable);
        mLongClickTriggerHandler.postDelayed(mLongClickTriggerRunnable, LONG_CLICK_TIME);
    }

    private void cancelLongClickWaiting() {
        if (mLongClickTriggerHandler != null)
            mLongClickTriggerHandler.removeCallbacks(mLongClickTriggerRunnable);
    }


    private void performLongClick() {
        if (mOnLongClickListener != null)
            mOnLongClickListener.onLongClick(this);
    }

    private void performClick() {
        if (mOnClickListener != null)
            mOnClickListener.onClick(this);
    }


    //-------------interface region--------------------
    public interface OnClickListener {
        void onClick(Module module);
    }

    public interface OnLongClickListener {
        void onLongClick(Module module);
    }

    //--------------endregion--------------------------


}
