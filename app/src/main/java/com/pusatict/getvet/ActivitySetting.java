package com.pusatict.getvet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pusatict.getvet.catken.CatkenList;
import com.pusatict.getvet.klinik.VetList;
import com.pusatict.getvet.shop.ShopList;
import com.pusatict.getvet.sponsor.SponsorList;
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
 * Created by ICT on 31/08/2015.
 */
public class ActivitySetting extends Activity{
    private ImageButton imgBack, btnProfile, btnMyVet, btnTerm, btnRate, btnLogout, btnSponsor, btnVersion,btnShop,btnCatken,btnSettingNotif;
    private TextView tvProfile, tvMyVet, tvTerm, tvRate, tvLogout, tvSponsor, tvVersion,tvShop,tvCatken,tvSettingNotif;
    private TextView tvDivVet,tvDivShop,tvDivcatken;
    DBAdapter db=new DBAdapter(ActivitySetting.this);
    public String dokter, ppshop;
    private tesKoneksiInet koneksiInet;
    private Toast toast;
    private Context context;
    private ProgressDialog pDialog;
    private JSONParser jsonParser= new JSONParser();
    private String id;
    public String stremail,dokterhewan, ppshops, uid, strnama, strhp, strkota,strsrv,strprof,pass,cetkan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        koneksiInet = new tesKoneksiInet(getApplicationContext());

        imgBack = (ImageButton)findViewById(R.id.imgBack);

        tvProfile = (TextView)findViewById(R.id.tvmenuprofile);
        tvMyVet = (TextView)findViewById(R.id.tvmenuvet);
        tvShop = (TextView)findViewById(R.id.tvmenushop);
        tvCatken = (TextView)findViewById(R.id.tvmenucatken);
        tvTerm = (TextView)findViewById(R.id.tvmenuterm);
        tvRate = (TextView)findViewById(R.id.tvmenurate);
        tvLogout = (TextView)findViewById(R.id.tvmenuout);
        tvSponsor=(TextView)findViewById(R.id.tvmenuseponsor);
        tvVersion=(TextView)findViewById(R.id.tvmenuver);
        tvSettingNotif=(TextView)findViewById(R.id.tvSettingNotif);

        btnProfile = (ImageButton)findViewById(R.id.imgmenuprofile);
        btnMyVet = (ImageButton)findViewById(R.id.imgmenuvet);
        btnShop = (ImageButton)findViewById(R.id.imgmenushop);
        btnCatken=(ImageButton)findViewById(R.id.imgmenucatken);
        btnTerm = (ImageButton)findViewById(R.id.imgmenuterm);
        btnRate = (ImageButton)findViewById(R.id.imgmenurate);
        btnLogout = (ImageButton)findViewById(R.id.imgmenuout);
        btnSponsor = (ImageButton)findViewById(R.id.imgmenuseponsor);
        btnVersion = (ImageButton)findViewById(R.id.imgmenuver);
        btnSettingNotif = (ImageButton)findViewById(R.id.imgSettingNotif);

        tvDivVet = (TextView)findViewById(R.id.tvdivvetclinic);
        tvDivShop = (TextView)findViewById(R.id.tvdivpetshop);
        tvDivcatken = (TextView)findViewById(R.id.tvdivcatken);

        db.open();
        Cursor c=db.getContact(1);
        if (c.moveToFirst()){
            dokter = c.getString(3);
            ppshop = c.getString(4);
            cetkan=c.getString(11);
        }

        db.close();

        isiBtnClick();
        if(dokter.equals("1")){
            btnMyVet.setVisibility(View.VISIBLE);
            tvMyVet.setVisibility(View.VISIBLE);
            tvDivVet.setVisibility(View.VISIBLE);
        }
        if(ppshop.equals("1")){
            btnShop.setVisibility(View.VISIBLE);
            tvShop.setVisibility(View.VISIBLE);
            tvDivShop.setVisibility(View.VISIBLE);
        }
        if(cetkan.equals("1")){
            btnCatken.setVisibility(View.VISIBLE);
            tvCatken.setVisibility(View.VISIBLE);
            tvDivcatken.setVisibility(View.VISIBLE);
        }
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        tvVersion.setText("Version : " + version);
    }

    public void isiBtnClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(koneksiInet.isNetworkAvailable()) {
                    new selectuser().execute();
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    new selectuser().execute();
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        tvSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySetting.this, SponsorList.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        btnSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySetting.this, SponsorList.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        tvTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySetting.this, TeamOfService.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        btnTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySetting.this, TeamOfService.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        tvSettingNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySetting.this, SettingNotif.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        btnSettingNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySetting.this, SettingNotif.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        tvMyVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(ActivitySetting.this, VetList.class);
                    startActivity(intent);
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        btnMyVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(ActivitySetting.this, VetList.class);
                    startActivity(intent);
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        tvShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(ActivitySetting.this, ShopList.class);
                    startActivity(intent);
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(ActivitySetting.this, ShopList.class);
                    startActivity(intent);
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        tvCatken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(ActivitySetting.this, CatkenList.class);
                    startActivity(intent);
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        btnCatken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(koneksiInet.isNetworkAvailable()) {
                    Intent intent = new Intent(ActivitySetting.this, CatkenList.class);
                    startActivity(intent);
                }else{
                    toast = Toast.makeText(getApplicationContext(), " Sorry! No internet connection. ",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                boolean rate;
                //Try Google play
                intent.setData(Uri.parse("market://details?id=com.pusatict.getvet"));
                try {
                    startActivity(intent);
                    rate=true;
                } catch (ActivityNotFoundException e) {
                    rate=false;
                }
                if (!rate) {
                    //Market (Google play) app seems not installed, let's try to open a webbrowser
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.pusatict.getvet"));
                    if (!rate) {
                    }
                }
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                boolean rate;
                //Try Google play
                intent.setData(Uri.parse("market://details?id=com.pusatict.getvet"));
                try {
                    startActivity(intent);
                    rate=true;
                } catch (ActivityNotFoundException e) {
                    rate=false;
                }
                if (!rate) {
                    //Market (Google play) app seems not installed, let's try to open a webbrowser
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.pusatict.getvet"));
                    if (!rate) {
                    }
                }

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                db.deleteContactLogin(1);
                db.close();
                Intent intent = new Intent(ActivitySetting.this, ActivityLogin.class);
                startActivity(intent);
                finish();
                finishAffinity();
                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                db.deleteContactLogin(1);
                db.close();
                Intent intent = new Intent(ActivitySetting.this, ActivityLogin.class);
                startActivity(intent);
                finish();
                finishAffinity();

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActivitySetting.this, MainActivity.class);
        startActivity(intent);
//        overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
        finish();
        overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
    }

    public class selectuser extends AsyncTask<String,String,String>{


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivitySetting.this);
            pDialog.setMessage("Finding Data... ");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            db.open();
            Cursor c=db.getContact(1);
            if (c.moveToFirst()){
                id=c.getString(5);
            }
            db.close();

            String url="http://getsimplebisnis.com/index.php/api/user";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("uid", id));
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                JSONArray jsonArray = json.getJSONArray("user");
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    uid=(obj.getString("uid"));
                    stremail=(obj.getString("uemail"));
                    dokterhewan=(obj.getString("ucekdokter"));
                    ppshops=(obj.getString("ucekshop"));
                    cetkan=(obj.getString("ucekcatken"));
                    strnama=(obj.getString("unama"));
                    strhp=(obj.getString("ukontak"));
                    strkota=(obj.getString("ukota"));
                    strsrv=(obj.getString("ustrv"));
                    strprof=(obj.getString("uprovinsi"));
                    pass=(obj.getString("upass"));
                    if(strsrv.equals("null")){
                        strsrv="";
                    }
                    if(strprof.equals("null")){
                        strprof="";
                    }
                }
            }catch (JSONException e){
                Log.e("eror",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
//            Log.e("id", stremail);
            db.open();
            db.updateprofil(1, stremail, dokterhewan, ppshops,cetkan, strnama, strhp, strkota, strsrv, strprof);
            db.close();
            Intent intent = new Intent(ActivitySetting.this, UserProfile.class);
            Bundle Parsing = new Bundle();
            Parsing.putString("password", pass);
            intent.putExtras(Parsing);
            startActivity(intent);

            overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
        }
    }
}
