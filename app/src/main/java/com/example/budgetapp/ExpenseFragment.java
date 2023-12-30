package com.example.budgetapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
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

public class ExpenseFragment extends Fragment  implements IncomeAdapter.DataChangeListener{
    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;

    //Recyclerview
    private RecyclerView recyclerView;
    private List<Data> data;
    private ExpenseAdapter adapter;
    private TextView expenseSumResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View myview = inflater.inflate(R.layout.fragment_expense, container, false);
        mAuth = FirebaseAuth.getInstance();

        data = new ArrayList<>();
        adapter = new ExpenseAdapter(getContext(), data);
        adapter.setDataChangeListener(this);

        expenseSumResult = myview.findViewById(R.id.expense_txt_result);
        recyclerView = myview.findViewById(R.id.recyler_id_expense);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase");

        mExpenseDatabase.orderByChild("userID").equalTo(currentUserID).addValueEventListener(new ValueEventListener() {
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
        expenseSumResult.setText(String.valueOf(totalSum));
    }

    private int calculateTotalSum() {
        int sum = 0;
        for (Data item : data) {
            sum += item.getAmount();
        }
        return sum;
    }



}

