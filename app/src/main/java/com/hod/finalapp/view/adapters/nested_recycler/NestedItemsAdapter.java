package com.hod.finalapp.view.adapters.nested_recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.view.ApplicationContext;
import com.hod.finalapp.view.adapters.ItemAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NestedItemsAdapter extends RecyclerView.Adapter<NestedItemsAdapter.NestedItemsViewHolder> {

    private ArrayList<ArrayList<Item>> mItemsByCategory;
    private String[] categories;
    private INestedItemsAdapterListener mCallback;

    public interface INestedItemsAdapterListener
    {
        public void onClick(Item iItem);
    }

    public NestedItemsAdapter(ArrayList<ArrayList<Item>> iNestedItems, INestedItemsAdapterListener iCallback)
    {
        mItemsByCategory = iNestedItems;
        categories = ApplicationContext.getAppContext().getResources().getStringArray(R.array.categories);
        mCallback = iCallback;
    }


    @NonNull
    @NotNull
    @Override
    public NestedItemsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,
                                                    int viewType) {
        View rootView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.nested_recycler_parent_item, parent, false);

        return new NestedItemsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NestedItemsAdapter.NestedItemsViewHolder holder,
                                 int position) {
        ArrayList<Item> itemsByCategory = mItemsByCategory.get(position);
        holder
                .mCategoryTitle
                .setText(categories[position]);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                        holder.mChildRecyclerView.getContext()
                        ,LinearLayoutManager.HORIZONTAL,false);
        layoutManager.setInitialPrefetchItemCount(itemsByCategory.size());
        ItemAdapter itemAdapter = new ItemAdapter(itemsByCategory);
        itemAdapter.setListener(new ItemAdapter.ItemListener() {
            @Override
            public void onItemClicked(Item iItem) {
                mCallback.onClick(iItem);
            }
        });
        holder.mChildRecyclerView.setLayoutManager(layoutManager);
        holder.mChildRecyclerView.setAdapter(itemAdapter);
    }

    @Override
    public int getItemCount() {
        return mItemsByCategory.size();
    }

    class NestedItemsViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mCategoryTitle;
        private RecyclerView mChildRecyclerView;

        NestedItemsViewHolder(final View iItemView)
        {
            super(iItemView);
            mCategoryTitle = iItemView.findViewById(R.id.nested_recycler_parent_parent_item_title);
            mChildRecyclerView = iItemView.findViewById(R.id.nested_recycler_parent_item_child_recyclerview);
        }
    }
}
