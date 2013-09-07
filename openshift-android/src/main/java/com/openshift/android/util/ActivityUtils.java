package com.openshift.android.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ActivityUtils {
	
	/**
	 * Shows a short toast display
	 * 
	 * @param ctx application context
	 * @param message message to display
	 */
	public static void showToast(Context ctx, String message) {
		Toast toast = Toast.makeText(ctx,message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

}
