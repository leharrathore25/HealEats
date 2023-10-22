package com.example.healeats;

import java.util.Arrays;

public class Disease {
    private String name;
    private String symptoms;
    private String causes;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }




    public Disease() {

    }


    public Disease(String name, String symptoms, String causes) {
        this.name = name;
        this.symptoms = symptoms;
        this.causes = causes;
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



}
