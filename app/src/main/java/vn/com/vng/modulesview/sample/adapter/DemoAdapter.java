package vn.com.vng.modulesview.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import vn.com.vng.modulesview.sample.DemoViewFactory;

/**
 * Created by HungNQ on 12/09/2017.
 */

public class DemoAdapter extends RecyclerView.Adapter<DemoViewHolder> {

    @Override
    public DemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DemoViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new DemoViewHolder(DemoViewFactory.buildDemoView(parent.getContext()));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(DemoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 40;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 1;
    }
}
