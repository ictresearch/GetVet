package com.pusatict.getvet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.NoCopySpan;
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
import com.pusatict.getvet.sponsor.Banner;
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
public class UserProfile extends Activity implements CompoundButton.OnCheckedChangeListener{
    private ImageButton imgBack, imgHome;
    private ProgressDialog pDialog;
    private JSONParser jsonParser=new JSONParser();
    private EditText etxNama,etxAlamat,etxKontak,etxSrv,etxOldpass,etxNewpass;
    private AutoCompleteTextView actKota,actProvinsi;
    private ArrayList<maskota> list2;
    private ArrayList<masprovinsi> list3;
    private ListAdapterMaskota adapter3;
    private ListAdapterMasprovinsi adapter4;
    private static String id;
    private tesKoneksiInet koneksiInet;
    CheckBox HKburung,HKkucing,HKanjing,HKkura,HKhamster,HKkelinci,HKexotic,HKequine,AAppshop,AAdokterhewan,AAcatken,AAtidakkeduanya;
    String burung="0",kucing="0",anjing="0",kelinci="0",kura="0",hamster="0",exotic="0",equine="0",ppshop="0",dokterhewan="0",catken="0",tidakkeduanya="0",uid,pass;
    int gakadacheckHK=0,gakadacheckAA=0;
    private Button btnSave,btnCheng;
    private Toast toast;
    final DBAdapter db = new DBAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        koneksiInet = new tesKoneksiInet(getApplicationContext());
        etxNama=(EditText) findViewById(R.id.eNama);
        etxAlamat=(EditText) findViewById(R.id.eEmail);
        etxKontak=(EditText) findViewById(R.id.ePhone);
        etxSrv=(EditText) findViewById(R.id.eStrv);
        etxOldpass=(EditText) findViewById(R.id.eOldpass);
        etxNewpass=(EditText) findViewById(R.id.eNewpass);
        btnSave=(Button) findViewById(R.id.btnSave);
        btnCheng=(Button) findViewById(R.id.btnChangePass);

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome = (ImageButton)findViewById(R.id.imgHome);

        setCheckBox();
        setOnAutocomplite();
        if(koneksiInet.isNetworkAvailable()) {
            new selectkategori().execute();
        }else{
            toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        db.open();
        Cursor c=db.getContact(1);
        if (c.moveToFirst()){
            etxAlamat.setText(c.getString(2));
            dokterhewan=c.getString(3);
            ppshop=c.getString(4);
            id=c.getString(5);
            etxNama.setText(c.getString(6));
            etxKontak.setText(c.getString(7));
            actKota.setText(c.getString(8));
            etxSrv.setText(c.getString(9));
            actProvinsi.setText(c.getString(10));
            catken=c.getString(11);
        }
        db.close();
        Bundle Parsing=getIntent().getExtras();
        pass=Parsing.getString("password");

        cek();


        isiBtnClick();
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
        HKexotic=(CheckBox) findViewById(R.id.chbExotic);
        HKexotic.setOnCheckedChangeListener(this);
        HKequine=(CheckBox) findViewById(R.id.chbEquine);
        HKequine.setOnCheckedChangeListener(this);

        AAdokterhewan=(CheckBox) findViewById(R.id.chbDokter);
        AAdokterhewan.setOnCheckedChangeListener(this);
        AAppshop=(CheckBox) findViewById(R.id.chbPet);
        AAppshop.setOnCheckedChangeListener(this);
        AAcatken=(CheckBox) findViewById(R.id.chbcatken);
        AAcatken.setOnCheckedChangeListener(this);
        AAtidakkeduanya=(CheckBox) findViewById(R.id.chbUser);
        AAtidakkeduanya.setOnCheckedChangeListener(this);
    }
    public void checkincheckbox(){
        if(burung.equals("1")){
            HKburung.setChecked(true);
        }
        if(anjing.equals("1")){
            HKanjing.setChecked(true);
        }
        if(kucing.equals("1")){
            HKkucing.setChecked(true);
        }
        if(kura.equals("1")){
            HKkura.setChecked(true);
        }
        if(hamster.equals("1")){
            HKhamster.setChecked(true);
        }
        if(kelinci.equals("1")){
            HKkelinci.setChecked(true);
        }
        if(equine.equals("1")){
            HKequine.setChecked(true);
        }
        if(exotic.equals("1")){
            HKexotic.setChecked(true);
        }
    }
    public void cek(){
        if(dokterhewan.equals("1")){
            AAdokterhewan.setChecked(true);
//            AAdokterhewan.setClickable(false);
        }else{
            etxSrv.setVisibility(View.GONE);
        }
        if(ppshop.equals("1")){
            AAppshop.setChecked(true);
//            AAppshop.setClickable(false);
        }
        if(catken.equals("1")){
            AAcatken.setChecked(true);
//            AAcatken.setClickable(false);
        }
        AAtidakkeduanya.setChecked(true);
        AAtidakkeduanya.setClickable(false);
    }
    public void setOnAutocomplite(){
        list2 = new ArrayList<maskota>();
        list3 = new ArrayList<masprovinsi>();
        if(koneksiInet.isNetworkAvailable()) {
            new autocomplite().execute("load");
            new autocomplite1().execute("load");
        }
        actKota =(AutoCompleteTextView)findViewById(R.id.eCity);
        actKota.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = actKota.getText().toString().toLowerCase(Locale.getDefault());
                if (text.equals("")) {
                    adapter3 = new ListAdapterMaskota(getApplicationContext(), list2);
                    actKota.setAdapter(adapter3);
                    actKota.setThreshold(1);
                } else {
                    adapter3 = new ListAdapterMaskota(getApplicationContext(), list2);
                    actKota.setAdapter(adapter3);
                    actKota.setThreshold(2);
                    adapter3.getFilter().filter(text);
                }

            }
        });
        actKota.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                LinearLayout rl = (LinearLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                actKota.setText(tv.getText().toString());
            }

        });

        actProvinsi =(AutoCompleteTextView)findViewById(R.id.eProvinsi);
        actProvinsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = actProvinsi.getText().toString().toLowerCase(Locale.getDefault());
                if (text.equals("")) {
                    adapter4 = new ListAdapterMasprovinsi(getApplicationContext(), list3);
                    actProvinsi.setAdapter(adapter4);
                    actProvinsi.setThreshold(1);
                } else {
                    adapter4 = new ListAdapterMasprovinsi(getApplicationContext(), list3);
                    actProvinsi.setAdapter(adapter4);
                    actProvinsi.setThreshold(2);
                    adapter4.getFilter().filter(text);
                }

            }
        });
        actProvinsi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                LinearLayout rl = (LinearLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                actProvinsi.setText(tv.getText().toString());
            }

        });
    }
    public void isiBtnClick(){
        btnCheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, UserChangePass.class);
                Bundle Parsing = new Bundle();
                Parsing.putString("password",pass);
                Parsing.putString("id",id);
                intent.putExtras(Parsing);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("cekhk",gakadacheckHK+"");
                Bundle Parsing = getIntent().getExtras();
                if(etxNama.getText().toString().equals("")||etxAlamat.getText().toString().equals("")||
                        etxKontak.getText().toString().equals("")||actKota.getText().toString().equals("")){
                    toast = Toast.makeText(getApplicationContext(), " Please fill in all fields. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(gakadacheckHK>0){
                    if(koneksiInet.isNetworkAvailable()) {
                        new updateUser().execute();
                        new kotaupdate().execute();
                        new provupdate().execute();
                        Log.e("update", dokterhewan + " " + ppshop);
                        db.open();
                        db.updateprofil(1, etxAlamat.getText().toString(), dokterhewan, ppshop, catken,etxNama.getText().toString(),
                                etxKontak.getText().toString(), actKota.getText().toString().trim().toUpperCase(), etxSrv.getText().toString(),
                                actProvinsi.getText().toString().trim().toUpperCase());
                        db.close();
                    }else{
                        toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Select Your Favorite Pets ",
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
                Intent intent = new Intent(UserProfile.this, MainActivity.class);
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

    @Override
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
                    Log.d("eror", "data lengt: " + jsonArray.length());
                }

            }catch (JSONException e) {

            }
            return null;
        }
    }
    public class autocomplite1 extends AsyncTask<String,String ,String> {

        @Override
        protected String doInBackground(String... params) {
            String url2="http://getsimplebisnis.com/index.php/api/provinsi";
            JSONParser jParser = new JSONParser();
            try {
                JSONObject json = jParser.getJSONFromUrl(url2);
                JSONArray jsonArray = json.getJSONArray("provinsi");
                masprovinsi mas=null;
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    mas = new masprovinsi();
                    mas.setKtnama(obj.getString("nama"));
                    list3.add(mas);
                    Log.d("eror", "data lengt: " + jsonArray.length());
                }

            }catch (JSONException e) {

            }
            return null;
        }
    }
    public class selectkategori extends AsyncTask<String,String ,String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserProfile.this);
            pDialog.setMessage(" Loading profile... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url="http://getsimplebisnis.com/index.php/api/kategori";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", id));
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            Log.e("amin",id);
            try {
                JSONArray jsonArray = json.getJSONArray("kategori");
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String katnama=obj.getString("katNama");
                    Log.e("amin",katnama+" "+id);
                    if(katnama.equals("CAT")){
                        kucing="1";
                    }
                    if(katnama.equals("DOG")){
                        anjing="1";
                    }
                    if(katnama.equals("FISH")){
                        kura="1";
                    }
                    if(katnama.equals("SMALL")){
                        hamster="1";
                    }
                    if(katnama.equals("REPTILES")){
                        kelinci="1";
                    }
                    if(katnama.equals("BIRDS")){
                        burung="1";
                    }
                    if(katnama.equals("EXOTIC")){
                        exotic="1";
                    }
                    if(katnama.equals("EQUINE")){
                        equine="1";
                    }
                }

            }catch (JSONException e) {
                Log.e("eror",e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            checkincheckbox();
        }
    }

    public class updateUser extends AsyncTask<String,String ,String> {

        String success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserProfile.this);
            pDialog.setMessage(" Updating profile... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url="http://getsimplebisnis.com/index.php/api/ubahuser";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", id));
            nvp.add(new BasicNameValuePair("unama", etxNama.getText().toString()));
            nvp.add(new BasicNameValuePair("uemail", etxAlamat.getText().toString()));
            nvp.add(new BasicNameValuePair("ukontak", etxKontak.getText().toString()));
            nvp.add(new BasicNameValuePair("ukota", actKota.getText().toString().trim().toUpperCase()));
            nvp.add(new BasicNameValuePair("ucekuser", "1"));
            nvp.add(new BasicNameValuePair("ucekdokter", dokterhewan));
            nvp.add(new BasicNameValuePair("ucekshop", ppshop));
            nvp.add(new BasicNameValuePair("catken", catken));
            nvp.add(new BasicNameValuePair("ustrv", etxSrv.getText().toString()));
            nvp.add(new BasicNameValuePair("uprovinsi", actProvinsi.getText().toString().trim().toUpperCase()));
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("ubahuser");

            }catch (JSONException e) {
                Log.e("eror",e.toString());
            }
            url="http://getsimplebisnis.com/index.php/api/hapuskategori";
            nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", id));
            nvp.add(new BasicNameValuePair("burung", burung));
            nvp.add(new BasicNameValuePair("kelinci", kelinci));
            nvp.add(new BasicNameValuePair("kucing", kucing));
            nvp.add(new BasicNameValuePair("kura", kura));
            nvp.add(new BasicNameValuePair("hamster", hamster));
            nvp.add(new BasicNameValuePair("anjing", anjing));
            nvp.add(new BasicNameValuePair("exotic", exotic));
            nvp.add(new BasicNameValuePair("equine", equine));
//            Log.e("katagori", id + "," + burung + "," + kelinci + "," + kura + "," + kucing + "," + hamster + "," + anjing);
            json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("hapuskategori");

            }catch (JSONException e) {
                Log.e("eror",e.toString());
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

            if (success.equals("1")) {
                toast = Toast.makeText(getApplicationContext(), " Your profile has been successfully updated ",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
    public class kotaupdate extends AsyncTask<String,String ,String> {

        String url="http://getsimplebisnis.com/index.php/api/insertkota";
        String strKota,success;
        @Override
        protected String doInBackground(String... params) {
            strKota=actKota.getText().toString().trim().toUpperCase();
            Log.e("auto", strKota);

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("ktnama", strKota));

            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("response");
                Log.e("asa",success);

            } catch (Exception e) {
                /*Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();*/
            }

            return null;
        }
    }
    public class provupdate extends AsyncTask<String,String ,String> {

        String url="http://getsimplebisnis.com/index.php/api/insertprov";
        String strProv,success;
        @Override
        protected String doInBackground(String... params) {
            strProv=actProvinsi.getText().toString().trim().toUpperCase();

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("proNama", strProv));

            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("response");
                Log.e("asa",success);

            } catch (Exception e) {
                /*Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();*/
            }

            return null;
        }
    }

}
