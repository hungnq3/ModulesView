package vn.com.vng.modulesview.modules_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;

/**
 * Created by HungNQ on 08/09/2017.
 */

public class Module {
    private static final long LONG_CLICK_TIME = 500;

    //if a bound (left, top, right, bottom) can not determine , let put BOUND_WRAP_CONTENT.
    //Depend on each module, bounds will be defined when the module call configModule()
    public static final int BOUND_WRAP_CONTENT = -1;


    //stuff
    protected Context mContext;
    private ModulesView mParent;

    //real area occupied

    //properties:
    //Bounds of a module is expressed with 4 values mLeft, mTop, mRight and mBottom, which determine by call setBounds(left, top, right, bottom)
    //For each bound, it can specify an exact number or BOUND_WRAP_CONTENT,
    // which means that the module will be specify when config.
    //Example: A TextModule can call setBounds(0,0,BOUND_WRAP_CONTENT,BOUND_WRAP_CONTENT) which mean
    //right and bottom bounds will fit the text when the TextModule call configModule().
    //And then the real bounds of element pass 4 values mRealLeft, mRealTop, mRealRight, mRealBottom
    private int mLeft, mTop, mRight, mBottom;

    //This is real bounds of module, where the module r eallylocated in the parent and how big it is.
    //Which be determined after configModule() called, depends on specific module that is definitely difference
    //For example:
    //with TextModule:
    //mLeft = BOUND_WRAP_CONTENT -> mRealLeft = 0;
    //mTop = BOUND_WRAP_CONTENT -> mRealTop = 0;
    //mRight = BOUND_WRAP_CONTENT -> mRealRight = the value that fit the text width and padding;
    //mBottom = BOUND_WRAP_CONTENT -> mRealRight = the value that fit the text height and padding;
    // with ImageModule:
    // BOUND_WRAP_CONTENT -> 0 for all bounds.
    protected int mRealLeft, mRealTop, mRealRight, mRealBottom;


    protected int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

    protected Drawable mBackgroundDrawable;

    private OnClickListener mOnClickListener;
    private OnLongClickListener mOnLongClickListener;
    private OnTouchListener mOnTouchListener;


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

    public int getRealLeft() {
        return mRealLeft;
    }

    public int getRealTop() {
        return mRealTop;
    }

    public int getRealRight() {
        return mRealRight;
    }

    public int getRealBottom() {
        return mRealBottom;
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

    public int getWidth() {
        return getRealRight() - getRealLeft();
    }

    public int getHeight() {
        return getRealBottom() - getRealTop();
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


    //call to initiate the module's layout
    final public void setBounds(int left, int top, int right, int bottom) {
        mLeft = left;
        mRight = right;
        mBottom = bottom;
        mTop = top;
        onSetBounds(mLeft, mTop, mRight, mBottom);
    }


    //you must call and recall configModule() when module's properties have changed to let module configs and prepares stuffs and layout for drawing.
    //if you don't, module maybe drawing the previous state although you changed some module's properties.
    public void configModule() {
        onSetBounds(mLeft, mTop, mRight, mBottom);
    }


    protected void onSetBounds(int left, int top, int right, int bottom) {
        mRealLeft = left > 0 ? left : 0;
        mRealTop = top > 0 ? top : 0;
        mRealRight = right > 0 ? right : 0;
        mRealBottom = bottom > 0 ? bottom : 0;
    }


    //this method was called in ModuleView parent to let module draw on the canvas of parent view.
    protected void draw(Canvas canvas) {
        drawBackground(canvas);
    }

    protected void drawBackground(Canvas canvas) {
        if (canvas != null && mBackgroundDrawable != null) {
            if (mRealLeft >= 0 && mRealTop >= 0 && mRealRight > 0 && mRealBottom > 0) {
                mBackgroundDrawable.setBounds(mRealLeft, mRealTop, mRealRight, mRealBottom);
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
        return handleTouchEvent(e) || handled;
    }


    protected boolean handleTouchEvent(MotionEvent event) {
        if (mOnTouchListener != null)
            return mOnTouchListener.onTouch(this, event);
        return false;
    }

    private Handler mLongClickTriggerHandler;
    private Runnable mLongClickTriggerRunnable = new Runnable() {
        @Override
        public void run() {
            performLongClick();
            //trigger a cancel touch event to parent view after long click
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


    public void performLongClick() {
        if (mOnLongClickListener != null)
            mOnLongClickListener.onLongClick(this);
    }

    public void performClick() {
        if (mOnClickListener != null)
            mOnClickListener.onClick(this);
    }


    public void invalidate() {
        if (mParent != null)
//            mParent.invalidateChild(this);
            mParent.invalidate();
    }

    //-------------interface region--------------------
    public interface OnClickListener {
        void onClick(Module module);
    }

    public interface OnLongClickListener {
        void onLongClick(Module module);
    }

    public interface OnTouchListener {
        boolean onTouch(Module module, MotionEvent event);
    }

    public OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    //--------------endregion--------------------------


}
