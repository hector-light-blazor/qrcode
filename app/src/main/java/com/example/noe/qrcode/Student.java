package com.example.noe.qrcode;

import android.graphics.Bitmap;
import android.net.Uri;



/**
 * Created by noe on 8/19/15.
 */
public class Student {
    private String id;
    private String fname;
    private String mname;
    private String lname;
    private String pid;
    private String email;
    private String dob;
    private String agency;
    private String phone;
    private Uri pic;
    private Bitmap pc;
    private Uri qr;
    private Bitmap q;
    private String picName;
    private String qrName;

    public Student()
    {
        fname = "";
        mname = "";
        lname = "";
        pid = "";
        email = "";
        dob = "";
        agency = "";
        phone = "";
        picName = "";
        qrName = "";
        pic = null;
        qr = null;
        pc = null;
        q = null;

    }
    public String getFName(){return fname;}
    public String getMName(){return mname;}
    public String getLName(){return lname;}
    public String getPID(){return pid;}
    public String getEmail(){return email;}
    public String getDOB(){return dob;}
    public String getAgency(){return agency;}
    public String getPhone(){return phone;}
    public Uri getPic(){return pic;}
    public Bitmap getPict(){return pc;}
    public Bitmap getQr(){return q;}
    public String getId(){return id;}
    public String getPicName(){return picName = fname + "_" + lname + "_" + pid + ".jpg";}
    public String getQrName(){return qrName = "QR_img_" + id + "_" + pid + "_.png";}
    public void setFName(String n){fname = n;}
    public void setMName(String m){mname = m;}
    public void setLName(String l){lname = l;}
    public void setPid(String p){pid = p;}
    public void setEmail(String e){email = e;}
    public void setDob(String d){dob = d;}
    public void setAgency(String a){agency = a;}
    public void setPhone(String p){phone = p;}
    public void setUriPic(Uri p){pic = p;}
    public void setBitmapPic(Bitmap p){pc = p;}
    public void setUriQr(Uri q){qr = q;}
    public void setBitmapQr(Bitmap r){q = r;}
    public void setId(String i){id = i;}


}
