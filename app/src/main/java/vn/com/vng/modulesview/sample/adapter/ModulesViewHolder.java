package vn.com.vng.modulesview.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import vn.com.vng.modulesview.modules_view.ModulesView;
import vn.com.vng.modulesview.sample.model.SocialModel;
import vn.com.vng.modulesview.sample.views.SocialModulesView;

/**
 * Created by HungNQ on 12/09/2017.
 */

public class ModulesViewHolder extends RecyclerView.ViewHolder {

    SocialModulesView mModulesView;
    SocialModel mModel;

    public ModulesViewHolder(SocialModulesView itemView) {
        super(itemView);
        mModulesView = itemView;
    }

    public void binData(SocialModel model) {
        mModel = model;
        mModulesView.setModel(mModel);
    }
}
