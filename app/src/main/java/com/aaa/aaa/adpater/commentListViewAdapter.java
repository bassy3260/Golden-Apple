package com.aaa.aaa.adpater;

/** 게시글 리사이클러뷰 어댑터 **/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aaa.aaa.R;
import com.aaa.aaa.commentInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class commentListViewAdapter extends RecyclerView.Adapter<commentListViewAdapter.commentListViewHolder> {
    // 보여줄 Item 목록을 저장할 List
    private ArrayList<commentInfo> items;
    private FirebaseFirestore database;
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Adapter 생성자 함수
    public class commentListViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView content;
        protected TextView time;

        public commentListViewHolder(@NonNull View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.commentNameText);
            this.content = (TextView) view.findViewById(R.id.commentText);
            this.time = (TextView) view.findViewById(R.id.commentTimeText);
        }
    }

    public commentListViewAdapter(FragmentActivity activity, ArrayList<commentInfo> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public commentListViewHolder  onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_view, viewGroup, false);
        commentListViewHolder commentListViewHolder = new commentListViewHolder (view);
        return   commentListViewHolder;
    }

    /* 리스트 뷰 텍스트 설정 */
    @Override
    public void onBindViewHolder(@NonNull commentListViewHolder viewHolder, int position) {
        View holder = viewHolder.itemView;
        TextView comment_name = holder.findViewById(R.id.commentNameText);
        String comment_uid=items.get(position).getComment_uid();
        database = FirebaseFirestore.getInstance();
        //FireStore에서 게시글 정보 받아오기
        database.collection("user")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("uid", comment_uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                comment_name.setText(document.getData().get("name").toString());
                            }
                        } else {
                        }
                    }
                });
        TextView content = holder.findViewById(R.id.commentText);
        content.setText(items.get(position).getComment_content());
        TextView date = holder.findViewById(R.id.commentTimeText);
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format_year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (format_year.format(now).equals(format_year.format(items.get(position).getComment_time()))) {
            if (format_date.format(now).equals(format_date.format(items.get(position).getComment_time()))) {
                date.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(items.get(position).getComment_time()));
            } else {
                date.setText(new SimpleDateFormat("MM-dd", Locale.getDefault()).format(items.get(position).getComment_time()));
            }
        } else {
            date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(items.get(position).getComment_time()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

