package vn.com.vng.modulesview.modules_view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by HungNQ on 08/09/2017.
 */

public class ModulesView extends View {
    public ModulesView(Context context) {
        super(context);
    }

    public ModulesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ModulesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private List<Module> mModules = new LinkedList<>();

    public void addModule(@NonNull Module module) {
        mModules.add(module);
        module.setParent(this);
    }

    public void addModule(@NonNull Module module, int left, int top, int right, int bottom) {
        module.setBounds(left, top, right, bottom);
        mModules.add(module);
        module.setParent(this);
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


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = right - left;
        int height = bottom - top;
        for (Module module : mModules) {
            if (module.getWhenParentLayout() != null)
                module.getWhenParentLayout().whenParentLayout(this, changed, width, height);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (Module module : mModules) {
            if (module.getWhenParentMeasure() != null)
                module.getWhenParentMeasure().whenParentMeasure(this, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Module module : mModules) {
            module.draw(canvas);
        }
    }


}
