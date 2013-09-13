package com.openshift.android.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.OpenshiftConstants;
import com.openshift.android.R;
import com.openshift.android.adapter.NewCartridgeAdapter;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftMessage;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestError;
import com.openshift.android.rest.OpenshiftRestManager;

public class CartridgeActivity extends Activity {

	private EditText aliasName;
	private Button addCartridgeButton;
	
	private ApplicationResource applicationResource;
	private ProgressDialog progressDialog;
	
	private NewCartridgeAdapter cartridgeAdapter;
	private List<CartridgeResource> cartridgeList = new ArrayList<CartridgeResource>();
	private Spinner cartridgeTypeSpinner;

	
	public static final String APPLICATION_RESOURCE = "com.openshift.android.APPLICATION_RESOURCE";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_cartridge);

	    Intent intent = getIntent();
	    final ApplicationResource applicationResource = (ApplicationResource) intent.getSerializableExtra(CartridgeActivity.APPLICATION_RESOURCE);
	    this.applicationResource = applicationResource;
	    
	    addCartridgeButton = (Button) findViewById(R.id.cartridge_button_add); 
	    
	    cartridgeTypeSpinner = (Spinner) findViewById(R.id.new_cartrige_spinner);
	    
	    
	    cartridgeAdapter = new NewCartridgeAdapter(this, R.layout.simple_textview_layout,cartridgeList);
	    cartridgeTypeSpinner.setAdapter(cartridgeAdapter);
	    
	    OpenshiftRestManager.getInstance().getAvailableCartridges(new Response.Listener<OpenshiftResponse<OpenshiftDataList<CartridgeResource>>>() {

			@Override
			public void onResponse(
					OpenshiftResponse<OpenshiftDataList<CartridgeResource>> response) {
				
				List<CartridgeResource> cartridgeResources = response.getData().getList();
				
				Iterator<CartridgeResource> cartridgeResourceIterator  = cartridgeResources.iterator();
				
				while(cartridgeResourceIterator.hasNext()) {
					
					CartridgeResource cartridgeResource = cartridgeResourceIterator.next();
					
					if(!"embedded".equals(cartridgeResource.getType().trim())) {
						
						cartridgeResourceIterator.remove();
						continue;
					}
					
					if(applicationResource.getCartridges() != null) {
						for(CartridgeResource appCartridgeResource : applicationResource.getCartridges()) {
							if(cartridgeResource.getName().equals(appCartridgeResource.getName())) {
								cartridgeResourceIterator.remove();
								continue;
							}
						}
					}
					
					
				}
				
				displayList(cartridgeResources);
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		}, OpenshiftConstants.CARTRIDGEACTIVITY_TAG);
	    
	    
	        
	}
	
	
	 	
	public void onAddButtonClick(View v) {
		
		addCartridgeButton.setEnabled(false);
		
		CartridgeResource selectedCartridge = (CartridgeResource) cartridgeTypeSpinner.getSelectedItem();
		
		if(selectedCartridge != null) {
		
			progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
			progressDialog.setTitle("Adding Cartridge");
			progressDialog.setMessage("Adding "+selectedCartridge.getDisplayName());
			progressDialog.setIndeterminate(true);
			progressDialog.show();
	
			OpenshiftRestManager.getInstance().addCartridge(applicationResource, selectedCartridge.getName(), new Response.Listener<OpenshiftResponse<CartridgeResource>>() {

				@Override
				public void onResponse(
						OpenshiftResponse<CartridgeResource> response) {
					
					progressDialog.dismiss();
					
					AlertDialog dialog = new AlertDialog.Builder(CartridgeActivity.this).setTitle("Cartridge Created").setMessage("Cartridge Added Successfully").setNeutralButton("OK", null).create();
					dialog.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss(DialogInterface dialog) {
							finish();
							
						}
					});
					dialog.show();

					
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					
					addCartridgeButton.setEnabled(true);
					progressDialog.dismiss();
					
					if(error instanceof OpenshiftRestError) {
						OpenshiftRestError e = (OpenshiftRestError) error;
						OpenshiftResponse<CartridgeResource> errObj = (OpenshiftResponse<CartridgeResource>) e.getObject();
						
						StringBuilder sb = new StringBuilder();
						if(errObj.getMessages() != null) {
							for(OpenshiftMessage message : errObj.getMessages()) {
								sb.append(message.getText());
							}
						}
						new AlertDialog.Builder(CartridgeActivity.this).setTitle("Cartridge Addition Failure").setMessage(sb.toString()).setNeutralButton("Close", null).create().show();
					}
					else {

						new AlertDialog.Builder(CartridgeActivity.this).setTitle("Cartridge Addition Failure").setMessage("Failed to Add Cartridge").setNeutralButton("Close", null).create().show();
					}
					
				}
			}, OpenshiftConstants.CARTRIDGEACTIVITY_TAG);
		}
			
		
		
	}
	
	public void onResetButtonClick(View v) {
		
		finish();
		
	}
	
    @Override
    public void onStop() {
    	
    	super.onStop();
    	
		OpenshiftAndroidApplication.getInstance().getRequestQueue().cancelAll(OpenshiftConstants.CARTRIDGEACTIVITY_TAG);

    }
    
	/**
	 * Binds the Cartridge Response Data to the List
	 * 
	 * @param response The response data to fill the adapter
	 */
	private void displayList(List<CartridgeResource> cartridgeResources) {
		
		
		// Sort the List
		Collections.sort(cartridgeResources, new Comparator<CartridgeResource>() {

			@Override
			public int compare(CartridgeResource resource1, CartridgeResource resource2) {
				return resource1.getDisplayName().toLowerCase().compareTo(resource2.getName().toLowerCase());
			}

		
		});
		
		cartridgeAdapter.clear();
		
		for(int i = 0; i<cartridgeResources.size();i++){ 
			CartridgeResource cartridgeResource = cartridgeResources.get(i);
			
			cartridgeAdapter.insert(cartridgeResource, i);
		}
		
		cartridgeAdapter.setNotifyOnChange(true);					
		cartridgeAdapter.notifyDataSetChanged();
	}
    
	
	
	

}
