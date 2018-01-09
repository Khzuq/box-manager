package com.example.joaolopes.gestbox;

/**
 * Created by joaolopes on 05/01/18.
 */

public class API {
    private static final String ROOT_URL = "http://10.0.2.2:80/BoxManager/";

    //Trainings
    public static final String URL_READ_TRAININGS = ROOT_URL + "class.Trainings.php?a=get-trainings";
    //Classes
    public static final String URL_READ_CLASSES = ROOT_URL + "class.Classes.php?a=get-classes";
    //Records
    public static final String URL_READ_RECORDS = ROOT_URL + "class.Records.php?a=get-records";
    public static final String URL_EDIT_RECORDS = ROOT_URL + "class.Records.php?a=edit-records";
    public static final String URL_DELETE_RECORDS = ROOT_URL + "class.Records.php?a=delete-records";

}
