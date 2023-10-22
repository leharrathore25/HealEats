package com.example.healeats;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AdminDiseasesListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private DiseaseAdapter adapter;
    private List<Disease> diseases = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_diseases_list);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the list of diseases from Firestore
        CollectionReference diseasesRef = db.collection("diseases");
        diseasesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    adapter = new DiseaseAdapter(task.getResult());
                    recyclerView.setAdapter(adapter);

                    // Handle item click to show the custom disease information dialog
                    adapter.setOnItemClickListener(new DiseaseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Disease disease) {
                            showCustomDiseaseInfoDialog(disease);
                        }
                    });
                }

            }
        });
    }

    private void showCustomDiseaseInfoDialog(final Disease disease) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.admin_sheet_select_disease);

        TextView txtDiseaseName = dialog.findViewById(R.id.txtDiseaseName);
        TextView txtSymptoms = dialog.findViewById(R.id.txtSymptoms);
        TextView txtCauses = dialog.findViewById(R.id.txtCauses);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnDelete = dialog.findViewById(R.id.btnDelete);

        // Populate the dialog UI with disease information
        txtDiseaseName.setText(disease.getName());
        txtSymptoms.setText(disease.getSymptoms());
        txtCauses.setText(disease.getCauses());

        // Handle "Update" and "Delete" actions as needed
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Update" action for the selected disease
                // You can open an edit form or perform the update action here
                // ...

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDiseaseFromDatabase(disease.getName()); // Pass the disease name
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void deleteDiseaseFromDatabase(final String diseaseName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference diseasesRef = db.collection("diseases");

        // Delete the disease document with the specified diseaseName
        diseasesRef.document(diseaseName).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle successful deletion from the database

                        // You don't need to manually remove the disease from the local list
                        // Update the adapter to reflect the removal of the disease
                        refreshDataAndUpdateAdapter(diseasesRef);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle deletion failure
                        // You might want to display an error message or log the error
                    }
                });
    }

    public void onAddDiseaseClick(View view) {
        // Show the "Add Disease" dialog when the button is clicked
        showAddDiseaseDialog();
    }

    private void showAddDiseaseDialog() {
        // Create a custom dialog for adding a new disease
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_disease, null);
        dialogBuilder.setView(dialogView);

        // Add your dialog layout's components and handle the addition process
        EditText editTextDiseaseName = dialogView.findViewById(R.id.editTextDiseaseName);
        EditText editTextSymptoms = dialogView.findViewById(R.id.editTextSymptoms);
        EditText editTextCauses = dialogView.findViewById(R.id.editTextCauses);

        // Handle the "Add" button click
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the values from the EditText fields
                String diseaseName = editTextDiseaseName.getText().toString();
                String symptoms = editTextSymptoms.getText().toString();
                String causes = editTextCauses.getText().toString();

                // Add the disease to Firestore with diseaseName as the document ID
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference diseasesRef = db.collection("diseases");

                // Create a reference to the document with the diseaseName as the ID
                DocumentReference diseaseDocRef = diseasesRef.document(diseaseName);

                // Create a Disease object with the provided information
                Disease newDisease = new Disease(diseaseName, symptoms, causes);

                // Set the data for the document
                diseaseDocRef.set(newDisease)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Handle a successful addition, if needed

                                // Refresh the data and update the adapter
                                refreshDataAndUpdateAdapter(diseasesRef);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle an error, if needed
                            }
                        });

                // Close the dialog
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialogBuilder.create().show();
    }

    private void refreshDataAndUpdateAdapter(final CollectionReference diseasesRef) {
        // Fetch the updated list of diseases from Firestore
        diseasesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    diseases.clear();  // Clear the local list
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Disease disease = document.toObject(Disease.class);
                        diseases.add(disease);  // Add the fetched disease to the local list
                    }
                    adapter.updateData(task.getResult());  // Notify the adapter of the data change
                }
            }
        });
    }
}
