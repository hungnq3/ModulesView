package vn.com.vng.modulesview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import vn.com.vng.modulesview.sample.model.SocialModel;
import vn.com.vng.modulesview.sample2.adapter.ModulesViewAdapter;
import vn.com.vng.modulesview.sample2.adapter.view_item.BaseViewItem;
import vn.com.vng.modulesview.sample2.adapter.view_item.SocialFooterViewItem;
import vn.com.vng.modulesview.sample2.adapter.view_item.SocialHeaderViewItem;
import vn.com.vng.modulesview.sample2.adapter.view_item.SocialImageContextViewItem;
import vn.com.vng.modulesview.sample2.adapter.view_item.SocialTextContentViewItem;

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

    private List<BaseViewItem> buildItems() {
        Bitmap img1 = BitmapFactory.decodeResource(getResources(), R.drawable.img);
        Bitmap img2 = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
        Bitmap img3 = BitmapFactory.decodeResource(getResources(), R.drawable.img3);
        Bitmap img4 = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
        Bitmap img5 = BitmapFactory.decodeResource(getResources(), R.drawable.img5);
        Bitmap img6 = BitmapFactory.decodeResource(getResources(), R.drawable.img6);
        Bitmap img7 = BitmapFactory.decodeResource(getResources(), R.drawable.img7);
        Bitmap img8 = BitmapFactory.decodeResource(getResources(), R.drawable.img8);
        Bitmap img9 = BitmapFactory.decodeResource(getResources(), R.drawable.img9);
        Bitmap img10 = BitmapFactory.decodeResource(getResources(), R.drawable.img10);

        List<BaseViewItem> items = new ArrayList<>(40);
        items.addAll(mockModel(img10, "Thổ dân", "12:07 Hôm nay", "Hello \n... \n... \n... \nmotor", 1022, 6233));
        items.addAll(mockModel(img1, "Cú vọ", "12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem", 5, 0, img1, img2));
        items.addAll(mockModel(img2, "Cú xám", "11:08 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 1, img1, img4, img2));
        items.addAll(mockModel(img3, "Cú cu", "10:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 5, 12));
        items.addAll(mockModel(img4, "Cú cụ", "9:00 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra hết đây mà xem đê ra hết đây mà xem đê!!!", 8, 0, img1, img3, img4));
        items.addAll(mockModel(img2, "Cú con", "08:07 Hôm qua", null, 60, 24, img1));
        items.addAll(mockModel(img8, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img4, img4, img5, img6, img7, img8));
        items.addAll(mockModel(img9, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img4, img4, img5, img6, img7, img8, img9));
        items.addAll(mockModel(img1, "Cú cha", "08:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 23, img4, img2));
        items.addAll(mockModel(img1, "Cú vợ", "08:01 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 2, 0, img2, img3, img1));
        items.addAll(mockModel(img2, "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, img1, img2, img4, img3));
        items.addAll(mockModel(img5, "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, img1, img2, img4, img3, img5));
        items.addAll(mockModel(img7, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img3, img4, img5, img6, img7));
        items.addAll(mockModel(img4, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img2));
        items.addAll(mockModel(img3, "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img2, img1));
        items.addAll(mockModel(img3, "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img2, img1, img4, img5, img6));
        items.addAll(mockModel(img3, "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img3, img3, img3, img3, img3));


        //double
        items.addAll(mockModel(img1, "Cú vọ", "12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem", 5, 0, img1, img2));
        items.addAll(mockModel(img2, "Cú xám", "11:08 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 1, img1, img4, img2));
        items.addAll(mockModel(img3, "Cú cu", "10:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 5, 12));
        items.addAll(mockModel(img4, "Cú cụ", "9:00 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra hết đây mà xem đê ra hết đây mà xem đê!!!", 8, 0, img1, img3, img4));
        items.addAll(mockModel(img2, "Cú con", "08:07 Hôm qua", null, 60, 24, img1));
        items.addAll(mockModel(img8, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img4, img4, img5, img6, img7, img8));
        items.addAll(mockModel(img9, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img4, img4, img5, img6, img7, img8, img9));
        items.addAll(mockModel(img1, "Cú cha", "08:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 23, img4, img2));
        items.addAll(mockModel(img1, "Cú vợ", "08:01 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 2, 0, img2, img3, img1));
        items.addAll(mockModel(img2, "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, img1, img2, img4, img3));
        items.addAll(mockModel(img5, "Cú chồng", "07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, img1, img2, img4, img3, img5));
        items.addAll(mockModel(img7, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img1, img2, img3, img4, img5, img6, img7));
        items.addAll(mockModel(img4, "Cú cháu", "07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42, img2));
        items.addAll(mockModel(img3, "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img2, img1));
        items.addAll(mockModel(img3, "Cú chắc", "06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img2, img1, img4, img5, img6));


        items.addAll(mockModel(img1, "Cú vọ", "12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem", 5, 0, img1, img1, img1, img1, img1, img1));

        return items;
    }

    private List<BaseViewItem> mockModel(Bitmap avatar, String name, String time, String content, int likeCount, int commentCount, Bitmap... bitmap) {
        List<BaseViewItem> items = new LinkedList<>();

        SocialModel model = new SocialModel();
        model.setAvatar(avatar);
        model.setName(name);
        model.setTime(time);
        model.setContent(content);
        model.setImages(Arrays.asList(bitmap));
        model.setLikeCount(likeCount);
        model.setCommentCount(commentCount);

        items.add(new SocialHeaderViewItem(model));
        items.add(new SocialTextContentViewItem(model));
        if (model.getImages().size() > 0)
            items.add(new SocialImageContextViewItem(model));
        items.add(new SocialFooterViewItem(model));

        return items;
    }

}
