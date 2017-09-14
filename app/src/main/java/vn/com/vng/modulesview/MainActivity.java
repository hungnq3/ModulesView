package vn.com.vng.modulesview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.com.vng.modulesview.sample.adapter.ModulesAdapter;
import vn.com.vng.modulesview.sample.model.SocialModel;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ModulesAdapter mModulesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        setupRecycler();

    }

    private void setupRecycler() {
        mModulesAdapter = new ModulesAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mModulesAdapter);
        mModulesAdapter.setItems(buildItems());

    }

    private List<SocialModel> buildItems() {
        Bitmap img1 = BitmapFactory.decodeResource(getResources(),R.drawable.img);
        Bitmap img2 = BitmapFactory.decodeResource(getResources(),R.drawable.img2);
        Bitmap img3 = BitmapFactory.decodeResource(getResources(),R.drawable.img3);
        Bitmap img4 = BitmapFactory.decodeResource(getResources(),R.drawable.img4);
        Bitmap img5 = BitmapFactory.decodeResource(getResources(),R.drawable.img5);
        Bitmap img6 = BitmapFactory.decodeResource(getResources(),R.drawable.img6);
        Bitmap img7 = BitmapFactory.decodeResource(getResources(),R.drawable.img7);
        Bitmap img8 = BitmapFactory.decodeResource(getResources(),R.drawable.img8);
        Bitmap img9 = BitmapFactory.decodeResource(getResources(),R.drawable.img9);

        List<SocialModel> items= new ArrayList<>(20);
        items.add(mockModel(img1,"Cú vọ","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem", 5, 0, img1, img2));
        items.add(mockModel(img2,"Cú xám","11:08 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 1, img1, img4, img2));
        items.add(mockModel(img3,"Cú cu","10:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 5, 12));
        items.add(mockModel(img4,"Cú cụ","9:00 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra hết đây mà xem đê ra hết đây mà xem đê!!!", 8, 0, img1, img3, img4));
        items.add(mockModel(img2,"Cú con","08:07 Hôm qua", null, 60, 24, img1));
        items.add(mockModel(img8,"Cú cháu","07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42,img1,img2,img4,img4,img5,img6,img7,img8));
        items.add(mockModel(img9,"Cú cháu","07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42,img1,img2,img4,img4,img5,img6,img7,img8,img9));
        items.add(mockModel(img1,"Cú cha","08:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 23,  img4, img2));
        items.add(mockModel(img1,"Cú vợ","08:01 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây!!!", 2, 0, img2, img3, img1));
        items.add(mockModel(img2,"Cú chồng","07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, img1, img2, img4, img3));
        items.add(mockModel(img5,"Cú chồng","07:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 123, 122, img1, img2, img4, img3, img5));
        items.add(mockModel(img7,"Cú cháu","07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42,img1, img2, img3, img4, img5, img6, img7));
        items.add(mockModel(img4,"Cú cháu","07:02 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 15, 42,img2));
        items.add(mockModel(img3,"Cú chắc","06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img2, img1));
        items.add(mockModel(img3,"Cú chắc","06:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 12, 26, img3, img2, img1, img4, img5, img6));
        return items;
    }

    private SocialModel mockModel(Bitmap avatar, String name, String time, String content, int likeCount, int commentCount, Bitmap... bitmap) {
        SocialModel model = new SocialModel();
        model.setAvatar(avatar);
        model.setName(name);
        model.setTime(time);
        model.setContent(content);
        model.setImages(Arrays.asList(bitmap));
        model.setLikeCount(likeCount);
        model.setCommentCount(commentCount);
        return model;
    }

}
