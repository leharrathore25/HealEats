package com.example.healeats;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
                        public void onItemClick(Disease disease) {
                            showBottomSheetDialog(disease);
                        }
                    });
                }

            }
        });
    }

    private void showBottomSheetDialog(Disease disease) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.bottom_sheet_select_disease);

        TextView txtDiseaseName = dialog.findViewById(R.id.txtDiseaseName);
        TextView txtSymptoms = dialog.findViewById(R.id.txtSymptoms);
        TextView txtCauses = dialog.findViewById(R.id.txtCauses);
        Button btnConfirmDisease = dialog.findViewById(R.id.btnConfirmDisease);

        // Retrieve the Symptoms and Causes values from the Disease object
        String symptoms = disease.getSymptoms();
        String causes = disease.getCauses();

        // Set the retrieved values to the text views
        txtDiseaseName.setText(disease.getName());
        txtSymptoms.setText(symptoms);
        txtCauses.setText(causes);

        btnConfirmDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogFragment progressDialog = new ProgressDialogFragment();
                progressDialog.show(getSupportFragmentManager(), "progressDialog");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Update the user's disease_id field in Firestore
                        updateUserDisease(disease.getName());

                        // Dismiss the dialog
                        progressDialog.dismiss();

                        // Pass the disease_id to the new activity
                        String diseaseId = disease.getName();
                        Intent intent = new Intent(CustomerDiseasesListActivity.this, CustomerMealPlanActivity.class);
                        intent.putExtra("disease_id", diseaseId);
                        startActivity(intent);
                    }
                }, 2000); // Adjust the duration as needed
            }
        });

        dialog.show();
    }
    public void updateUserDisease(String diseaseName) {
        // Retrieve the user data from the Singleton
        User user = UserDataSingleton.getInstance().getUser();

        if (user != null) {
            // Get the user's username from the user object
            String username = user.getUsername(); // Replace with the actual method to get the user's username

            // Reference to the Firestore collection "users"
            CollectionReference usersRef = db.collection("users");

            // Query to find the user's document by their username
            Query query = usersRef.whereEqualTo("username", username);

            // Execute the query
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && !task.getResult().getDocuments().isEmpty()) {
                        // Retrieve the user's document
                        DocumentSnapshot userDoc = task.getResult().getDocuments().get(0);

                        // Update the "disease_id" field with the chosen disease name
                        userDoc.getReference().update("disease_id", diseaseName)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Handle a successful update, if needed
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle an error, if needed
                                    }
                                });
                    }
                }
            });
        }
    }

}