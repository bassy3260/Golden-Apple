package com.aaa.aaa;

/** 게시글 리스트 (질문 답변) **/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class mycommentActivity extends AppCompatActivity {
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

        final ArrayList<writeInfo> postList = new ArrayList<>();
        final ArrayList<String> postIdList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        //FireStore에서 게시글 정보 받아오기
        database.collection("comment")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("comment_uid", user.getUid())
                //시간순 정렬
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                postIdList.add(document.getData().get("post_id").toString());
                            }
                        } else {
                        }
                    }
                });
    }
}