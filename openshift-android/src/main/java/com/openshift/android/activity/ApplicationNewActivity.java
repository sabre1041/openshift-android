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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.OpenshiftConstants;
import com.openshift.android.R;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.CartridgeResource;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.NameValuePair;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftMessage;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestError;
import com.openshift.android.rest.OpenshiftRestManager;

public class ApplicationNewActivity extends Activity {

	private ArrayAdapter<NameValuePair> cartridgeAdapter;
	private List<NameValuePair> cartridgeTypeList = new ArrayList<NameValuePair>();
	private Spinner cartridgeTypeSpinner;
	private EditText appName;
	private Button createAppButton;
	
	private DomainResource domainResource;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_application_new);

	    Intent intent = getIntent();
	    DomainResource domain = (DomainResource) intent.getSerializableExtra(DomainActivity.DOMAIN_RESOURCE_EXTRA);
	    this.domainResource = domain;

	    
	    cartridgeTypeSpinner = (Spinner) findViewById(R.id.new_app_cartrige_spinner);
	    appName = (EditText) findViewById(R.id.new_app_name);
	    createAppButton = (Button) findViewById(R.id.new_app_button_create);
	    
	    
	    cartridgeAdapter = new ArrayAdapter<NameValuePair>(this, R.layout.simple_textview_layout,cartridgeTypeList);
	    cartridgeTypeSpinner.setAdapter(cartridgeAdapter);
	    
	    OpenshiftRestManager.getInstance().getAvailableCartridges(new Response.Listener<OpenshiftResponse<OpenshiftDataList<CartridgeResource>>>() {

			@Override
			public void onResponse(
					OpenshiftResponse<OpenshiftDataList<CartridgeResource>> response) {
				
				List<CartridgeResource> cartridgeResources = response.getData().getList();
				
				Iterator<CartridgeResource> cartridgeResourceIterator  = cartridgeResources.iterator();
				
				while(cartridgeResourceIterator.hasNext()) {
					
					CartridgeResource cartridgeResource = cartridgeResourceIterator.next();
					
					if(!"standalone".equals(cartridgeResource.getType().trim())) {
						
						cartridgeResourceIterator.remove();
					}
				}
				
				displayList(cartridgeResources);
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		}, OpenshiftConstants.APPLICATIONNEWACTIVITY_TAG);
	}
	
	
	/**
	 * Binds the Application Response Data to the List
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
			
			cartridgeAdapter.insert(new NameValuePair(cartridgeResource.getDisplayName(), cartridgeResource.getName()), i);
		}
		
		cartridgeAdapter.setNotifyOnChange(true);					
		cartridgeAdapter.notifyDataSetChanged();
	}
	
	public void onCreateButtonClick(View v) {
		
		createAppButton.setEnabled(false);
		
		NameValuePair selectedCartridge = (NameValuePair) cartridgeTypeSpinner.getSelectedItem();
		
		if(selectedCartridge != null) {
		
			progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
			progressDialog.setTitle("Creating Application");
			progressDialog.setMessage("Creating "+appName.getText().toString());
			progressDialog.setIndeterminate(true);
			progressDialog.show();
	
			OpenshiftRestManager.getInstance().createApplication(domainResource.getName(), appName.getText().toString(), selectedCartridge.getValue(), new Response.Listener<OpenshiftResponse<ApplicationResource>>() {

				@Override
				public void onResponse(
						OpenshiftResponse<ApplicationResource> response) {
					
					progressDialog.dismiss();
					
					AlertDialog dialog = new AlertDialog.Builder(ApplicationNewActivity.this).setTitle("Application Created").setMessage("Application Created Successfully").setNeutralButton("OK", null).create();
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
					
					createAppButton.setEnabled(true);
					progressDialog.dismiss();
					
					if(error instanceof OpenshiftRestError) {
						OpenshiftRestError e = (OpenshiftRestError) error;
						OpenshiftResponse<ApplicationResource> errObj = (OpenshiftResponse<ApplicationResource>) e.getObject();
						
						StringBuilder sb = new StringBuilder();
						if(errObj.getMessages() != null) {
							for(OpenshiftMessage message : errObj.getMessages()) {
								sb.append(message.getText());
							}
						}
						new AlertDialog.Builder(ApplicationNewActivity.this).setTitle("Application Creation Failure").setMessage(sb.toString()).setNeutralButton("Close", null).create().show();
					}
					else {

						new AlertDialog.Builder(ApplicationNewActivity.this).setTitle("Application Creation Failure").setMessage("Failed to Create Application").setNeutralButton("Close", null).create().show();
					}
					
				}
			}, OpenshiftConstants.APPLICATIONNEWACTIVITY_TAG);
			
			
			
		}
		
	}
	
	public void onResetButtonClick(View v) {
		
		finish();
		
	}
	
    @Override
    public void onStop() {
    	
    	super.onStop();
    	
		OpenshiftAndroidApplication.getInstance().getRequestQueue().cancelAll(OpenshiftConstants.APPLICATIONNEWACTIVITY_TAG);

    }

	
	

}
