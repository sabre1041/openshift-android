package com.openshift.android.util;

import android.content.Context;

public class ImageUtils {
	
	private static final String PREFIX = "openshift_";
	private static final String VERSION_SPLIT = "-";
	private static final String LOGO = "_logo";
	private static final String DEFAULT_IMAGE = "openshift_logo";

	
	public static int getImageResourceId(Context context, String name) {
		
		String resourceName = PREFIX.concat(name.substring(0, name.indexOf(VERSION_SPLIT))).concat(LOGO);
		
		int resource =context.getResources().getIdentifier(resourceName,
                "drawable", context.getPackageName());
		
		if(resource == 0) {
			resource = context.getResources().getIdentifier(DEFAULT_IMAGE,
	                "drawable", context.getPackageName());
		}
		
        return resource; 

	}

}
