package com.pusatict.getvet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

/**
 * Created by bronky on 05/09/2015.
 */
public class TeamOfService extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_view);
        webView = (WebView) findViewById(R.id.webView2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://mygetvet.com/terms-of-service/");
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
        super.onBackPressed();

        overridePendingTransition(R.animator.act_back_in, R.animator.act_back_out);
    }
}
