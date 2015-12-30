package com.pusatict.getvet.datalistadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pusatict.getvet.R;

import java.util.List;

public class ListAdapteriklan2 extends BaseAdapter{
	private Context context;
	private List<iklan> list, filterd;

	public ListAdapteriklan2(Context context, List<iklan> list) {
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
			convertView = inflater.inflate(R.layout.list_item, null);
		}
		iklan ikn = filterd.get(position);
		TextView textnama_dokter=(TextView)convertView.findViewById(R.id.nama_dokter);
		textnama_dokter.setText(ikn.getJudul());

//		TextView textalamat_dokter=(TextView)convertView.findViewById(R.id.alamat_dokter);
//		textalamat_dokter.setText(ikn.getHead());

		TextView textdetail_dokter=(TextView)convertView.findViewById(R.id.detail_dokter);
		textdetail_dokter.setText(ikn.getHead());

//		WebView webView=(WebView)convertView.findViewById(R.id.webView);
//		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//		webView.loadDataWithBaseURL("file:///sdcard/dir",ikn.getHtml(),"text/html","utf-8","");

//		TextView htmlTextView = (TextView)convertView.findViewById(R.id.html_text);
//		htmlTextView.setText(Html.fromHtml(ikn.getHtml()));
		return convertView;
	}
	
//	@Override
//	public Filter getFilter() {
//		maskotaFilter filter = new maskotaFilter();
//		return filter;
//	}
//
//	/** Class filter untuk melakukan filter (pencarian) */
//	private class maskotaFilter extends Filter{
//
//		@Override
//		protected FilterResults performFiltering(CharSequence constraint) {
//			List<maskota> filteredData = new ArrayList<maskota>();
//			FilterResults result = new FilterResults();
//			String filterString = constraint.toString().toLowerCase();
//			for(maskota mhs: list){
//				if(mhs.getKtnama().toLowerCase().contains(filterString)){
//					filteredData.add(mhs);
//				}
//			}
//			result.count = filteredData.size();
//			result.values =  filteredData;
//			return result;
//		}
//
//		@Override
//		protected void publishResults(CharSequence constraint, FilterResults results) {
//			filterd = (List<maskota>) results.values;
//			notifyDataSetChanged();
//		}

//	}

}