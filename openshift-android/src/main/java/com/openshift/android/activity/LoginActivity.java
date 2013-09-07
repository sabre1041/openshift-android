package com.openshift.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.R;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.model.UserResource;
import com.openshift.android.rest.OpenshiftRestManager;
import com.openshift.android.security.AuthorizationManager;
import com.openshift.android.util.ActivityUtils;

/**
 * Primary entry point to the application. Allows the user to use their OpenShift 
 * account credentials to access OpenShift resources
 * 
 * @author Andrew Block
 * 
 * @see Activity
 *
 */
public class LoginActivity extends Activity {
	
	private EditText openshiftAccount;
	
	private EditText openshiftPassword;
	
	private Button loginButton;
		
	private static final String OPENSHIFT_DEFAULT_URL = "https://openshift.redhat.com/broker/rest/";
	
	
	private ProgressDialog progressDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    	    
	    
	    if(OpenshiftAndroidApplication.getInstance().getAuthorizationManger().checkAuthentication()) {
			Intent intent = new Intent(this, DomainActivity.class);
			startActivity(intent);
	    	this.finish();
		    return;
	    }
	    

	    setContentView(R.layout.activity_login);
	    
	    openshiftAccount = (EditText) findViewById(R.id.openshiftAccount);
	    
	    openshiftPassword = (EditText) findViewById(R.id.openshiftPassword);
	    
	    loginButton = (Button) findViewById(R.id.loginButton);
	   
	    
	}
	
	/**
	 * Method invoked when the Login button is clicked
	 * 
	 * @param v the View
	 */
    public void loginClick(View v) {
    	
    	loginButton.setEnabled(false);
    	
    	if(openshiftAccount.getText().toString().length()==0 || openshiftPassword.getText().toString().trim().length()==0) {
    		ActivityUtils.showToast(getApplicationContext(), "Openshift Account and Password are Required");
    		loginButton.setEnabled(true);
    		return;
    	}
    	
    	// Do Authorization Check
    	final AuthorizationManager authenticationManager = OpenshiftAndroidApplication.getInstance().getAuthorizationManger();
    	authenticationManager.saveAuthentication(openshiftAccount.getText().toString(), openshiftPassword.getText().toString(), OPENSHIFT_DEFAULT_URL);
    	
		progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("Validating");
		progressDialog.setMessage("Validating with Openshift");
		progressDialog.setIndeterminate(true);
		progressDialog.show();

	    OpenshiftRestManager.getInstance().getUser(new Response.Listener<OpenshiftResponse<UserResource>>() {

					@Override
					public void onResponse(
							OpenshiftResponse<UserResource> response) {
						progressDialog.dismiss();
						Intent homeIntent = new Intent(getApplicationContext(), DomainActivity.class);
						startActivity(homeIntent);
						finish();
						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						progressDialog.dismiss();
						authenticationManager.invalidateAuthentication();
						new AlertDialog.Builder(LoginActivity.this).setTitle("Failure").setMessage("Could not validate credentials").setNeutralButton("Close", null).create().show();
						openshiftPassword.setText("");
						loginButton.setEnabled(true);
					}
				});

    	
    }

	public void onPause() {
		
		super.onPause();
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
				
	}


}
