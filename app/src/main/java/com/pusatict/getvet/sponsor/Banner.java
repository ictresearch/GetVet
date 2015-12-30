package com.pusatict.getvet.sponsor;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.pusatict.getvet.MainActivity;
import com.pusatict.getvet.R;
import com.pusatict.getvet.tool.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bronky on 05/09/2015.
 */
public class Banner extends Activity {
    private WebView webView;
    private String tdid;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_view);
        Bundle Parsing=getIntent().getExtras();
        webView = (WebView) findViewById(R.id.webView2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Parsing.getString("banner"));
        if(Parsing.getString("broadcash").equals("1")){
            tdid=Parsing.getString("tdid");
            new updatetdopen().execute();
        }
        setOnClickbtn();
    }
    public void setOnClickbtn(){
        ImageButton imgBack=(ImageButton)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void onBackPressed() {
        Bundle Parsing=getIntent().getExtras();
        if(Parsing.getString("broadcash").equals("1")){
            Intent intent = new Intent(Banner.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            super.onBackPressed();
        }
        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
    private class updatetdopen extends AsyncTask<String, Void, String> {
        private String success3, judul, head, htmlbroad;
        private int idbroad;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "http://getsimplebisnis.com/index.php/api/UpdateTdopen";

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("tdid", tdid));

            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success3 = json.getString("succes");
            } catch (JSONException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
