package com.example.travel;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LoadPlaces extends AsyncTask<String, String, PlaceList> {

	public ProgressDialog pDialog;
	public Context mContext;
	public GooglePlaces googlePlaces;
	String types;
	double radius = 1000;
	public ListView list;
	public GPSTracker gps;
	public PlaceList nearPlaces;
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name 
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>(); 
	
	public LoadPlaces(Context context, GPSTracker gps, ListView mList, String placetype, double radius) {
		mContext = context;
		this.gps = gps;
		list = mList;
		types = placetype;
		this.radius = radius;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show(); 
		
	}
	@Override
	protected PlaceList doInBackground(String... params) {
		// TODO Auto-generated method stub
		googlePlaces = new GooglePlaces();
		try {
			nearPlaces = googlePlaces.search(gps.getLatitude(),
					gps.getLongitude(), radius, types);
			} catch (Exception e) {
				e.printStackTrace();
				}
		return nearPlaces;
		
	}
	
	
	@Override
	protected void onPostExecute(final PlaceList result) {
		// TODO Auto-generated method stub
		//super.onPostExecute(result);
		pDialog.dismiss();
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String status = result.status;
				if(status.equals("OK")) {
					if(result.results != null) {
						for(Place p:result.results) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(KEY_REFERENCE, p.reference);
							map.put(KEY_NAME, p.name);
							placesListItems.add(map);
						}
						ListAdapter adapter = new SimpleAdapter(mContext, placesListItems, R.layout.list_item, new String[] {KEY_REFERENCE,KEY_NAME }, new int[] {R.id.reference,R.id.name});
						list.setAdapter(adapter);
						list.setVisibility(View.VISIBLE);
						list.setOnItemClickListener(itemClicklistener);
					}
				}
				else if(status.equals("ZERO_RESULTS")) {
					Toast.makeText(mContext, "No place has found", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(mContext, "Error in finding.."+status, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private OnItemClickListener itemClicklistener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			TextView tv = (TextView) view.findViewById(R.id.reference);
			String reference = (String) tv.getText();
			Intent in = new Intent(mContext,SinglePlaceActivity.class);
			in.putExtra(LoadPlaces.KEY_REFERENCE, reference);
			mContext.startActivity(in);
			
		}
	};

}
