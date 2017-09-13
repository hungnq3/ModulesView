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
        Bitmap ava = BitmapFactory.decodeResource(getResources(),R.drawable.img);

        List<SocialModel> items= new ArrayList<>(20);
        items.add(mockModel(ava,"Cú vọ","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 5, 0, ava, ava, ava));
        items.add(mockModel(ava,"Cú xám","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 5, 0, ava, ava, ava));
        items.add(mockModel(ava,"Cú cu","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 5, 0, ava, ava, ava));
        items.add(mockModel(ava,"Cú cụ","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 5, 0, ava, ava, ava));
        items.add(mockModel(ava,"Cú con","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 5, 0, ava, ava, ava));
        items.add(mockModel(ava,"Cú cha","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 5, 0, ava, ava, ava));
        items.add(mockModel(ava,"Cú vợ","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 5, 0, ava, ava, ava));
        items.add(mockModel(ava,"Cú chồng","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 5, 0, ava, ava, ava));
        items.add(mockModel(ava,"Cú cháu","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 5, 0, ava, ava, ava));
        items.add(mockModel(ava,"Cú chắc","12:07 Hôm qua", "Cả cả cả mọi người trên thể giới ra đây mà xem, ra mà xem, ra mà xem, ra hết đây mà xem đê!!!", 5, 0, ava, ava, ava));
        return items;
    }

    private SocialModel mockModel(Bitmap avatar, String name, String time, String content, int likeCount, int commentCount, Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3) {
        SocialModel model = new SocialModel();
        model.setAvatar(avatar);
        model.setName(name);
        model.setTime(time);
        model.setContent(content);
        model.setImages(Arrays.asList(bitmap1, bitmap2, bitmap3));
        model.setLikeCount(likeCount);
        model.setCommentCount(commentCount);
        return model;
    }

}
