package com.redhat.openshift.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.util.Base64;

/**
 * A class that provides object manipulation utilities
 */
public class ObjectUtil {

	/**
	 * Converts an object to a base64 encoded string
	 *
	 * @param o The object to convert
	 * @return The Base64 encoded string
	 * @throws IOException
	 */
	public static String objectToString(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
	}
	
	/**
	 * Converts a Base64 string into an object
	 *
	 * @param s The Base64 encoded string
	 * @return The converted object
	 * @throws StreamCorruptedException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object stringToObject(String s) throws StreamCorruptedException, IOException, ClassNotFoundException {
		byte[] data = Base64.decode(s, Base64.DEFAULT);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}
}
