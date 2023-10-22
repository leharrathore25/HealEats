package com.example.healeats;

import java.util.Arrays;

public class Disease {
    private String name;
    private String symptoms;
    private String causes;
    private String recommendations;
    private String symptomTags;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getSymptomTags() {
        return symptomTags;
    }

    public void setSymptomTags(String symptomTags) {
        this.symptomTags = symptomTags;
    }


    public Disease(String name, String symptoms, String causes, String recommendations, String symptomTags) {
        this.name = name;
        this.symptoms = symptoms;
        this.causes = causes;
        this.recommendations = recommendations;
        this.symptomTags = Arrays.asList(symptomTags.split(",\\s*")).toString();
        isSelected=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getCauses() {
        return causes;
    }

    public void setCauses(String causes) {
        this.causes = causes;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

}
