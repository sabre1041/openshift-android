package com.openshift.android.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.openshift.android.model.OpenshiftResource;
import com.openshift.android.processor.Processor;
import com.openshift.android.rest.RestRequest;

public class OpenshiftService extends IntentService {
	
	public static final String METHOD_EXTRA = "com.redhat.openshift.service.METHOD_EXTRA";
	
	public static final String SERVICE_CALLBACK = "com.redhat.openshift.service.SERVICE_CALLBACK";

	public static final String ORIGINAL_INTENT_EXTRA = "com.redhat.openshift.service.ORIGINAL_INTENT_EXTRA";

	public static final String RESOURCE_TYPE_EXTRA = "com.redhat.openshift.service.RESOURCE_TYPE_EXTRA";
	
	public static final String RESOURCE_RESPONSE_EXTRA = "com.redhat.openshift.service.RESOURCE_RESPONSE_EXTRA";
	
	public static final String RESOURCE_REQUEST_EXTRA = "com.redhat.openshift.service.RESOURCE_REQUEST_EXTRA";
	
	private static final int REQUEST_INVALID = -1;

	private Intent mOriginalRequestIntent;
	
	private RestRequest<? extends OpenshiftResource> request;
	
	private ResultReceiver mCallback;
	
	public static final int RESOURCE_TYPE_USER = 1;

	public static final int RESOURCE_TYPE_DOMAIN = 2;
	
	public static final int RESOURCE_TYPE_APPLICATION = 3;
	
	public static final int RESOURCE_TYPE_CARTRIDGE = 4;
	
	
	
	public OpenshiftService() {
		super("OpenshiftService");
	}

	@Override
	protected void onHandleIntent(Intent requestIntent) {
		
		mOriginalRequestIntent = requestIntent;

		// Get request data from Intent
		mCallback = requestIntent.getParcelableExtra(OpenshiftService.SERVICE_CALLBACK);
		request = (RestRequest<? extends OpenshiftResource>) requestIntent.getSerializableExtra(OpenshiftService.RESOURCE_REQUEST_EXTRA);

		Processor processor = new Processor(getApplicationContext());
		
		OpenshiftCallback openshiftCallback = new OpenshiftCallback() {

			@Override
			public void send(int statusCode,
					RestRequest<? extends OpenshiftResource> response) {
			Bundle bundle = getOriginalIntentBundle();
			bundle.putSerializable(OpenshiftService.RESOURCE_RESPONSE_EXTRA, response);
			mCallback.send(response.getStatus(), bundle);
				
			}
		};
		
		processor.setCallback(openshiftCallback);
		processor.execute(request);
		

//		switch (resourceType) {
//		case RESOURCE_TYPE_USER:
//
//			if (method.equalsIgnoreCase(RestMethod.GET.toString())) {
//				UserProcessor processor = new UserProcessor(getApplicationContext());
////				processor.getUserInformation(new OpenshiftCallback<UserResource>() {
////					
////					@Override
////					public void send(int status, RestRequest<UserResource> response) {
////						Bundle bundle = getOriginalIntentBundle();
////						bundle.putSerializable(OpenshiftService.RESOURCE_RESPONSE_EXTRA, response);
////						mCallback.send(response.getStatus(), bundle);
////						
////					}
////				});
//			} else {				
//				mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
//			}
//			break;
//		case RESOURCE_TYPE_DOMAIN:
//			if (method.equalsIgnoreCase(RestMethod.GET.toString())) {
//				DomainProcessor processor = new DomainProcessor(getApplicationContext());
////				processor.getDomains(new OpenshiftCallback<OpenshiftDataList<DomainResource>>() {
////					
////					@Override
////					public void send(int status, Response<OpenshiftDataList<DomainResource>> response) {
////						Bundle bundle = getOriginalIntentBundle();
////						bundle.putSerializable(OpenshiftService.RESOURCE_RESPONSE_EXTRA, response);
////						mCallback.send(response.getStatus(), bundle);
////						
////					}
////				});
//			} else {				
//				mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
//			}			
//			break;
//			
//		default:
//			mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
//			break;
//		}

		
	}
	
	protected Bundle getOriginalIntentBundle() {
		Bundle originalRequest = new Bundle();
		originalRequest.putParcelable(ORIGINAL_INTENT_EXTRA, mOriginalRequestIntent);
		return originalRequest;
	}


}
