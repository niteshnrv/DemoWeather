package com.example.niteshverma.demoweather.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.niteshverma.demoweather.R;
import com.example.niteshverma.demoweather.model.Bookmark;

import java.util.ArrayList;
import java.util.List;

public class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkListAdapter.BookmarkViewHolder>{

    private List<Bookmark> itemList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public BookmarkListAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bookmark,
                parent, false);
        return new BookmarkViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int i) {
        Bookmark result = itemList.get(i);
        holder.name.setText(result.getLocationName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<Bookmark> results) {
        itemList = results;
        notifyDataSetChanged();
    }

    public List<Bookmark> getItems() {
        return itemList;
    }

    public void addItems(Bookmark location) {
        itemList.add(location);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(Bookmark bookmark) {
        removeItem(itemList.indexOf(bookmark));
    }

    public void restoreItem(Bookmark location, int position) {
        itemList.add(position, location);
        notifyItemInserted(position);
    }

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private OnItemClickListener onItemClickListener;

        public View viewBackground;
        public View viewForeground;
        public TextView name;

        public BookmarkViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            name = itemView.findViewById(R.id.name);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(onItemClickListener != null){
                onItemClickListener.onItemClicked(view, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClicked(View v, int position);
    }
}
