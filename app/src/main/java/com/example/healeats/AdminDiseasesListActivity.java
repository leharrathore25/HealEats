//package com.example.healeats;
//
//import static android.content.ContentValues.TAG;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AdminDiseasesListActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private FirebaseFirestore db;
//    private DiseaseListAdapter adapter;
//    private List<Disease> diseases = new ArrayList<>();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_diseases_list);
//        db = FirebaseFirestore.getInstance();
//        recyclerView = findViewById(R.id.recyclerViewDiseases);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new DiseaseListAdapter(new ArrayList<>(), this);
//        recyclerView.setAdapter(adapter);
//
//        loadDiseases();
//    }
//
////    private void loadDiseases() {
////        db.collection("diseases")
////                .get()
////                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
////                    @Override
////                    public void onSuccess(QuerySnapshot documentSnapshots) {
////                        List<Disease> diseases = new ArrayList<>(); // Use Disease objects instead of String
////                        for (QueryDocumentSnapshot document : documentSnapshots) {
////                            String diseaseName = document.getId();
////                            String symptoms = (String) document.get("Symptoms");
////                            String causes = (String) document.get("Causes");
////                            String recommendations = (String) document.get("Recommendations");
////
////                            // Create a Disease object
////                            Disease disease = new Disease(diseaseName, symptoms, causes, recommendations);
////                            disease.setExpanded(false);
////                            diseases.add(disease);
////                        }
////
////                        // Update the adapter with the new disease data
////                        adapter.updateDiseases(diseases);
////                    }
////                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error getting documents.", e);
//                    }
//                });
//    }
//
//    public void showAddDiseaseDialog(View view){
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_disease, null);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(dialogView)
//                .setTitle("Add Disease")
//                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Handle the "Add" button click, extract data from the dialog fields
//                        EditText etDiseaseName = dialogView.findViewById(R.id.editTextDiseaseName);
//                        EditText etSymptoms = dialogView.findViewById(R.id.editTextSymptoms);
//                        EditText etCauses = dialogView.findViewById(R.id.editTextCauses);
//                        EditText etRecommendations = dialogView.findViewById(R.id.editTextRecommendations);
//
//                        // Extract the disease details and add to the database
//                        String diseaseName = etDiseaseName.getText().toString();
//                        String symptoms = etSymptoms.getText().toString();
//                        String causes = etCauses.getText().toString();
//                        String recommendations =etRecommendations.getText().toString();
//
//                        Map<String, Object> diseaseData = new HashMap<>();
//                        diseaseData.put("Symptoms", symptoms);
//                        diseaseData.put("Causes", causes);
//                        diseaseData.put("Recommendations", recommendations);
//
//                        // Add the disease details to the "diseases" collection in Firestore
//                        db.collection("diseases")
//                                .document(diseaseName)  // Use disease name as document ID
//                                .set(diseaseData)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        // Successful addition of disease details
//                                        Toast.makeText(AdminDiseasesListActivity.this, "Disease added successfully", Toast.LENGTH_SHORT).show();
//                                        loadDiseases();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // Handle errors
//                                        Toast.makeText(AdminDiseasesListActivity.this, "Failed to add disease", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Cancel the dialog
//                        dialog.dismiss();
//                    }
//                });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//}