package com.example.remindme.home;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme.Notes;
import com.example.remindme.R;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private List<Notes> listdata = new ArrayList<>();

    private ItemClickListener mItemClickListener;

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(View v,int position,Notes notes);
    }

    public void AddAllList(List<Notes> list){
        listdata = list;
        notifyDataSetChanged();
    }

    public List<Notes> getAllList(){
        return listdata;
    }

    @NonNull
    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_reminder, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ViewHolder holder, int position) {
        final Notes myListData = listdata.get(position);
        holder.tvDetails.setText(myListData.getDetails());
        holder.tvDate.setText(String.format("%s %s", myListData.getDate(), myListData.getTime()));

        if(myListData.getDone()){
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTitle.setText(myListData.getTitle());
        }else {
            holder.tvTitle.setPaintFlags(0);
            holder.tvTitle.setText(myListData.getTitle());
        }

        holder.ivCheck.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v,position,myListData);
            }
        });
        holder.ivDelete.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v,position,myListData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDetails, tvDate;
        ImageView ivDelete, ivCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.tvDetails = itemView.findViewById(R.id.tvDetails);
            this.tvDate = itemView.findViewById(R.id.tvDate);
            this.ivCheck = itemView.findViewById(R.id.ivCheck);
            this.ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
