package com.example.flipnews;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FavouriteAdapter extends ArrayAdapter<String>
{
	Activity c;
	ArrayList<String> newsItemLink;
	
	public FavouriteAdapter(Activity context, ArrayList<String> newsItemLink) 
	{
		super(context, R.layout.fav_row, newsItemLink);
		this.c = context;
		this.newsItemLink = newsItemLink;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflator = c.getLayoutInflater();
		View row = inflator.inflate(R.layout.fav_row, null);
		
		TextView text = (TextView) row.findViewById(R.id.textView1);
		text.setText(newsItemLink.get(position).toString());
		
		return row;
	}

}
