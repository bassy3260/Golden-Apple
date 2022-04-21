/**
 * 마이페이지 액티비티
 **/
package com.aaa.aaa;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class writeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;
    Toolbar writeToolbar;
    private FirebaseAuth firebaseAuth;
    String format_yyyyMMdd = "yyyy/MM/dd";
    String format_hhmm = "hh:mm";
    private Date currentTime;
    FirebaseUser user;
    Button image;

    private ArrayList<String> partList = new ArrayList<>();
    private LinearLayout parent;
    private int successCount = 0;
    private int partCount = 0;

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        parent = findViewById(R.id.contentsView);
        ActionBar actionBar = getSupportActionBar();

        Toolbar tb = (Toolbar) findViewById(R.id.write_toolbar);
        setSupportActionBar(tb);//액션바를 툴바로 바꿔줌
        getSupportActionBar().setTitle("글 쓰기");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        // Spinner
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        image = findViewById(R.id.addimageButton);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }


    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.community_write, menu);
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
            case R.id.menu_write:
                // User chose the "Settings" item, show the app settings UI...
                storageUpload();
                break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri file = data.getData();
                    String uri = file.toString();
                    partList.add(uri);

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    ImageView imageView = new ImageView(writeActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    Glide.with(this).load(uri).override(1000).into(imageView);
                    parent.addView(imageView);

                    EditText editText = new EditText(writeActivity.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    editText.setBackground(null);
                    parent.addView(editText);


                } catch (Exception e) {

                }
            }
        } else {

        }
    }

    private void storageUpload() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        //타이틀
        final String title = ((EditText) findViewById(R.id.titleText)).getText().toString();
        //글 내용
        final String contents = ((EditText) findViewById(R.id.commentText)).getText().toString();
        //사용자 가져오기
        user = firebaseAuth.getCurrentUser();
        //스피너(카테고리)
        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        String category = spinner.getSelectedItem().toString();
        String u_id = user.getUid();
        //시간 가져오기
        currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format_date = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
        SimpleDateFormat format_time = new SimpleDateFormat(format_hhmm, Locale.getDefault());
        String time = format_date.format(currentTime);
        String date = format_time.format(currentTime);

        DocumentReference postRef = firebaseFirestore.collection("post").document();


        if (title.length() > 0) {
            ArrayList<String> contentList = new ArrayList<>();
            user = FirebaseAuth.getInstance().getCurrentUser();
            StorageReference storageRef = storage.getReference();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                if (view instanceof EditText) {
                    String text = ((EditText) view).getText().toString();
                    if (text.length() > 0) {
                        contentList.add(text);
                    }
                } else {
                    contentList.add(partList.get(partCount));
                    StorageReference riversRef = storageRef.child("post/" + postRef.getId() + "/" + (contentList.size() - 1) + ".jpg");
                    try {

                        String url = partList.get(partCount);
                        Uri file = Uri.parse(url);
                        StorageMetadata metadata = new StorageMetadata.Builder()
                                .setCustomMetadata("index", "" + (contentList.size() - 1)).build();
                        UploadTask uploadTask = riversRef.putFile(file, metadata);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.e("로그", "url:" + uri);
                                        contentList.set(index, uri.toString());
                                        successCount++;
                                        Log.e("로그", contentList.get(1));
                                        if (partList.size() == successCount) {
                                            writeInfo writeInfo = new writeInfo(category, title, u_id, date,
                                                    time, contentList);
                                            postRef.set(writeInfo)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            toast("글 업로드 완료");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            toast("글 업로드 실패");
                                                        }
                                                    });
                                            finish();
                                        }
                                    }
                                });
                            }
                        });
                    } catch (Exception e) {

                    }
                }
            }
        } else {
            toast("제목이 비어있습니다!");
        }
    }

    //토스트메시지 함수
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


}
