package com.example.budgetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomnavigationView;
    private FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Expense Manager");
        setSupportActionBar(toolbar);

        bottomnavigationView=findViewById(R.id.buttomNavigationBar);
        frameLayout=findViewById(R.id.main_frame);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.naView);
        navigationView.setNavigationItemSelectedListener(this);

        bottomnavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, homeFragment)
                .addToBackStack(String.valueOf(R.id.dashboard))
                .commit();
        //bottomnavigationView.setItemBackgroundResource(R.color.dashboard_color);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    HomeFragment homeFragment=new HomeFragment();
    IncomeFragment incomeFragment=new IncomeFragment();
    ExpenseFragment expenseFragment=new ExpenseFragment();

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.dashboard){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, homeFragment)
                    .addToBackStack(String.valueOf(R.id.dashboard))
                    .commit();
            bottomnavigationView.setItemBackgroundResource(R.color.dashboard_color);
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else if (item.getItemId() == R.id.income){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, incomeFragment)
                    .addToBackStack(String.valueOf(R.id.dashboard))
                    .commit();
            bottomnavigationView.setItemBackgroundResource(R.color.income_color);
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else if (item.getItemId() == R.id.expense){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, expenseFragment)
                    .addToBackStack(String.valueOf(R.id.dashboard))
                    .commit();
            bottomnavigationView.setItemBackgroundResource(R.color.expense_color);
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else if (item.getItemId() == R.id.logout){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Confirm Logout.");
            builder.setMessage("Are you sure you want to exit?");
            builder.setCancelable(false);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Toast.makeText(Dashboard.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Dashboard.this, MainActivity.class));
                    finish();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(Dashboard.this, "Logout Cancelled", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

}