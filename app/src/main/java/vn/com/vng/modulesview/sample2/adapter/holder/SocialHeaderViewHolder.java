package vn.com.vng.modulesview.sample2.adapter.holder;

import android.view.View;
import android.widget.Toast;

import vn.com.vng.modulesview.modules_view.Module;
import vn.com.vng.modulesview.sample2.adapter.view_item.BaseViewItem;
import vn.com.vng.modulesview.sample2.adapter.view_item.SocialHeaderViewItem;
import vn.com.vng.modulesview.sample2.social_view.SocialHeaderView;

/**
 * Created by HungNQ on 15/09/2017.
 */

public class SocialHeaderViewHolder extends BaseViewHolder {
    SocialHeaderView mSocialHeaderView;
    public SocialHeaderViewHolder(SocialHeaderView itemView) {
        super(itemView);
        mSocialHeaderView = itemView;
        init();
    }

    private void init() {
        mSocialHeaderView.setOnAvaClickListener(new Module.OnClickListener() {
            @Override
            public void onClick(Module module) {
                Toast.makeText(module.getContext(), "Click ava", Toast.LENGTH_SHORT).show();

            }
        });
        mSocialHeaderView.setOnNameClickListener(new Module.OnClickListener() {
            @Override
            public void onClick(Module module) {
                Toast.makeText(module.getContext(), "Click name", Toast.LENGTH_SHORT).show();

            }
        });
        mSocialHeaderView.setOnTimeClickListener(new Module.OnClickListener() {
            @Override
            public void onClick(Module module) {
                Toast.makeText(module.getContext(), "Click time", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBind(BaseViewItem item) {
        super.onBind(item);
        if(item instanceof SocialHeaderViewItem)
            mSocialHeaderView.bindHeader(((SocialHeaderViewItem) item).getSocialModel());
    }
}
