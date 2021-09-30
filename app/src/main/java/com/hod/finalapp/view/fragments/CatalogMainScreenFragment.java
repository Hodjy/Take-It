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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hod.finalapp.R;
import com.hod.finalapp.model.adapters.ItemAdapter;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CatalogMainScreenFragment extends Fragment
{
    private ICatalogMainScreenFragmentListener m_Callback;

    RecyclerView mItemsListRecyclerView;
    ItemAdapter mItemAdapter;
    List<Item> mItemsList;

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

        initUi(rootView);

        //TextView firstnameTv = rootView.findViewById(R.id.fragment_user_main_screen_firstname_tv);
        //firstnameTv.setText(UserRepository.getInstance().getCurrentUser().getFirstName());

        return rootView;
    }

    private void initUi(View iRootView) {
        mItemsListRecyclerView = iRootView.findViewById(R.id.fragment_catalog_main_screen_item_list_rv);
        //TODO IMPLEMENT VIEWMODEL METHOD GETLIST
        //mItemAdapter = new ItemAdapter(Viewmodel.getList);
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
