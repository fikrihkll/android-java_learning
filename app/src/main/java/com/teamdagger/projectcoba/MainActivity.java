package com.teamdagger.projectcoba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    TextView tv_name;
    EditText et_nama_baru,et_key;
    Button btn_add,btn_all_name,btn_del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_key = findViewById(R.id.et_key);
        btn_del = findViewById(R.id.btn_del);

        tv_name = findViewById(R.id.tv_name);
        et_nama_baru = findViewById(R.id.et_new_name);
        btn_add = findViewById(R.id.btn_add);
        btn_all_name = findViewById(R.id.btn_all_name);

        getName();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addName();
            }
        });

        btn_all_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllName();
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

    }

    private void deleteData(){
        String key = et_key.getText().toString();

        FirebaseDatabase.getInstance().getReference().child("daftar_nama").child(key).setValue("");

    }

    private void getAllName(){
        FirebaseDatabase.getInstance().getReference()
                .child("daftar_nama")
                .child("total_nama")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int totalName = Integer.parseInt(snapshot.getValue().toString());

                final String[] allName = {""};
                for(int i =1 ; i <= totalName ; i++){

                    FirebaseDatabase.getInstance().getReference().child("daftar_nama").child("nama_"+i).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            allName[0] += snapshot.getValue().toString() + "\n";
                            tv_name.setText(allName[0]);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void addName(){
        String namaBaru = et_nama_baru.getText().toString();


        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("daftar_nama").child("total_nama").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                int totalNama = Integer.parseInt(snapshot.getValue().toString());

                totalNama++;
                db.child("daftar_nama").child("nama_"+totalNama).setValue(namaBaru);
                db.child("daftar_nama").child("total_nama").setValue(totalNama);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void getName(){
        FirebaseDatabase.getInstance().getReference().child("daftar_nama").child("total_nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String namaSaya = snapshot.getValue().toString();
                tv_name.setText(namaSaya);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}