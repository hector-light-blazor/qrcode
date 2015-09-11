package com.example.noe.qrcode;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by noe on 8/14/15.
 */
public class startClass extends ActionBarActivity implements View.OnClickListener, View.OnFocusChangeListener, AdapterView.OnItemClickListener {
    private EditText courseName, courseNumber, courseHours,
    fName, mName, lName,
    beginDate, endDate, location, sizeLimit, providerName;
    private EditText emailS, dobS, pidS, courseS, emailI, dobI, pidI, phoneI, agencyI;
    private Button dialogButton, dialogBC, searchAllC, searchAllI, addInstructor;
    private int mYear, mMonth, mDay;
    private course cinfo;
    private Instructor instructor;
    private Information information;
    private ListView masterList;
    JSONArray courses = null;
    JSONArray instructors = null;
    final Context context = this;
    Dialog dialog;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the content view of the xml we choose to display..
        setContentView(R.layout.start_class);

        //Initialize all our objects from the xml file..
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399CC")));
        dialog = new Dialog(context);
        //Course Information setup EditText
        courseName = (EditText) findViewById(R.id.editText);
        courseNumber = (EditText) findViewById(R.id.editText2);
        courseHours = (EditText) findViewById(R.id.editText3);

        //Instructor Information....
        fName = (EditText) findViewById(R.id.editText4);
        mName = (EditText) findViewById(R.id.editText5);
        lName = (EditText) findViewById(R.id.editText6);

        //Class Information...
        beginDate = (EditText) findViewById(R.id.editText7);
        endDate = (EditText) findViewById(R.id.editText8);
        location = (EditText) findViewById(R.id.editText9);
        sizeLimit = (EditText)findViewById(R.id.editText10);
        providerName = (EditText) findViewById(R.id.editText11);

        //Set Listeners to Edit Text...
        beginDate.setOnFocusChangeListener(this);
        endDate.setOnFocusChangeListener(this);
        location.setOnFocusChangeListener(this);
        sizeLimit.setOnFocusChangeListener(this);
        providerName.setOnFocusChangeListener(this);

        //Create empty data for three classes...
        cinfo = new course();
        instructor = new Instructor();
        information = new Information();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start_course, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_save:

               information.setLocation(location.getText().toString());
               information.setSizeLimit(sizeLimit.getText().toString());
               information.setProviderName(providerName.getText().toString());
                if(validate2())
                {
                    Toast.makeText(getApplicationContext(), "Please submit new instructor to database", Toast.LENGTH_LONG).show();
                }
                else if(validate3()){
                    Toast.makeText(getApplicationContext(), "Please submit new course to database", Toast.LENGTH_LONG).show();
                }
                else{
                    if(!validate())
                    {
                        Toast.makeText(getApplicationContext(), "Fill Information Out", Toast.LENGTH_LONG).show();

                    }
                    else{
                        try {
                            JSONObject toSend = new JSONObject();
                            toSend.put("idc", cinfo.getID());
                            toSend.put("idins", instructor.getStringId());
                            toSend.put("bdate", information.getBeginDate());
                            toSend.put("endate",information.getEndDate());
                            toSend.put("location", information.getLocation());
                            toSend.put("slimit", information.getSizeLimit());
                            toSend.put("pname", information.getProviderName());
                            JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/add_start_class.php");

                            JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
                            if(t.get("msg") == 0) {
                                Log.d("json test",t.toString());
                                Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_LONG).show();
                                cinfo.setName(t.get("name").toString());
                                cinfo.setHours(t.get("hour").toString());
                                cinfo.setID(t.get("id").toString());
                                cinfo.setNumber(t.get("number").toString());

                                courseName.setText(cinfo.getName());
                                courseNumber.setText(cinfo.getNumber());
                                courseHours.setText(cinfo.getHours());
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Unsuccessfull, Please Try Again.", Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            case R.id.action_sinstructor:
                num = 1;
                searchInstructorDialog();
                break;
            case R.id.action_cancel:
                finish();
                break;
            case R.id.action_scourse:
                num = 2;
                searchCourseDialog();
                break;
            case R.id.action_acourse:
                addCourse();
                break;
            case R.id.action_ainstructor:
                addNewInstructor();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    private void searchInstructorDialog() //Display pop dialog...
    {
        instructor.setDOB("");
        instructor.setPid("");
        instructor.setEmail("");
        dialog.setContentView(R.layout.layout);
        dialog.setTitle("Search Instructor...  Please choose one or All");
        emailS = (EditText) dialog.findViewById(R.id.emailS);
        dobS = (EditText) dialog.findViewById(R.id.dobS);
        pidS = (EditText) dialog.findViewById(R.id.pidS);

        //Setting up Listeners for functionallity easier..
        emailS.setOnFocusChangeListener(this);
        dobS.setOnFocusChangeListener(this);
        pidS.setOnFocusChangeListener(this);

        searchAllI = (Button)dialog.findViewById(R.id.searchAllI);
        dialogButton = (Button) dialog.findViewById(R.id.dialogS);
        searchAllI.setOnClickListener(this);
        dialogButton.setOnClickListener(this);
        dialog.show();
    }
    private void searchCourseDialog()
    {
        dialog.setContentView(R.layout.layout_course);
        dialog.setTitle("Search Course... ");
        courseS = (EditText) dialog.findViewById(R.id.courseS);
        dialogBC = (Button) dialog.findViewById(R.id.dialogSC);
        searchAllC = (Button) dialog.findViewById(R.id.searchCAll);
        searchAllC.setOnClickListener(this);
        dialogBC.setOnClickListener(this);
        dialog.show();
    }
    public void addCourse()
    {
        if(courseName.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please Fill out CourseName", Toast.LENGTH_LONG).show();
            return;
        }
        else if(courseNumber.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please Fill out Course Number", Toast.LENGTH_LONG).show();
            return;
        }
        else if(courseHours.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please Fill out Course Hours", Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            try {
                String number = "";
                JSONObject toSend = new JSONObject();
                toSend.put("name", courseName.getText().toString() );
                toSend.put("hour", courseHours.getText().toString());
                toSend.put("number",courseNumber.getText().toString());
                JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/add_course_rid.php");
                JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
                if(t.getInt("msg") == 0)
                {
                    cinfo.setID(t.get("id").toString());
                    cinfo.setName(courseName.getText().toString());
                    cinfo.setNumber(courseNumber.getText().toString());
                    cinfo.setHours(courseHours.getText().toString());

                    Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_LONG).show();

                }else {

                    Toast.makeText(getApplicationContext(), "Sorry, No Records", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    public void addNewInstructor()
    {
        if(fName.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please Fill out First Name", Toast.LENGTH_LONG).show();
            return;
        }
        else if(lName.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please Fill out Last Name", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            //Create Dialog.. for this information...
            dialog.setContentView(R.layout.layout_add_instructor);
            dialog.setTitle("Add New Instructor... ");
            emailI = (EditText)dialog.findViewById(R.id.emailS);
            dobI = (EditText) dialog.findViewById(R.id.dobS);
            pidI = (EditText) dialog.findViewById(R.id.pidS);
            phoneI = (EditText) dialog.findViewById(R.id.phoneIDAS);
            agencyI = (EditText) dialog.findViewById(R.id.aidNewI);
            addInstructor = (Button) dialog.findViewById(R.id.dialogS);

            dobI.setOnFocusChangeListener(this);
            addInstructor.setOnClickListener(this);
            dialog.show();
        }
    }
    private boolean validate() {
        if(cinfo.getName().trim().equals("")) {
            return false;
        }
        else if(cinfo.getNumber().trim().equals(""))
        {
            Log.d("second", "0");
            return false;
        }
        else if(instructor.getFirstName().trim().equals("")) {
            Log.d("third", "0");
            return false;
        }
        else if(instructor.getLastName().trim().equals(""))
            return false;
        else if(information.getBeginDate().trim().equals(""))
            return false;
        else if(information.getEndDate().trim().equals(""))
            return false;
        else if(information.getLocation().trim().equals(""))
            return false;
        else if(information.getSizeLimit().trim().equals(""))
            return false;
        else if(information.getProviderName().trim().equals(""))
            return false;
        return true;
    }
    private boolean validate2() {
        if(instructor.getStringId().trim().equals(""))
            return true;
        return false;
    }
    private boolean validate3(){
        if(cinfo.getID().trim().equals(""))
            return true;
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v == dialogButton) //Instructor Search....
        {
            //Toast.makeText(getApplicationContext(), "Searching Instructor", Toast.LENGTH_LONG).show();
            instructor.setEmail(emailS.getText().toString());
            instructor.setDOB(dobS.getText().toString());
            instructor.setPid(pidS.getText().toString());
            dialog.dismiss();
            try {
                String number = "";
                JSONObject toSend = new JSONObject();
                toSend.put("dob", instructor.getDOB());
                toSend.put("pid", instructor.getPid());
                toSend.put("email", instructor.getEmail());
                if(instructor.getPid().length() > 0)
                    number = "0";
                else if(instructor.getDOB().length() > 0)
                    number = "1";
                else if(instructor.getEmail().length() > 0)
                    number = "2";
                toSend.put("number", number);
                JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/search_instructor.php");
                JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
                instructors = t.getJSONArray("msg");
                instructor.setArraySize(instructors.length());
                if(instructors.length() > 0) {
                    Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_LONG).show();
                    for(int i = 0; i < instructors.length(); i++)
                    {
                        JSONObject c = instructors.getJSONObject(i);

                        instructor.setFnameArray(i, c.getString(instructor.TAG_FNAME));
                        instructor.setMnameArray(i, c.getString(instructor.TAG_MNAME));
                        instructor.setLnameArray(i, c.getString(instructor.TAG_LNAME));
                        instructor.setDobArray(i, c.getString(instructor.TAG_DOB));
                        instructor.setIdArray(i, c.getString(instructor.TAG_ID));
                        instructor.setCombineArray(i, c.getString(instructor.TAG_FNAME) + " " + c.getString(instructor.TAG_LNAME) + " "
                        + "DOB: " + c.getString(instructor.TAG_DOB));

                    }
                    setUpInstructorListDialog();

                }else{
                    Toast.makeText(getApplicationContext(), "Sorry, No Records", Toast.LENGTH_LONG).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(v == dialogBC)
        {
            //Toast.makeText(getApplicationContext(), "Searching Course", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            try {
                JSONObject toSend = new JSONObject();
                toSend.put("number", courseS.getText().toString());
                JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/search_course.php");

                JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
                if(t.get("msg") == 0) {
                    Log.d("json test",t.toString());
                    Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_LONG).show();
                    cinfo.setName(t.get("name").toString());
                    cinfo.setHours(t.get("hour").toString());
                    cinfo.setID(t.get("id").toString());
                    cinfo.setNumber(t.get("number").toString());

                    courseName.setText(cinfo.getName());
                    courseNumber.setText(cinfo.getNumber());
                    courseHours.setText(cinfo.getHours());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Unsuccessfull, Please Try Again.", Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(v == searchAllC)
        {
           // Toast.makeText(getApplicationContext(), "Searching All Course's", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            try {
                JSONObject toSend = new JSONObject();
                JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/search_all_course.php");

                JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
                //Log.d("json all", t.toString());

                courses = t.getJSONArray("msg");
                cinfo.setJSONARRAY(courses);
                cinfo.setSizeName(courses.length());
                cinfo.setSizeId(courses.length());
                if(courses.length() > 0)
                {
                    Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_LONG).show();
                    for(int i = 0; i < courses.length(); i++)
                    {
                        JSONObject c = courses.getJSONObject(i);
                        cinfo.setNameArray(i, c.getString(cinfo.getTAGNAME()));

                        cinfo.setIdArray(i, c.getString(cinfo.getTAGID()));

                        cinfo.setNumberArray(i, c.getString(cinfo.getTAGNUMBER()));

                        cinfo.setHoursArray(i, c.getString(cinfo.getTAGHOURS()));
                    }
                    setUpCourseListDialog();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry, No Records", Toast.LENGTH_LONG).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(v == searchAllI)
        {
            dialog.dismiss();
            try {
                JSONObject toSend = new JSONObject();
                JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/search_all_instructor.php");

                JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
                //Log.d("json all", t.toString());

                instructors = t.getJSONArray("msg");
                instructor.setArraySize(instructors.length());
                if(instructors.length() > 0)
                {
                    Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_LONG).show();
                    for(int i = 0; i < instructors.length(); i++)
                    {
                        JSONObject c = instructors.getJSONObject(i);

                        instructor.setFnameArray(i, c.getString(instructor.TAG_FNAME));

                        instructor.setMnameArray(i, c.getString(instructor.TAG_MNAME));

                        instructor.setLnameArray(i, c.getString(instructor.TAG_LNAME));

                        instructor.setDobArray(i, c.getString(instructor.TAG_DOB));

                        instructor.setIdArray(i, c.getString(instructor.TAG_ID));

                        instructor.setCombineArray(i, c.getString(instructor.TAG_FNAME) + " " + c.getString(instructor.TAG_LNAME) + " "
                                + "DOB: " + c.getString(instructor.TAG_DOB));

                    }
                    setUpInstructorListDialog();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry, No Records", Toast.LENGTH_LONG).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(v == addInstructor)
        {
            instructor.setEmail(emailI.getText().toString());
            instructor.setPid(pidI.getText().toString());
            instructor.setDOB(dobI.getText().toString());
            instructor.setPhone(phoneI.getText().toString());
            instructor.setAgency(agencyI.getText().toString());
            dialog.dismiss();
            try {
                instructor.setFirstName(fName.getText().toString());
                instructor.setMiddleName(mName.getText().toString());
                instructor.setLastName(lName.getText().toString());

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

                } else {

                    Toast.makeText(getApplicationContext(), "Unsuccessfull, Please Try Again.", Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void setUpCourseListDialog()
    {
        dialog.setContentView(R.layout.layout_list);
        dialog.setTitle("Select Course's... ");
        masterList = (ListView) dialog.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cinfo.getNameFullArray());
        masterList.setAdapter(adapter);
        masterList.setOnItemClickListener(this);
        dialog.show();

    }
    public void setUpInstructorListDialog()
    {
        dialog.setContentView(R.layout.layout_list);
        dialog.setTitle("Select Instructors... ");
        masterList = (ListView) dialog.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,instructor.getCombineArray());
        masterList.setAdapter(adapter);
        masterList.setOnItemClickListener(this);
        dialog.show();

    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            if (v == beginDate) {
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

                                beginDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                information.setBeginDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
            else if(v == endDate)
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

                                endDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                information.setEndDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();

            }
            else if(v == dobS)
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

                                dobS.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
            else if(v == dobI)
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

                                dobI.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(num == 1)
            {
                instructor.setFirstName(instructor.getFnameArray(position));
                instructor.setMiddleName(instructor.getMnameArray(position));
                instructor.setLastName(instructor.getLnameArray(position));
                fName.setText(instructor.getFnameArray(position));
                lName.setText(instructor.getLnameArray(position));
                mName.setText(instructor.getMnameArray(position));
                instructor.setId(Integer.parseInt(instructor.getIdArray(position)));
                dialog.dismiss();
            }else{
                cinfo.setID(cinfo.getIdArray(position));
                cinfo.setName(cinfo.getNameArray(position));
                cinfo.setNumber(cinfo.getNumberArray(position));
                cinfo.setHours(cinfo.getHoursArray(position));
                courseNumber.setText(cinfo.getNumberArray(position));
                courseHours.setText(cinfo.getHoursArray(position));
                courseName.setText(cinfo.getNameArray(position));
                dialog.dismiss();

            }

    }
}