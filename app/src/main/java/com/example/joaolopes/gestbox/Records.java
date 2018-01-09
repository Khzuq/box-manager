package com.example.joaolopes.gestbox;

/**
 * Created by joaolopes on 08/01/18.
 */

public class Records {
    private int id, weight;
    private String modality, student, date;

    public Records(int id, int weight, String modality, String student, String date) {
        this.id = id;
        this.weight = weight;
        this.modality = modality;
        this.student = student;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public String getModality() {
        return modality;
    }

    public String getStudent() {
        return student;
    }

    public String getDate() {
        return date;
    }
}
