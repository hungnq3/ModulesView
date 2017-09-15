package vn.com.vng.modulesview.sample2.adapter.view_item;

import vn.com.vng.modulesview.sample.model.SocialModel;
import vn.com.vng.modulesview.sample2.adapter.ViewType;

/**
 * Created by HungNQ on 15/09/2017.
 */

public class SocialImageContextViewItem extends BaseViewItem {

    private SocialModel mSocialModel;

    public SocialImageContextViewItem(SocialModel socialModel) {
        mSocialModel = socialModel;
    }


    @Override
    public int getViewType() {
        return Math.min(getImagesCount() + ViewType.SOCIAL_IMAGES_CONTENT_0, ViewType.SOCIAL_IMAGES_CONTENT_9);
    }

    public SocialModel getSocialModel() {
        return mSocialModel;
    }

    public void setSocialModel(SocialModel socialModel) {
        mSocialModel = socialModel;
    }

    public int getImagesCount(){
        return (mSocialModel != null  && mSocialModel.getImages() != null) ? mSocialModel.getImages().size() : 0;
    }
}
