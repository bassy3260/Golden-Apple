/**
 * 마이페이지 액티비티
 **/
package com.aaa.aaa;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class writeActivity extends AppCompatActivity {
    Toolbar writeToolbar;
    FirebaseAuth firebaseAuth;
    String format_yyyyMMdd = "yyyy/MM/dd";
    String format_time = "hh:mm";
    private Date currentTime;
    FirebaseUser user;
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        ActionBar actionBar =getSupportActionBar();

        Toolbar tb = (Toolbar) findViewById(R.id.write_toolbar);
        setSupportActionBar(tb);//액션바를 툴바로 바꿔줌
        getSupportActionBar().setTitle("글 쓰기");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Spinner
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);



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
                finish();
                break;
        }
        return true;
    }


    //멤버 정보 가져오기
    private void getUserinf(){
        user= firebaseAuth.getCurrentUser();
        final String category=((Spinner)findViewById(R.id.category_spinner)).getSelectedItem().toString();
        final String u_id=user.getUid();
        final String title=((EditText)findViewById(R.id.titleEditText)).getText().toString().trim();
        final String content=((EditText)findViewById(R.id.contentEditText)).getText().toString().trim();
        //시간 가져오기
        currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format_date=new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
        SimpleDateFormat format_time=new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
        final String time=format_date.format(currentTime);
        final String date=format_time.format(currentTime);

        if(title.length()>0&&content.length()>0){
            writeInfo writeInfo=new writeInfo(category,u_id,title,content,time,date);

        }
        else if(title.length()<0){
            toast("제목을 입력해주세요.");
        }
        else if(content.length()<0){
            toast("글 내용이 비어있습니다.");
        }
    }

    //토스트메시지 함수
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void uploader(writeInfo writeInfo){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("post").document()
    }

}
