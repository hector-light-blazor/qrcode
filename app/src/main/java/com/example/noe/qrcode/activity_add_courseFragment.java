package com.example.noe.qrcode;

import android.app.Activity;
import android.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONObject;


/**
 * A placeholder fragment containing a simple view.
 */
public class activity_add_courseFragment extends ActionBarActivity implements View.OnClickListener {
    EditText courseNumber, courseName, courseHours;
    ImageButton  connectedB;
    int count = 0;
    boolean first = false;


    course c;

    private Handler handler = new Handler();


    private Runnable runnable = new Runnable()
    {

        public void run() {

//testing this out
                if (count == 10) {
                    handler.removeCallbacks(runnable);
                    if (isConnected() && !first) {
                        connectedB.setImageResource(R.drawable.connected);
                        Toast.makeText(getApplicationContext(), "Connection Successful", Toast.LENGTH_SHORT).show();
                        first = true;

                    } else if (!first) {
                        connectedB.setImageResource(R.drawable.disconnected);
                              try{
                                 Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
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

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_add_course);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399CC")));
        connectedB = (ImageButton) findViewById(R.id.connectionID);
        courseNumber = (EditText) findViewById(R.id.courseNumberId);
        courseName = (EditText) findViewById(R.id.courseTitleId);
        courseHours = (EditText) findViewById(R.id.courseHours);


        runnable.run();

    }

    public boolean isConnected() {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
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
                first = true;
                handler.removeCallbacks(runnable);
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void saveInfo()
    {
        if (!validate()) { //Check that nothing is blank....
            Toast.makeText(this, "Fill all the data", Toast.LENGTH_LONG).show();
        } else {
            // call AsynTask to perform network operation on separate thread
            c = new course();

            c.setName(courseName.getText().toString());
            c.setNumber(courseNumber.getText().toString());
            c.setHours(courseHours.getText().toString());
            //Send Information...
            try {
                JSONObject toSend = new JSONObject();
                toSend.put("name", c.getName());
                toSend.put("number", c.getNumber());
                toSend.put("hour", c.getHours());

                JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/add_course.php");

                JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
                if(t.get("msg") == 0) {
                    Toast.makeText(this, "Successfull", Toast.LENGTH_LONG).show();
                    courseName.setText("");
                    courseNumber.setText("");
                    courseHours.setText("");

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
    @Override
    public void onClick(View v) {


    }
    private boolean validate(){
        if(courseName.getText().toString().trim().equals(""))
            return false;
        else if(courseNumber.getText().toString().trim().equals(""))
            return false;
        else return !courseHours.getText().toString().trim().equals("");
    }
}
