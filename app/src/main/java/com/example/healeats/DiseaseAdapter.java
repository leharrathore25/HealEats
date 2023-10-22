package com.example.healeats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.ViewHolder> {
    private QuerySnapshot mDiseaseList;
    private OnItemClickListener mListener;

    public DiseaseAdapter(QuerySnapshot diseaseList) {
        mDiseaseList = diseaseList;
    }


    public void updateData(QuerySnapshot diseaseList) {
        mDiseaseList = diseaseList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Disease disease); // Pass the entire Disease object
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView diseaseName;

        public ViewHolder(View itemView) {
            super(itemView);
            diseaseName = itemView.findViewById(R.id.textDiseaseName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            QueryDocumentSnapshot document = (QueryDocumentSnapshot) mDiseaseList.getDocuments().get(position);
                            Disease disease = document.toObject(Disease.class);
                            mListener.onItemClick(disease); // Pass the entire Disease object
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.disease_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QueryDocumentSnapshot document = (QueryDocumentSnapshot) mDiseaseList.getDocuments().get(position);
        holder.diseaseName.setText(document.getId());
    }

    @Override
    public int getItemCount() {
        return mDiseaseList.size();
    }
}



