package vn.com.vng.modulesview.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import vn.com.vng.modulesview.sample.model.SocialModel;
import vn.com.vng.modulesview.sample.views.SocialModulesView;

/**
 * Created by HungNQ on 12/09/2017.
 */

public class ModulesAdapter extends RecyclerView.Adapter<ModulesViewHolder> {

    private List<SocialModel> mItems;


    @Override
    public ModulesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ModulesViewHolder holder = null;
//        holder = new ModulesViewHolder(DemoViewFactory.buildDemoView(parent.getContext()));
        holder = new ModulesViewHolder(new SocialModulesView(parent.getContext()));
        return holder;
    }

    @Override
    public void onBindViewHolder(ModulesViewHolder holder, int position) {
        holder.binData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public List<SocialModel> getItems() {
        return mItems;
    }

    public void setItems(List<SocialModel> items) {
        mItems = items;
    }
}
