package com.openshift.android.activity;

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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.OpenshiftConstants;
import com.openshift.android.R;
import com.openshift.android.model.ApplicationAliasResource;
import com.openshift.android.model.OpenshiftMessage;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.rest.OpenshiftRestError;
import com.openshift.android.rest.OpenshiftRestManager;
import com.openshift.android.util.ActivityUtils;

public class AliasActivity extends Activity {

	private EditText aliasName;
	private Button modifyAliasButton;
	
	private ApplicationAliasResource applicationAliasResource;
	private ProgressDialog progressDialog;
	
	public static final String APPLICATION_ALIAS_RESOURCE = "com.openshift.android.APPLICATION_ALIAS_RESOURCE";
	
	private static final String CREATE_ALIAS_TEXT = "Create";
	private static final String MODIFY_ALIAS_TEXT = "Modify";
	
	private boolean newAlias = false;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_alias);

	    Intent intent = getIntent();
	    ApplicationAliasResource applicationAliasResource = (ApplicationAliasResource) intent.getSerializableExtra(AliasActivity.APPLICATION_ALIAS_RESOURCE);
	    this.applicationAliasResource = applicationAliasResource;

	    
	    aliasName = (EditText) findViewById(R.id.alias_name);
	    modifyAliasButton = (Button) findViewById(R.id.alias_button_modify);
	    
	    
	    // If the ID value is null, we have a 
	    if(applicationAliasResource.getId() == null) {
	    	modifyAliasButton.setText(CREATE_ALIAS_TEXT);
	    	this.newAlias = true;
		    setTitle("Create Alias");
	    }
	    else {
	    	modifyAliasButton.setText(MODIFY_ALIAS_TEXT);
	    	aliasName.setText(applicationAliasResource.getId());
	    	setTitle("Modify Alias");
	    	
	    }
	    
	}
	
	
	 	
	public void onModifyButtonClick(View v) {
		
		modifyAliasButton.setEnabled(false);
		
    	if(aliasName.getText().toString().length()==0) {
    		ActivityUtils.showToast(getApplicationContext(), "Alias Name Required");
    		modifyAliasButton.setEnabled(true);
    		return;
    	}		
		
		// Set Values from Form
		applicationAliasResource.setId(aliasName.getText().toString());
		
		progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		
		if(newAlias) {
			progressDialog.setTitle("Creating Alias");
			progressDialog.setMessage("Creating "+applicationAliasResource.getId());
			progressDialog.setIndeterminate(true);
			progressDialog.show();
			
			OpenshiftRestManager.getInstance().createApplicationAlias(applicationAliasResource, new Response.Listener<OpenshiftResponse<ApplicationAliasResource>>() {

				@Override
				public void onResponse(
						OpenshiftResponse<ApplicationAliasResource> response) {
					
					progressDialog.dismiss();
					
					AlertDialog dialog = new AlertDialog.Builder(AliasActivity.this).setTitle("Alias Created").setMessage("Alias Created Successfully").setNeutralButton("OK", null).create();
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
					
					modifyAliasButton.setEnabled(true);
					progressDialog.dismiss();
					
					if(error instanceof OpenshiftRestError) {
						OpenshiftRestError e = (OpenshiftRestError) error;
						OpenshiftResponse<ApplicationAliasResource> errObj = (OpenshiftResponse<ApplicationAliasResource>) e.getObject();
						
						StringBuilder sb = new StringBuilder();
						if(errObj.getMessages() != null) {
							for(OpenshiftMessage message : errObj.getMessages()) {
								sb.append(message.getText());
							}
						}
						new AlertDialog.Builder(AliasActivity.this).setTitle("Alias Creation Failure").setMessage(sb.toString()).setNeutralButton("Close", null).create().show();
					}
					else {

						new AlertDialog.Builder(AliasActivity.this).setTitle("Alias Creation Failure").setMessage("Failed to Create Alias").setNeutralButton("Close", null).create().show();
					}
					
				}
			}, OpenshiftConstants.ALIASACTIVITY_TAG);

			
			
		}
		else {
			//TODO: Handle Modifications to 
		}
		
	}
	
	public void onResetButtonClick(View v) {
		
		finish();
		
	}
	
    @Override
    public void onStop() {
    	
    	super.onStop();
    	
		OpenshiftAndroidApplication.getInstance().getRequestQueue().cancelAll(OpenshiftConstants.ALIASACTIVITY_TAG);

    }
	
	
	

}
