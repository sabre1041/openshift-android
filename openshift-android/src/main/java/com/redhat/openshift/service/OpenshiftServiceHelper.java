package com.redhat.openshift.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.redhat.openshift.processor.OpenshiftActions;
import com.redhat.openshift.rest.RestMethod;

public class OpenshiftServiceHelper {
	private Context ctx;
	private static OpenshiftServiceHelper instance;
	
	public static String ACTION_REQUEST_RESULT = "REQUEST_RESULT";
	public static String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
	public static String EXTRA_RESULT_CODE = "EXTRA_RESULT_CODE";
	public static String EXTRA_RESULT_DATA = "EXTRA_RESULT_DATA";


	
	private OpenshiftServiceHelper(Context ctx) {
		this.ctx = ctx;
	}
	
	public static OpenshiftServiceHelper getInstance(Context ctx) {
		if(instance==null){
			instance = new OpenshiftServiceHelper(ctx);
		}
		return instance;
	}
	
	public void listDomains() {
		ResultReceiver serviceCallback = new ResultReceiver(null){

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				Intent origIntent = (Intent)resultData.getParcelable(OpenshiftService.ORIGINAL_INTENT_EXTRA);
				
				if(origIntent != null){


					Intent resultBroadcast = new Intent(OpenshiftActions.LIST_DOMAINS);
					resultBroadcast.putExtra(EXTRA_RESULT_CODE, resultCode);
					resultBroadcast.putExtra(EXTRA_RESULT_DATA, resultData.getSerializable(OpenshiftService.RESOURCE_RESPONSE_EXTRA));
					
					ctx.sendBroadcast(resultBroadcast);
				}
			}

		};

		Intent intent = new Intent(this.ctx, OpenshiftService.class);
		intent.putExtra(OpenshiftService.METHOD_EXTRA, RestMethod.GET.toString());
		intent.putExtra(OpenshiftService.RESOURCE_TYPE_EXTRA, OpenshiftService.RESOURCE_TYPE_DOMAIN);
		intent.putExtra(OpenshiftService.SERVICE_CALLBACK, serviceCallback);

		this.ctx.startService(intent);

	}
	
	public void getUserInformation() {
		
		ResultReceiver serviceCallback = new ResultReceiver(null){

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				Intent origIntent = (Intent)resultData.getParcelable(OpenshiftService.ORIGINAL_INTENT_EXTRA);
				
				if(origIntent != null){


					Intent resultBroadcast = new Intent(OpenshiftActions.USER_DETAIL);
					resultBroadcast.putExtra(EXTRA_RESULT_CODE, resultCode);
					resultBroadcast.putExtra(EXTRA_RESULT_DATA, resultData.getSerializable(OpenshiftService.RESOURCE_RESPONSE_EXTRA));
					
					ctx.sendBroadcast(resultBroadcast);
				}
			}

		};

		Intent intent = new Intent(this.ctx, OpenshiftService.class);
		intent.putExtra(OpenshiftService.METHOD_EXTRA, RestMethod.GET.toString());
		intent.putExtra(OpenshiftService.RESOURCE_TYPE_EXTRA, OpenshiftService.RESOURCE_TYPE_USER);
		intent.putExtra(OpenshiftService.SERVICE_CALLBACK, serviceCallback);

		this.ctx.startService(intent);

		
	}
	
}
