package com.openshift.android.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.R;
import com.openshift.android.adapter.CartridgeAdapter;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftAndroidRequest;
import com.openshift.android.rest.OpenshiftRestManager;


public class ApplicationViewActivity extends Activity {
	private CartridgeAdapter cartridgeAdapter;
	
	private ApplicationResource applicationResource;
	private TextView appName;
	private TextView appFramework;
	private TextView appUrl;
	private ListView cartridgeListView;
	
	private List<CartridgeResource> cartridgeList = new ArrayList<CartridgeResource>();
	
//	private BroadcastReceiver requestReceiver;
//
//	private OpenshiftServiceHelper mOpenshiftServiceHelper;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.activity_application);
	    
	    Intent intent = getIntent();
	    ApplicationResource application = (ApplicationResource) intent.getSerializableExtra(ApplicationListActivity.APPLICATION_RESOURCE_EXTRA);
	    this.applicationResource = application;
	    
	    appName = (TextView) findViewById(R.id.appName);

	    appUrl = (TextView) findViewById(R.id.appUrl);


	    appName.setText(applicationResource.getName());

	    appUrl.setText(applicationResource.getApplicationUrl());

	    cartridgeListView = (ListView) findViewById(R.id.cartridgeList);

	    
	    cartridgeAdapter = new CartridgeAdapter(this, R.layout.cartridge_row_layout, cartridgeList);
	    cartridgeListView.setAdapter(cartridgeAdapter);
	    	    
		OpenshiftRestManager.getInstance().getApplicationWithCartridge(applicationResource.getDomainId(), applicationResource.getName(), new Response.Listener<OpenshiftResponse<ApplicationResource>>() {

					@Override
					public void onResponse(
							OpenshiftResponse<ApplicationResource> response) {
						updateList(response.getData().getCartridges());
												
						for(CartridgeResource cartridge : response.getData().getCartridges()) {

							OpenshiftRestManager.getInstance().getCartridge(applicationResource.getDomainId(), applicationResource.getName(), cartridge.getName(), new Response.Listener<OpenshiftResponse<CartridgeResource>>() {

										@Override
										public void onResponse(
												OpenshiftResponse<CartridgeResource> response) {
											updateList(response.getData());
											
										}
									}, new Response.ErrorListener() {

										@Override
										public void onErrorResponse(VolleyError error) {
											showToast("Cannot get Application List: "+error.getMessage());
										}
									});	
						}

						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("Cannot get Application: "+error.getMessage());
					}
				});
	    
//	    OpenshiftAndroidApplication.getInstance().getRequestQueue().add(applicationWithCartridgeRequest);

	    
	
	}
	
	@Override
	public void onPause() {
//		if(requestReceiver != null) {
//			this.unregisterReceiver(requestReceiver);
//		}
		
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
				
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(OpenshiftActions.APPLICATION_DETAIL_WITH_CARTRIDGE);
//		filter.addAction(OpenshiftActions.CARTRIDGE_WITH_STATUS);
//		requestReceiver = new BroadcastReceiver() {
//
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				 
//				if(OpenshiftActions.APPLICATION_DETAIL_WITH_CARTRIDGE.equals(intent.getAction())) {
//					
//					RestRequest<OpenshiftResponse<ApplicationResource>> applicationRequest = (RestRequest<OpenshiftResponse<ApplicationResource>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
//					Log.v(ApplicationViewActivity.class.getPackage().getName(),"Retrieved Application Response Code: "+applicationRequest.getStatus());
//					OpenshiftResponse<ApplicationResource> response = (OpenshiftResponse<ApplicationResource>) applicationRequest.getResponse();
//					
//					if(applicationRequest.getStatus()==200){	
//		
//
//						if(response.getData().getCartridges() != null) {
//		
//							updateList(response.getData().getCartridges());
//							
//							for(CartridgeResource cartridge : response.getData().getCartridges()) {
//								OpenshiftResponse<CartridgeResource> cachedCartridgeResource = mOpenshiftServiceHelper.getCartridgeWithStatus(applicationResource, cartridge.getName());
//							}
//							
//						}
//						else {
//							Log.v(ApplicationViewActivity.class.getPackage().getName(),"Cartridge Data is NULL");
//						}
//	//					displayList(response);
//					}
//					else {
//						showToast("Failed to Retrieve Application Information: "+applicationRequest.getMessage());
//					}
//					
//					
//				}
//				else if(OpenshiftActions.CARTRIDGE_WITH_STATUS.equals(intent.getAction())) {
//					RestRequest<OpenshiftResponse<CartridgeResource>> cartridgeRequest = (RestRequest<OpenshiftResponse<CartridgeResource>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
//					Log.v(ApplicationViewActivity.class.getPackage().getName(),"Retrieved Cartridge Response Code: "+cartridgeRequest.getStatus());
//					OpenshiftResponse<CartridgeResource> response = (OpenshiftResponse<CartridgeResource>) cartridgeRequest.getResponse();
//					
//					if(cartridgeRequest.getStatus()==200){	
//						updateList(response.getData());
//					}
//				}
//			}
//			
//		};
//		
//		mOpenshiftServiceHelper = OpenshiftServiceHelper.getInstance(this);
//		this.registerReceiver(requestReceiver, filter);
//		
//		OpenshiftResponse<ApplicationResource> cachedApplicationResource = mOpenshiftServiceHelper.getApplicationInformationWithCartridge(applicationResource);

		
	}
	
	/**
	 * Binds the Application Response Data to the List
	 * 
	 * @param response The response data to fill the adapter
	 */
	private void updateList(List<CartridgeResource> cartridgeResources) {
		cartridgeAdapter.clear();
		
		
		for(int i = 0; i<cartridgeResources.size();i++){
			Log.v(ApplicationViewActivity.class.getPackage().getName(),"Cartridge Name: "+cartridgeResources.get(i).getName());
			cartridgeAdapter.insert(cartridgeResources.get(i), i);
		}
		
		cartridgeAdapter.setNotifyOnChange(true);					
		cartridgeAdapter.notifyDataSetChanged();
	}
	
	private void updateList(CartridgeResource cartridgeResource) {
		for(int i = 0; i<cartridgeList.size();i++){
			if(cartridgeList.get(i).getName().equals(cartridgeResource.getName())) {
				cartridgeAdapter.remove(cartridgeList.get(i));
				cartridgeAdapter.insert(cartridgeResource, i);
				cartridgeAdapter.setNotifyOnChange(true);					
				cartridgeAdapter.notifyDataSetChanged();
				return;
			}
		}
		
	}



}
