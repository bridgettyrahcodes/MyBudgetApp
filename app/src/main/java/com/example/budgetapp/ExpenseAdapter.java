package com.example.budgetapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetapp.Model.Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Data> data;
    //data item value
    private String type;
    private String note;

    public ExpenseAdapter(Context context, List<Data> data) {
        this.context = context;
        this.data = (ArrayList<Data>) data;
    }

    @NonNull
    @Override
    public ExpenseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(context, R.layout.expense_recycler_data, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.MyViewHolder holder, int position) {
        Data myData=data.get(position);
        holder.date_txt_expense.setText(myData.getDate());
        holder.type_txt_expense.setText(myData.getType());
        holder.amount_txt_expense.setText(String.valueOf(myData.getAmount()));
        holder.note_txt_expense.setText(myData.getNote());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                FirebaseUser mUser = mAuth.getCurrentUser();
                String uid = mUser.getUid();

                DatabaseReference mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase");

                AlertDialog.Builder mydialog = new AlertDialog.Builder(context);
                LayoutInflater myInflater = LayoutInflater.from(context);
                View myView = myInflater.inflate(R.layout.update_data_item, null);
                mydialog.setView(myView);

                EditText edtAmount = myView.findViewById(R.id.amount_edt);
                EditText edtType = myView.findViewById(R.id.type_edt);
                EditText edtNote = myView.findViewById(R.id.note_edt);

                //set data edit to text
                edtType.setText(myData.getType());
                edtNote.setText(myData.getNote());
                edtAmount.setText(String.valueOf(myData.getAmount()));

                Button btnUpdate = myView.findViewById(R.id.btn_update);
                Button btnDelete = myView.findViewById(R.id.btn_delete);

                AlertDialog dialog = mydialog.create();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type = edtType.getText().toString().trim();
                        note = edtNote.getText().toString().trim();
                        String amount = edtAmount.getText().toString().trim();
                        int myAmount = Integer.parseInt(amount);

                        String mDate = DateFormat.getDateInstance().format(new Date());

                        DatabaseReference specificItemRef = mIncomeDatabase.child(myData.getId());

                        specificItemRef.child("amount").setValue(myAmount);
                        specificItemRef.child("note").setValue(note);
                        specificItemRef.child("type").setValue(type);
                        specificItemRef.child("date").setValue(mDate);

                        Toast.makeText(context, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        dataChangeListener.onDataChanged();
                    }
                });


                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference specificItemRef = mIncomeDatabase.child(myData.getId());
                        specificItemRef.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        dataChangeListener.onDataChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to Delete Data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });



                dialog.show();
            }
        });

    }

    public interface DataChangeListener {
        void onDataChanged();
    }

    private IncomeAdapter.DataChangeListener dataChangeListener;

    public void setDataChangeListener(IncomeAdapter.DataChangeListener listener) {
        this.dataChangeListener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date_txt_expense, type_txt_expense, amount_txt_expense, note_txt_expense;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date_txt_expense=itemView.findViewById(R.id.date_txt_expense);
            type_txt_expense=itemView.findViewById(R.id.type_txt_expense);
            amount_txt_expense=itemView.findViewById(R.id.amount_txt_expense);
            note_txt_expense=itemView.findViewById(R.id.note_txt_expense);
        }
    }
}
