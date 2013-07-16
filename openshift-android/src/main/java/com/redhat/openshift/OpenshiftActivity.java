package com.redhat.openshift;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.redhat.openshift.model.UserResource;
import com.redhat.openshift.processor.OpenshiftActions;
import com.redhat.openshift.rest.Response;
import com.redhat.openshift.service.OpenshiftServiceHelper;

public class OpenshiftActivity extends Activity {
		
	private BroadcastReceiver requestReceiver;

	private OpenshiftServiceHelper mOpenshiftServiceHelper;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_openshift);
	    	    
	    Button button = (Button) findViewById(R.id.button1);
	        
	    
		IntentFilter filter = new IntentFilter(OpenshiftActions.USER_DETAIL);
		requestReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				int resultCode = intent.getIntExtra(OpenshiftServiceHelper.EXTRA_RESULT_CODE, 0);
				
				Response<UserResource> userInformation = (Response<UserResource>) intent.getSerializableExtra(OpenshiftServiceHelper.EXTRA_RESULT_DATA);
				
				if(resultCode==200){
					new AlertDialog.Builder(OpenshiftActivity.this).setTitle("Message").setMessage("Login Name: "+userInformation.getResource().getLogin()).setNeutralButton("Close", null).show();	
				}
				else {
					showToast("Failed to Retrieve User Information: "+userInformation.getMessage());
				}
				
				
			}

		};
		
		mOpenshiftServiceHelper = OpenshiftServiceHelper.getInstance(this);
		this.registerReceiver(requestReceiver, filter);

	    
	    
	    button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
					
				mOpenshiftServiceHelper.getUserInformation();
			}
		});
	}
	
	private void showToast(String message) {
		if(!isFinishing()){
			Toast toast = Toast.makeText(this,message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}
	}

}
