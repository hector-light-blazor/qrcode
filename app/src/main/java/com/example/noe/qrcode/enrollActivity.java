package com.example.noe.qrcode;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
/**
 * Created by noe on 8/21/15.
 */
public class enrollActivity extends ActionBarActivity implements View.OnClickListener{
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    JSONArray downloadArray = null;
    private ImageView qr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399CC")));
        Toast.makeText(getApplicationContext(), "Fetching Data ", Toast.LENGTH_LONG).show();
        qr = (ImageView) findViewById(R.id.qr);
        qr.setOnClickListener(this);
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        downloadData();
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    public void downloadData()
    {
        try {
            String number = "";
            JSONObject toSend = new JSONObject();
            JSONTransmitter transmitter = new JSONTransmitter("http://gis.lrgvdc911.org/php/qr_class/fetch_all_course.php");
            JSONObject t = transmitter.execute(new JSONObject[]{toSend}).get();
            downloadArray = t.getJSONArray("msg");

            if(downloadArray.length() > 0) {
                Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_LONG).show();


            }else{
                Toast.makeText(getApplicationContext(), "Sorry, No Records", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void prepareListData()
    {
        listDataHeader = new ArrayList<String>();
        List<List> listArray = new ArrayList<List>();

        listDataChild = new HashMap<String, List<String>>();
        // Adding child data
        try{
            for(int i = 0; i < downloadArray.length() ; i++)
            {
                List<String> listchild = new ArrayList<String>();
                JSONObject c = downloadArray.getJSONObject(i);
                listchild.add("Begin Date: " + c.getString("begin_date"));
                listchild.add("End Date: " + c.getString("end_date"));
                listchild.add("Location: " + c.getString("location"));
                listArray.add(listchild);
                listDataHeader.add((i + 1) + ".) Course Name: " + c.getString("course_name"));
                listDataChild.put(listDataHeader.get(i), listArray.get(i));

            }

                ///listDataChild.put(listDataHeader.get(2), listArray.get(2));

        }catch(Exception e)
        {

        }


    }

    @Override
    public void onClick(View v) {
       if(v == qr)
       {
           IntentIntegrator integrator = new IntentIntegrator(this);
           integrator.initiateScan();
       }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String scanContent = scanResult.getContents();
            //mQRCodeTextView.setText(scanContent);
            Log.d("QR", "QR Scan Content: " + scanContent);
        }
    }
}
