package com.example.joaolopes.gestbox;

/**
 * Created by joaolopes on 06/01/18.
 */

public class Classes {
    private int id;
    private String classe_name;
    private String teacher;
    private int students;
    private int max_students;
    private String data;

    public Classes(int id, String classe_name, String teacher, int students, int max_students, String data) {
        this.id = id;
        this.classe_name = classe_name;
        this.teacher = teacher;
        this.students = students;
        this.max_students = students;
        this.data = data;
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

    public int getStudents() {
        return students;
    }

    public int getMax_students() {
        return max_students;
    }

    public String getData() {
        return data;
    }
}
