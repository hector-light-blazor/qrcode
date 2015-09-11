package com.example.noe.qrcode;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by noe on 8/8/15.
 */
public class course {
    private String id;
    private String [] id_list;
    private String name;
    private String [] name_course;
    private String number;
    private String [] number_list;
    private String hours;
    private String [] hours_list;
    private String TAG_ID = "id_course";
    private String TAG_NAME = "course_name";
    private String TAG_NUMBER = "course_number";
    private String TAG_HOURS = "course_hours";
    private JSONArray returnData;
    public course()
    {
        name = "";
        number = "";
        hours = "";
        id = "";

    }
    public course(String n, String nmb, String h)
    {
        name = n;
        number = nmb;
        hours = h;
    }
    public void setSizeName(int l){name_course = new String[l]; setSizeHours(name_course.length); setSizeNumber(name_course.length);}
    public void setSizeId(int i){id_list = new String[i];}
    public void setSizeHours(int i){hours_list = new String[i];}
    public void setSizeNumber(int i){number_list = new String[i];}
    public void setNameArray(int i, String value){name_course[i] = value;}
    public void setIdArray(int i, String value){id_list[i] = value;}
    public void setHoursArray(int i, String value){hours_list[i] = value;}
    public void setNumberArray(int i, String value){number_list[i] = value;}
    public void setID(String i){id = i;}
    public void setName(String n){name = n;}
    public void setNumber(String nmb){number = nmb;}
    public void setHours(String h){hours = h;}
    public void setJSONARRAY(JSONArray d){returnData = d;}
    public String getID(){return id;}
    public String getName(){return name;}
    public String getNumber(){return number;}
    public String getHours(){return hours;}
    public String getTAGID(){return TAG_ID;}
    public String getTAGNAME(){return TAG_NAME;}
    public String getTAGNUMBER(){return TAG_NUMBER;}
    public String getTAGHOURS(){return TAG_HOURS;}
    public JSONArray getJSONArray(){return returnData;}
    public String [] getNameFullArray(){return name_course;}
    public String getNameArray(int i){return name_course[i];}
    public String getHoursArray(int i){return hours_list[i];}
    public String getNumberArray(int i){return number_list[i];}
    public String getIdArray(int i){return id_list[i];}

}
