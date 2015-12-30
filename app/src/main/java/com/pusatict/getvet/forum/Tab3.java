package com.pusatict.getvet.forum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pusatict.getvet.R;
import com.pusatict.getvet.datalistadapter.Katforum;
import com.pusatict.getvet.datalistadapter.ListAdapterKatforum;
import com.pusatict.getvet.datalistadapter.ListAdapterThread;
import com.pusatict.getvet.datalistadapter.thread;
import com.pusatict.getvet.tool.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab3 extends Fragment implements CompoundButton.OnCheckedChangeListener,AdapterView.OnItemSelectedListener{
    private ListAdapterThread adapter;
    private ListAdapterKatforum adapter1;
    private ListView lvkatforum;
    private LinearLayout ll1, ll2;
    private ProgressDialog pDialog3;
    private List<thread> list;
    private List<Katforum> list1;
    private int offset;
    private int posisi=0;
    private String katid,kat="1";
    private JSONParser jsonParser= new JSONParser();
    private thread selectedList;
    private Katforum selectedList1;
    private ArrayAdapter<String> dataAdapter;
    List<String> categories;
    private boolean flag_loading=false;
    private Button btncari, btn1;
    private ImageButton imgDown;
    private CheckBox HKburung,HKkucing,HKanjing,HKReptile,HKfish,HKexotic,HKequine,HKother;
    private int gakadacheckHK=0;
    private String burung="0",kucing="0",anjing="0",Reptile="0",Fish="0",exotic="0",equine="0",other="0";
    private Toast toast;
    private Spinner spinner,spinner1;
    private int isUp=1;


    //    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_kat, container, false);
//        return v;
//    }
        //forum_kat
        //ll1=(LinearLayout)v.findViewById(R.id.LL1);
        ll2=(LinearLayout)v.findViewById(R.id.llbtn);

        //btncari=(Button)v.findViewById(R.id.btnSave);
//        lvkatforum=(ListView)v.findViewById(R.id.lvKatforum);
        spinner = (Spinner)v.findViewById(R.id.spinner);
//        spinner1 = (Spinner)v.findViewById(R.id.spinner1);
        //imgDown = (ImageButton)v.findViewById(R.id.imgdown);
        //imgDown.setImageResource(R.drawable.up_icon);
        list=new ArrayList<thread>();
        adapter = new ListAdapterThread(v.getContext(), list);
        spinner.setOnItemSelectedListener(this);
//        spinner1.setOnItemSelectedListener(this);
        list1=new ArrayList<Katforum>();
        categories = new ArrayList<String>();
        adapter1 = new ListAdapterKatforum(v.getContext(), list1);
//        new selectsubkat().execute("1");
//        Log.e("logpos",posisi+"");
//        if(posisi==0) {
        //forum_kat1
        new selectKategori().execute();
//        new selectKategori1().execute();
        Log.e("catk",categories.size()+""+posisi);


        //forum_kat

        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
//            spinner1.setAdapter(dataAdapter);
//        }
//        lvkatforum.setAdapter(adapter);
        //Load more
//        lvkatforum.setOnScrollListener(new AbsListView.OnScrollListener() {
//            int i = 0;
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
//                    if (flag_loading == false) {
//                        flag_loading = true;
//                        Log.e("roll", offset+"");
//                        new selectthread().execute();
//                    }
//                    i = i + 1;
//                    Log.e("roll", "septi" + i);
//
//                }
//            }
//        });
        //forum_kat
        /*HKburung=(CheckBox)v.findViewById(R.id.chbBurung);
        HKburung.setOnCheckedChangeListener(this);
        HKkucing=(CheckBox)v.findViewById(R.id.chbKucing);
        HKkucing.setOnCheckedChangeListener(this);
        HKanjing=(CheckBox)v.findViewById(R.id.chbAnjing);
        HKanjing.setOnCheckedChangeListener(this);
        HKReptile=(CheckBox)v.findViewById(R.id.chbReptile);
        HKReptile.setOnCheckedChangeListener(this);
        HKfish=(CheckBox)v.findViewById(R.id.chbfish);
        HKfish.setOnCheckedChangeListener(this);
        HKother=(CheckBox)v.findViewById(R.id.chbOther);
        HKother.setOnCheckedChangeListener(this);
        HKequine=(CheckBox)v.findViewById(R.id.chbEquine);
        HKequine.setOnCheckedChangeListener(this);
        HKexotic=(CheckBox)v.findViewById(R.id.chbExotic);
        HKexotic.setOnCheckedChangeListener(this);*/
        fillListView();
//        lvkatforum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                ll1.setVisibility(View.VISIBLE);
////                btncari.setVisibility(View.VISIBLE);
//
////                if (posisi == 1) {
////                    Log.e("posisi", posisi + "");
////                    selectedList1 = (Katforum) lvkatforum.getAdapter().getItem(position);
//////
//////                    list = new ArrayList<thread>();
//////                    adapter = new ListAdapterThread(getActivity(), list);
////                    katid = selectedList1.getId().toString();
////                Log.e("asik tumpak sitik jos",katid+"");
////                    lvkatforum.setAdapter(adapter);
////                } else {
//                    selectedList = (thread) lvkatforum.getAdapter().getItem(position);
//                    Intent intent = new Intent(getActivity(), ForumDetails.class);
//                    Bundle Parsing = new Bundle();
//                    Parsing.putString("thid", selectedList.getId().toString());
//                    Parsing.putString("judul", selectedList.getJudul().toString());
//                    Parsing.putString("isi", selectedList.getIsi().toString());
//                    Parsing.putString("uid", selectedList.getUid().toString());
//                    Parsing.putString("katid", selectedList.getKatid().toString());
//                    Parsing.putString("date", selectedList.getDate().toString());
//                    Parsing.putString("namauser", selectedList.getNamauser().toString());
//                    Parsing.putString("jumcomment", selectedList.getComment().toString());
//                    Parsing.putString("foto", selectedList.getFoto().toString());
//                    intent.putExtras(Parsing);
//                    startActivity(intent);
////
//                    getActivity().overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
////                }
//            }
//        });
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

        return v;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String item = parent.getItemAtPosition(position).toString();
        selectedList1 = (Katforum) adapter1.getItem(position);
//        selectedList = (Katforum) parent.getAdapter().getItem(position);
        String stv = selectedList1.getId().toString();
        katid= selectedList1.getId().toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + katid+","+stv, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    ////
    public void fillListView(){
        /*btncari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("gak da", gakadacheckHK + "");
                Intent intent = new Intent(getActivity(), Tab3_1.class);
                startActivity(intent);
//                if(gakadacheckHK>0) {
//                    isUp = 0;
//                    ll1.setVisibility(View.GONE);
//                    spinner.setVisibility(View.GONE);
//                    imgDown.setImageResource(R.drawable.down_icon);
//                    kat = "1";
//                    katlis();
//                    offset = 0;
//                    list=new ArrayList<thread>();
//                    adapter = new ListAdapterThread(v.getContext(), list);
//                    new selectthread().execute();
//                    lvkatforum.setAdapter(adapter);
//                }else{
//                    toast = Toast.makeText(getActivity().getApplicationContext(), " Please cek in kategori. ",
//                            Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
//                Log.e("Asda",kat+","+katid+"");
            }
        });
        imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUp == 1) {
                    isUp = 0;
                    ll1.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    imgDown.setImageResource(R.drawable.down_icon);
                } else {
                    isUp = 1;
                    ll1.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    imgDown.setImageResource(R.drawable.up_icon);
                }
            }
        });*/
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        /*switch (buttonView.getId()){
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
            case R.id.chbReptile:
                if(isChecked){
                    Reptile="1";
                    gakadacheckHK++;
                }else {
                    Reptile="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbOther:
                if(isChecked){
                    other="1";
                    gakadacheckHK++;
                }else {
                    other="0";
                    gakadacheckHK--;
                }
                break;
            case R.id.chbfish:
                if(isChecked){
                    Fish="1";
                    gakadacheckHK++;
                }else {
                    Fish="0";
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

        }*/
    }
    private void katlis(){
        /*if(burung.equals("1")){
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
        kat=kat.replace("1,","");*/

    }

    private class selectthread extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog3 = new ProgressDialog(getActivity());
            pDialog3.setMessage("3 ");
            pDialog3.setIndeterminate(true);
            pDialog3.setCancelable(false);
            pDialog3.show();
        }
        @Override
        protected String doInBackground(String... params) {
//                String url="http://10.0.2.2/klinikhewan/select_all.php";
//                String url="http://192.168.0.103/klinikhewan/selectkliniklimit";
            String url="http://getsimplebisnis.com/index.php/api/threadkat";
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("offset", offset+""));
            nvp.add(new BasicNameValuePair("katid", katid+""));
            nvp.add(new BasicNameValuePair("kategori", kat+""));
//
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

            adapter.notifyDataSetChanged();
            offset=offset+10;
            posisi=posisi+1;
        }
    }
    private class selectKategori extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog3= new ProgressDialog(getActivity());
            pDialog3.setMessage("Finding Data... ");
            pDialog3.setIndeterminate(true);
            pDialog3.setCancelable(false);
            pDialog3.show();
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
                    dok.setJumlah(obj.getString("jml"));
                    Log.e("tes", dok.getNama());
                    list1.add(dok);
                    categories.add(obj.getString("katNama"));
                    posisi++;
                }
            }catch (JSONException e) {

            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog3.dismiss();
            dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listspinner ,categories);
            adapter1 = new ListAdapterKatforum(getActivity().getApplicationContext(), list1);
            dataAdapter.setDropDownViewResource(R.layout.listdown);
            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
//            spinner1.setAdapter(dataAdapter);

            for (int i = 0; i < list1.size(); i++) {
                Button btn = new Button(getActivity());
                TextView tv = new TextView(getActivity());
                Katforum dok=new Katforum();
                dok=list1.get(i);
                btn.setId(dok.getId());
                final int id_ = btn.getId();
                final String Nma = dok.getNama();
                btn.setText(dok.getNama() + " (" + dok.getJumlah() + ")");
                btn.setBackgroundColor(Color.rgb(255, 255, 255));
                btn.setTextColor(Color.rgb(43, 0, 92));

                float scaleDp = getResources().getDisplayMetrics().density;
                int pixelDp = (int) (5 * scaleDp + 0.5f);
                btn.setPadding(pixelDp, 0, pixelDp, 0);

                btn.setGravity(Gravity.CENTER_VERTICAL);

                int pixelDp2 = (int) (70 * scaleDp + 0.5f);
                btn.setHeight(pixelDp2);

                ////////////////////////////
                tv.setBackgroundColor(Color.rgb(230, 76, 101));
                int pixelDp3 = (int) (1 * scaleDp + 0.5f);
                tv.setHeight(pixelDp3);

                ll2.addView(btn);
                ll2.addView(tv);
                btn1 = ((Button)getActivity().findViewById(id_));
                btn1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Tab3_1.class);
                        Bundle Parsing = new Bundle();
                        Parsing.putString("katid", id_+"");
                        Parsing.putString("katnama", Nma);
                        intent.putExtras(Parsing);
                        startActivity(intent);
                        /*Toast.makeText(view.getContext(),
                                "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                                .show();*/
                    }
                });
            }
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
}