package com.example.flipnews;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TrendActivity extends Activity 
{
	ArrayList<String> trendList;
	AssetDatabaseOpenHelper open;
	TrendAdapter adapter;
	TextView userName, favourites;
	
	ListView list;
	String newsTrendName;
	String displayName;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trend);
		list = (ListView) findViewById(android.R.id.list);
		userName = (TextView) findViewById(R.id.userName);
		favourites = (TextView) findViewById(R.id.favourites);
		
		//Get the intent;
		Intent getTrendIntent = getIntent();
		Bundle bundle = getTrendIntent.getExtras();
		trendList = (ArrayList<String>) bundle.getStringArrayList("Trend");
		
		//get the display name from the intent
		displayName = getTrendIntent.getStringExtra("userNameToDisplay");
		
		//set the userName to display name
		userName.setText(displayName);
		
		//set the adapter
		adapter = new TrendAdapter(this, trendList);
		
		//set the adapter to the list
		list.setAdapter(adapter);
		
		//set up on click listener
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				Intent newsListIntent = new Intent(TrendActivity.this, NewsList.class);
				Bundle bundle = new Bundle();
				newsTrendName = adapter.getItem(arg2);
				bundle.putString("newsTrendName", newsTrendName);
				bundle.putString("displayName", displayName);
				bundle.putStringArrayList("trendList", trendList);
				newsListIntent.putExtras(bundle);
				
				startActivity(newsListIntent);
				TrendActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			}
		});
		
		//setup favourite on click listener
		favourites.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent favouriteIntent = new Intent(TrendActivity.this, Favourites.class);
				favouriteIntent.putExtra("displayName", displayName);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("TrendList", trendList);				
				favouriteIntent.putExtras(bundle);
				
				startActivity(favouriteIntent);
				TrendActivity.this.overridePendingTransition(R.anim.fly_in_from_top_corner, R.anim.slide_out_left);
				
			}
		});
		
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
		TrendActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trend, menu);
		return true;
	}

}
