package com.hod.finalapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.view.adapters.nested_recycler.NestedItemsAdapter;
import com.hod.finalapp.view.viewmodel.CatalogMainScreenViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CatalogMainScreenFragment extends Fragment
{
    private ICatalogMainScreenFragmentListener m_Callback;

    private RecyclerView mItemsListRecyclerView;
    private NestedItemsAdapter mNestedItemAdapter;
    private CatalogMainScreenViewModel mCatalogMainScreenViewModel;
    private ArrayList<ArrayList<Item>> mItemsByCategory;

    //TODO remove this button (mTempButton)
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

        initVars();
        initUi(rootView);
        initObservers(rootView);

        return rootView;
    }

    private void initVars()
    {
        mItemsByCategory = new ArrayList<>();
        int categories = mCatalogMainScreenViewModel.getCategoriesAmount();

        for(int i = 0; i < categories; i++)
        {
            mItemsByCategory.add(new ArrayList<>());
        }
    }

    private void initUi(View iRootView) {
        mItemsListRecyclerView = iRootView.findViewById(R.id.fragment_catalog_main_screen_item_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mNestedItemAdapter = new NestedItemsAdapter(mItemsByCategory, new NestedItemsAdapter.INestedItemsAdapterListener() {
            @Override
            public void onClick(Item iItem) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("item", iItem);
                NavHostFragment.findNavController(CatalogMainScreenFragment.this).navigate(R.id.itemDescriptionFragment, bundle);
            }
        });
        //mItemsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //mItemsListRecyclerView.setHasFixedSize(true);
        mItemsListRecyclerView.setAdapter(mNestedItemAdapter);
        mItemsListRecyclerView.setLayoutManager(linearLayoutManager);
        mCatalogMainScreenViewModel.initItemsListByCategory();

        //TODO remove this button (mTempButton)
        mTempButton = iRootView.findViewById(R.id.fragment_catalog_main_screen_temp_btn);
        mTempButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_to_createNewItemFragment);
        });
    }

    private void initObservers(View iRootView)
    {

        mCatalogMainScreenViewModel.getItemsByCategory().observe(getViewLifecycleOwner(), new Observer<ArrayList<ArrayList<Item>>>() {
            @Override
            public void onChanged(ArrayList<ArrayList<Item>> arrayLists) {
                int i = 0;
                for (ArrayList<Item> items : arrayLists)
                {
                    mItemsByCategory.get(i).clear();
                    mItemsByCategory.get(i).addAll(items);
                    i++;
                }
                mNestedItemAdapter.notifyDataSetChanged();
            }
        });

        mCatalogMainScreenViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(iRootView,s,Snackbar.LENGTH_LONG).show();
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
