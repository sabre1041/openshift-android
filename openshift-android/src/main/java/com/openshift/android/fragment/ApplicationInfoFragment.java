package com.openshift.android.fragment;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openshift.android.R;
import com.openshift.android.activity.ApplicationsActivity;
import com.openshift.android.model.ApplicationResource;

public class ApplicationInfoFragment extends Fragment {
	
	private static final String APPINFO_TAG ="ApplicationInfoFragment";
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	private OnApplicationUpdateListener onApplicationUpdateListener;
	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		    View view = inflater.inflate(R.layout.fragment_appinfo,
			        container, false);

		  
		Bundle args = getArguments();
		
		ApplicationResource applicationResource = (ApplicationResource) args.getSerializable(ApplicationsActivity.APPLICATION_RESOURCE_EXTRA);
				
		TextView appNameTv = (TextView) view.findViewById(R.id.appInfo_appName);
		appNameTv.setText(applicationResource.getName());
		
		TextView appFrameworkTv = (TextView) view.findViewById(R.id.appInfo_appFramework);
		appFrameworkTv.setText(applicationResource.getFramework());
		

		TextView appGearProfile = (TextView) view.findViewById(R.id.appInfo_gearProfile);
		appGearProfile.setText(applicationResource.getGearProfile());
		
		TextView appGearCount = (TextView) view.findViewById(R.id.appInfo_gearCount);
		appGearCount.setText(applicationResource.getGearCount());
		
		
		//TODO: Format Date
		TextView appDateCreated = (TextView) view.findViewById(R.id.appInfo_dateCreated);
		appDateCreated.setText(DATE_FORMAT.format(applicationResource.getCreationTime()));
		
	    return view;
	  }
	  
	  @Override
	  public void onAttach(Activity activity) {
		  super.onAttach(activity);
		  try {
			  onApplicationUpdateListener = (OnApplicationUpdateListener)activity;
		  } catch (ClassCastException e) {
			  throw new ClassCastException(activity.toString() +" must implement OnApplicationUpdateListener");
		  }
	  }	  

}
