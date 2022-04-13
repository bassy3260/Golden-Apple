package com.aaa.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Frag3 extends Fragment {

    private View view;

    private Button mypage;
    FloatingActionButton write;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);
        setHasOptionsMenu(true); //메뉴 보이기

        //글쓰기 화면으로 이동
        write= (FloatingActionButton) view.findViewById(R.id.writeButton);

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),writeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        return view;
    }

    //메뉴 설정 함수
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.community_menu, menu);
    }
    //메뉴 버튼 함수
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //글쓰기 버튼 함수
        switch (item.getItemId()){
            case R.id.menu_write:
                Intent intent = new Intent(getActivity(), postActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //액션바 타이틀 설정 함수
   @Override
    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((BottomActivity) activity).setActionBarTitle("커뮤니티");
        }
    }
}



