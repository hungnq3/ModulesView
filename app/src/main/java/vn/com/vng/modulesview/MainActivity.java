package vn.com.vng.modulesview;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import vn.com.vng.modulesview.modules_view.ImageModule;
import vn.com.vng.modulesview.modules_view.Module;
import vn.com.vng.modulesview.modules_view.ModulesView;
import vn.com.vng.modulesview.modules_view.TextModule;

public class MainActivity extends AppCompatActivity {

    ViewGroup mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootLayout = (ViewGroup) findViewById(R.id.root_layout);

        ModulesView view = buildModulesView();
        mRootLayout.addView(view);

    }


    ModulesView buildModulesView(){
        ModulesView view= new ModulesView(this);
        view.setSize(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        view.setBackgroundColor(0xffdddddd);

        //create image module
        ImageModule imgModule = new ImageModule();
        imgModule.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.img ));
        imgModule.setScaleType(ImageModule.ScaleType.CENTER_CROP);
        imgModule.setRoundCorner(ImageModule.ROUND_CIRCLE);


        //create text module
        final TextModule textModule = new TextModule();
        textModule.setTextSize(sp(16));
        textModule.setText("Hello world world world world world world world world  ");


        view.addModule(imgModule, 20, 20, 280,280);
        view.addModule(textModule);

        //set bound on runtime
        view.setOnMeasureListener(new ModulesView.OnMeasureListener() {
            @Override
            public void onMeasure(ModulesView view, int withMeasureSpec, int heightMeasureSpec) {
                int width = View.MeasureSpec.getSize(view.getMeasuredWidth());
                textModule.setBounds(320,20, width-20, Module.BOUND_UNKNOWN);
            }
         });

        return view;
    }

    private int sp(int sp){
        return (int) (getResources().getDisplayMetrics().scaledDensity * sp);
    }

    private int dp(int dp){
        return (int) (getResources().getDisplayMetrics().density * dp);
    }



}
