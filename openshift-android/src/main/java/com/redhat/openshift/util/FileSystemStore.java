package com.redhat.openshift.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.widget.Toast;


/**
 * A class that provides a filesystem based persistence store
 *
 * @see DataStore
 * 
 * @author Joey yore
 * @version 1.0
 */
public class FileSystemStore implements DataStore {

	private Context context = null;
	private String rootDir = "";
	
	/**
	 * Simple constructor
	 *
	 * @param context The android context to use
	 */
	public FileSystemStore(Context context) {
		this.context = context;
	}
	
	/**
	 * Namespace Constructor
	 *
	 * @param context The android context to use
	 * @param rootDir The root directory to use as a namespace
	 */
	public FileSystemStore(Context context, String rootDir) {
		this.context = context;
		this.rootDir = rootDir;
	}
	
	private void writeFile(String filename, String data, Context ctx) throws FileNotFoundException, IOException {
		FileOutputStream fos = ctx.openFileOutput(filename,Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(data.getBytes());
		oos.close();
		fos.close();
	}
	
	private String readFile(String filename, Context ctx) throws FileNotFoundException, IOException {
		FileInputStream fis = ctx.openFileInput(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		
		String line = null, input="";
		while((line = reader.readLine()) != null) {
			input += line;
		}
		reader.close();
		fis.close();
		return input;
	}
	
	@Override
	public void create(String key, Object o) {
		try {
			writeFile(rootDir + key, ObjectUtil.objectToString(o), context);
		} catch (FileNotFoundException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to create fs record", Toast.LENGTH_LONG);
			toast.show();
		} catch (IOException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to create fs record", Toast.LENGTH_LONG);
			toast.show();
		} 
	}

	@Override
	public Object get(String key) {
		Object o = null;
		
		try {
			o = ObjectUtil.stringToObject(readFile(rootDir + key, context));
		} catch (FileNotFoundException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to get fs record", Toast.LENGTH_LONG);
			toast.show();
		} catch (IOException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to get fs record", Toast.LENGTH_LONG);
			toast.show();
		} catch (ClassNotFoundException e) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to get fs record", Toast.LENGTH_LONG);
			toast.show();
		}
		
		return o;
	}

	@Override
	public int update(String key, Object o) {
		create(key,o);
		return 1;
	}

	@Override
	public void delete(String key) {
		File file = new File(context.getFilesDir()+rootDir+key);
		if(!file.delete()) {
			//TODO: How do we want to handle a fail here?
			Toast toast = Toast.makeText(context, "Failed to delete fs record", Toast.LENGTH_LONG);
			toast.show();
		}
	}


}
