package com.hod.finalapp.view.adapters.nested_recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NestedItemChildAdapter extends  RecyclerView.Adapter<NestedItemChildAdapter.NestedItemChildAdapterViewHolder>{
    private ArrayList<Item> mItems;

    NestedItemChildAdapter(ArrayList<Item> iItems)
    {
        mItems = iItems;
    }


    @NonNull
    @NotNull
    @Override
    public NestedItemChildAdapterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.nested_recycler_child_item, parent, false);

        return new NestedItemChildAdapterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NestedItemChildAdapter.NestedItemChildAdapterViewHolder holder,
                                 int position) {
        Item item = mItems.get(position);
        holder.mItemTitle.setText(item.getItemName());
        loadUriImage(holder, item);

    }

    private void loadUriImage(@NotNull NestedItemChildAdapter.NestedItemChildAdapterViewHolder holder, Item item) {
        Glide.with(holder.itemView.getContext())
                .load(item.getPicturesUrls().get(0))
                .error(R.drawable.ic_baseline_photo_24)
                .into(holder.mItemImage);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NestedItemChildAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView mItemTitle;
        ImageView mItemImage;

        public NestedItemChildAdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mItemTitle = itemView.findViewById(R.id.nested_recycler_child_item_child_item_name);
            mItemImage = itemView.findViewById(R.id.nested_recycler_child_item_child_item_image);
        }
    }
}
