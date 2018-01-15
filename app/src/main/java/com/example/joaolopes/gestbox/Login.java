package com.example.joaolopes.gestbox;

/**
 * Created by joaolopes on 15/01/18.
 */

public class Login {
    private static int id;
    private static int id_type;
    private static String nome;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Login.id = id;
    }

    public static int getId_type() {
        return id_type;
    }

    public static void setId_type(int id_type) {
        Login.id_type = id_type;
    }

    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        Login.nome = nome;
    }
}
