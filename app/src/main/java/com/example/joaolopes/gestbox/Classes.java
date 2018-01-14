package com.example.joaolopes.gestbox;

/**
 * Created by joaolopes on 06/01/18.
 */

public class Classes {
    private int id;
    private String classe_name;
    private String teacher;
    //private int students;
    private int max_students;
    private String data, timer;

    public Classes(int id, String classe_name, String teacher, int max_students, String data, String timer) {
        this.id = id;
        this.classe_name = classe_name;
        this.teacher = teacher;
        //this.students = students;
        this.max_students = max_students;
        this.data = data;
        this.timer = timer;
    }

    public int getId() {
        return id;
    }

    public String getClasse_name() {
        return classe_name;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getMax_students() {
        return max_students;
    }

    public String getData() {
        return data;
    }

    public String getTimer() {
        return timer;
    }
}
