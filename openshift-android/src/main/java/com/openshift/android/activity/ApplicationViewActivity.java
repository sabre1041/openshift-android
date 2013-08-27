package com.openshift.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.openshift.android.R;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.processor.OpenshiftActions;
import com.openshift.android.rest.RestRequest;
import com.openshift.android.service.OpenshiftServiceHelper;


public class ApplicationViewActivity extends Activity {

	private ApplicationResource applicationResource;
	private TextView appName;
	private TextView appFramework;
	private TextView appUrl;
	
	private BroadcastReceiver requestReceiver;

	private OpenshiftServiceHelper mOpenshiftServiceHelper;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.activity_application);
	    
	    Intent intent = getIntent();
	    ApplicationResource application = (ApplicationResource) intent.getSerializableExtra(ApplicationListActivity.APPLICATION_RESOURCE_EXTRA);
	    this.applicationResource = application;
	    
	    appName = (TextView) findViewById(R.id.appName);
	    appFramework = (TextView) findViewById(R.id.appFramework);
	    appUrl = (TextView) findViewById(R.id.appUrl);


	    appName.setText(applicationResource.getName());
	    appFramework.setText(applicationResource.getFramework());
	    appUrl.setText(applicationResource.getApplicationUrl());

	
	}
	
	@Override
	public void onPause() {
		if(requestReceiver != null) {
			this.unregisterReceiver(requestReceiver);
		}
		
		super.onPause();
	}
	
	/**
	 * Shows a short toast display
	 * 
	 * @param message message to display
	 */
	private void showToast(String message) {
		if(!isFinishing()){
			Toast toast = Toast.makeText(this,message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}

	}
	
	@Override
	public void onResume() {
		
		super.onResume();
				
		IntentFilter filter = new IntentFilter(OpenshiftActions.APPLICATION_DETAIL_WITH_CARTRIDGE);
		requestReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				 
				RestRequest<OpenshiftResponse<ApplicationResource>> applicationRequest = (RestRequest<OpenshiftResponse<ApplicationResource>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
				Log.v(ApplicationListActivity.class.getPackage().getName(),"Retrieved Application Response Code: "+applicationRequest.getStatus());
				OpenshiftResponse<ApplicationResource> response = (OpenshiftResponse<ApplicationResource>) applicationRequest.getResponse();
				
				if(applicationRequest.getStatus()==200){	
					
					if(response.getData().getCartridges() != null) {
						for(CartridgeResource cartridge : response.getData().getCartridges()) {
							new AlertDialog.Builder(ApplicationViewActivity.this).setTitle("Cartridge Info").setMessage("Cartridge Name: "+cartridge.getName()).setNeutralButton("Close", null).show();	
						}
						
					}
					else {
						Log.v(ApplicationListActivity.class.getPackage().getName(),"Cartridge Data is NULL");
					}
//					displayList(response);
				}
				else {
					showToast("Failed to Retrieve Application Information: "+applicationRequest.getMessage());
				}
				
				
			}
			
		};
		
		mOpenshiftServiceHelper = OpenshiftServiceHelper.getInstance(this);
		this.registerReceiver(requestReceiver, filter);
		
		OpenshiftResponse<ApplicationResource> cachedApplicationResource = mOpenshiftServiceHelper.getApplicationInformationWithCartridge(applicationResource);

		
	}


}
