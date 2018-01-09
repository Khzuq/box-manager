package com.example.joaolopes.gestbox;

/**
 * Created by joaolopes on 04/01/18.
 */

public class Trainings {
    private int id;
    private String warmup, skill, wod, data;

    public Trainings(int id, String warmup, String skill, String wod, String data) {
        this.id = id;
        this.warmup = warmup;
        this.skill = skill;
        this.wod = wod;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getWarmup() {
        return warmup;
    }

    public String getSkill() {
        return skill;
    }

    public String getWod() {
        return wod;
    }

    public String getData() {
        return data;
    }
}
