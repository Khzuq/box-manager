package com.example.joaolopes.gestbox;

/**
 * Created by joaolopes on 14/01/18.
 */

public class Modalities {
    private int id;
    private String modality;

    public Modalities(int id, String modality) {
        this.id = id;
        this.modality = modality;
    }

    public int getId() {
        return id;
    }

    public String getModality() {
        return modality;
    }
}
