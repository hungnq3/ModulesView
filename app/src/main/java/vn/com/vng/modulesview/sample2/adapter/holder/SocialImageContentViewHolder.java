package vn.com.vng.modulesview.sample2.adapter.holder;

import android.view.View;
import android.widget.Toast;

import vn.com.vng.modulesview.modules_view.Module;
import vn.com.vng.modulesview.sample2.adapter.view_item.BaseViewItem;
import vn.com.vng.modulesview.sample2.adapter.view_item.SocialImageContextViewItem;
import vn.com.vng.modulesview.sample2.social_view.SocialImageContentView;

/**
 * Created by HungNQ on 15/09/2017.
 */

public class SocialImageContentViewHolder extends BaseViewHolder {
    SocialImageContentView mSocialImageContentView;
    public SocialImageContentViewHolder(SocialImageContentView itemView) {
        super(itemView);
        mSocialImageContentView = itemView;
        init();

    }

    private void init() {
        mSocialImageContentView.setOnImageClickListener(new Module.OnClickListener() {
            @Override
            public void onClick(Module module) {
                Toast.makeText(module.getContext(), "Click img", Toast.LENGTH_SHORT).show();
            }
        });
        mSocialImageContentView.setOnImageLongClickListener(new Module.OnLongClickListener() {
            @Override
            public void onLongClick(Module module) {
                Toast.makeText(module.getContext(), "Long click img", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBind(BaseViewItem item) {
        super.onBind(item);
        if(item instanceof SocialImageContextViewItem)
            mSocialImageContentView.bindModel(((SocialImageContextViewItem) item).getSocialModel());
    }
}
