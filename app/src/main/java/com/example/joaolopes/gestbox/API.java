package com.example.joaolopes.gestbox;

/**
 * Created by joaolopes on 05/01/18.
 */

public class API {
    private static final String ROOT_URL = "http://lopes.zonaporto.com/BoxManager/";

    //Trainings
    public static final String URL_READ_TRAININGS = ROOT_URL + "class.Trainings.php?a=get-trainings";
    public static final String URL_CREATE_TRAININGS = ROOT_URL + "class.Trainings.php?a=insert-trainings";
    public static final String URL_DELETE_TRAININGS = ROOT_URL + "class.Trainings.php?a=delete-trainings";
    public static final String URL_EDIT_TRAININGS = ROOT_URL + "class.Trainings.php?a=edit-trainings";
    //Classes
    public static final String URL_CREATE_CLASSES = ROOT_URL + "class.Classes.php?a=insert-classes";
    public static final String URL_READ_CLASSES = ROOT_URL + "class.Classes.php?a=get-classes";
    public static final String URL_EDIT_CLASSES = ROOT_URL + "class.Classes.php?a=edit-classes";
    public static final String URL_DELETE_CLASSES = ROOT_URL + "class.Classes.php?a=delete-classes&id=";
    public static final String URL_READbyDAY_CLASSES = ROOT_URL + "class.Classes.php?a=get-classes-byday&data=";
    public static final String URL_ENTRY_CLASSES = ROOT_URL + "class.Classes.php?a=entry-classes";
    public static final String URL_USERS_CLASSES = ROOT_URL + "class.Classes.php?a=get-students-class";
    //Records
    public static final String URL_CREATE_RECORDS = ROOT_URL + "class.Records.php?a=insert-records";
    public static final String URL_READ_RECORDS = ROOT_URL + "class.Records.php?a=get-records";
    public static final String URL_EDIT_RECORDS = ROOT_URL + "class.Records.php?a=edit-records";
    public static final String URL_DELETE_RECORDS = ROOT_URL + "class.Records.php?a=delete-records&id=";
    //Teachers
    public static final String URL_READ_TEACHERS = ROOT_URL + "class.Teachers.php?a=get-teachers";
    //Modalities
    public static final String URL_READ_MODALITIES = ROOT_URL + "class.Modalities.php?a=get-modalities";
    //Users
    public static final String URL_LOGIN = ROOT_URL + "class.Users.php?a=login";
    public static final String URL_PW = ROOT_URL + "class.Users.php?a=change-pw";
}