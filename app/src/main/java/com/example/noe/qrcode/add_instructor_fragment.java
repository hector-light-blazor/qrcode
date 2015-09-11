package com.example.noe.qrcode;

import android.app.DatePickerDialog;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
/**
 * Created by noe on 8/10/15.
 */
public class add_instructor_fragment extends ActionBarActivity implements View.OnClickListener, View.OnFocusChangeListener {
    ImageView photo, qr;
    ImageButton connectedB;
    Instructor instructor;
    EditText fname, mname, lname, email, phone, pid, dob, agency;
    private Bitmap uploadPIC;
    private boolean took = false;
    private boolean qr_generate = false;
    final int TAKE_PICTURE = 1;
    final int PIC_CROP = 2;
    private ProgressDialog pd;
    private Uri imageUri, selectedImage;
    private Camera camera;
    private int cameraId = 0;
    int count = 0;
    boolean first = false;
    private int mYear, mMonth, mDay;
    private final static String DEBUG_TAG = "MakePhotoActivity";
    private ProgressDialog ringProgressDialog;

    private Handler handler = new Handler();


    private Runnable runnable = new Runnable()
    {

        public void run() {


            if (count == 10) {
                handler.removeCallbacks(runnable);

                if (isConnected() && !first) {
                    connectedB.setImageResource(R.drawable.connected);
                    Toast.makeText(getApplicationContext(), "Connection Successful", Toast.LENGTH_SHORT).show();
                    first = true;
                } else if (!first) {
                    connectedB.setImageResource(R.drawable.disconnected);
                    try{
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                    }catch(Exception e)
                    {

                    }
                    first = true;

                }

            } else if (count % 2 == 0) {
                count++;
                connectedB.setImageResource(R.drawable.connected);
            } else {
                count++;
                connectedB.setImageResource(R.drawable.disconnected);
            }

            handler.postDelayed(this, 800);
        }


    };
    public boolean isConnected() {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;
        }catch(Exception e)
        {

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_save:
                saveInfo();
                break;
            case R.id.action_cancel:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_add_instructor);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399CC")));
        //Setting Up all the Edit Text
        fname = (EditText) findViewById(R.id.fnameID);

        fname.requestFocus();
        mname = (EditText) findViewById(R.id.mnameID);
        lname = (EditText) findViewById(R.id.lnameID);
        email = (EditText) findViewById(R.id.emailID);
        phone = (EditText) findViewById(R.id.phoneID);
        pid = (EditText) findViewById(R.id.pidID);
        dob = (EditText) findViewById(R.id.dobID);
        agency = (EditText) findViewById(R.id.agencyID);

        //Setting up all the Image Button

        connectedB = (ImageButton) findViewById(R.id.imageButton);

        //Setting up all the ImageView
        photo = (ImageView) findViewById(R.id.photo);
        qr = (ImageView) findViewById(R.id.qr);

        //Setting Up all the Listeners...
        dob.setOnFocusChangeListener(this);
        photo.setOnClickListener(this);
        runnable.run();
        initialize();

    }


    public void initialize() {
        //Start to check if there is any camera

        if (!getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(getApplicationContext(), "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(getApplicationContext(), "No front facing camera found.", Toast.LENGTH_LONG).show();
            } else {
                camera = Camera.open(cameraId);
            }
        }
    }
    public void saveInfo()
    {
        if (!validate()) { //Check that nothing is blank....
            Toast.makeText(getApplicationContext(), "Fill all the data", Toast.LENGTH_LONG).show();
        } else {
            ringProgressDialog = ProgressDialog.show(getApplicationContext(), "Please wait ...", "Uploading Information ...", true);
            ringProgressDialog.setCancelable(true);
            //Initialize instructor class

            instructor.setFirstName(fname.getText().toString());
            instructor.setMiddleName(mname.getText().toString());
            instructor.setLastName(lname.getText().toString());
            instructor.setEmail(email.getText().toString());
            instructor.setPhone(phone.getText().toString());
            instructor.setPid(pid.getText().toString());
            instructor.setDOB(dob.getText().toString());
            instructor.setAgency(agency.getText().toString());

            try {
                JSONObject toSend = new JSONObject();
                toSend.put("fname", instructor.getFirstName());
                toSend.put("mname", instructor.getMiddleName());
                toSend.put("lname", instructor.getLastName());
                toSend.put("email", instructor.getEmail());
                toSend.put("phone", instructor.getPhone());
                toSend.put("pid", instructor.getPid());
                toSend.put("dob", instructor.getDOB());
                toSend.put("agency", instructor.getAgency());



                JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/add_instructor.php");

                JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
                if (t.get("msg") == 0) {
                    instructor.setId(t.getInt("id"));
                    // Toast.makeText(getApplicationContext(), "Successfull id is " + instructor.getStringId(), Toast.LENGTH_LONG).show();
                    fname.setText("");
                    mname.setText("");
                    lname.setText("");
                    email.setText("");
                    phone.setText("");
                    pid.setText("");
                    dob.setText("");
                    agency.setText("");
                    // Toast.makeText(getApplicationContext(), "Generating QR CODE Please Wait..", Toast.LENGTH_LONG).show();
                    generateQR();
                } else {
                    if(ringProgressDialog.isShowing()) {
                        ringProgressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Unsuccessfull, Please Try Again.", Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        // intent.putExtra(MediaStore.EXTRA_OUTPUT,
        //       Uri.fromFile(photo));
        //imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(DEBUG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case TAKE_PICTURE:
                    if (resultCode == Activity.RESULT_OK) {
                        //Uri imageUri;
                        selectedImage = data.getData();
                        getApplicationContext().getContentResolver().notifyChange(selectedImage, null);

                        ContentResolver cr = getApplicationContext().getContentResolver();
                        Bitmap bitmap;
                        try {
                            bitmap = android.provider.MediaStore.Images.Media
                                    .getBitmap(cr, selectedImage);


                            instructor.setPhotoPath(getRealPathFromURI(selectedImage));
                           Intent cropIntent = new Intent("com.android.camera.action.CROP");
                            //indicate image type and Uri
                            cropIntent.setDataAndType(selectedImage, "image/*");
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
                            Toast.makeText(getApplicationContext(), "Failed to load", Toast.LENGTH_SHORT)
                                    .show();
                            Log.e("Camera", e.toString());
                        }
                    }
                case PIC_CROP: {
                    //get the returned data
                    Bundle extras = data.getExtras();
                    //get the cropped bitmap
                    uploadPIC = extras.getParcelable("data");
                    instructor.setCropPath(getRealPathFromURI(getImageUri(getApplicationContext(), uploadPIC)));
                    instructor.setPhoto(uploadPIC);

                    took = true;


                    photo.setImageBitmap(uploadPIC);

                }
            }

        } catch (Exception e) {

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }
    public byte[] getBytest(String fileName)
    {
        File file = new File(fileName);
        int length = (int) file.length();
        try
        {
            BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = new byte[length];
            reader.read(bytes, 0, length);
            reader.close();
            return bytes;
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        if (v == photo) {
            //takePhoto(); //Executes Function to take photo..
            String path = Environment.getExternalStorageDirectory().toString()+"/Documents";
            Log.d("Files", "Path: " + path);
            File f = new File(path);
            File file[] = f.listFiles();
            Log.d("Files", "Size: "+ file.length);
            for (int i=0; i < file.length; i++)
            {
                instructor.set64pdf(Base64.encodeToString(getBytest(path + "/" + file[i].getName()), Base64.DEFAULT));
                Log.d("Files", "FileName:" + file[i].getName());
            }

        }


    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            //Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(getApplicationContext(),
            new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
            int monthOfYear, int dayOfMonth) {
            //Display Selected date in textbox

             dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                }
            }, mYear, mMonth, mDay);
             dpd.show();


        }else {
            //Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
        }
    }

    public void generateQR() {
        //Generates QR Code once the information is uploaded...
        //Find screen size
        WindowManager manager = (WindowManager) getApplicationContext().getSystemService(getApplicationContext().WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x / 10;
        int height = point.y / 10;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(instructor.getStringId(),
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {

            //now that we have bitmap now lets send it to the server....

            instructor.setQR(qrCodeEncoder.encodeAsBitmap());
            //MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "QR Test", "GENERATING QR");
            Uri t = getImageUri(getApplicationContext(), instructor.getQR());

            String path = getRealPathFromURI(t);
            instructor.setQRPath(path);
            qr.setImageURI(t);

            if (took) {


                UploadImage PH = new UploadImage(instructor.getQR(), instructor.getPhoto(), instructor.getQRName(), instructor.getPhotoName(), instructor.getStringId());
                PH.execute();
            }else
            {
                UploadImage nophoto = new UploadImage(instructor.getQR(), instructor.getQRName(), instructor.getStringId());
                nophoto.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void deleteFile()
    {
        try
        {
        File file = new File(instructor.getPhotoPath());
        file.delete();
        File fl = new File(instructor.getQRPath());
        fl.delete();
        File cr = new File(instructor.getCropPath());
        cr.delete();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    private boolean validate() {
        if(fname.getText().toString().trim().equals(""))
            return false;
        else if(lname.getText().toString().trim().equals(""))
            return false;
        else if(email.getText().toString().trim().equals(""))
            return false;
        else if(phone.getText().toString().trim().equals(""))
            return false;
        else if(pid.getText().toString().trim().equals(""))
            return false;
        else if(dob.getText().toString().trim().equals(""))
            return false;
        else if(agency.getText().toString().trim().equals(""))
            return false;
        return true;
    }

    private class UploadImage extends AsyncTask<Void, Void, Void> {
        private Bitmap bitmap;
        private Bitmap photo;
        private String name;
        private String pname;
        private String id;
        private String section;


        public UploadImage(Bitmap b, String n, String id) {
            bitmap = b;
            name = n;
            this.id = id;
        }
        public UploadImage(Bitmap b, Bitmap photo, String n, String pn, String id)
        {
            this.bitmap = b;
            this.photo = photo;
            this.id  = id;
            this.name = n;
            this.pname = pn;
        }



        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            String encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

            section = "qr";
            if(took)
            {
                ByteArrayOutputStream st = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 90, st);

                String encodedPhoto = Base64.encodeToString(st.toByteArray(), Base64.DEFAULT);
                dataToSend.add(new BasicNameValuePair("photo", encodedPhoto));
                dataToSend.add(new BasicNameValuePair("photoname", pname));
                section = "photo";
            }

            dataToSend.add(new BasicNameValuePair("section", section));
            dataToSend.add(new BasicNameValuePair("qr", instructor.get64pdf()));
            dataToSend.add(new BasicNameValuePair("name", "test.pdf"));
            dataToSend.add(new BasicNameValuePair("id", id));



            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
            HttpPost post = new HttpPost("http://gis.lrgvdc911.org/php/qr_class/add_qr_image_instructor.php");

            try
            {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);

                HttpResponse response;
                response = client.execute(post);
                String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());
                Log.d("Response QR", resFromServer);
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(ringProgressDialog.isShowing())
            {
                ringProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Finish Uploading", Toast.LENGTH_LONG).show();
                deleteFile();

            }

        }

        private HttpParams getHttpRequestParam() {
            HttpParams httpparams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpparams, 1000 * 30);
            HttpConnectionParams.setSoTimeout(httpparams, 1000 * 30);
            return httpparams;
        }


    }
}
