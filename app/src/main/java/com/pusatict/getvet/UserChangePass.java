package com.pusatict.getvet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pusatict.getvet.datalistadapter.ListAdapterMaskota;
import com.pusatict.getvet.datalistadapter.ListAdapterMasprovinsi;
import com.pusatict.getvet.datalistadapter.maskota;
import com.pusatict.getvet.datalistadapter.masprovinsi;
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

/**
 * Created by ICT on 31/08/2015.
 */
public class UserChangePass extends Activity {
    private ImageButton imgBack, imgHome;
    private ProgressDialog pDialog;
    private JSONParser jsonParser=new JSONParser();
    private EditText etxOldpass,etxNewpass;
    private static String id;
    private tesKoneksiInet koneksiInet;
    String uid,pass;
    private Button btnSave;
    private Toast toast;
    final DBAdapter db = new DBAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_changepass);

        koneksiInet = new tesKoneksiInet(getApplicationContext());
        etxOldpass=(EditText) findViewById(R.id.eOldpass);
        etxNewpass=(EditText) findViewById(R.id.eNewpass);
        btnSave=(Button) findViewById(R.id.btnSave);

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome = (ImageButton)findViewById(R.id.imgHome);

        if(!koneksiInet.isNetworkAvailable()) {
            toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        Bundle Parsing=getIntent().getExtras();
        pass=Parsing.getString("password");
        uid=Parsing.getString("id");
        if(pass.equals("null")){
            etxOldpass.setVisibility(View.GONE);
        }
        isiBtnClick();
    }

    public void isiBtnClick(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("cekhk",gakadacheckHK+"");
                Bundle Parsing = getIntent().getExtras();
                if(pass.equals("null")){
                    etxOldpass.setText("null");
                }
                if(etxNewpass.getText().toString().equals("")||etxOldpass.getText().equals("")){
                    toast = Toast.makeText(getApplicationContext(), " Please fill in all fields. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(koneksiInet.isNetworkAvailable()) {
                    Log.e("asem",pass+","+uid);
                        if(pass.equals(etxOldpass.getText().toString())){
                            new setPass().execute(uid);
                        }else{
                            toast = Toast.makeText(getApplicationContext(), " Sorry! your old password wrong .",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }else{
                        toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserChangePass.this, MainActivity.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }

    public class setPass extends AsyncTask<String,String ,String> {
        String success1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserChangePass.this);
            pDialog.setMessage(" Change Password Proses ... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            String url1 = "http://192.168.0.109/klinikhewan/checkemail.php";
            String url1 = "http://getsimplebisnis.com/index.php/api/ubahpass";


            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", params[0]));
            nvp.add(new BasicNameValuePair("upass",etxNewpass.getText().toString().trim()));
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
            pDialog.dismiss();
            Intent intent = new Intent(UserChangePass.this, MainActivity.class);
            startActivity(intent);

            overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
        }
    }
}
