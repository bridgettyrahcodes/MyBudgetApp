package com.example.budgetapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetapp.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IncomeFragment extends Fragment implements IncomeAdapter.DataChangeListener{
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;

    //Recyclerview
    private RecyclerView recyclerView;
    private TextView incomeTotalSum;
    private List<Data> data;
    private IncomeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_income, container, false);
        mAuth = FirebaseAuth.getInstance();

        data = new ArrayList<>();
        adapter = new IncomeAdapter(getContext(), data);
        adapter.setDataChangeListener(this);

        incomeTotalSum = myview.findViewById(R.id.income_txt_result);
        recyclerView = myview.findViewById(R.id.recyler_id_income);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData");

        mIncomeDatabase.orderByChild("userID").equalTo(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Data myItems = itemSnapshot.getValue(Data.class);
                    data.add(myItems);
                }
                adapter.notifyDataSetChanged();
                onDataChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
            }
        });

        return myview;
    }


    @Override
    public void onDataChanged() {
        // Calculate the total sum and update the TextView
        int totalSum = calculateTotalSum();
        incomeTotalSum.setText(String.valueOf(totalSum));
    }

    private int calculateTotalSum() {
        int sum = 0;
        for (Data item : data) {
            sum += item.getAmount();
        }
        return sum;
    }


        }