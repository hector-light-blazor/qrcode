package com.example.noe.qrcode;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.Uri;
import com.google.zxing.BarcodeFormat;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by noe on 8/19/15.
 */
public class addStudent extends ActionBarActivity implements View.OnClickListener, View.OnFocusChangeListener{
    private EditText fname, mname, lname, pidN, email, dob, agency, phone;
    private ImageView pic, qr;
    private Student student;
    private int mYear, mMonth, mDay;
    final int TAKE_PICTURE = 1;
    final int PIC_CROP = 2;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save)
            sendInfoSetup();
        else if(item.getItemId() == R.id.action_cancel)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == pic)
            setUpPicCam();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student);

        //setting up Orientation landscape..
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Setup the Title Color....
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399CC")));

        //setting up student class constructor...
        student = new Student();

        //Setting up all The Edit Text
        fname = (EditText) findViewById(R.id.editText7);
        mname = (EditText) findViewById(R.id.editText8);
        lname = (EditText) findViewById(R.id.editText9);
        pidN = (EditText) findViewById(R.id.editText10);
        email = (EditText) findViewById(R.id.editText11);
        dob = (EditText) findViewById(R.id.editText12);
        agency = (EditText) findViewById(R.id.editText13);
        phone = (EditText) findViewById(R.id.editText14);

        //Set ImageViewss....
        pic = (ImageView) findViewById(R.id.imageView);
        qr = (ImageView) findViewById(R.id.imageView3);

        //Set Some Listeners..
        dob.setOnFocusChangeListener(this);
        pic.setOnClickListener(this);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
        {
            if(v == dob)
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //Display Selected date in textbox

                                dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                student.setDob(dob.getText().toString());

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        }

    }

    public void setUpPicCam()
    {

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        startActivityForResult(intent, TAKE_PICTURE);

    }
    public void generateQr()
    {
        WindowManager manager = (WindowManager) this.getSystemService(this.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x / 10;
        int height = point.y / 10;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(student.getId(),
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);

        try{
            student.setBitmapQr(qrCodeEncoder.encodeAsBitmap());
            qr.setImageBitmap(student.getQr());
            updateQRDatabase();

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public void sendInfoSetup()
    {
        if(!validate())
        {
            getInformationFromEditText();
            if(student.getPict() != null)
            {
                //Get Picture into base 64 String encode...
                try {
                    JSONObject toSend = new JSONObject();
                    toSend.put("option", "0");
                    toSend.put("fname", student.getFName());
                    toSend.put("mname", student.getMName());
                    toSend.put("lname", student.getLName());
                    toSend.put("email", student.getEmail());
                    toSend.put("phone", student.getPhone());
                    toSend.put("pid", student.getPID());
                    toSend.put("dob", student.getDOB());
                    toSend.put("agency", student.getAgency());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    student.getPict().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    String encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                    toSend.put("img", encodedImage);
                    toSend.put("name", student.getPicName());
                    JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/add_student.php");

                    JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
                    if(t.get("msg") == 0) {
                        student.setId(t.get("id").toString());
                        Toast.makeText(this, "Setting up QR Code... Please Wait", Toast.LENGTH_LONG).show();
                        generateQr();

                    }
                    else
                    {
                        Toast.makeText(this, "Unsuccessfull, Please Try Again.", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
            else
            {

                try {
                    JSONObject toSend = new JSONObject();
                    toSend.put("option", "1");
                    toSend.put("fname", student.getFName());
                    toSend.put("mname", student.getMName());
                    toSend.put("lname", student.getLName());
                    toSend.put("email", student.getEmail());
                    toSend.put("phone", student.getPhone());
                    toSend.put("pid", student.getPID());
                    toSend.put("dob", student.getDOB());
                    toSend.put("agency", student.getAgency());
                    JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/add_student.php");
                    JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
                    if(t.get("msg") == 0) {
                        student.setId(t.get("id").toString());
                        Toast.makeText(this, "Setting up QR Code... Please Wait", Toast.LENGTH_LONG).show();
                        generateQr();

                    }
                    else
                    {
                        Toast.makeText(this, "Unsuccessfull, Please Try Again.", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void updateQRDatabase()
    {
        try {
            JSONObject toSend = new JSONObject();
            toSend.put("option", "2");
            toSend.put("id", student.getId());
            toSend.put("qrname", student.getQrName());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            student.getQr().compress(Bitmap.CompressFormat.PNG, 100, stream);
            String encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
            toSend.put("qr", encodedImage);
            JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/add_student.php");
            JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
            if(t.get("msg") == 0) {


                Toast.makeText(this, "Information Has Been Uploaded.. Successfully", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Unsuccessfull, Please Try Again.", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public boolean validate()
    {
        if(fname.getText().toString().trim().equals("") && lname.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Please Fill Out First and Last name", Toast.LENGTH_LONG).show();
            return true;
        }
        else if(pidN.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Please Fill Out PID Number", Toast.LENGTH_LONG).show();
            return true;
        }
        else if(dob.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Please Fill Out Date of Birth", Toast.LENGTH_LONG).show();
            return true;
        }
        else if(agency.getText().toString().trim().equals(""))
        {
            Toast.makeText(this,"Please Fill out Agency Name", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    public void getInformationFromEditText()
    {
        student.setFName(fname.getText().toString());
        student.setMName(mname.getText().toString());
        student.setLName(lname.getText().toString());
        student.setPid(pidN.getText().toString());
        student.setEmail(email.getText().toString());
        student.setDob(dob.getText().toString());
        student.setAgency(agency.getText().toString());
        student.setPhone(phone.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case TAKE_PICTURE:
                    if (resultCode == Activity.RESULT_OK) {
                        //Uri imageUri;
                        student.setUriPic(data.getData());
                        this.getContentResolver().notifyChange(student.getPic(), null);

                        ContentResolver cr = this.getContentResolver();
                        Bitmap bitmap;
                        try {
                            bitmap = android.provider.MediaStore.Images.Media
                                    .getBitmap(cr, student.getPic());



                            Intent cropIntent = new Intent("com.android.camera.action.CROP");
                            //indicate image type and Uri
                            cropIntent.setDataAndType(student.getPic(), "image/*");
                            //set crop properties
                            cropIntent.putExtra("crop", "true");
                            //indicate aspect of desired crop
                            cropIntent.putExtra("aspectX", 1);
                            cropIntent.putExtra("aspectY", 1);
                            //indicate output X and Y
                            cropIntent.putExtra("outputX", 256);
                            cropIntent.putExtra("outputY", 256);
                            //retrieve data on return
                            cropIntent.putExtra("return-data", true);
                            //start the activity - we handle returning in onActivityResult
                            startActivityForResult(cropIntent, PIC_CROP);
                        } catch (Exception e) {
                            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                    .show();
                            Log.e("Camera", e.toString());
                        }
                    }
                case PIC_CROP: {
                    //get the returned data
                    Bundle extras = data.getExtras();
                    //get the cropped bitmap
                    Bitmap t = extras.getParcelable("data");
                    student.setBitmapPic(t);
                    pic.setImageBitmap(student.getPict());


                }
            }

        } catch (Exception e) {

        }
    }

}
