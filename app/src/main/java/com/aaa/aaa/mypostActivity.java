package com.aaa.aaa;

/** 게시글 리스트 (질문 답변) **/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.aaa.aaa.adpater.communityListViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class mypostActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private communityListViewAdapter adapter;
    private FirebaseFirestore database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        /** 리사이클러 뷰(게시글 리스트) 생성 **/
        final ArrayList<PostInfo> postList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        //FireStore에서 게시글 정보 받아오기
        database.collection("post")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("uid", user.getUid())
                //시간순 정렬
                .orderBy("created", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                postList.add(new PostInfo(
                                        document.getData().get("category").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("uid").toString(),
                                        new Date(document.getDate("created").getTime()),
                                        (ArrayList<String>) document.getData().get("content"),
                                        document.getData().get("postKey").toString()));
                            }
                            //리사이클러 뷰 생성
                            recyclerView = findViewById(R.id.mypostRecyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(mypostActivity.this));
                            adapter = new communityListViewAdapter(mypostActivity.this, postList);
                            recyclerView.setAdapter(adapter);
                        } else {
                        }
                    }
                });
    }
}