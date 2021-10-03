package com.hod.finalapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hod.finalapp.R;
import com.hod.finalapp.model.adapters.ItemAdapter;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.repositories.UserRepository;
import com.hod.finalapp.view.viewmodel.CatalogMainScreenViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CatalogMainScreenFragment extends Fragment
{
    private ICatalogMainScreenFragmentListener m_Callback;

    private RecyclerView mItemsListRecyclerView;
    private ItemAdapter mItemAdapter;
    private CatalogMainScreenViewModel mCatalogMainScreenViewModel;
    private ArrayList<Item> mItemsList;

    //TODO remove this button
    Button mTempButton;

    public interface ICatalogMainScreenFragmentListener
    {
        public void fragmentActiveStateChanged(boolean iIsActive);
    }
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_catalog_main_screen, container, false);
        mCatalogMainScreenViewModel = new ViewModelProvider(this).get(CatalogMainScreenViewModel.class);

        mItemsList = new ArrayList<>();
        initUi(rootView);
        initObservers();

        return rootView;
    }

    private void initUi(View iRootView) {
        mItemsListRecyclerView = iRootView.findViewById(R.id.fragment_catalog_main_screen_item_list_rv);
        mItemAdapter = new ItemAdapter(mItemsList);
        mItemsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mItemsListRecyclerView.setHasFixedSize(true);
        mItemsListRecyclerView.setAdapter(mItemAdapter);
        mItemAdapter.setListener(new ItemAdapter.ItemListener() {
            @Override
            public void onItemClicked(int position, View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("item", mItemsList.get(position));
                NavHostFragment.findNavController(CatalogMainScreenFragment.this).navigate(R.id.itemDescriptionFragment, bundle);
            }
        });

        mCatalogMainScreenViewModel.getItemsList();

        //TODO remove this button
        mTempButton = iRootView.findViewById(R.id.fragment_catalog_main_screen_temp_btn);
        mTempButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.createNewItemFragment);
        });
    }

    private void initObservers() {

        mCatalogMainScreenViewModel.getItemsListLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> items) {
                mItemsList.clear();
                for(Item item: items){
                    if(!mCatalogMainScreenViewModel.isMyItem(item)){
                        mItemsList.add(item);
                    }
                }
                mItemAdapter.setItems(mItemsList);
                mItemAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try
        {
            m_Callback = (ICatalogMainScreenFragmentListener)context;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException("Activity does not implement ICatalogMainScreenFragmentListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        m_Callback.fragmentActiveStateChanged(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        m_Callback.fragmentActiveStateChanged(false);
    }
}
