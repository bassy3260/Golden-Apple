package com.aaa.aaa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Session2Command;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Frag1 extends Fragment implements View.OnClickListener {

    private View view;
    private ImageView img;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag1,container,false);
        init();
    return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera:
            {
                //카메라 앱 실행
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //requestCode 0을 전송해줌 화면 돌아왔을때 구분해야함
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.btn_gallery:
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //
                intent.setType("image/*");
                //requestCode 1을 전송해줌 화면 돌아왔을때 구분해야함
                startActivityForResult(intent, 1);
                break;
            }
        }
    }
    private void init(){

        //각 뷰들 초기화
        img = view.findViewById(R.id.img);

        ((Button)view.findViewById(R.id.btn_camera)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.btn_gallery)).setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            //requestCode가 0일때 Bundle로 사진 데이터 받아서 비트맵으로 전환 후 이미지에 적용
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);

        }else if (requestCode == 1){
            //requestCode가 1일때 선택된 이미지 값을  Uri로 받아서 이미지에 적용
            Uri uri = data.getData();
            img.setImageURI(uri);
        }
    }
}
