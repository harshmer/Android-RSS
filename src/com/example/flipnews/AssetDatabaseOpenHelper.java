package com.example.flipnews;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AssetDatabaseOpenHelper implements Serializable{

    private static final String DB_NAME = "newsDB.sqlite";

    private Context context;

    public AssetDatabaseOpenHelper(Context c) {
        this.context = c;
    }
    

    /*
     * checks if the database is in the phone memory and calls the copy database() accordingly
     */
    public SQLiteDatabase openDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME);
        File databaseFile = new File("/data/data/com.example.flipnews/databases");

        if (!dbFile.exists()) {
            try {
            	databaseFile.mkdir();
                copyDatabase(dbFile);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    
    /*
     * copy database to phone memory if doesn't exist on memory
     */
    private void copyDatabase(File dbFile) throws IOException {
        InputStream is = context.getAssets().open("newsDB.sqlite");
        OutputStream os = new FileOutputStream(dbFile.getAbsoluteFile(), false);

        byte[] buffer = new byte[1024];
        while (is.read(buffer) > 0) {
            os.write(buffer);
        }

        os.flush();
        os.close();
        is.close();
    }
    
    
    /*
     * get the news trends from database and set the newsTrends object in List<Trends>
     */
    public Trends newsTrends(SQLiteDatabase db)
    {
    	Trends trends = new Trends();
    	Cursor c = db.rawQuery("SELECT name FROM sqlite_master where type = 'table'", null);
    	if (c.moveToFirst()) {
    	    while ( !c.isAfterLast() ) 
    	    {
    	    		if(c.getString(0).equalsIgnoreCase("technology")){
    	    			trends.setTechnology(c.getString(0));
    	    		}
    	    		else if(c.getString(0).equalsIgnoreCase("business")){
    	    			trends.setBusiness(c.getString(0));
    	    		}
					
    	    		else if(c.getString(0).equalsIgnoreCase("sports")){
    	    			trends.setSports(c.getString(0));
    	    		}
					
    	    		else if(c.getString(0).equalsIgnoreCase("entertainment")){
    	    			trends.setEntertainment(c.getString(0));
    	    		}
					
    	    		else if(c.getString(0).equalsIgnoreCase("science")){
    	    			trends.setScience(c.getString(0));
    	    		}
    	    		
    	    		else if(c.getString(0).equalsIgnoreCase("world")){
    	    			trends.setWorld(c.getString(0));
    	    		}
					
    	    		else if(c.getString(0).equalsIgnoreCase("health")){
    	    			trends.setHealth(c.getString(0));
    	    		}
    	    		
    	    		c.moveToNext();

				}
    	    
    	}
    	
    	return trends;
    	
    }

    
    /*
     * function to send cursor object of particular trend table entries of newsItems
     */
    public Cursor newsTableItems(String tableName, SQLiteDatabase db){
    	
    	Cursor c = db.rawQuery("SELECT * FROM " + tableName, null);
		return c;
    	
    }
    
    
    /*
     * insert users favourites
     */
    public int insertUserPrefData(SQLiteDatabase db, String linkUrl){
    	
    	ContentValues values = new ContentValues();
    	values.put("newslink", linkUrl);
    	int rowCheck = (int) db.insert("user_pref", null, values);
		return rowCheck;
    	
    	
    	
    }

    /*
     * Get the users favourites
     */
    public Cursor getUserFavourites(SQLiteDatabase db){
    	Cursor c = db.rawQuery("SELECT * FROM user_pref", null);
    	return c;
    }
    
    
    /*
     * store user in the database
     */
    public int storeUser(SQLiteDatabase db, String userName, String emailId){
    	ContentValues values = new ContentValues();
    	values.put("name", userName);
    	values.put("email", emailId);
    	int rowCheck = (int) db.insert("user", null, values);
    	return rowCheck;
    	
    }

    
    /*
     * Check the user in the db
     */
    public String checkUser(SQLiteDatabase db){
    	
    	String name = null;
    	
    	Cursor c = db.rawQuery("SELECT name FROM user", null);
    	while(c.moveToNext()){
    		name = c.getString(0);
    	}
    	return name;
    }
}
