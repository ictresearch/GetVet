package com.pusatict.getvet.datalistadapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.pusatict.getvet.R;
import com.pusatict.getvet.klinik.VetSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bronky on 11/08/2015.
 */

public class ListAdapterDokter extends BaseAdapter implements Filterable{
    private Context context;
    private List<dokter> list,filterd;
    private int index;
    private String cari1="",cari2="",cari3="";
    private Toast toast;

    public void setCari1(String cari1,String cari2,String cari3) {
        if(cari1.equals("")){
//            cari1="ict2015";
            cari1="";
        }
        if(cari2.equals("")){
//            cari2="ict2015";
            cari2="";
        }
        if(cari3.equals("")){
//            cari3="ict2015";
            cari3="";
        }
        this.cari1 = cari1;
        this.cari2 = cari2;
        this.cari3 = cari3;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ListAdapterDokter(Context context,List<dokter> list) {
        this.context=context;
        this.list=list;
        this.filterd=this.list;
    }


    public int getCount1() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return filterd.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return filterd.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(this.context);
            convertView=inflater.inflate(R.layout.list_item,null);
        }
        dokter dok =filterd.get(position);
        TextView textnama_dokter=(TextView)convertView.findViewById(R.id.nama_dokter);
        textnama_dokter.setText(dok.getNama()+" - "+dok.getNamaDok());

        TextView textalamat_dokter=(TextView)convertView.findViewById(R.id.alamat_dokter);
        textalamat_dokter.setText(dok.getAlamat()+", "+dok.getKota());

        TextView textdetail_dokter=(TextView)convertView.findViewById(R.id.detail_dokter);
        textdetail_dokter.setText(dok.getPelayanan());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        MahasiswaFilter filter = new MahasiswaFilter();
//        notifyDataSetChanged();
        Log.e("filter", index + "");
//        filter.setI(index);
        return filter;
    }


    /** Class filter untuk melakukan filter (pencarian) */
    private class MahasiswaFilter extends Filter {
        public int i;

        public int getI() {
            return i;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<dokter> filteredData = new ArrayList<dokter>();
//            filterd=filteredData;
            FilterResults result = new FilterResults();
            String filterString = constraint.toString().toLowerCase();
            for(dokter mhs: list){
                //ori
                /*String Nama=mhs.getNama()+""+mhs.getNamaDok();
                String Alamat=mhs.getAlamat()+""+mhs.getKota();
                String Layanan=mhs.getPelayanan();
                if(Nama.toLowerCase().contains(cari1)&&Alamat.toLowerCase().contains(cari2)&&Layanan.toLowerCase().contains(cari3)){
                    filteredData.add(mhs);
                }*/
                //gabung
                String Nama =mhs.getNama();
                String NamaDok = mhs.getNamaDok();
                String Alamat =mhs.getAlamat();
                String Kota =mhs.getKota();
                String Layanan =mhs.getPelayanan();
                if((Nama.toLowerCase().contains(cari1)||NamaDok.toLowerCase().contains(cari1))&&
                        (Alamat.toLowerCase().contains(cari2)||Kota.toLowerCase().contains(cari2))&&
                        (Layanan.toLowerCase().contains(cari3))){
                    filteredData.add(mhs);

                }
                //sercing or
//                if(Nama.toLowerCase().contains(cari1)||Alamat.toLowerCase().contains(cari2)||Layanan.toLowerCase().contains(cari3)){
//                    filteredData.add(mhs);
//                }

            }
            result.count = filteredData.size();
            result.values =  filteredData;

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterd = (List<dokter>) results.values;
//            VetSearch b=new VetSearch();
//            b.setFound(filterd.size(),b.tvfound);

//            b.findViewById(R.layout.vet_search);
//            b.tvfound.findViewById(R.id.tvfound);
//            b.tvfound.setText(filterd.size());
            toast = Toast.makeText(context, "About " + filterd.size() + " results",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//            Log.e("dafuk", filterd.size()+"");
            notifyDataSetChanged();
        }

    }


}
