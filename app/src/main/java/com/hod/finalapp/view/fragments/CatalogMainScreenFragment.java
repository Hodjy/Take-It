package com.hod.finalapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

    RecyclerView mItemsListRecyclerView;
    ItemAdapter mItemAdapter;
    CatalogMainScreenViewModel mCatalogMainScreenViewModel;
    ArrayList<Item> mItemsList;

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

//        mItemsList =
        initUi(rootView);
        initObservers();

        //TextView firstnameTv = rootView.findViewById(R.id.fragment_user_main_screen_firstname_tv);
        //firstnameTv.setText(UserRepository.getInstance().getCurrentUser().getFirstName());

        return rootView;
    }

    private void initUi(View iRootView) {
        mItemsListRecyclerView = iRootView.findViewById(R.id.fragment_catalog_main_screen_item_list_rv);
        //TODO IMPLEMENT VIEWMODEL METHOD GETLIST
        mItemAdapter = new ItemAdapter(mItemsList);
        mItemsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mItemsListRecyclerView.setHasFixedSize(true);
        mItemsListRecyclerView.setAdapter(mItemAdapter);
        mItemAdapter.setListener(new ItemAdapter.ItemListener() {
            @Override
            public void onItemClicked(int position, View view) {
                //TODO OPEN ITEM DETAILS FRAGMENT
            }
        });

    }

    private void initObservers() {

        mCatalogMainScreenViewModel.getItemsListLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> items) {

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
