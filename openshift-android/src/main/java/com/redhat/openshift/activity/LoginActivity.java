package com.redhat.openshift.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.redhat.openshift.R;
import com.redhat.openshift.model.OpenshiftResponse;
import com.redhat.openshift.model.UserResource;
import com.redhat.openshift.processor.OpenshiftActions;
import com.redhat.openshift.rest.RestRequest;
import com.redhat.openshift.security.AuthorizationManager;
import com.redhat.openshift.service.OpenshiftServiceHelper;

public class LoginActivity extends Activity {
	
	private EditText openshiftAccount;
	
	private EditText openshiftPassword;
	
	private BroadcastReceiver requestReceiver;

	private OpenshiftServiceHelper mOpenshiftServiceHelper;
	
	private static final String OPENSHIFT_DEFAULT_URL = "https://openshift.redhat.com/broker/rest/";
	
	private static AuthorizationManager authenticationManager;
	
	private ProgressDialog progressDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    authenticationManager = AuthorizationManager.getInstance(this);
	    
	    if(authenticationManager.checkAuthentication()) {
			Intent intent = new Intent(this, DomainActivity.class);
			startActivity(intent);
	    	this.finish();
		    return;
	    }
	    

	    setContentView(R.layout.activity_login);
	    
	    openshiftAccount = (EditText) findViewById(R.id.openshiftAccount);
	    
	    openshiftPassword = (EditText) findViewById(R.id.openshiftPassword);
	   
	    
	}
	
    public void loginClick(View v) {
    	
    	if(openshiftAccount.getText().toString().length()==0 || openshiftPassword.getText().toString().trim().length()==0) {
    		Toast.makeText(getApplicationContext(), "Openshift Account and Password are Required", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	// Do Authorization Check
    	
    	authenticationManager.saveAuthentication(openshiftAccount.getText().toString(), openshiftPassword.getText().toString(), OPENSHIFT_DEFAULT_URL);
    	
		progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("Validating");
		progressDialog.setMessage("Validating with Openshift");
		progressDialog.show();

    	
    	mOpenshiftServiceHelper.getUserInformation();
    	
    }

	public void onPause() {
		if(requestReceiver != null) {
			this.unregisterReceiver(requestReceiver);
		}
		
		super.onPause();
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		
		IntentFilter filter = new IntentFilter(OpenshiftActions.USER_DETAIL);
		requestReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				 
				RestRequest<OpenshiftResponse<UserResource>> userRequest = (RestRequest<OpenshiftResponse<UserResource>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
				progressDialog.dismiss();
				if(userRequest.getStatus()==200){	
				
					Intent homeIntent = new Intent(getApplicationContext(), DomainActivity.class);
					startActivity(homeIntent);
					finish();

				}
				else {
					authenticationManager.invalidateAuthentication();
					new AlertDialog.Builder(LoginActivity.this).setTitle("Failure").setMessage("Could not validate credentials").setNeutralButton("Close", null).show();
					openshiftPassword.setText("");
				}
				
				
			}
			
		};
		
		mOpenshiftServiceHelper = OpenshiftServiceHelper.getInstance(this);
		this.registerReceiver(requestReceiver, filter);
		
		mOpenshiftServiceHelper.listDomains();

	}


}
