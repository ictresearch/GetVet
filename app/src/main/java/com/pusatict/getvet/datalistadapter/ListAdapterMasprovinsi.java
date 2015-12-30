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

public class ListAdapterMasprovinsi extends BaseAdapter implements Filterable{
	private Context context;
	private List<masprovinsi> list, filterd;

	public ListAdapterMasprovinsi(Context context, List<masprovinsi> list) {
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
		masprovinsi provinsi = filterd.get(position);
		TextView textNama = (TextView) convertView.findViewById(R.id.textauto);
		textNama.setText(provinsi.getKtnama());
		return convertView;
	}
	
	@Override
	public Filter getFilter() {
		masprovinsiFilter filter = new masprovinsiFilter();
		return filter;
	}
	
	/** Class filter untuk melakukan filter (pencarian) */
	private class masprovinsiFilter extends Filter{
			
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			List<masprovinsi> filteredData = new ArrayList<masprovinsi>();
			FilterResults result = new FilterResults();
			String filterString = constraint.toString().toLowerCase();
			for(masprovinsi mhs: list){
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
			filterd = (List<masprovinsi>) results.values;
			notifyDataSetChanged();
		}

	}

}