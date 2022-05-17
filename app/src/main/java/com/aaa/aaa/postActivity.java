package com.aaa.aaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaa.aaa.adpater.commentListViewAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.aaa.aaa.Util.isStorageUrl;

public class postActivity extends AppCompatActivity {
    private FirebaseFirestore database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private commentListViewAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView postImageView;
    private Date comment_time;
    private String comment_content;
    private String comment_id;
    private String comment_uid;
    private String post_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpage);
        findViewById(R.id.commentWriteButton).setOnClickListener(onClickListener);
        String postKey = (String) getIntent().getSerializableExtra("postpostKey");
        String category = (String) getIntent().getSerializableExtra("postCategory");
        Toolbar tb = (Toolbar) findViewById(R.id.write_toolbar);
        setSupportActionBar(tb);//액션바를 툴바로 바꿔줌
        getSupportActionBar().setTitle(category);
        Log.e("로그", "url:" + postKey);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = (String) getIntent().getSerializableExtra("postTitle");
        TextView titleTextView = findViewById(R.id.postTitleTextView);
        titleTextView.setText(title);

        Date created = (Date) getIntent().getSerializableExtra("postCreated");
        TextView TimeTextView = findViewById(R.id.postCreatedTextView);
        TimeTextView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(created));

        String uid = (String) getIntent().getSerializableExtra("postUid");
        TextView uidTextView = findViewById(R.id.postUserTextView);

        postImageView=(ImageView)findViewById(R.id.postImageView);
        database = FirebaseFirestore.getInstance();
        //FireStore에서 게시글 정보 받아오기
        database.collection("user")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                uidTextView.setText(document.getData().get("name").toString());
                                String url = document.getData().get("profile_pic").toString();
                                Uri file = Uri.parse(url);
                                Glide.with(postActivity.this).load(url).centerCrop().override(500).into(postImageView);
                            }
                        } else {

                        }
                    }
                });
        LinearLayout contentsLayout = findViewById(R.id.postcontentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> contentsList = (ArrayList<String>) getIntent().getSerializableExtra("postContent");

        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(contentsList)) {
            contentsLayout.setTag(contentsList);
            contentsLayout.removeAllViews();
            for (int i = 0; i < contentsList.size(); i++) {
                String contents = contentsList.get(i);
                if (isStorageUrl(contents)) {
                    ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setPadding(0,0,0,30);
                    contentsLayout.addView(imageView);
                    Glide.with(this).load(contents).override(1000).thumbnail(0.1f).into(imageView);
                } else {
                    TextView textView = new TextView(this);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(contents);
                    textView.setTextSize(18);
                    textView.setPadding(0,0,0,30);
                    textView.setTextColor(Color.rgb(0, 0, 0));
                    contentsLayout.addView(textView);
                }
            }
        }

        /** 리사이클러 뷰(게시글 리스트) 생성**/
        final ArrayList<commentInfo> commentList=new ArrayList<>();
        database= FirebaseFirestore.getInstance();

        //FireStore에서 게시글 정보 받아오기
        database.collection("comment")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("post_id",postKey)
                //시간순 정렬
                .orderBy("comment_time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                commentList.add(new commentInfo(
                                        document.getData().get("comment_id").toString(),
                                        document.getData().get("comment_uid").toString(),
                                        document.getData().get("comment_content").toString(),
                                        document.getData().get("post_id").toString(),
                                        new Date(document.getDate("comment_time").getTime())));
                            }

                            //리사이클러 뷰 생성
                            recyclerView= findViewById(R.id.commentRecyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(postActivity.this));
                            adapter = new commentListViewAdapter(postActivity.this,commentList);
                            recyclerView.setAdapter(adapter);
                        } else {
                        }
                    }
                });

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.commentWriteButton:
                    commentUpload();
                    break;
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.default_post_menu, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                finish();
                break;
        }
        return true;
    }

    private void commentUpload() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String comment_content = ((EditText) findViewById(R.id.commentEditText)).getText().toString();
        String comment_uid = user.getUid();
        String post_id = (String) getIntent().getSerializableExtra("postpostKey");
        //시간 가져오기
        comment_time = Calendar.getInstance().getTime();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference commentRef = firebaseFirestore.collection("comment").document();
        String comment_id = commentRef.getId();
        if (comment_content.length() > 0) {
            commentInfo commentInfo = new commentInfo(comment_id, comment_uid, comment_content, post_id, comment_time);
            commentRef.set(commentInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            toast("댓글 업로드 완료");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toast("댓글 업로드 실패");
                        }
                    });
            Log.e("로그", "url:" + post_id);
        } else {
            toast("댓글을 입력해주세요.");
        }
    }

    //토스트메시지 함수
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}