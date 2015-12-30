package com.pusatict.getvet.sponsor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.pusatict.getvet.MainActivity;
import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.ListAdapteriklan2;
import com.pusatict.getvet.datalistadapter.iklan;
import com.pusatict.getvet.klinik.VetDetails;
import com.pusatict.getvet.tool.DBAdapter;
import com.pusatict.getvet.tool.JSONParser;
import com.pusatict.getvet.tool.tesKoneksiInet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ICT on 31/08/2015.
 */
public class SponsorList extends Activity {
    private ListView listView;
    private ListAdapteriklan2 adapter;
    private List<iklan> list;
    private iklan selectedList;
    JSONParser jsonParser = new JSONParser();

    private ListView lvVetList;
    private ImageButton imgBack, imgHome, imgAdd;
    private ProgressDialog pDialog;
    private tesKoneksiInet koneksiInet;
    private Toast toast;
    DBAdapter db=new DBAdapter(SponsorList.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sponsor_list);
        koneksiInet = new tesKoneksiInet(getApplicationContext());

        lvVetList = (ListView)findViewById(R.id.lvVetList);
        list= new ArrayList<iklan>();
        imgBack = (ImageButton)findViewById(R.id.imgBack);
        imgHome=(ImageButton)findViewById(R.id.imgHome);

        db.open();
        list = db.getAllToDos1();
        adapter= new ListAdapteriklan2(getApplicationContext(),list);
        lvVetList.setAdapter(adapter);
        db.close();
        isiBtnClick();

        fillListView();
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
                Intent intent = new Intent(SponsorList.this, MainActivity.class);
                startActivity(intent);

                overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
            }
        });

    }

    public void fillListView(){
        lvVetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList = (iklan) lvVetList.getAdapter().getItem(position);
                Intent intent = new Intent(SponsorList.this, iklanView.class);
                Bundle Parsing = new Bundle();
                Parsing.putString("html", selectedList.getHtml());
                intent.putExtras(Parsing);
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

}
