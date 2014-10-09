package com.example.flipnews;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TrendAdapter extends ArrayAdapter<String> 
{
	ArrayList<String> trendList;
	Activity c;
	public TrendAdapter(Activity context, ArrayList<String> trendList) 
	{
		super(context, R.layout.trendrow, trendList);
		c= context;
		this.trendList = trendList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		LayoutInflater inflator = c.getLayoutInflater();
		View row = inflator.inflate(R.layout.trendrow, null);
		
		TextView text = (TextView) row.findViewById(R.id.textView1);
		text.setText(trendList.get(position).toString().toUpperCase());
		
		return row;
	}

	
}
