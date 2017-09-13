package vn.com.vng.modulesview.modules_view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by HungNQ on 08/09/2017.
 */

public class ModulesView extends View {

    public interface OnMeasureListener {
        void onMeasure(ModulesView view, int withMeasureSpec, int heightMeasureSpec);
    }

    public interface OnLayoutListener {
        void onLayout(ModulesView view, boolean changed, int width, int height);
    }

    public ModulesView(Context context) {
        this(context, null);
    }

    public ModulesView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    //stuff
    private List<Module> mModules = new LinkedList<>();
    private OnMeasureListener mOnMeasureListener;
    private OnLayoutListener mOnLayoutListener;
    private Module mTouchFocusModule;



    protected void init() {

    }


    public void addModule(@NonNull Module module) {
        mModules.add(module);
        module.setParent(this);
    }

    public void addModule(@NonNull Module module, int left, int top, int right, int bottom) {
        mModules.add(module);
        module.setParent(this);
        module.setBounds(left, top, right, bottom);
    }

    public void clearModules() {
        for (Module module : mModules) {
            module.setParent(null);
        }
        mModules.clear();
    }

    public void removeModule(Module module) {
        if (module != null)
            mModules.remove(module);
    }

    public Module removeModule(int position) {
        if (position >= 0 && position < mModules.size()) {
            Module module = mModules.remove(position);
            module.setParent(null);
            return module;
        }
        return null;
    }

    public List<Module> getModules() {
        return mModules;
    }

    public int getModulesCount() {
        return mModules.size();
    }

    public Module getModule(int position) {
        if (position >= 0 && position < mModules.size())
            return mModules.get(position);
        return null;
    }


    public void setSize(int width, int height) {
        ViewGroup.LayoutParams lParams = getLayoutParams();
        if (lParams == null)
            lParams = new ViewGroup.LayoutParams(width, height);
        else {
            lParams.width = width;
            lParams.height = height;
        }
        setLayoutParams(lParams);
    }


    public OnMeasureListener getOnMeasureListener() {
        return mOnMeasureListener;
    }

    public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
        mOnMeasureListener = onMeasureListener;
    }

    public OnLayoutListener getOnLayoutListener() {
        return mOnLayoutListener;
    }

    public void setOnLayoutListener(OnLayoutListener onLayoutListener) {
        mOnLayoutListener = onLayoutListener;
    }

    public void notifyModulesUpdate() {
        for (Module module : mModules) {
            module.configModule();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mOnMeasureListener != null)
            mOnMeasureListener.onMeasure(this, widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int width = right - left;
        int height = bottom - top;
        //notify listener & config modules
        if (mOnLayoutListener != null)
            mOnLayoutListener.onLayout(this, changed, width, height);
//
//        for (Module module : mModules) {
//            if (!module.isIgnoreConfigFromParent())
//                module.configModule(changed);
//        }
    }

    public void setMeasureDimension(int width, int height) {
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Module module : mModules) {
            module.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handle = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                for (Module module : mModules) {
                    if (checkEventRegion(module, event)) {
                        handle = module.onTouchEvent(event);
                        if (handle) {
                            mTouchFocusModule = module;
                            break;
                        }
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                if (mTouchFocusModule != null) {
                    if (checkEventRegion(mTouchFocusModule, event))
                        handle = mTouchFocusModule.onTouchEvent(event);
                    mTouchFocusModule = null;
                }

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (mTouchFocusModule != null) {
                    if (checkEventRegion(mTouchFocusModule, event))
                        handle = mTouchFocusModule.onTouchEvent(event);
                    else {
                        MotionEvent eventCancel = MotionEvent.obtain(event);
                        eventCancel.setAction(MotionEvent.ACTION_CANCEL);
                        handle = mTouchFocusModule.onTouchEvent(eventCancel);
                        mTouchFocusModule = null;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                if (mTouchFocusModule != null) {
                    handle = mTouchFocusModule.onTouchEvent(event);
                    mTouchFocusModule = null;
                }
                break;
            }
        }
        return handle || super.onTouchEvent(event);
    }

    /**
     * @param module
     * @param event
     * @return true if event occuren in module's bounds
     */

    private boolean checkEventRegion(Module module, MotionEvent event) {
        int x = (int) (event.getX() - getX());
        int y = (int) (event.getY() - getX());

        return x < module.getRight() && x > module.getLeft()
                && y > module.getTop() && y < module.getBottom();
    }
}
