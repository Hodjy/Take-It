package com.hod.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.hod.finalapp.model.repositories.RepoInitializer;
import com.hod.finalapp.model.repositories.UserRepository;
import com.hod.finalapp.view.fragments.CatalogMainScreenFragment;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements CatalogMainScreenFragment.ICatalogMainScreenFragmentListener
{
    private DrawerLayout mDrawerLayout;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);
        NavigationView navigationView = findViewById(R.id.activity_main_navigation_view);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                                            .findFragmentById(R.id.activity_main_nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        Toolbar appToolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);

        mActionBar = getSupportActionBar();

        mActionBar.setDisplayHomeAsUpEnabled(false);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item)
            {//TODO make it better or at least try to (maybe stragey design pattern).
                if(item.getItemId() == R.id.drawer_menu_profile)
                {
                    navController.navigate(R.id.action_userMainScreenFragment_to_userProfileFragment);
                }
                else if(item.getItemId() == R.id.drawer_menu_chats)
                {
                    navController.navigate(R.id.action_userMainScreenFragment_to_userChatsFragment);
                }
                else if(item.getItemId() == R.id.drawer_menu_my_items)
                {
                    navController.navigate(R.id.action_userMainScreenFragment_to_userItemsFragment);
                }
                else if(item.getItemId() == R.id.drawer_menu_log_out_item)
                {
                    if(UserRepository.getInstance().isUserLoggedIn())
                    {
                        UserRepository.getInstance().signUserOut();
                        RepoInitializer.closeAllRepo();
                        navController.navigate(R.id.action_to_welcomeScreenFragment);
                    }
                }
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            mDrawerLayout.openDrawer(Gravity.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void fragmentActiveStateChanged(boolean iIsActive) // toggle hamburger icon if entered or left item catalog fragment.
    {
        mActionBar.setDisplayHomeAsUpEnabled(iIsActive);

        if(iIsActive)
        {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        }
        else
        {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }
    }
}