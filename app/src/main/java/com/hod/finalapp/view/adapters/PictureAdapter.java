package com.hod.finalapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.hod.finalapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {

    private ArrayList<String> pictures;

    public PictureAdapter(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public interface PictureListener{
        void onItemClicked(int position, View view);
    }

    private PictureListener listener;

    public void setListener(PictureListener listener)
    {
        this.listener = listener;
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView pictureIv;

        public PictureViewHolder(@NonNull @NotNull View PictureView) {
            super(PictureView);

            pictureIv = PictureView.findViewById(R.id.picture_layout_picture_iv);

            PictureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(getAdapterPosition(), v);
                }
            });
        }
    }

    @NonNull
    @NotNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_layout, parent, false);
        PictureViewHolder pictureViewHolder = new PictureViewHolder(view);
        return pictureViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PictureAdapter.PictureViewHolder holder, int position) {
        String stringUrl = pictures.get(position);
        Glide.with(holder.itemView.getContext())
                .load(stringUrl)
                .into(holder.pictureIv);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }
}