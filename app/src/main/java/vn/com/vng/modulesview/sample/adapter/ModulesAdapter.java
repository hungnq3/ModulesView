package vn.com.vng.modulesview.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import vn.com.vng.modulesview.sample.model.SocialModel;
import vn.com.vng.modulesview.sample.views.Social1ImgModulesView;
import vn.com.vng.modulesview.sample.views.Social2ImgModulesView;
import vn.com.vng.modulesview.sample.views.Social3ImgModulesView;
import vn.com.vng.modulesview.sample.views.Social4ImgModulesView;
import vn.com.vng.modulesview.sample.views.Social5ImgModulesView;
import vn.com.vng.modulesview.sample.views.Social6ImgModulesView;
import vn.com.vng.modulesview.sample.views.Social7ImgModulesView;
import vn.com.vng.modulesview.sample.views.Social8ImgModulesView;
import vn.com.vng.modulesview.sample.views.Social9ImgModulesView;
import vn.com.vng.modulesview.sample.views.SocialModulesView;

/**
 * Created by HungNQ on 12/09/2017.
 */

public class ModulesAdapter extends RecyclerView.Adapter<ModulesViewHolder> {

    private List<SocialModel> mItems;

    @Override
    public ModulesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ModulesViewHolder holder = null;
        switch (viewType){
            case ViewType.SOCIAL_ITEM_NO_IMG:
                holder = new ModulesViewHolder(new SocialModulesView(parent.getContext()));
                break;
            case ViewType.SOCIAL_ITEM_1_IMG:
                holder = new ModulesViewHolder(new Social1ImgModulesView(parent.getContext()));
                break;
            case ViewType.SOCIAL_ITEM_2_IMG:
                holder = new ModulesViewHolder(new Social2ImgModulesView(parent.getContext()));
                break;
            case ViewType.SOCIAL_ITEM_3_IMG:
                holder = new ModulesViewHolder(new Social3ImgModulesView(parent.getContext()));
                break;
            case ViewType.SOCIAL_ITEM_4_IMG:
                holder = new ModulesViewHolder(new Social4ImgModulesView(parent.getContext()));
                break;
            case ViewType.SOCIAL_ITEM_5_IMG:
                holder = new ModulesViewHolder(new Social5ImgModulesView(parent.getContext()));
                break;
            case ViewType.SOCIAL_ITEM_6_IMG:
                holder = new ModulesViewHolder(new Social6ImgModulesView(parent.getContext()));
                break;
            case ViewType.SOCIAL_ITEM_7_IMG:
                holder = new ModulesViewHolder(new Social7ImgModulesView(parent.getContext()));
                break;
            case ViewType.SOCIAL_ITEM_8_IMG:
                holder = new ModulesViewHolder(new Social8ImgModulesView(parent.getContext()));
                break;
            case ViewType.SOCIAL_ITEM_9_IMG:
                holder = new ModulesViewHolder(new Social9ImgModulesView(parent.getContext()));
                break;
        }
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

    @Override
    public int getItemViewType(int position) {
        SocialModel model = mItems.get(position);
        if(model != null) {
            int imgCount  = model.getImages() != null ? model.getImages().size() : 0;
            return Math.min(imgCount + ViewType.SOCIAL_ITEM_NO_IMG, ViewType.SOCIAL_ITEM_9_IMG);
        }
        return super.getItemViewType(position);
    }
}
