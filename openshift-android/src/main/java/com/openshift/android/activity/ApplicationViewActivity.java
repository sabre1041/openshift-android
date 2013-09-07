package com.openshift.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.R;
import com.openshift.android.adapter.CartridgeAdapter;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestManager;
import com.openshift.android.util.ActivityUtils;


public class ApplicationViewActivity extends Activity {
	private CartridgeAdapter cartridgeAdapter;
	
	private ApplicationResource applicationResource;
	private TextView appName;
	private TextView appUrl;
	private ListView cartridgeListView;
	
	private List<CartridgeResource> cartridgeList = new ArrayList<CartridgeResource>();
	
	
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
											ActivityUtils.showToast(getApplicationContext(), "Unable to Retrieve Application");
										}
									});	
						}

						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						ActivityUtils.showToast(getApplicationContext(), "Unable to Retrieve Application");
					}
				});
	    

	    
	
	}
	
	@Override
	public void onPause() {
		
		super.onPause();
	}
	

	
	@Override
	public void onResume() {
		
		super.onResume();
		
	}
	
	/**
	 * Binds the Application Response Data to the List
	 * 
	 * @param response The response data to fill the adapter
	 */
	private void updateList(List<CartridgeResource> cartridgeResources) {
		cartridgeAdapter.clear();
		
		
		for(int i = 0; i<cartridgeResources.size();i++){
			cartridgeAdapter.insert(cartridgeResources.get(i), i);
		}
		
		cartridgeAdapter.setNotifyOnChange(true);					
		cartridgeAdapter.notifyDataSetChanged();
	}
	
	private synchronized void updateList(CartridgeResource cartridgeResource) {
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
