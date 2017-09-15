package vn.com.vng.modulesview.sample2.adapter.holder;

import android.view.View;
import android.widget.Toast;

import vn.com.vng.modulesview.modules_view.Module;
import vn.com.vng.modulesview.sample2.adapter.view_item.BaseViewItem;
import vn.com.vng.modulesview.sample2.adapter.view_item.SocialTextContentViewItem;
import vn.com.vng.modulesview.sample2.social_view.SocialContentTextView;

/**
 * Created by HungNQ on 15/09/2017.
 */

public class SocialTextContentViewHolder extends BaseViewHolder {
    SocialContentTextView mSocialContentTextView;

    public SocialTextContentViewHolder(SocialContentTextView itemView) {
        super(itemView);
        mSocialContentTextView = itemView;
        init();
    }

    private void init() {
        mSocialContentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Click content", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onBind(BaseViewItem item) {
        super.onBind(item);
        if (item instanceof SocialTextContentViewItem)
            mSocialContentTextView.bindModel(((SocialTextContentViewItem) item).getSocialModel());
    }
}
