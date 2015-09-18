package com.example.noe.qrcode;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;

//import com.google.google.integration.android.IntentIntegrator;
//import com.google.google.integration.android.IntentResult;

public class MainActivity extends ActionBarActivity {
    GridView gridView;
    Intent intent;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    CustomGridViewAdapter customGridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.activity_main);
        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399CC")));

        //setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399CC")));
        Bitmap newDocumentIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.newfile);
        Bitmap startCourseIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_file_100);
        Bitmap searchIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.search);
        Bitmap searchUserIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.finduser);
        Bitmap searchIIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.find_user_100_2);
        Bitmap generateQRCode = BitmapFactory.decodeResource(this.getResources(), R.drawable.qrcode);
        Bitmap newInstructor = BitmapFactory.decodeResource(this.getResources(), R.drawable.adduser);
        Bitmap enrollment = BitmapFactory.decodeResource(this.getResources(), R.drawable.enroll);
        Bitmap generatePDF = BitmapFactory.decodeResource(this.getResources(), R.drawable.pdf_icon);
        Bitmap newStudent = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_user_100);
        Bitmap helpIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.help_100);
        Bitmap aboutIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.about_100);

        gridArray.add(new Item(newDocumentIcon," Add Course"));
        gridArray.add(new Item(newInstructor, " Add Instructor"));
        gridArray.add(new Item(newStudent, " Add Student"));
        gridArray.add(new Item(startCourseIcon, " Start Class"));
        gridArray.add(new Item(enrollment, "  Enroll Student"));
        gridArray.add(new Item(searchIcon," Search Course"));
        gridArray.add(new Item(searchIIcon, " Search Instructor"));
        gridArray.add(new Item(searchUserIcon," Search Student"));
        gridArray.add(new Item(generateQRCode,"  Generate QR"));
        gridArray.add(new Item(generatePDF, " Generate PDF"));
        gridArray.add(new Item(helpIcon, "            Help"));
        gridArray.add(new Item(aboutIcon, "     About"));
        gridView = (GridView) findViewById(R.id.gridView1);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
        gridView.setAdapter(customGridAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(position == 0)
                {
                    intent = new Intent(MainActivity.this, activity_add_courseFragment.class);
                    intent.putExtra("add", position);
                    startActivity(intent);
                    return;
                }
                else if(position == 1)
                {
                    intent = new Intent(MainActivity.this, add_instructor_fragment.class);
                    intent.putExtra("add", position);
                    startActivity(intent);
                    return;
                }
                else if(position == 2)
                {
                    intent = new Intent(MainActivity.this, addStudent.class);

                    startActivity(intent);
                }
                else if(position == 3)
                {
                    intent = new Intent(MainActivity.this, startClass.class);

                    startActivity(intent);
                }
                else if(position == 4)
                {
                    intent = new Intent(MainActivity.this, enrollActivity.class);
                    startActivity(intent);
                }
            }
        });
    }




    //public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//retrieve scan result
       // IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //if (scanningResult != null) {
            //we have a result
          //  String scanContent = scanningResult.getContents();
          //  String scanFormat = scanningResult.getFormatName();


       // }
        //else{
          //  Toast toast = Toast.makeText(getApplicationContext(),
            //        "No scan data received!", Toast.LENGTH_SHORT);
           // toast.show();
       // }
    //}
}
