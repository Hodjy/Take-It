package com.hod.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hod.finalapp.model.listeners.ISnackbarLogInActionListener;
import com.hod.finalapp.model.repositories.RepoInitializer;
import com.hod.finalapp.model.repositories.UserRepository;
import com.hod.finalapp.view.fragments.CatalogMainScreenFragment;
import com.hod.finalapp.view.fragments.WelcomeScreenFragment;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity
        implements CatalogMainScreenFragment.ICatalogMainScreenFragmentListener,
        ISnackbarLogInActionListener
{
    private DrawerLayout mDrawerLayout;
    private ActionBar mActionBar;
    private MenuItem mSignInOutItem;
    private MenuItem mProfileItem;
    private MenuItem mChatsItem;
    private MenuItem mAddItemMenuItem;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);
        NavigationView navigationView = findViewById(R.id.activity_main_navigation_view);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                                            .findFragmentById(R.id.activity_main_nav_host_fragment);
        mNavController = navHostFragment.getNavController();

        mSignInOutItem = navigationView.getMenu().findItem(R.id.drawer_menu_log_out_item);
        mProfileItem = navigationView.getMenu().findItem(R.id.drawer_menu_profile);
        mChatsItem = navigationView.getMenu().findItem(R.id.drawer_menu_chats);

        Toolbar appToolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);

        mActionBar = getSupportActionBar();

        mActionBar.setDisplayHomeAsUpEnabled(false);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item)
            {//TODO make it better or at least try to (maybe stragey design pattern).
                switch(item.getItemId())
                {
                    case R.id.drawer_menu_profile:
                        mNavController.navigate(R.id.action_userMainScreenFragment_to_userProfileFragment);
                        break;
                    case R.id.drawer_menu_chats:
                        mNavController.navigate(R.id.action_userMainScreenFragment_to_userChatsFragment);
                        break;
                    case R.id.drawer_menu_log_out_item:
                        if(UserRepository.getInstance().isUserLoggedIn())
                        {
                            UserRepository.getInstance().signUserOut();
                            RepoInitializer.closeAllRepo();
                            mNavController.navigate(R.id.action_to_welcomeScreenFragment);
                        }
                        else
                        {
                            mNavController.navigate(R.id.action_to_signInFragment);
                        }
                        break;
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
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.toolbar_menu_add_new_item:
                addItemWasPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addItemWasPressed() {
        if(UserRepository.getInstance().isUserLoggedIn())
        {
            mNavController.navigate(R.id.action_to_createNewItemFragment);
        }
        else
        {
            View fragmentContainer = findViewById(R.id.activity_main_nav_host_fragment); // dummy view for snackbar.
            ISnackbarLogInActionListener callback = this;
            String error = getString(R.string.guest_user_cannot_use_this);
            Snackbar.make(fragmentContainer,error,Snackbar.LENGTH_LONG)
                    .setAction(R.string.sign_in, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callback.goToLogIn();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void fragmentActiveStateChanged(boolean iIsActive) // toggle hamburger icon if entered or left item catalog fragment.
    {
        mActionBar.setDisplayHomeAsUpEnabled(iIsActive);
        mAddItemMenuItem.setVisible(iIsActive);

        if(iIsActive)
        {
            String signMessage = getResources().getString(R.string.sign_out);
            Drawable icon = ResourcesCompat.getDrawable(getResources(),R.drawable.outline_logout_24,getTheme());
            boolean isUserLoggedIn = UserRepository.getInstance().isUserLoggedIn();
            if(!isUserLoggedIn)
            {
                signMessage = getResources().getString(R.string.sign_in);
                icon = ResourcesCompat.getDrawable(getResources(),R.drawable.outline_login_24,getTheme());
            }

            setMenuItems(isUserLoggedIn, signMessage, icon);
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

    private void setMenuItems(boolean iIsUserLoggedIn, String iSignInOutTitle, Drawable iIcon)
    {
        mSignInOutItem.setTitle(iSignInOutTitle);
        mSignInOutItem.setIcon(iIcon);
        mProfileItem.setVisible(iIsUserLoggedIn);
        mChatsItem.setVisible(iIsUserLoggedIn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        mAddItemMenuItem = menu.findItem(R.id.toolbar_menu_add_new_item);
        mAddItemMenuItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void goToLogIn() {
        mNavController.navigate(R.id.action_to_signInFragment);
    }
}