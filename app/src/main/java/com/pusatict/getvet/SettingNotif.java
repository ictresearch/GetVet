package com.pusatict.getvet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.pusatict.getvet.tool.DBAdapter;

/**
 * Created by bronky on 02/11/2015.
 */
public class SettingNotif extends Activity implements CompoundButton.OnCheckedChangeListener{
    CheckBox CBThread,CBComThread;
    private ImageButton imgBack, imgHome;
    String thread,comthreade;
    final DBAdapter db = new DBAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_notif);
        CBThread=(CheckBox)findViewById(R.id.chbThread);
        CBThread.setOnCheckedChangeListener(this);
        CBComThread=(CheckBox)findViewById(R.id.chbComent);
        CBComThread.setOnCheckedChangeListener(this);
        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome = (ImageButton)findViewById(R.id.imgHome);
        DBAdapter db = new DBAdapter(SettingNotif.this);
        db.open();
        Cursor c=db.getContact(1);
        if (c.moveToFirst()) {
            thread=c.getString(12);
            comthreade=c.getString(13);
        }
        db.close();
        if(thread.equals("1")){
            CBThread.setChecked(true);
        }else{
            CBThread.setChecked(false);
        }
        if(comthreade.equals("1")){
            CBComThread.setChecked(true);
        }else{
            CBComThread.setChecked(false);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingNotif.this, MainActivity.class);
                startActivity(intent);
                finish();

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
        switch (buttonView.getId()) {
            case R.id.chbThread:
                if(isChecked){
                    thread="1";
                    db.open();
                    db.updatenotif(1,"1");
                    db.close();
                }else{
                    db.open();
                    db.updatenotif(1,"0");
                    db.close();
                    thread="0";
                }
                break;
            case R.id.chbComent:
                if(isChecked){
                    comthreade="1";
                    db.open();
                    db.updatenotifcom(1, "1");
                    db.close();
                }else{
                    db.open();
                    db.updatenotifcom(1,"0");
                    db.close();
                    comthreade="0";
                }
                break;
        }
    }
}
