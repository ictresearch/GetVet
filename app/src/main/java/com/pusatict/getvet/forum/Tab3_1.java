package com.pusatict.getvet.forum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pusatict.getvet.AnalyticsApplication;
import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.Katforum;
import com.pusatict.getvet.datalistadapter.ListAdapterKatforum;
import com.pusatict.getvet.datalistadapter.ListAdapterThread;
import com.pusatict.getvet.datalistadapter.thread;
import com.pusatict.getvet.sponsor.Banner;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab3_1 extends Activity implements AdapterView.OnItemSelectedListener{
    private ListAdapterThread adapter;
    private ListAdapterKatforum adapter1;
    private ListView lvkatforum;
    private LinearLayout ll1;
    private ProgressDialog pDialog3;
    private List<thread> list;
    private List<Katforum> list1;
    private int offset;
    private int posisi=0;
    private String kat1="'BIRDS','OTHERS','EQUINE AND BIG MAMMALS','EXOTIC ANIMALS','REPTILES AND AMPHIBIANS','DOG','CAT'";
    private String katid,kat="1";
    private JSONParser jsonParser= new JSONParser();
    private thread selectedList;
    private Katforum selectedList1;
    private ArrayAdapter<String> dataAdapter;
    List<String> categories;
    private Spinner spinner;
    private boolean flag_loading=false;
    private Button btncari;
    private ImageButton imgBack,btnNewThread;
    private CheckBox HKburung,HKkucing,HKanjing,HKReptile,HKfish,HKexotic,HKequine,HKother;
    private int gakadacheckHK=8;
    private String burung="1",kucing="1",anjing="1",Reptile="1",Fish="1",exotic="1",equine="1",other="1";
    private Toast toast;
    private TextView tvkat;
    private int isUp=1;
    private EditText etxCari;
    private ImageView imgBanner;
    DBAdapter db=new DBAdapter(Tab3_1.this);
    private String link,urlgambar,uid,tanggal;
    private int poin,poin2=0;
    Calendar c1 = Calendar.getInstance();
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd");
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_kat1);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("All Subcategory");
        categories.add("Cat");
        categories.add("Dog");
        categories.add("Birds");
        categories.add("Reptiles And Amphibians");
        categories.add("Fish");
        categories.add("Exotic Animals");
        categories.add("Equine And Big Mammals");
        categories.add("Other");

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this, R.layout.listspinner, categories);


        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.listdown);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        imgBanner=(ImageView)findViewById(R.id.imgBanner);

        etxCari=(EditText)findViewById(R.id.etxCari);
        Bundle Parsing=getIntent().getExtras();
        tvkat=(TextView)findViewById(R.id.tvkat);
        tvkat.setText(Parsing.getString("katnama"));
        //forum_kat
//        ll1=(LinearLayout)findViewById(R.id.LL1);
        btncari=(Button)findViewById(R.id.btnSave);
        lvkatforum=(ListView)findViewById(R.id.lvKatforum);
//        spinner = (Spinner)findViewById(R.id.spinner);

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        btnNewThread=(ImageButton)findViewById(R.id.imgNew);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        list=new ArrayList<thread>();
        adapter = new ListAdapterThread(Tab3_1.this, list);
        Log.e("septian", "septo");
        new selectthread().execute();
        tanggal = Ftgl.format(c1.getTime());
        db.open();
        Cursor c1 = db.getContactbanner(1);
        if (c1.moveToFirst()) {
            poin=c1.getInt(1)+1;
        }
        Cursor c = db.getContact(1);
        if (c.moveToFirst()) {
            uid=c.getString(5);
        }
        db.close();
        new MainActivityAsync1().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        lvkatforum.setAdapter(adapter);
        spinner.setAdapter(dataAdapter);

//        spinner.setOnItemSelectedListener(this);
//        list1=new ArrayList<Katforum>();
//        categories = new ArrayList<String>();
//        adapter1 = new ListAdapterKatforum(Tab3_1.this, list1);
//        new selectsubkat().execute("1");
//        Log.e("logpos",posisi+"");
//        if(posisi==0) {
        //forum_kat1
//        new selectKategori().execute();
//        new selectKategori1().execute();


        //forum_kat

//            dataAdapter = new ArrayAdapter<String>(Tab3_1.this, android.R.layout.simple_spinner_item, categories);
//
//            // Drop down layout style - list view with radio button
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            // attaching data adapter to spinner
//            spinner.setAdapter(dataAdapter);
//        }
        lvkatforum.setAdapter(adapter);
        //Load more
        lvkatforum.setOnScrollListener(new AbsListView.OnScrollListener() {
            int i = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {
                        flag_loading = true;
                        Log.e("roll", offset+"");
                        new selectthread().execute();
                    }
                    i = i + 1;
                    Log.e("roll", "septi" + i);

                }
            }
        });
        //forum_kat
//        HKburung=(CheckBox)findViewById(R.id.chbBurung);
//        HKburung.setOnCheckedChangeListener(this);
//        HKkucing=(CheckBox)findViewById(R.id.chbKucing);
//        HKkucing.setOnCheckedChangeListener(this);
//        HKanjing=(CheckBox)findViewById(R.id.chbAnjing);
//        HKanjing.setOnCheckedChangeListener(this);
//        HKReptile=(CheckBox)findViewById(R.id.chbReptile);
//        HKReptile.setOnCheckedChangeListener(this);
//        HKfish=(CheckBox)findViewById(R.id.chbfish);
//        HKfish.setOnCheckedChangeListener(this);
//        HKother=(CheckBox)findViewById(R.id.chbOther);
//        HKother.setOnCheckedChangeListener(this);
//        HKequine=(CheckBox)findViewById(R.id.chbEquine);
//        HKequine.setOnCheckedChangeListener(this);
//        HKexotic=(CheckBox)findViewById(R.id.chbExotic);
//        HKexotic.setOnCheckedChangeListener(this);
        fillListView();

        lvkatforum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ll1.setVisibility(View.VISIBLE);
//                btncari.setVisibility(View.VISIBLE);

//                if (posisi == 1) {
//                    Log.e("posisi", posisi + "");
//                    selectedList1 = (Katforum) lvkatforum.getAdapter().getItem(position);
////
////                    list = new ArrayList<thread>();
////                    adapter = new ListAdapterThread(getActivity(), list);
//                    katid = selectedList1.getId().toString();
//                Log.e("asik tumpak sitik jos",katid+"");
//                    lvkatforum.setAdapter(adapter);
//                } else {
                    selectedList = (thread) lvkatforum.getAdapter().getItem(position);
                    Intent intent = new Intent(Tab3_1.this, ForumDetails.class);
                    Bundle Parsing = new Bundle();
                    Parsing.putString("thid", selectedList.getId().toString());
                    Parsing.putString("judul", selectedList.getJudul().toString());
                    Parsing.putString("isi", selectedList.getIsi().toString());
                    Parsing.putString("uid", selectedList.getUid().toString());
                    Parsing.putString("katid", selectedList.getKatid().toString());
                    Parsing.putString("date", selectedList.getDate().toString());
                    Parsing.putString("namauser", selectedList.getNamauser().toString());
                    Parsing.putString("jumcomment", selectedList.getComment().toString());
                    Parsing.putString("foto", selectedList.getFoto().toString());
                    Parsing.putString("katnama",tvkat.getText().toString());
                    Parsing.putString("to", "0");
                    intent.putExtras(Parsing);
                    startActivity(intent);
//
                    overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
//                }
            }
        });
//        FragmentManager fm = getFragmentManager();
//        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//            }
//        });
        //coding back button
//        v.setFocusableInTouchMode(true);
//        v.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
////                    if(posisi==2) {
////                        ll1.setVisibility(View.GONE);
////                        btncari.setVisibility(View.GONE);
////                        list1 = new ArrayList<Katforum>();
////                        adapter1 = new ListAdapterKatforum(getActivity(), list1);
////                        new selectKategori().execute();
////                        lvkatforum.setAdapter(adapter1);
////                        posisi=posisi-1;
////                    }
//                    return true;
//                }
//                return false;
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      String item = parent.getItemAtPosition(position).toString();
//        if(poin2==0){
//            poin2++;
//            super.onBackPressed();
//        }
        spinner.setSelection(position);
        if(item.equals("All Subcategory")){
            kat1="'BIRDS','OTHERS','EQUINE AND BIG MAMMALS','EXOTIC ANIMALS','REPTILES AND AMPHIBIANS','DOG','CAT'";
        }else{
            item=item.toUpperCase();
            kat1="'"+item+"'";
        }
        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), kat1+"", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    ////
    public void fillListView(){
        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tab3_1.this, Banner.class);
                Bundle Parsing = new Bundle();
//                Parsing.putString("banner", "http://www.animalrapidtest.com/");
                Parsing.putString("banner", link);
                Parsing.putString("broadcash", "0");
                Parsing.putString("tdid", "");
                intent.putExtras(Parsing);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        btnNewThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tab3_1.this, ForumAdd.class);

                Bundle Parsing = new Bundle();
                Parsing.putString("pos", "0");
                intent.putExtras(Parsing);
                startActivity(intent);
                finish();
                overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
            }
        });
        btncari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("gak da",gakadacheckHK+"");
                if(gakadacheckHK>0) {
                    isUp = 1;
//                    Log.e("septian aq", spinner.))
//                    kat = "1";
//                    katlis();
                    offset = 0;
                    list=new ArrayList<thread>();
                    adapter = new ListAdapterThread(v.getContext(), list);
                    new selectthread().execute();
                    lvkatforum.setAdapter(adapter);
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Search kategori forum")
                            .setAction(etxCari.getText().toString()+",kat="+kat1)
                            .build());
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    } catch (Exception e) {

                    }
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Please choose subcategory. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
//                Log.e("Asda",kat+","+katid+"");
            }
        });
    }


//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        switch (buttonView.getId()){
//            case R.id.chbBurung:
//                if(isChecked){
//
//                    burung="1";
//                    gakadacheckHK++;
//                }else {
//                    burung="0";
//                    gakadacheckHK--;
//                }
//                break;
//            case R.id.chbAnjing:
//                if(isChecked){
//                    anjing="1";
//                    gakadacheckHK++;
//                }else {
//                    anjing="0";
//                    gakadacheckHK--;
//                }
//                break;
//            case R.id.chbKucing:
//                if(isChecked){
//                    kucing="1";
//                    gakadacheckHK++;
//                }else {
//                    kucing="0";
//                    gakadacheckHK--;
//                }
//                break;
//            case R.id.chbReptile:
//                if(isChecked){
//                    Reptile="1";
//                    gakadacheckHK++;
//                }else {
//                    Reptile="0";
//                    gakadacheckHK--;
//                }
//                break;
//            case R.id.chbOther:
//                if(isChecked){
//                    other="1";
//                    gakadacheckHK++;
//                }else {
//                    other="0";
//                    gakadacheckHK--;
//                }
//                break;
//            case R.id.chbfish:
//                if(isChecked){
//                    Fish="1";
//                    gakadacheckHK++;
//                }else {
//                    Fish="0";
//                    gakadacheckHK--;
//                }
//                break;
//            case R.id.chbEquine:
//                if(isChecked){
//                    equine="1";
//                    gakadacheckHK++;
//                }else {
//                    equine="0";
//                    gakadacheckHK--;
//                }
//                break;
//            case R.id.chbExotic:
//                if(isChecked){
//                    exotic="1";
//                    gakadacheckHK++;
//                }else {
//                    exotic="0";
//                    gakadacheckHK--;
//                }
//                break;
//
//        }
//    }
    private void katlis(){
        if(burung.equals("1")){
            kat=kat+",'BIRDS'";
        }
        if(kucing.equals("1")){
            kat=kat+",'CAT'";
        }
        if(anjing.equals("1")){
            kat=kat+",'DOG'";
        }
        if(Reptile.equals("1")){
            kat=kat+",'REPTILES AND AMPHIBIANS'";
        }
        if(Fish.equals("1")){
            kat=kat+",'FISH'";
        }
        if(exotic.equals("1")){
            kat=kat+",'EXOTIC ANIMALS'";
        }
        if (equine.equals("1")){
            kat=kat+",'EQUINE AND BIG MAMMALS'";
        }
        if(other.equals("1")){
            kat=kat+",'OTHERS'";
        }

        kat=kat.replace("1,","");

    }

    private class selectthread extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog3 = new ProgressDialog(Tab3_1.this);
            pDialog3.setMessage("Loading data... ");
            pDialog3.setIndeterminate(true);
            pDialog3.setCancelable(false);
            pDialog3.show();
        }
        @Override
        protected String doInBackground(String... params) {
            Bundle Parsing=getIntent().getExtras();
            katid=Parsing.getString("katid");
//                String url="http://10.0.2.2/klinikhewan/select_all.php";
//                String url="http://192.168.0.103/klinikhewan/selectkliniklimit";
            String url="http://getsimplebisnis.com/index.php/api/threadkat";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("offset", offset+""));
            nvp.add(new BasicNameValuePair("katid", katid+""));
            nvp.add(new BasicNameValuePair("kategori", kat1+""));
            nvp.add(new BasicNameValuePair("cari", etxCari.getText().toString()));
            Log.e("eror", "data lengt: " + katid+","+offset+","+kat1);
            JSONObject json = jsonParser.makeHttpRequest(url, "GET", nvp);
//            JSONParser jParser = new JSONParser();
//            JSONObject json = jParser.getJSONFromUrl(url);
            try {
                JSONArray jsonArray = json.getJSONArray("TK");
                thread thr=null;
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    thr = new thread();
                    thr.setId(obj.getLong("thid"));
                    thr.setJudul(obj.getString("thjudul"));
                    thr.setIsi(obj.getString("thisi"));
                    thr.setUid(obj.getString("uid"));
                    thr.setSpid(obj.getString("spid"));
                    thr.setKatid(obj.getString("katid"));
                    thr.setDate(obj.getString("thtanggalPost"));
                    thr.setComment(obj.getString("com"));
                    thr.setNamauser(obj.getString("unama"));
                    thr.setFoto(obj.getString("thfoto"));
                    Log.e("septi",obj.getString("uid"));
                    if(!obj.getString("uid").equals("")){
                        flag_loading=false;
                    }
                    adapter.add(thr);
                    Log.d("eror", "data lengt: " + jsonArray.length());
                }
            }catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog3.dismiss();
            Log.e("eror",posisi+"");
            if(posisi==0){
             lvkatforum.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
            offset=offset+10;
            posisi=posisi+1;
        }
    }
    private class selectKategori extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog3= new ProgressDialog(getActivity());
//            pDialog3.setMessage("Finding Data... ");
//            pDialog3.setIndeterminate(true);
//            pDialog3.setCancelable(false);
//            pDialog3.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String url="http://getsimplebisnis.com/index.php/api/katforum";
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);

            try {
                JSONArray jsonArray = json.getJSONArray("katForum");
                Katforum dok=null;
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    dok = new Katforum();
                    dok.setId(obj.getInt("katid"));
                    dok.setNama(obj.getString("katNama"));
                    Log.e("tes", dok.getNama());
                    list1.add(dok);
                    categories.add(obj.getString("katNama"));
                }
            }catch (JSONException e) {

            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
//            pDialog3.dismiss();
            dataAdapter = new ArrayAdapter<String>(Tab3_1.this, R.layout.listspinner ,categories);
            adapter1 = new ListAdapterKatforum(getApplicationContext(), list1);
            dataAdapter.setDropDownViewResource(R.layout.listdown);
            // attaching data adapter to spinner
//            spinner.setAdapter(dataAdapter);

        }
    }
    private class selectKategori1 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog3= new ProgressDialog(getActivity());
//            pDialog3.setMessage("Finding Data... ");
//            pDialog3.setIndeterminate(true);
//            pDialog3.setCancelable(false);
//            pDialog3.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String url="http://getsimplebisnis.com/index.php/api/katforum";
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);

            try {
                JSONArray jsonArray = json.getJSONArray("katForum");
                Katforum dok=null;
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    dok = new Katforum();
                    dok.setId(obj.getInt("katid"));
                    dok.setNama(obj.getString("katNama"));
                    Log.e("tes", dok.getNama());
//                    list1.add(dok);
                    adapter1.add(dok);
//                    categories.add(obj.getString("katNama"));
                }
            }catch (JSONException e) {

            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
//            pDialog3.dismiss();
//            dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listspinner ,categories);
//            adapter1 = new ListAdapterKatforum(getActivity().getApplicationContext(), list1);
            adapter1.notifyDataSetChanged();
//            dataAdapter.setDropDownViewResource(R.layout.listdown);
            // attaching data adapter to spinner
//            spinner.setAdapter(dataAdapter);

        }
    }
    private class MainActivityAsync1 extends AsyncTask<String, Void, String> {

        String succes="0",succes1="0";
        Bitmap bitmap = null;
        int id;
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog3 = new ProgressDialog(Tab3_1.this);
//            pDialog3.setMessage("Finding Data... ");
//            pDialog3.setIndeterminate(true);
//            pDialog3.setCancelable(false);
//            pDialog3.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            String jam=waktu();
            String url="http://getsimplebisnis.com/index.php/api/baner";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("tikorder", poin+""));
            nvp.add(new BasicNameValuePair("tikmulai", tanggal));
            nvp.add(new BasicNameValuePair("uid", uid));
            Log.e("benner", poin + "," + uid+","+tanggal);
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);

            try {
                succes1=json.getString("success");
                Log.e("benner", succes1);
                if(succes1.equals("1")){

                    JSONArray jsonArray = json.getJSONArray("baner");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        link = (obj.getString("tikhtml"));
                        urlgambar = (obj.getString("tikgambar1"));
                        poin=(obj.getInt("tikorder"));
                        Log.e("benner", link + "," + urlgambar);
                        dowloadgambar(urlgambar);

                    }
                }
            }catch (JSONException e) {
                Log.e("asem", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            pDialog3.dismiss();
//            db.open();
//            db.updatebanner(1, 0);
//            db.close();
            if(succes1.equals("0")){
                Log.e("benner1", succes1);
                db.open();
                db.updatebanner(1, 0);
                if(poin>1) {
                    Cursor c1 = db.getContactbanner(1);
                    if (c1.moveToFirst()) {
                        poin = c1.getInt(1) + 1;
                    }
                    db.close();
                    new MainActivityAsync1().execute();
                } else {
                    imgBanner.setVisibility(View.GONE);
                    float scaleDp = getResources().getDisplayMetrics().density;
                    int pixelDp10 = (int) (10 * scaleDp + 0.5f);
//                    lvkatforum.setPadding(0, 0,0,0);
                }
            }else{
                db.open();
                db.updatebanner(1, poin);
                db.close();
                imgBanner.setVisibility(View.VISIBLE);
                float scaleDp = getResources().getDisplayMetrics().density;
                int pixelDp10 = (int) (10 * scaleDp + 0.5f);
                int pixelDp = (int) (80 * scaleDp + 0.5f);
//                lvkatforum.setPadding(0,0,0,pixelDp);
            }

        }
    }
    public void dowloadgambar(String URL){
        String imageURL = URL;

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
            imgBanner.setImageBitmap(bitmap);
//            Utils.saveImage(bitmap, fname, coba.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}