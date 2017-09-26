package vn.com.vng.modulesview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import vn.com.vng.modulesview.sample.model.SocialModel;
import vn.com.vng.modulesview.sample.adapter.ModulesViewAdapter;
import vn.com.vng.modulesview.sample.adapter.view_item.BaseViewItem;
import vn.com.vng.modulesview.sample.adapter.view_item.SocialFooterViewItem;
import vn.com.vng.modulesview.sample.adapter.view_item.SocialHeaderViewItem;
import vn.com.vng.modulesview.sample.adapter.view_item.SocialImageContentViewItem;
import vn.com.vng.modulesview.sample.adapter.view_item.SocialTextContentViewItem;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ModulesViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        setupRecycler();

//        getWindow().setBackgroundDrawable(null);


    }

    private void setupRecycler() {
        mAdapter = new ModulesViewAdapter(buildItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

    }

    String[] IMGS = new String[]{
            "https://www.smashingmagazine.com/wp-content/uploads/2015/06/10-dithering-opt.jpg",
            "https://www.w3schools.com/css/img_fjords.jpg",
            "https://3.bp.blogspot.com/-W__wiaHUjwI/Vt3Grd8df0I/AAAAAAAAA78/7xqUNj8ujtY/s1600/image02.png",
            "https://cdn.athemes.com/wp-content/uploads/Original-JPG-Image.jpg",
            "https://i.sharefa.st/1295569823374302192636.jpg",
            "http://iforo.3djuegos.com/files_foros/89/894.jpg",
            "http://www.ikea.com/gb/en/images/products/pj%C3%A4tteryd-picture-silver-deer__0455534_pe603586_s4.jpg",
            "http://cdn1-www.dogtime.com/assets/uploads/gallery/bull-terier-dog-breed-pictures/6-siderun.jpg",
            "https://cdn.pixabay.com/photo/2016/10/27/16/58/full-moon-1775764_640.jpg",
            "http://southafrica.worldswimsuit.com/images/made/images/uploads/website_images/195/sports_12_ler_21_56239-v1.web_1360_907_c1.jpg"
    };


    private String getRandomImg(){
        return IMGS[new Random().nextInt(IMGS.length)];
    }

    private List<String> getRandomImgs(){
        List<String> imgs = new LinkedList<>();
        int size = new Random().nextInt(IMGS.length);
        for(int i=0; i<size; ++i)
            imgs.add(getRandomImg());
        return imgs;
    }

    private List<BaseViewItem> buildItems() {

        List<BaseViewItem> items = new ArrayList<>(40);
        items.addAll(mockModel(getRandomImg(), "Thổ dân", "12:07 Hôm nay", "Hello \n... \n... \n... \nmotor", 1022, 6233, null));
        items.addAll(mockModel(getRandomImg(), "Cú vọ", "12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem", 5, 0, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú xám", "11:08 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 1, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cu", "10:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 5, 12, null));
        items.addAll(mockModel(getRandomImg(), "Cú cụ", "9:00 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra hết đây mà xem đê ra hết đây mà xem đê!!!", 8, 0, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú con", "08:07 Hôm qua", null, 60, 24, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cha", "08:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 23, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú vợ", "08:01 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 2, 0, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!",13,5, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!",21,0, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!",22,2, getRandomImgs()));


        //double
        items.addAll(mockModel(getRandomImg(), "Cú vọ", "12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem", 5, 0, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú xám", "11:08 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 1, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cu", "10:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 5, 12, null));
        items.addAll(mockModel(getRandomImg(), "Cú cụ", "9:00 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra hết đây mà xem đê ra hết đây mà xem đê!!!", 8, 0, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú con", "08:07 Hôm qua", null, 60, 24, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cha", "08:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 23, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú vợ", "08:01 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 2, 0, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, getRandomImgs()));
        items.addAll(mockModel(getRandomImg(), "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, getRandomImgs()));


        items.addAll(mockModel(getRandomImg(), "Cú vọ", "12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem", 5, 0, getRandomImgs()));

        return items;
    }

    private List<BaseViewItem> mockModel(String avatar, String name, String time, String content, int likeCount, int commentCount, List<String> imgUrls) {
        List<BaseViewItem> items = new LinkedList<>();

        SocialModel model = new SocialModel();
        model.setAvatar(avatar);
        model.setName(name);
        model.setTime(time);
        model.setContent(content);
        model.setImages(imgUrls);
        model.setLikeCount(likeCount);
        model.setCommentCount(commentCount);

        items.add(new SocialHeaderViewItem(model));
        if (!TextUtils.isEmpty(content))
            items.add(new SocialTextContentViewItem(model));
        if (imgUrls != null && imgUrls.size() > 0)
            items.add(new SocialImageContentViewItem(model));
        items.add(new SocialFooterViewItem(model));
        return items;
    }


    //    private List<BaseViewItem> buildItems() {
//        Bitmap img1 = BitmapFactory.decodeResource(getResources(), R.drawable.img);
//        Bitmap img2 = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
//        Bitmap img3 = BitmapFactory.decodeResource(getResources(), R.drawable.img3);
//        Bitmap img4 = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
//        Bitmap img5 = BitmapFactory.decodeResource(getResources(), R.drawable.img5);
//        Bitmap img6 = BitmapFactory.decodeResource(getResources(), R.drawable.img6);
//        Bitmap img7 = BitmapFactory.decodeResource(getResources(), R.drawable.img7);
//        Bitmap img8 = BitmapFactory.decodeResource(getResources(), R.drawable.img8);
//        Bitmap img9 = BitmapFactory.decodeResource(getResources(), R.drawable.img9);
//        Bitmap img10 = BitmapFactory.decodeResource(getResources(), R.drawable.img10);
//
//        List<BaseViewItem> items = new ArrayList<>(40);
//        items.addAll(mockModel(img10, "Thổ dân", "12:07 Hôm nay", "Hello \n... \n... \n... \nmotor", 1022, 6233));
//        items.addAll(mockModel(img1, "Cú vọ", "12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem", 5, 0, img1, img2));
//        items.addAll(mockModel(img2, "Cú xám", "11:08 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 1, img1, img4, img2));
//        items.addAll(mockModel(img3, "Cú cu", "10:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 5, 12));
//        items.addAll(mockModel(img4, "Cú cụ", "9:00 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra hết đây mà xem đê ra hết đây mà xem đê!!!", 8, 0, img1, img3, img4));
//        items.addAll(mockModel(img2, "Cú con", "08:07 Hôm qua", null, 60, 24, img1));
//        items.addAll(mockModel(img8, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img4, img4, img5, img6, img7, img8));
//        items.addAll(mockModel(img9, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img4, img4, img5, img6, img7, img8, img9));
//        items.addAll(mockModel(img1, "Cú cha", "08:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 23, img4, img2));
//        items.addAll(mockModel(img1, "Cú vợ", "08:01 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 2, 0, img2, img3, img1));
//        items.addAll(mockModel(img2, "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, img1, img2, img4, img3));
//        items.addAll(mockModel(img5, "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, img1, img2, img4, img3, img5));
//        items.addAll(mockModel(img7, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img3, img4, img5, img6, img7));
//        items.addAll(mockModel(img4, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img2));
//        items.addAll(mockModel(img3, "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img2, img1));
//        items.addAll(mockModel(img3, "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img2, img1, img4, img5, img6));
//        items.addAll(mockModel(img3, "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img3, img3, img3, img3, img3));
//
//
//        //double
//        items.addAll(mockModel(img1, "Cú vọ", "12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem", 5, 0, img1, img2));
//        items.addAll(mockModel(img2, "Cú xám", "11:08 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 1, img1, img4, img2));
//        items.addAll(mockModel(img3, "Cú cu", "10:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 5, 12));
//        items.addAll(mockModel(img4, "Cú cụ", "9:00 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra hết đây mà xem đê ra hết đây mà xem đê!!!", 8, 0, img1, img3, img4));
//        items.addAll(mockModel(img2, "Cú con", "08:07 Hôm qua", null, 60, 24, img1));
//        items.addAll(mockModel(img8, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img4, img4, img5, img6, img7, img8));
//        items.addAll(mockModel(img9, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img4, img4, img5, img6, img7, img8, img9));
//        items.addAll(mockModel(img1, "Cú cha", "08:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 23, img4, img2));
//        items.addAll(mockModel(img1, "Cú vợ", "08:01 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 2, 0, img2, img3, img1));
//        items.addAll(mockModel(img2, "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, img1, img2, img4, img3));
//        items.addAll(mockModel(img5, "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, img1, img2, img4, img3, img5));
//        items.addAll(mockModel(img7, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img3, img4, img5, img6, img7));
//        items.addAll(mockModel(img4, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img2));
//        items.addAll(mockModel(img3, "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img2, img1));
//        items.addAll(mockModel(img3, "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img2, img1, img4, img5, img6));
//
//
//        items.addAll(mockModel(img1, "Cú vọ", "12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem", 5, 0, img1, img1, img1, img1, img1, img1));
//
//        return items;
//    }


    //    private List<BaseViewItem> mockModel(Bitmap avatar, String name, String time, String content, int likeCount, int commentCount, Bitmap... bitmap) {
//        List<BaseViewItem> items = new LinkedList<>();
//
//        SocialModel model = new SocialModel();
//        model.setAvatar(avatar);
//        model.setName(name);
//        model.setTime(time);
//        model.setContent(content);
//        model.setImages(Arrays.asList(bitmap));
//        model.setLikeCount(likeCount);
//        model.setCommentCount(commentCount);
//
//        items.add(new SocialHeaderViewItem(model));
//        items.add(new SocialTextContentViewItem(model));
//        if (model.getImages().size() > 0)
//            items.add(new SocialImageContentViewItem(model));
//        items.add(new SocialFooterViewItem(model));
//
//        return items;
//    }

}
