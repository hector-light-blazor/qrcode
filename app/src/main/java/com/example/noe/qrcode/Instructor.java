package com.example.noe.qrcode;

import android.graphics.Bitmap;

/**
 * Created by noe on 8/11/15.
 */
public class Instructor {
    private String fname;
    private String [] fname_list;
    private String [] mname_list;
    private String [] lname_list;
    private String [] id_list;
    private String [] dob_list;
    private String [] combine;
    private String mname;
    private String lname;
    private String email;
    private String phone;
    private String pid;
    private String dob;
    private String agency;
    private int id;
    private Bitmap qr;
    private Bitmap photo;
    private String pathp;
    private String qrpath;
    private String croppath;
    private String generateqrname;
    private String generatephotoname;
    private String pdfencoded;
    public final static String TAG_ID = "id_instructor";
    public final static String TAG_FNAME = "fname";
    public final static String TAG_MNAME = "mname";
    public final static String TAG_LNAME = "lname";
    public final static String TAG_DOB = "dob";


    public Instructor()
    {
        fname = "";
        mname = "";
        lname = "";
        email = "";
        phone = "";
        pid = "";
        dob = "";
        agency = "";
    }

    public Instructor(String f, String m, String l, String e, String ph, String p, String d, String a)
    {
        fname = f;
        mname = m;
        lname = l;
        email = e;
        phone = ph;
        pid = p;
        dob = d;
        agency = a;
    }
    //Set and Get Methods for this class..


    public String getFirstName(){return fname;}
    public String getMiddleName(){return mname;}
    public String getLastName(){return lname;}
    public String getEmail(){return email;}
    public String getPhone(){return phone;}
    public String getPid(){return pid;}
    public String getDOB(){return dob;}
    public String getAgency(){return agency;}
    public int getId(){return id;}
    public String getStringId(){return Integer.toString(id);}
    public Bitmap getQR(){return qr;}
    public Bitmap getPhoto(){return photo;}
    public String getPhotoPath(){return pathp;}
    public String getQRPath(){return qrpath;}
    public String getCropPath(){return croppath;}
    public String getQRName(){return generateqrname = "QR_" + getStringId() + "_" + getFirstName() + "_.png";}
    public String getPhotoName(){return generatephotoname = getFirstName() + "_" + getLastName() + "_" + getStringId() + ".JPG";}
    public String get64pdf(){return pdfencoded;}
    public String [] getCombineArray(){return combine;}
    public String getFnameArray(int i){return fname_list[i];}
    public String getMnameArray(int i){return mname_list[i];}
    public String getLnameArray(int i){return lname_list[i];}
    public String getIdArray(int i){return id_list[i];}
    public void setArraySize(int s){
        fname_list = new String[s];
        mname_list = new String[s];
        lname_list = new String[s];
        id_list = new String[s];
        dob_list = new String[s];
        combine = new String[s];
    }

    public void setFirstName(String f){fname = f;}
    public void setMiddleName(String m){mname = m;}
    public void setLastName(String l){lname = l;}
    public void setEmail(String e){email = e;}
    public void setPhone(String p){phone = p;}
    public void setPid(String p){pid = p;}
    public void setDOB(String d){dob = d;}
    public void setAgency(String a){agency = a;}
    public void setId(int i){id = i;}
    public void setPhoto(Bitmap p){photo = p;}
    public void setQR(Bitmap q){qr = q;}
    public void setPhotoPath(String p){pathp = p;}
    public void setQRPath(String p){qrpath = p;}
    public void setCropPath(String p){croppath = p;}
    public void set64pdf(String p){pdfencoded = p;}
    public void setFnameArray(int i, String value){fname_list[i] = value;}
    public void setMnameArray(int i, String value){mname_list[i] = value;}
    public void setLnameArray(int i, String value){lname_list[i] = value;}
    public void setIdArray(int i, String value){id_list[i] = value;}
    public void setDobArray(int i, String value){dob_list[i] = value;}
    public void setCombineArray(int i, String value){combine[i] = value;}

}
