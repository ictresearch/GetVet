package com.pusatict.getvet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
//import android.widget.Toast;

import com.pusatict.getvet.datalistadapter.ListAdapterMaskota;
import com.pusatict.getvet.datalistadapter.maskota;
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
 * Created by ICT on 25/08/2015.
 */
public class FirstRegister extends Activity {
    private Button btnReg;
    JSONParser jsonParser = new JSONParser();
    Intent a;
    Button b;
    EditText nama,email,hp;
    private ArrayList<maskota> list2;
    private AutoCompleteTextView autoComplete;
    private ListAdapterMaskota adapter3;
    private ProgressDialog pDialog;
    private Toast toast;
    private tesKoneksiInet koneksiInet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_register);
        koneksiInet = new tesKoneksiInet(getApplicationContext());

        setOnClickButton();
        setOnEditText();
        setOnAutocomplite();

    }

    public void setOnClickButton(){
        btnReg = (Button)findViewById(R.id.btnReg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("asik",nama.getText().toString()+" 1"+hp.getText().toString()+".");
//                Log.e("asik",email.getText().toString()+"");
//                Log.e("asik","");
//                Log.e("asik",autoComplete.getText().toString()+"");
                if(nama.getText().toString().equals("")||email.getText().toString().equals("")||hp.getText().toString().equals("")||autoComplete.getText().toString().equals("")){
                    toast = Toast.makeText(getApplicationContext(), " Please fill in all fields. ",
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

                        finish();
                    }
                }
            }
        });
    }
    public void setOnEditText(){
        nama=(EditText)findViewById(R.id.eNama);
        email=(EditText)findViewById(R.id.eEmail);
        hp=(EditText)findViewById(R.id.ePhone);
    }

    public void setOnAutocomplite(){
        list2 = new ArrayList<maskota>();
        if(koneksiInet.isNetworkAvailable()) {
            new autocomplite().execute("load");
        }

        autoComplete = (AutoCompleteTextView) findViewById(R.id.eCity);
        autoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text=autoComplete.getText().toString().toLowerCase(Locale.getDefault());
                if (text.equals("")) {
                    adapter3 = new ListAdapterMaskota(getApplicationContext(), list2);
                    autoComplete.setAdapter(adapter3);
                    autoComplete.setThreshold(1);
                } else {
                    adapter3 = new ListAdapterMaskota(getApplicationContext(), list2);
                    autoComplete.setAdapter(adapter3);
                    autoComplete.setThreshold(2);
                    adapter3.getFilter().filter(text);
                }

            }
        });

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                LinearLayout rl = (LinearLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                autoComplete.setText(tv.getText().toString());
            }

        });
    }

    public class autocomplite extends AsyncTask<String,String ,String> {

        @Override
        protected String doInBackground(String... params) {
            String url2="http://getsimplebisnis.com/index.php/api/kota";
            JSONParser jParser = new JSONParser();
            try {
                JSONObject json = jParser.getJSONFromUrl(url2);
                JSONArray jsonArray = json.getJSONArray("kota");
                maskota mas=null;
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    mas = new maskota();
                    mas.setKtnama(obj.getString("kota"));
                    list2.add(mas);
                    Log.d("eror", "data length: " + jsonArray.length());
                }

            }catch (JSONException e) {

            }
            return null;
        }
    }

    public class cekemail extends AsyncTask<String,String ,String>{

        String success1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FirstRegister.this);
            pDialog.setMessage(" Check Email Availability... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            String url1 = "http://192.168.0.109/klinikhewan/checkemail.php";
            String url1 = "http://getsimplebisnis.com/index.php/api/checkemail";
            String stremail = email.getText().toString().trim();

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("email", stremail));

            JSONObject json = jsonParser.makeHttpRequest(url1, "POST", nvp);
            try {
                success1 = json.getString("cekemail");
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Error",
//                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            Log.e("as", success1);
            if (success1.equals("1")) {
                toast = Toast.makeText(getApplicationContext(), " Email has already been registered. ",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent intent = new Intent(FirstRegister.this, NextRegister.class);
                Bundle Parsing = new Bundle();
                Parsing.putString("parse_nama", nama.getText().toString());
                Parsing.putString("parse_alamat", email.getText().toString());
                Parsing.putString("parse_nohp",hp.getText().toString());
                Parsing.putString("parse_kota", autoComplete.getText().toString().trim().toUpperCase());
                intent.putExtras(Parsing);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        }
    }
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
}
