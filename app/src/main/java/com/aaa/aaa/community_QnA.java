package com.aaa.aaa;

/** 게시글 리스트 (질문 답변) **/
import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class community_QnA extends Fragment {
    private RecyclerView recyclerView;
    private communityListViewAdapter adapter;
    private FirebaseFirestore database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_community_qn, container, false);

        /** 리사이클러 뷰(게시글 리스트) 생성 **/
        final ArrayList<PostInfo> postList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        //FireStore에서 게시글 정보 받아오기
        database.collection("post")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("category", "질문")
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
                            recyclerView = v.findViewById(R.id.qnaRecyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            adapter = new communityListViewAdapter(getActivity(), postList);
                            recyclerView.setAdapter(adapter);

                        } else {
                        }
                    }
                });

        final SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.QnArefresh_layout);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                postList.clear();
                //FireStore에서 게시글 정보 받아오기
                database.collection("post")
                        // 카테고리에 따라 게시글 받아오기
                        .whereEqualTo("category", "질문")
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
                                    recyclerView = v.findViewById(R.id.qnaRecyclerView);
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    adapter = new communityListViewAdapter(getActivity(), postList);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                }
                            }
                        });

                ft.detach(community_QnA.this).attach(community_QnA.this).commit();
                pullToRefresh.setRefreshing(false);
            }
        });
        return v;
    }

}