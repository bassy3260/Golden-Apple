package com.aaa.aaa;

/** 게시글 리사이클러뷰 어댑터**/
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class communityListViewAdapter extends RecyclerView.Adapter<communityListViewAdapter.communityListViewHolder> {
    // 보여줄 Item 목록을 저장할 List
    private ArrayList<writeInfo> items;
    Context context;

    // Adapter 생성자 함수
    public class communityListViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView content;
        protected TextView time;

        public communityListViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.listTitleText);
            this.content = (TextView) view.findViewById(R.id.listContentText);
            this.time = (TextView) view.findViewById(R.id.listTimeText);
        }
    }

    public communityListViewAdapter(FragmentActivity activity, ArrayList<writeInfo> items) {
        this.items = items;
    }

    public communityListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.community_list_view, viewGroup, false);
        communityListViewHolder communityListViewHolder = new communityListViewHolder(view);
        return communityListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull communityListViewHolder viewHolder, int position) {
        View holder = viewHolder.itemView;
        TextView title = holder.findViewById(R.id.listTitleText);
        title.setText(items.get(position).getTitle());
        TextView content = holder.findViewById(R.id.listContentText);
        content.setText(items.get(position).getCategory());
        TextView date = holder.findViewById(R.id.listTimeText);
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format_year = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (format_year.format(now).equals (format_year.format(items.get(position).getCreated()))) {
            if (format_date.format(now) .equals (format_date.format(items.get(position).getCreated()))) {
                date.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(items.get(position).getCreated()));
            } else {
                date.setText(new SimpleDateFormat("MM-dd", Locale.getDefault()).format(items.get(position).getCreated()));
            }
        } else {
            date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(items.get(position).getCreated()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

