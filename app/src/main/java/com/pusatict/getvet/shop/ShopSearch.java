package com.pusatict.getvet.shop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pusatict.getvet.AnalyticsApplication;
import com.pusatict.getvet.MainActivity;
import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.ListAdapterPetShop;
import com.pusatict.getvet.datalistadapter.PetShop;
import com.pusatict.getvet.datalistadapter.dokter;
import com.pusatict.getvet.klinik.VetDetails;
import com.pusatict.getvet.sponsor.Banner;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.tesKoneksiInet;

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
 * Created by ICT on 28/08/2015.
 */
public class ShopSearch extends Activity {
    private ImageButton imgBack, imgDown;
    private ImageView imgBanner;
    private Button btnSearch;
    private ListView lvSearch;
    private List<PetShop> list;
    private EditText eSearch, eSearchAlamat, eSearchJasa;
    private ListAdapterPetShop adapter,adapter1;
    private LinearLayout lyMain;

    private int isUp;
    private PetShop selectedList;
    private tesKoneksiInet koneksiInet;
    private Toast toast;
    private ProgressDialog pDialog;
    private int awal=0;
    private Tracker mTracker;
    private ImageButton image;


    DBAdapter db=new DBAdapter(ShopSearch.this);
    String tanggal;
    Calendar c1 = Calendar.getInstance();
    SimpleDateFormat Ftgl=new SimpleDateFormat("yyyy-MM-dd");
    JSONParser jsonParser = new JSONParser();
    private String link,urlgambar,uid;
    private int poin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_search);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgDown = (ImageButton)findViewById(R.id.imgDown);
        lvSearch = (ListView)findViewById(R.id.lvSearch);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        eSearch = (EditText)findViewById(R.id.eSearch);
        eSearchAlamat = (EditText)findViewById(R.id.eSearchAlamat);
        eSearchJasa = (EditText)findViewById(R.id.eSearchJasa);
        imgBanner =(ImageView)findViewById(R.id.imgBanner);

        lyMain =(LinearLayout)findViewById(R.id.lymain);

        isUp = 0;
        eSearchAlamat.setVisibility(View.GONE);
        eSearchJasa.setVisibility(View.GONE);
        imgDown.setImageResource(R.drawable.down_icon);
        list= new ArrayList<PetShop>();

        tanggal=Ftgl.format(c1.getTime());
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
        new MainActivityAsync1().execute();
        new MainActivityAsync().execute("load");
        fillListView();
        isiBtnClick();

//        image=(ImageButton)findViewById(R.id.imgBanner);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("asek", newConfig.keyboard + "");

        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }

    public void fillListView(){
//        String[] values = new String[] { "Android List View",
//                "Adapter implementation",
//                "Simple List View In Android",
//                "Create List View Android",
//                "Android Example",
//                "List View Source Code",
//                "List View Array Adapter",
//                "Android Example List View"
//        };
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, values);
//
//        lvSearch.setAdapter(adapter);

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList = (PetShop) lvSearch.getAdapter().getItem(position);
                Intent intent = new Intent(ShopSearch.this, ShopDetails.class);
                Bundle Parsing = new Bundle();
                Parsing.putString("nama", selectedList.getNama().toString());
                Parsing.putString("alamat", selectedList.getAlamat().toString());
                Parsing.putString("kota", selectedList.getKota().toString());
                Parsing.putString("kontak", selectedList.getDetail().toString());
                Parsing.putString("jasa", selectedList.getPelayanan().toString());
                Parsing.putString("jadwal", selectedList.getJadwal().toString());
                Parsing.putString("namadok", selectedList.getNamaDok().toString());
                Parsing.putString("klfoto", selectedList.getFoto().toString());
                intent.putExtras(Parsing);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        lvSearch.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList = (PetShop) lvSearch.getAdapter().getItem(position);
                String stv = selectedList.getId().toString();
                if (!stv.equals("0")) {
                    munculDialog(position, stv);
                }
                return false;
            }
        });
    }
    private void munculDialog(int i,final String txt) {
        final CharSequence[] items = {"Call", "SMS"};
        selectedList = (PetShop) lvSearch.getAdapter().getItem(i);
        AlertDialog.Builder kk = new AlertDialog.Builder(this);
        kk.setTitle(" Choose Action ");
        kk.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if(items[item].equals("Call")){
                    String toDial="tel:"+selectedList.getDetail().toString();
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(toDial)));
                }else{
                    Uri uri =Uri.parse("smsto:"+selectedList.getDetail().toString());

                    Intent smsIntent=new Intent(Intent.ACTION_SENDTO,uri);
                    smsIntent.putExtra("sms_body","");
                    try {
                        startActivity(smsIntent);
                    }catch (Exception e){

                    }
                }
            }
        }).show();
    }

    public void isiBtnClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUp = 0;
                eSearchAlamat.setVisibility(View.GONE);
                eSearchJasa.setVisibility(View.GONE);
                imgDown.setImageResource(R.drawable.down_icon);

                final String search=eSearch.getText().toString().toLowerCase();
                final String alamat=eSearchAlamat.getText().toString().toLowerCase();
                final String jasa=eSearchJasa.getText().toString().toLowerCase();

                if(search.equals("")&&jasa.equals("")&&alamat.equals("")){
                    populateListView();
                    awal++;
                    toast = Toast.makeText(getApplicationContext(),"About " + adapter1.getCount() + " results",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    if(awal==0){
                        populateListView();

//                        toast = Toast.makeText(getApplicationContext(), "Found " + adapter1.getCount() + " record(s)",
//                                Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        awal++;
                    }
                    adapter1.setCari1(search, alamat, jasa);
                    adapter1.getFilter().filter("12937182");
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Search Pet Shop")
                            .setAction(search + "," + alamat + "," + jasa)
                            .build());
                }

                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }

//                Log.e("jancok",search+" "+alamat+" "+jasa);


//                adapter1.setIndex(0);
//                adapter1.getFilter().filter(search);
//                adapter1.setIndex(1);
//                adapter1.getFilter().filter(alamat);
//                adapter1.setIndex(2);
//                adapter1.getFilter().filter(jasa);


            }
        });

        imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUp == 1) {
                    isUp = 0;
                    eSearchAlamat.setVisibility(View.GONE);
                    eSearchJasa.setVisibility(View.GONE);
                    imgDown.setImageResource(R.drawable.down_icon);
                } else {
                    isUp = 1;
                    eSearchAlamat.setVisibility(View.VISIBLE);
                    eSearchJasa.setVisibility(View.VISIBLE);
                    imgDown.setImageResource(R.drawable.up_icon);
                }
            }
        });
        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopSearch.this, Banner.class);
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
    }
    private class MainActivityAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShopSearch.this);
            pDialog.setMessage("Loading data... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String url="http://getsimplebisnis.com/index.php/api/petshop";
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);
            PetShop shop=null;
            try {
                JSONArray jsonArray = json.getJSONArray("shop");
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    shop = new PetShop();
                    shop.setId(obj.getInt("spid"));
                    shop.setNama(obj.getString("spnama"));
                    shop.setAlamat(obj.getString("spalamat"));
                    shop.setDetail(obj.getString("spkontak"));
                    shop.setKota(obj.getString("spkota"));
                    shop.setPelayanan(obj.getString("spjasa"));
                    shop.setJadwal(obj.getString("spjadwal"));
                    shop.setNamaDok(obj.getString("unama"));
                    shop.setFoto(obj.getString("spfoto"));
                    list.add(shop);
                    Log.d("eror", "data lengt: " + jsonArray.length());
                }
            }catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
//            populateListView();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
        }
    }
    private void populateListView(){
        adapter1 = new ListAdapterPetShop(getApplicationContext(), list);
        lvSearch.setAdapter(adapter1);
    }
    private class MainActivityAsync1 extends AsyncTask<String, Void, String> {

        String succes="0",succes1="0";
        Bitmap bitmap = null;
        int id;
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(ShopSearch.this);
//            pDialog.setMessage("Finding Data... ");
//            pDialog.setIndeterminate(true);
//            pDialog.setCancelable(false);
//            pDialog.show();
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
            pDialog.dismiss();
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
                    lyMain.setPadding(pixelDp10, pixelDp10, pixelDp10, pixelDp10);
                }
            }else{
                db.open();
                db.updatebanner(1, poin);
                db.close();
                imgBanner.setVisibility(View.VISIBLE);
                float scaleDp = getResources().getDisplayMetrics().density;
                int pixelDp10 = (int) (10 * scaleDp + 0.5f);
                int pixelDp = (int) (80 * scaleDp + 0.5f);
                lyMain.setPadding(pixelDp10, pixelDp10, pixelDp10, pixelDp);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ShopSearch.this, MainActivity.class);
        startActivity(intent);
//        overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
        finish();
        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }

}
