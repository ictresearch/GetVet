package com.pusatict.getvet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by ICT on 25/08/2015.
 */
public class NextRegister extends Activity implements CompoundButton.OnCheckedChangeListener{
    private Button btnReg;
    CheckBox HKburung,HKkucing,HKanjing,HKkura,HKhamster,HKkelinci,HKexotic,HKequine,AAppshop,AAdokterhewan,AAtidakkeduanya,AAcatken;
    int gakadacheckHK=0,gakadacheckAA=0;
    String burung,kucing,anjing,kelinci,kura,hamster,exotic,equine,ppshop="0",dokterhewan="0",catken="0",tidakkeduanya="0",uid;
    String email, nama,nohp,kota;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private TextView tvAgree;
    private Toast toast;
    private tesKoneksiInet koneksiInet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_register);
        koneksiInet = new tesKoneksiInet(getApplicationContext());

        setTvLink();
        setCheckBox();
        btnRegOnClick();
    }

    public void setTvLink(){
        tvAgree = (TextView)findViewById(R.id.tvAgree);
        String textAgree = "By clicking ''REGISTER'' you agree to the <a href=\"http://mygetvet.com/terms-of-service/\">Terms Of Service</a> and <a href=\"http://mygetvet.com/privacy-policy/\">Privacy Policy</a>";
        tvAgree.setText(Html.fromHtml(textAgree));
        tvAgree.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setCheckBox(){
        HKburung=(CheckBox) findViewById(R.id.chbBurung);
        HKburung.setOnCheckedChangeListener(this);
        HKkucing=(CheckBox) findViewById(R.id.chbKucing);
        HKkucing.setOnCheckedChangeListener(this);
        HKanjing=(CheckBox) findViewById(R.id.chbAnjing);
        HKanjing.setOnCheckedChangeListener(this);
        HKkelinci=(CheckBox) findViewById(R.id.chbKelinci);
        HKkelinci.setOnCheckedChangeListener(this);
        HKkura=(CheckBox) findViewById(R.id.chbKura);
        HKkura.setOnCheckedChangeListener(this);
        HKhamster=(CheckBox) findViewById(R.id.chbHamster);
        HKhamster.setOnCheckedChangeListener(this);
        HKequine=(CheckBox) findViewById(R.id.chbEquine);
        HKequine.setOnCheckedChangeListener(this);
        HKexotic=(CheckBox) findViewById(R.id.chbExotic);
        HKexotic.setOnCheckedChangeListener(this);
        AAdokterhewan=(CheckBox) findViewById(R.id.chbDokter);
        AAdokterhewan.setOnCheckedChangeListener(this);
        AAppshop=(CheckBox) findViewById(R.id.chbPet);
        AAppshop.setOnCheckedChangeListener(this);
        AAcatken=(CheckBox) findViewById(R.id.chbcatken);
        AAcatken.setOnCheckedChangeListener(this);
        AAtidakkeduanya=(CheckBox) findViewById(R.id.chbUser);
        AAtidakkeduanya.setOnCheckedChangeListener(this);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.chbBurung:
                if(isChecked){
                    burung="1";
                    gakadacheckHK++;
                }else {
                    burung="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbAnjing:
                if(isChecked){
                    anjing="1";
                    gakadacheckHK++;
                }else {
                    anjing="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbKucing:
                if(isChecked){
                    kucing="1";
                    gakadacheckHK++;
                }else {
                    kucing="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbKelinci:
                if(isChecked){
                    kelinci="1";
                    gakadacheckHK++;
                }else {
                    kelinci="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbHamster:
                if(isChecked){
                    hamster="1";
                    gakadacheckHK++;
                }else {
                    hamster="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbKura:
                if(isChecked){
                    kura="1";
                    gakadacheckHK++;
                }else {
                    kura="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbEquine:
                if(isChecked){
                    equine="1";
                    gakadacheckHK++;
                }else {
                    equine="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbExotic:
                if(isChecked){
                    exotic="1";
                    gakadacheckHK++;
                }else {
                    exotic="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbDokter:
                if(isChecked){
                    dokterhewan="1";
                    gakadacheckAA++;
                }else {
                    dokterhewan="0";
                    gakadacheckAA--;
                }
                break;
            case R.id.chbPet:
                if(isChecked){
                    ppshop="1";
                    gakadacheckAA++;
                }else {
                    ppshop="0";
                    gakadacheckAA--;
                }
                break;
            case R.id.chbcatken:
                if(isChecked){
                    catken="1";
                    gakadacheckAA++;
                }else {
                    catken="0";
                    gakadacheckAA--;
                }
                break;
            case R.id.chbUser:
                if(isChecked){
                    tidakkeduanya="1";
                    gakadacheckAA++;
                }else {
                    tidakkeduanya="0";
                    gakadacheckAA--;
                }
                break;
        }
    }

    public void btnRegOnClick(){
        btnReg = (Button)findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gakadacheckHK != 0 && gakadacheckAA != 0) {
                    if(koneksiInet.isNetworkAvailable()) {
                        new kotaupdate().execute();
                        new DaftarUser().execute();
                    }else{
                        toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } else if (gakadacheckHK == 0) {
                    toast = Toast.makeText(getApplicationContext(), " Select Your Favorite Pets ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    toast = Toast.makeText(getApplicationContext(), " Who Are You ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }

    public class kotaupdate extends AsyncTask<String,String ,String> {

        String url="http://getsimplebisnis.com/index.php/api/insertkota";
        String strKota, success;
        Bundle Parsing=getIntent().getExtras();
        @Override
        protected String doInBackground(String... params) {
            strKota=Parsing.getString("parse_kota").toUpperCase();
            Log.e("auto", strKota);

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("ktnama", strKota));

            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("response");
                Log.e("asa",success);

            } catch (Exception e) {
        /*        Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();*/
            }

            return null;
        }
    }
    public class DaftarUser extends AsyncTask<String,String ,String> {

        String success, strnama, stremail, strhp, strkota;
        DBAdapter db=new DBAdapter(NextRegister.this);
        Bundle Parsing=getIntent().getExtras();
        String url = "http://getsimplebisnis.com/index.php/api/registrasi1";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NextRegister.this);
            pDialog.setMessage(" Registering User... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            strnama = Parsing.getString("parse_nama");
            stremail = Parsing.getString("parse_alamat");
//            String strpassword = password.getText().toString();
            strhp = Parsing.getString("parse_nohp");
            strkota = Parsing.getString("parse_kota");
            tidakkeduanya="1";

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("nama", strnama));
            nvp.add(new BasicNameValuePair("email", stremail));
//            nvp.add(new BasicNameValuePair("password", strpassword));
            nvp.add(new BasicNameValuePair("nohp", strhp));
            nvp.add(new BasicNameValuePair("kota", strkota));
            nvp.add(new BasicNameValuePair("burung", burung));
            nvp.add(new BasicNameValuePair("kelinci", kelinci));
            nvp.add(new BasicNameValuePair("kucing", kucing));
            nvp.add(new BasicNameValuePair("kura", kura));
            nvp.add(new BasicNameValuePair("hamster", hamster));
            nvp.add(new BasicNameValuePair("anjing", anjing));
            nvp.add(new BasicNameValuePair("exotic", exotic));
            nvp.add(new BasicNameValuePair("equine", equine));
            nvp.add(new BasicNameValuePair("dokterhewan", dokterhewan));
            nvp.add(new BasicNameValuePair("ppshop", ppshop));
            nvp.add(new BasicNameValuePair("catken", catken));
            nvp.add(new BasicNameValuePair("tidakkeduanya", tidakkeduanya));
            Log.e("erro", strnama + " " + stremail + " " + strkota+ " "+ppshop);

            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("success");
//                uid = json.getString("uid");

            } catch (Exception e) {
/*                Toast.makeText(getApplicationContext(), " Registration failed. ",
                        Toast.LENGTH_LONG).show();*/
            }

            String url2="http://getsimplebisnis.com/index.php/api/cekuser";
            email=Parsing.getString("parse_alamat");
            nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uemail", email));
            json = jsonParser.makeHttpRequest(url2, "POST", nvp);
            try {
                JSONArray jsonArray = json.getJSONArray("cekuser");
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    uid=(obj.getString("id"));
                }

            }catch (JSONException e) {

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

            if (success.equals("1")) {
                toast = Toast.makeText(getApplicationContext(), "Registration success. ",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Intent intent = new Intent(NextRegister.this, coba.class);

                db.open();
                db.insertCustomer(1,"1", stremail, dokterhewan, ppshop,catken, uid, strnama, strhp, strkota,"","","1","1");
                db.close();
                Parsing.putString("parse_alamat", stremail);
                Parsing.putString("parse_dokter", dokterhewan);
                Parsing.putString("parse_ppshop", ppshop);
                Parsing.putString("parse_catken", catken);
                intent.putExtras(Parsing);
                startActivity(intent);
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
                finish();
            }
        }
    }
}

