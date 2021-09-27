package com.hod.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.hod.finalapp.model.FirebaseHandler;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity
{
    private DrawerLayout m_DrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_DrawerLayout = findViewById(R.id.activity_main_drawer_layout);
        NavigationView navigationView = findViewById(R.id.activity_main_navigation_view);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                                            .findFragmentById(R.id.activity_main_nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        Toolbar appToolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item)
            {
                if(item.getItemId() == R.id.drawer_menu_log_out_item)
                {
                    if(UserRepository.getInstance().isUserLoggedIn())
                    {
                        UserRepository.getInstance().signUserOut();
                        navController.navigate(R.id.action_to_welcomeScreenFragment);
                    }
                }
                m_DrawerLayout.closeDrawers();
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
            m_DrawerLayout.openDrawer(Gravity.START);
        }
        return super.onOptionsItemSelected(item);
    }
}