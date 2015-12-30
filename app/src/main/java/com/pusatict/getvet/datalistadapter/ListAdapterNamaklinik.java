package com.pusatict.getvet.datalistadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pusatict.getvet.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterNamaklinik extends BaseAdapter implements Filterable{
	private Context context;
	private List<namaklinik> list, filterd;

	public ListAdapterNamaklinik(Context context, List<namaklinik> list) {
		this.context = context;
		this.list = list;
		this.filterd = this.list;
	}
	
	@Override
	public int getCount() {
		return filterd.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(this.context);
			convertView = inflater.inflate(R.layout.list_autocomplite, null);
		}
		namaklinik nama = filterd.get(position);
		TextView textNama = (TextView) convertView.findViewById(R.id.textauto);
		textNama.setText(nama.getKtnama());
		return convertView;
	}
	
	@Override
	public Filter getFilter() {
		namaklinikFilter filter = new namaklinikFilter();
		return filter;
	}
	
	/** Class filter untuk melakukan filter (pencarian) */
	private class namaklinikFilter extends Filter{
			
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			List<namaklinik> filteredData = new ArrayList<namaklinik>();
			FilterResults result = new FilterResults();
			String filterString = constraint.toString().toLowerCase();
			for(namaklinik mhs: list){
				if(mhs.getKtnama().toLowerCase().contains(filterString)){
					filteredData.add(mhs);
				}
			}
			result.count = filteredData.size();
			result.values =  filteredData;
			return result;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			filterd = (List<namaklinik>) results.values;
			notifyDataSetChanged();
		}

	}

}