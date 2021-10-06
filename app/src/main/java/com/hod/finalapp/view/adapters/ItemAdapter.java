package com.hod.finalapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Item> mItems;

    public ItemAdapter(ArrayList<Item> mItems) {
        this.mItems = mItems;
    }

    public interface ItemListener{
        void onItemClicked(Item iItem);
    }

    private ItemListener listener;

    public void setListener(ItemListener listener)
    {
        this.listener = listener;
    }

    public void setmItems(ArrayList<Item> mItems) {
        this.mItems = mItems;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView titleTv;
        ShapeableImageView itemPictureIv;

        public ItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.item_layout_title_tv);
            itemPictureIv = itemView.findViewById(R.id.item_layout_item_image_iw);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    listener.onItemClicked(mItems.get(adapterPosition));
                }
            });
        }
    }

    @NonNull
    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemAdapter.ItemViewHolder holder, int position) {
        Item item = mItems.get(position);
        holder.titleTv.setText(item.getItemName());
        loadUriImage(holder, item);
    }

    private void loadUriImage(@NotNull ItemViewHolder holder, Item item) {
        Glide.with(holder.itemView.getContext())
                .load(item.getPicturesUrls().get(0))
                .error(R.drawable.ic_baseline_photo_24)
                .into(holder.itemPictureIv);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
