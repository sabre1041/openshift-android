package com.openshift.android.activity;

import java.lang.reflect.Type;

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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.google.gson.reflect.TypeToken;
import com.openshift.android.OpenshiftAndroidApplication;
import com.openshift.android.R;
import com.openshift.android.cache.TwoStageCache;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.model.UserResource;
import com.openshift.android.processor.OpenshiftActions;
import com.openshift.android.rest.OpenshiftAndroidRequest;
import com.openshift.android.rest.RestRequest;
import com.openshift.android.security.AuthorizationManager;
import com.openshift.android.service.OpenshiftServiceHelper;

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
	
//	private BroadcastReceiver requestReceiver;
//
//	private OpenshiftServiceHelper mOpenshiftServiceHelper;
	
	private static final String OPENSHIFT_DEFAULT_URL = "https://openshift.redhat.com/broker/rest/";
	
	
	private ProgressDialog progressDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    // Initialize Cache
	    TwoStageCache.getInstance(this);
	    
	    
	    if(OpenshiftAndroidApplication.getInstance().getAuthorizationManger().checkAuthentication()) {
			Intent intent = new Intent(this, DomainActivity.class);
			startActivity(intent);
	    	this.finish();
		    return;
	    }
	    

	    setContentView(R.layout.activity_login);
	    
	    openshiftAccount = (EditText) findViewById(R.id.openshiftAccount);
	    
	    openshiftPassword = (EditText) findViewById(R.id.openshiftPassword);
	   
	    
	}
	
	/**
	 * Method invoked when the Login button is clicked
	 * 
	 * @param v the View
	 */
    public void loginClick(View v) {
    	
    	if(openshiftAccount.getText().toString().length()==0 || openshiftPassword.getText().toString().trim().length()==0) {
    		Toast.makeText(getApplicationContext(), "Openshift Account and Password are Required", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	// Do Authorization Check
    	final AuthorizationManager authenticationManager = OpenshiftAndroidApplication.getInstance().getAuthorizationManger();
    	authenticationManager.saveAuthentication(openshiftAccount.getText().toString(), openshiftPassword.getText().toString(), OPENSHIFT_DEFAULT_URL);
    	
		progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("Validating");
		progressDialog.setMessage("Validating with Openshift");
		progressDialog.show();
		
	    Type type = new TypeToken<OpenshiftResponse<UserResource>>() {}.getType();


		OpenshiftAndroidRequest<OpenshiftResponse<UserResource>> userRequest = new OpenshiftAndroidRequest<OpenshiftResponse<UserResource>>(Method.GET,
	    		OpenshiftAndroidApplication.getInstance().getAuthorizationManger().getOpenshiftUrl()+"user?nolinks=true", type, null,null,
	    		new Response.Listener<OpenshiftResponse<UserResource>>() {

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
					}
				});
	    
	    OpenshiftAndroidApplication.getInstance().getRequestQueue().add(userRequest);

    	
    }

	public void onPause() {
//		if(requestReceiver != null) {
//			this.unregisterReceiver(requestReceiver);
//		}
		
		super.onPause();
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		
//		IntentFilter filter = new IntentFilter(OpenshiftActions.USER_DETAIL);
//		requestReceiver = new BroadcastReceiver() {
//
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				 
//				RestRequest<OpenshiftResponse<UserResource>> userRequest = (RestRequest<OpenshiftResponse<UserResource>>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
//				progressDialog.dismiss();
//				if(userRequest.getStatus()==200){	
//				
//					Intent homeIntent = new Intent(getApplicationContext(), DomainActivity.class);
//					startActivity(homeIntent);
//					finish();
//
//				}
//				else {
//					authenticationManager.invalidateAuthentication();
//					new AlertDialog.Builder(LoginActivity.this).setTitle("Failure").setMessage("Could not validate credentials").setNeutralButton("Close", null).show();
//					openshiftPassword.setText("");
//				}
//				
//				
//			}
//			
//		};
//		
//		mOpenshiftServiceHelper = OpenshiftServiceHelper.getInstance(this);
//		this.registerReceiver(requestReceiver, filter);
		
	}


}
