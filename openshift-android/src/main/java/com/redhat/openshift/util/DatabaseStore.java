package com.redhat.openshift.util;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * This class allows for a database backed persistence store.
 *
 * @see SQLiteOpenHelper
 * @see DataStore
 *
 * @author Joey Yore
 * @version 1.0
 */
public class DatabaseStore extends SQLiteOpenHelper implements DataStore {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "objectstore";
	private static final String TABLE_NAME = "objects";
	private static final String KEY_KEY = "key";
	private static final String KEY_VALUE = "value";
	private String tableName;
	
	private Context context = null;
	
	/**
	 * Simple constructor
	 *
	 * @param context The android context to use
	 */
	public DatabaseStore(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		this.context = context;
		this.tableName = TABLE_NAME;
	}
	
	/**
	 * Namespace Constructor
	 *
	 * <p>Provides a constructor to initialize under a custom namespace 
	 * or context root
	 *
	 * @param context The android context to use
	 * @param contextRoot The name of the namespace to put entries under
	 */
	public DatabaseStore(Context context, String contextRoot) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		this.context = context;
		this.tableName = contextRoot + "_" + TABLE_NAME;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + tableName + "(" + KEY_KEY + " TEXT PRIMARY KEY,"
				     + KEY_VALUE + " TEXT)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + tableName);
		onCreate(db);
	}
	
	@Override
	public void create(String key, Object o) {
		SQLiteDatabase db = this.getWritableDatabase();
			
		try {
			ContentValues vals = new ContentValues();
			vals.put(KEY_KEY,key);
			vals.put(KEY_VALUE, ObjectUtil.objectToString(o));
			db.insert(tableName, null, vals);
		} catch (IOException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to create DB record", Toast.LENGTH_LONG);
			toast.show();
		}
			

		db.close();
	}

	@Override
	public Object get(String key) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(tableName,new String[] {KEY_KEY,KEY_VALUE},KEY_KEY + "=?",
				 new String[] {key},null,null,null,null);
		Object o = null;
		
		try {
			if(cursor != null) {
				cursor.moveToFirst();
				o = ObjectUtil.stringToObject(cursor.getString(1));
			}
		} catch (StreamCorruptedException e) {
			///TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to get DB record", Toast.LENGTH_LONG);
			toast.show();
		} catch (IOException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to get DB record", Toast.LENGTH_LONG);
			toast.show();
		} catch (ClassNotFoundException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to get DB record", Toast.LENGTH_LONG);
			toast.show();
		}
		
		cursor.close();
		db.close();
		
		return o;
	}
	
	/**
	 * Method that allows you to get all objects from the datastore
	 *
	 * @return A list of all objects that are in the store
	 */
	public List<Object> getAll() {
		List<Object> objList = new ArrayList<Object>();
		
		String sql = "SELECT * FROM " + tableName;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql,null);
		
		
		try {
			if(cursor.moveToFirst()) {
				do {
					objList.add(ObjectUtil.stringToObject(cursor.getString(1)));
				} while(cursor.moveToNext());
			}
		} catch (StreamCorruptedException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to get DB record", Toast.LENGTH_LONG);
			toast.show();
		} catch (IOException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to get DB record", Toast.LENGTH_LONG);
			toast.show();
		} catch (ClassNotFoundException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to get DB record", Toast.LENGTH_LONG);
			toast.show();
		}

		cursor.close();
		db.close();
		
		return objList;
	}

	@Override
	public int update(String key, Object o) {
		SQLiteDatabase db = this.getWritableDatabase();
		int ret = -1;
		
		try {
			ContentValues vals = new ContentValues();
			vals.put(KEY_KEY,key);
			vals.put(KEY_VALUE, ObjectUtil.objectToString(o));
			ret = db.update(tableName,vals,KEY_KEY + "=?", new String[] {key});
		} catch (IOException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to update DB record", Toast.LENGTH_LONG);
			toast.show();
		}
		
		db.close();
		return ret;
	}
	
	/**
	 * A method that will update an existing entry or create a new one
	 * if that entry does not yet exist
	 *
	 * @param key The key name to store the entry under
	 * @param o The payload of the entry
	 */
	public void updateOrCreate(String key,Object o) {
		Object obj = get(key);
		if(obj == null) {
			create(key,o);
		} else {
			update(key,o);
		}
	}

	@Override
	public void delete(String key) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(tableName,KEY_KEY + "=?",new String[] {key});
		db.close();
	}
}
