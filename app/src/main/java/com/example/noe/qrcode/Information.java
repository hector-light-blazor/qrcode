package com.example.noe.qrcode;

/**
 * Created by noe on 8/18/15.
 */
public class Information {
    private String beginDate;
    private String endDate;
    private String location;
    private String sizeLimit;
    private String providerName;

    public Information()
    {
        beginDate = "";
        endDate = "";
        location = "";
        sizeLimit = "";
        providerName = "";
    }
    public Information(String b, String e, String l, String s, String p)
    {
        beginDate = b;
        endDate = e;
        location = l;
        sizeLimit = s;
        providerName = p;
    }

    public String getBeginDate(){return beginDate;}
    public String getEndDate(){return endDate;}
    public String getLocation(){return location;}
    public String getSizeLimit(){return sizeLimit;}
    public String getProviderName(){return providerName;}

    public void setBeginDate(String b){beginDate = b;}
    public void setEndDate(String e){endDate = e;}
    public void setLocation(String l){location = l;}
    public void setSizeLimit(String s){sizeLimit = s;}
    public void setProviderName(String p){providerName = p;}
}
