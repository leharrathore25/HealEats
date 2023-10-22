package com.example.healeats;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerDiseasesListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseases_list);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the list of diseases from Firestore
        CollectionReference diseasesRef = db.collection("diseases");
        diseasesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    DiseaseAdapter adapter = new DiseaseAdapter(task.getResult());
                    recyclerView.setAdapter(adapter);

                    // Handle item click to show the bottom sheet dialog
                    adapter.setOnItemClickListener(new DiseaseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(String diseaseId) {
                            showBottomSheetDialog(diseaseId);
                        }
                    });
            }

            }
        });
    }

    private void showBottomSheetDialog(String diseaseId) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.bottom_sheet_select_disease);
        Button btnSelectDisease = dialog.findViewById(R.id.btnSelectDisease);

        // Handle the "Select Disease" button click
        btnSelectDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your logic here for selecting the disease
                // For example, you can start a new activity or perform an action.
                // In this example, we'll dismiss the dialog.
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}