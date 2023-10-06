package com.example.healeats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiseaseListAdapter extends RecyclerView.Adapter<DiseaseListAdapter.ViewHolder> {

    private List<String> diseaseNames;
    private Context context;

    public DiseaseListAdapter(List<String> diseaseNames, Context context) {
        this.diseaseNames = diseaseNames;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_disease, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String diseaseName = diseaseNames.get(position);
        holder.diseaseTextView.setText(diseaseName);
    }

    @Override
    public int getItemCount() {
        return diseaseNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView diseaseTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            diseaseTextView = itemView.findViewById(R.id.diseaseTextView);
        }
    }
}