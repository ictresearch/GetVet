package com.pusatict.getvet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pusatict.getvet.datalistadapter.ListAdapterMaskota;
import com.pusatict.getvet.datalistadapter.dokter;
import com.pusatict.getvet.datalistadapter.maskota;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.tesKoneksiInet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import android.widget.Toast;

/**
 * Created by ICT on 25/08/2015.
 */
public class ActivityLogin extends Activity {
    private Button btnReg,btnLogin;
    JSONParser jsonParser = new JSONParser();
    Intent a;
    Button b;
    EditText email,pass;
    private ProgressDialog pDialog;
    private Toast toast;
    private tesKoneksiInet koneksiInet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        koneksiInet = new tesKoneksiInet(getApplicationContext());

        setOnClickButton();
        setOnEditText();

    }

    public void setOnClickButton(){
        btnReg = (Button)findViewById(R.id.btnReg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this,FirstRegister.class);
                startActivity(intent);
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")||pass.getText().toString().equals("")){
                    toast = Toast.makeText(getApplicationContext(), "Please fill in all fields.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    if(koneksiInet.isNetworkAvailable()) {
                        new cekemail().execute();
                    }else{
                        toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        });
    }
    public void setOnEditText(){
        email=(EditText)findViewById(R.id.eEmail);
        pass=(EditText)findViewById(R.id.ePass);
    }

    public class cekemail extends AsyncTask<String,String ,String>{

        String success1, strnama, stremail, strhp, strkota,dokterhewan,ppshop,catken,uid,uprovinsi,ustrv,upass;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityLogin.this);
            pDialog.setMessage(" Check Email Availability... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            String url1 = "http://192.168.0.109/klinikhewan/checkemail.php";
            String url1 = "http://getsimplebisnis.com/index.php/api/checkemail";
            stremail = email.getText().toString().trim();

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("email", stremail));

            JSONObject json = jsonParser.makeHttpRequest(url1, "POST", nvp);
            try {
                success1 = json.getString("cekemail");
                if(success1.equals("1")) {
                    JSONArray jsonArray = json.getJSONArray("cekemail1");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        strnama = obj.getString("unama");
                        strhp = obj.getString("ukontak");
                        strkota = obj.getString("ukota");
                        dokterhewan = obj.getString("ucekdokter");
                        ppshop = obj.getString("ucekshop");
                        catken=obj.getString("ucekcatken");
                        uid = obj.getString("uid");
                        uprovinsi = obj.getString("uprovinsi");
                        ustrv = obj.getString("ustrv");
                        upass = obj.getString("upass");
                    }
                }
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Error",
//                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            DBAdapter db = new DBAdapter(ActivityLogin.this);
            if (success1.equals("1")){
                if (upass.equals("null")) {
                    new setPass().execute(uid);
                    db.open();
                    db.insertCustomer(1, "1", stremail, dokterhewan, ppshop,catken, uid, strnama, strhp, strkota, ustrv, uprovinsi,"1","1");
                    db.close();
                    Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (upass.equals(pass.getText().toString())) {
                        db.open();
                        db.insertCustomer(1, "1", stremail, dokterhewan, ppshop,catken, uid, strnama, strhp, strkota, ustrv, uprovinsi,"1","1");
                        db.close();
                        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        toast = Toast.makeText(getApplicationContext(), "Incorrect Password ",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    //cek password sama apa tidak di dalam database
                }
            }else{
                toast = Toast.makeText(getApplicationContext(), "Email has not found. Please make sure your email is correct or register if you don't have account.",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
    public class setPass extends AsyncTask<String,String ,String> {
            String success1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(ActivityLogin.this);
//            pDialog.setMessage(" Check Email Availability... ");
//            pDialog.setIndeterminate(true);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            String url1 = "http://192.168.0.109/klinikhewan/checkemail.php";
            String url1 = "http://getsimplebisnis.com/index.php/api/ubahpass";


            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", params[0]));
            nvp.add(new BasicNameValuePair("upass",pass.getText().toString()));
            JSONObject json = jsonParser.makeHttpRequest(url1, "POST", nvp);
            try {
//                JSONArray jsonArray = json.getJSONArray("cekemail1");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject obj = jsonArray.getJSONObject(i);
//                    strnama = obj.getString("unama");
//                    strhp = obj.getString("ukontak");
//                    strkota = obj.getString("ukota");
//                    dokterhewan = obj.getString("ucekdokter");
//                    ppshop = obj.getString("ucekshop");
//                    uid = obj.getString("uid");
//                    uprovinsi = obj.getString("uprovinsi");
//                    ustrv = obj.getString("ustrv");
//                    upass = obj.getString("upass");
//                }
                success1 = json.getString("ubahpass");

            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Error",
//                        Toast.LENGTH_LONG).show();
            }

            return null;
        }
        protected void onPostExecute(String file_url) {
        }
    }
}
