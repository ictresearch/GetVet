package com.pusatict.getvet.sponsor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.TwoStatePreference;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.pusatict.getvet.MainActivity;
import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.ListAdapteriklan;
import com.pusatict.getvet.datalistadapter.iklan;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.Utils;
import com.pusatict.getvet.tool.tesKoneksiInet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by bronky on 20/08/2015.
 */
public class iklanView extends Activity {
    ListAdapteriklan adapteriklan;
    private List<iklan> list;
    private ListView listview;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iklan_view);
        listview=(ListView) findViewById(R.id.listView);
        list=new ArrayList<iklan>();
        Bundle Parsing=getIntent().getExtras();
        iklan ikn = new iklan();
        ikn.setHtml(Parsing.getString("html"));
        list.add(ikn);
        adapteriklan =new ListAdapteriklan(getApplicationContext(),list);
        listview.setAdapter(adapteriklan);
        setOnClickbtn();

    }
    public void setOnClickbtn(){
        ImageButton btnBack = (ImageButton)findViewById(R.id.imgBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
        finish();
    }
}
