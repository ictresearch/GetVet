package com.pusatict.getvet.klinik;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pusatict.getvet.MainActivity;
import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.ListAdapterMaskota;
import com.pusatict.getvet.datalistadapter.ListAdapterNamaklinik;
import com.pusatict.getvet.datalistadapter.dokter;
import com.pusatict.getvet.datalistadapter.maskota;
import com.pusatict.getvet.datalistadapter.namaklinik;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.Utils;
import com.pusatict.getvet.tool.tesKoneksiInet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ICT on 31/08/2015.
 */
public class VetAdd extends Activity {
    private ImageButton imgBack, imgHome;

    private ProgressDialog pDialog;
    private JSONParser jsonParser=new JSONParser();
    private Intent a;
    private EditText etxAlamat,etxKontak,etxPelayanan,etxJadwal;
    private AutoCompleteTextView actKota,etxNama;
    private ArrayList<maskota> list2;
    private ArrayList<namaklinik> list;
    private ArrayList<dokter> list3;
    private ListAdapterMaskota adapter3;
    private ListAdapterNamaklinik adapter2;
    private Button btnSave;
    private Toast toast;
    private tesKoneksiInet koneksiInet;
    public String urlfoto=null;

    //Deklare Pick Photo
    private ImageButton btnAddPhoto, btnClearPhoto;
    private TextView tvPhoto;
    private Bitmap bitmap,bitmap1;
    private ImageView imgPhoto;
    Uri fileUri;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    String ba1;
    //API Upload URL
//    public static String URL = "http://www.getsimplebisnis.com/Download/getvet/vetphotos/upload.php";
    public static String URL = "http://www.getsimplebisnis.com/index.php/api/coba";
    public static String URL1 = "http://www.getsimplebisnis.com/index.php/api/coba2";
    //API Save Klinik
    public static String url="http://getsimplebisnis.com/index.php/api/insertklinik";
    //API Update Klinik
    public static String url1="http://getsimplebisnis.com/index.php/api/ubahklinik";
    //API Select Kota
    public static String url2="http://getsimplebisnis.com/index.php/api/kota";
    //API Select Namaklinik Autocomplite
    public static String url3="http://getsimplebisnis.com/index.php/api/viewklinik";
    //API Update Kota
    public static String url4="http://getsimplebisnis.com/index.php/api/insertkota";
    //API Select Klinik
    public static String url5="http://getsimplebisnis.com/index.php/api/klinikby";
    private String fname;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vet_add);
        Bundle Parsing=getIntent().getExtras();
        koneksiInet = new tesKoneksiInet(getApplicationContext());

        etxAlamat=(EditText) findViewById(R.id.eAddress);
        etxKontak=(EditText) findViewById(R.id.ePhone);
        etxPelayanan=(EditText) findViewById(R.id.eServices);
        etxJadwal=(EditText) findViewById(R.id.eJadwal);
        btnSave=(Button) findViewById(R.id.btnSave);

        btnAddPhoto=(ImageButton) findViewById(R.id.btnAddPhoto);
        btnClearPhoto=(ImageButton) findViewById(R.id.btnClearPhoto);
        tvPhoto=(TextView) findViewById(R.id.tvPhoto);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);

        list2 = new ArrayList<maskota>();
        list = new ArrayList<namaklinik>();
        if(koneksiInet.isNetworkAvailable()) {
            new autocomplite().execute("load");
            new autocomplitenama().execute("load");
        }

        setAutocompliteview();
        if(Parsing.getString("pos").equals("1")){
            etxNama.setText(Parsing.getString("nama"));
            etxAlamat.setText(Parsing.getString("alamat"));
            etxKontak.setText(Parsing.getString("kontak"));
            etxPelayanan.setText(Parsing.getString("jasa"));
            actKota.setText(Parsing.getString("kota"));
            etxJadwal.setText(Parsing.getString("jadwal"));
            Log.e("foto", Parsing.getString("foto"));
//            deleteGambar("k");
            if(!Parsing.getString("foto").equals("null")){
                new DownloadImage().execute(Parsing.getString("foto"));
//                new DownloadImage().execute(Parsing.getString("foto"));

            }else{
                imgPhoto.setImageResource(R.drawable.picture_100_blue);
                float scaleDp = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (50*scaleDp + 0.5f);
                imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                ba1=null;
            }
        }
        setBtnSave();

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome = (ImageButton)findViewById(R.id.imgHome);

        isiBtnClick();
    }
    public void deleteGambar(String name){
        String patch=VetAdd.this.getFilesDir()+"/dir";
        File dir = new File(patch);

        new File(dir,name+".jpg").delete();
//        File dir = new File(Environment.getExternalStorageDirectory()+"/dir");
//        new File(dir,"data1.jpg").delete();
//        Log.e("asik1", dir.toString() + " " + name);
////        file.delete();
    }
    public void setBtnSave(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle Parsing=getIntent().getExtras();
                if(etxNama.getText().toString().equals("")||etxAlamat.getText().toString().equals("")||
                        etxKontak.getText().toString().equals("")||etxPelayanan.getText().toString().equals("")||
                        actKota.getText().toString().equals("")||etxJadwal.getText().toString().equals("")){

                    toast = Toast.makeText(getApplicationContext(), " Please fill in all fields. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    if (Parsing.getString("pos").equals("1")) {
                        if(koneksiInet.isNetworkAvailable()) {
//                            if(ba1==null) {
//                                new kotaupdate().execute("kota");
//                                new updateKlinik().execute("update");
//                            }else{
                            new kotaupdate().execute("kota");
                            new updateKlinik().execute("update");

//                            }
                        }else{
                            toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {
                        if(koneksiInet.isNetworkAvailable()) {
                            if(ba1==null) {
                                urlfoto="null";
                                new kotaupdate().execute("kota");
                                new savedata().execute("masuk");
                            }else{
                                new uploadToServer2().execute();
                                new kotaupdate().execute("kota");
                                new savedata().execute("masuk");
                            }
                        }else{
                            toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    }
                }
            }
        });
    }
    public void setAutocompliteview(){
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

        //auto Nama klinik
        etxNama =(AutoCompleteTextView)findViewById(R.id.eNama);
        etxNama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = etxNama.getText().toString().toLowerCase(Locale.getDefault());
                if (text.equals("")) {
                    adapter2 = new ListAdapterNamaklinik(getApplicationContext(), list);
                    etxNama.setAdapter(adapter2);
                    etxNama.setThreshold(1);
                } else {
                    adapter2 = new ListAdapterNamaklinik(getApplicationContext(), list);
                    etxNama.setAdapter(adapter2);
                    etxNama.setThreshold(2);
                    adapter2.getFilter().filter(text);
                }

            }
        });
        etxNama.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                LinearLayout rl = (LinearLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                etxNama.setText(tv.getText().toString());
                new selectKlinik().execute();
                etxAlamat.requestFocus();
            }

        });
    }
    public class autocomplite extends AsyncTask<String,String ,String> {

        @Override
        protected String doInBackground(String... params) {
//            String url2="http://getsimplebisnis.com/index.php/api/kota";
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
    public class autocomplitenama extends AsyncTask<String,String ,String> {

        @Override
        protected String doInBackground(String... params) {

//            String url2="http://getsimplebisnis.com/index.php/api/viewklinik";
            JSONParser jParser = new JSONParser();
            try {
                JSONObject json = jParser.getJSONFromUrl(url3);
                JSONArray jsonArray = json.getJSONArray("viewklinik");

                namaklinik mas=null;
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    mas=new namaklinik();
                    mas.setKtnama(obj.getString("klnama"));
                    list.add(mas);
                    Log.d("eror", "data lengt: " + jsonArray.length());
                }

            }catch (JSONException e) {

            }
            return null;
        }
    }
    public class updateKlinik extends AsyncTask<String,String ,String> {

        //        String url="http://getsimplebisnis.com/index.php/api/ubahklinik";
        String strNama,strAlamat,strKontak,strPelayanan,strJadwal,strKota,id,success;
        DBAdapter db=new DBAdapter(VetAdd.this);
        Bundle Parsing=getIntent().getExtras();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VetAdd.this);
            pDialog.setMessage(" Saving data... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            id=Parsing.getString("id");
            strNama=etxNama.getText().toString();
            strAlamat=etxAlamat.getText().toString();
            strKontak=etxKontak.getText().toString();
            strPelayanan=etxPelayanan.getText().toString();
            strJadwal=etxJadwal.getText().toString();
            strKota=actKota.getText().toString().trim().toUpperCase();

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("klid", id));
            nvp.add(new BasicNameValuePair("klnama", strNama));
            nvp.add(new BasicNameValuePair("klalamat", strAlamat));
            nvp.add(new BasicNameValuePair("klkontak", strKontak));
            nvp.add(new BasicNameValuePair("klkota", strKota));
            nvp.add(new BasicNameValuePair("kljasa", strPelayanan));
            nvp.add(new BasicNameValuePair("kljadwal", strJadwal));
//            nvp.add(new BasicNameValuePair("uid", uid));

            JSONObject json = jsonParser.makeHttpRequest(url1, "POST", nvp);
            try {
                success = json.getString("ubahklinik");

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            if(ba1!=null) {
                new uploadToServer().execute();
                toast = Toast.makeText(getApplicationContext(), " Your vet clinic has been successfully updated ",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }else{
                new uploadToServer3().execute();
//                pDialog.dismiss();
//                onBackPressed();
            }
//            pDialog.dismiss();
//            if (success.equals("1")) {
//                toast = Toast.makeText(getApplicationContext(), " Your vet clinic has been successfully updated ",
//                        Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//            }

        }
    }
    public class savedata extends AsyncTask<String,String ,String> {

        //String url="http://getsimplebisnis.com/index.php/api/insertklinik";
        String strNama,strAlamat,strKontak,strPelayanan,strJadwal,strKota,uid,success;
        DBAdapter db=new DBAdapter(VetAdd.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VetAdd.this);
            pDialog.setMessage(" Saving data... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            db.open();
            Cursor c=db.getContact(1);
            if (c.moveToFirst()) {
                uid=c.getString(5);
            }
            db.close();
            strNama=etxNama.getText().toString();
            strAlamat=etxAlamat.getText().toString();
            strKontak=etxKontak.getText().toString();
            strPelayanan=etxPelayanan.getText().toString();
            strJadwal=etxJadwal.getText().toString();
            strKota=actKota.getText().toString().trim().toUpperCase();

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", uid));
            nvp.add(new BasicNameValuePair("klnama", strNama));
            nvp.add(new BasicNameValuePair("klalamat", strAlamat));
            nvp.add(new BasicNameValuePair("klkontak", strKontak));
            nvp.add(new BasicNameValuePair("klkota", strKota));
            nvp.add(new BasicNameValuePair("kljasa", strPelayanan));
            nvp.add(new BasicNameValuePair("kljadwal", strJadwal));
            nvp.add(new BasicNameValuePair("klfoto", urlfoto));

            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("insert");

            } catch (Exception e) {

            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

            if (success.equals("1")) {
                onBackPressed();
            }
        }
    }
    public class kotaupdate extends AsyncTask<String,String ,String> {

        //        String url="http://getsimplebisnis.com/index.php/api/insertkota";
        String strKota,success;
        @Override
        protected String doInBackground(String... params) {
            strKota=actKota.getText().toString().trim().toUpperCase();
            Log.e("auto",strKota);

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("ktnama", strKota));

            JSONObject json = jsonParser.makeHttpRequest(url4, "POST", nvp);
            try {
                success = json.getString("response");
                Log.e("asa",success);

            } catch (Exception e) {

            }

            return null;
        }
    }
    public void isiBtnClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VetAdd.this, MainActivity.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        btnClearPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPhoto.setVisibility(View.GONE);
                btnClearPhoto.setVisibility(View.GONE);

                imgPhoto.setImageResource(R.drawable.picture_100_blue);
                float scaleDp = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (50*scaleDp + 0.5f);
                imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                ba1=null;
            }
        });
    }

    public class selectKlinik extends AsyncTask<String,String,String>{
        String success;
        dokter dok = new dokter();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VetAdd.this);
            pDialog.setMessage(" Processing... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            String url="http://getsimplebisnis.com/index.php/api/klinikby";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("klnama", etxNama.getText().toString()));
            JSONObject json = jsonParser.makeHttpRequest(url5, "POST", nvp);
            try {
                success="1";
                JSONArray jsonArray = json.getJSONArray("getklinik");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    dok.setId(obj.getInt("klid"));
                    dok.setNama(obj.getString("klnama"));
                    dok.setAlamat(obj.getString("klalamat"));
                    dok.setDetail(obj.getString("klkontak"));
                    dok.setKota(obj.getString("klkota"));
                    dok.setPelayanan(obj.getString("kljasa"));
                    dok.setJadwal(obj.getString("kljadwal"));
                    dok.setNamaDok(obj.getString("unama"));
//                    etxNama.setText(obj.getString("klnama").toString());
//                    etxAlamat.setText(obj.getString("klalamat").toString());
//                    etxKontak.setText(obj.getString("klkontak").toString());
//                    actKota.setText(obj.getString("klkota").toString());
//                    etxPelayanan.setText(obj.getString("kljasa").toString());
//                    etxJadwal.setText(obj.getString("kljadwal").toString());
//                    list3.add(dok);
                }
            } catch (JSONException e) {
                Log.e("asem", list.toString());
                success="0";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            etxNama.setText(dok.getNama());
            etxAlamat.setText(dok.getAlamat());
            etxKontak.setText(dok.getDetail());
            actKota.setText(dok.getKota());
            etxPelayanan.setText(dok.getPelayanan());
            etxJadwal.setText(dok.getJadwal());
        }
    }
    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(VetAdd.this);
        Bundle Parsing=getIntent().getExtras();
        String id=Parsing.getString("id");
        String namafoto=Parsing.getString("foto").trim();
        String success,success1;
        protected void onPreExecute() {
            super.onPreExecute();
//
            pDialog = new ProgressDialog(VetAdd.this);
            pDialog.setMessage("Wait image uploading!");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            namafoto=namafoto.replace("http://www.getsimplebisnis.com/Download/getvet/vetphotos/","");
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            Log.e("jah",namafoto);
            JSONObject json;
//            if(!namafoto.equals("null")){

            nvp.add(new BasicNameValuePair("name",namafoto));

            String delete="http://www.getsimplebisnis.com/index.php/api/coba3";
            json = jsonParser.makeHttpRequest(delete, "POST", nvp);
            try {
                success1=json.getString("hasil");
            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
//            }
//            if(ba1==null) {
                nvp = new ArrayList<NameValuePair>();
                //Wahyu Gatel
                nvp.add(new BasicNameValuePair("klid", id));
                nvp.add(new BasicNameValuePair("image", ba1));
                if (namafoto.equals("null")) {
                    nvp.add(new BasicNameValuePair("filename", System.currentTimeMillis() + ".jpg"));
                } else {
                    nvp.add(new BasicNameValuePair("filename", namafoto));
                }

//            nvp.add(new BasicNameValuePair("base64", ba1));
//            nvp.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
                json = jsonParser.makeHttpRequest(URL, "POST", nvp);
                try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(URL);
//                httppost.setEntity(new UrlEncodedFormEntity(nvp));
//                HttpResponse response = httpclient.execute(httppost);
//                String st = EntityUtils.toString(response.getEntity());
                    success = json.getString("ubahklinik");

//                Log.v("log_tag", "In the try Loop" + st);

                } catch (Exception e) {
                    Log.v("log_tag", "Error in http connection " + e.toString());
                }

            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (success.equals("1")) {
            toast = Toast.makeText(getApplicationContext(), " Your vet clinic has been successfully updated ",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

//            }
            onBackPressed();
            pDialog.dismiss();
        }
    }
    public class uploadToServer2 extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(VetAdd.this);
        //        Bundle Parsing=getIntent().getExtras();
//        String id=Parsing.getString("id");
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            //Wahyu Gatel
//            nvp.add(new BasicNameValuePair("klid",id));
            nvp.add(new BasicNameValuePair("image", ba1));
            nvp.add(new BasicNameValuePair("filename", System.currentTimeMillis() + ".jpg"));

//            nvp.add(new BasicNameValuePair("base64", ba1));
//            nvp.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
            JSONObject json = jsonParser.makeHttpRequest(URL1, "POST", nvp);
            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(URL);
//                httppost.setEntity(new UrlEncodedFormEntity(nvp));
//                HttpResponse response = httpclient.execute(httppost);
//                String st = EntityUtils.toString(response.getEntity());
                urlfoto=json.getString("url");
//                urlfoto=json.getString("url");

                Log.v("log_tag", "In the try Loop" + urlfoto);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }
    public class uploadToServer3 extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(VetAdd.this);
        Bundle Parsing=getIntent().getExtras();
        String id=Parsing.getString("id");
        String namafoto=Parsing.getString("foto").trim();
        String success,success1;
        protected void onPreExecute() {
            super.onPreExecute();
//
            pDialog = new ProgressDialog(VetAdd.this);
            pDialog.setMessage("Wait image uploading!");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            namafoto=namafoto.replace("http://www.getsimplebisnis.com/Download/getvet/vetphotos/","");
            ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
            Log.e("jah",namafoto);
            JSONObject json;
            if(!namafoto.equals("null")) {

                nvp.add(new BasicNameValuePair("name", namafoto));

                String delete = "http://www.getsimplebisnis.com/index.php/api/coba3";
                json = jsonParser.makeHttpRequest(delete, "POST", nvp);
                try {
                    success1 = json.getString("hasil");
                } catch (Exception e) {
                    Log.v("log_tag", "Error in http connection " + e.toString());
                }
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (success.equals("1")) {
            toast = Toast.makeText(getApplicationContext(), " Your vet clinic has been successfully updated ",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

//            }
            onBackPressed();
            pDialog.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(VetAdd.this, VetList.class);
        startActivity(intent);
        finish();*/

        Intent intent = new Intent(VetAdd.this, VetList.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int si=0;
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1){
                try {
                    Uri selectedImage = data.getData();

                    String[] filePathColumn = { MediaStore.MediaColumns.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
//                    Log.e("base64", "-----" + si);

                    int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
                    cursor.moveToFirst();


                    picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    decodeFile(picturePath);

                    // Image location URL
//                    Log.e("path", "----------------" + picturePath);

                    // Image
                    Bitmap bm = BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    //compres pertama kali
//                    bm.compress(Bitmap.CompressFormat.JPEG, 94, bao);
                    bm.compress(Bitmap.CompressFormat.JPEG, 40, bao);
                    byte[] ba = bao.toByteArray();
                    ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
                    si=ba.length/1024;
//                    Log.e("base64", "-----" + ba1);
                    Log.e("base64", "-----" + si);
                }catch (Exception e){
                    toast = Toast.makeText(getApplicationContext(), "File not allowed, Please select another picture.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    tvPhoto.setVisibility(View.GONE);
                    btnClearPhoto.setVisibility(View.GONE);

                    imgPhoto.setImageResource(R.drawable.picture_100_blue);
                    float scaleDp = getResources().getDisplayMetrics().density;
                    int dpAsPixels = (int) (50*scaleDp + 0.5f);
                    imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                    ba1=null;
                }
//                if(si>512){
//                    tvPhoto.setVisibility(View.GONE);
//                    btnClearPhoto.setVisibility(View.GONE);
//
//                    imgPhoto.setImageResource(R.drawable.picture_100_blue);
//                    float scaleDp = getResources().getDisplayMetrics().density;
//                    int dpAsPixels = (int) (50*scaleDp + 0.5f);
//                    imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
//                    ba1=null;
//                    toast = Toast.makeText(getApplicationContext(), "maaf foto terlalu besar",
//                            Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
            }
        }
    }

    public void decodeFile(String filePath) {
        Log.e("patc",filePath);
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        // Show the Selected Image on ImageView
        tvPhoto.setVisibility(View.VISIBLE);
        btnClearPhoto.setVisibility(View.VISIBLE);

        imgPhoto.setImageBitmap(bitmap);
        float scaleDp = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (2*scaleDp + 0.5f);
        imgPhoto.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VetAdd.this);
            pDialog.setMessage(" Processing... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];
            fname="k";
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] ba = bao.toByteArray();
                ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Utils.saveImage1(result, fname, VetAdd.this);
            decodeFile(VetAdd.this.getFilesDir().toString() + "/dir/k.jpg");
            pDialog.dismiss();
        }
    }
}
