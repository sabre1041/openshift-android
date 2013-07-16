package com.redhat.openshift.service;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.redhat.openshift.model.DomainResource;
import com.redhat.openshift.model.OpenshiftDataList;
import com.redhat.openshift.model.UserResource;
import com.redhat.openshift.processor.DomainProcessor;
import com.redhat.openshift.processor.UserProcessor;
import com.redhat.openshift.rest.Response;
import com.redhat.openshift.rest.RestMethod;

public class OpenshiftService extends IntentService {
	
	public static final String METHOD_EXTRA = "com.redhat.openshift.service.METHOD_EXTRA";
	
	public static final String SERVICE_CALLBACK = "com.redhat.openshift.service.SERVICE_CALLBACK";

	public static final String ORIGINAL_INTENT_EXTRA = "com.redhat.openshift.service.ORIGINAL_INTENT_EXTRA";

	public static final String RESOURCE_TYPE_EXTRA = "com.redhat.openshift.service.RESOURCE_TYPE_EXTRA";
	
	public static final String RESOURCE_RESPONSE_EXTRA = "com.redhat.openshift.service.RESOURCE_RESPONSE_EXTRA";
	
	private static final int REQUEST_INVALID = -1;

	private Intent mOriginalRequestIntent;
	
	private ResultReceiver mCallback;
	
	public static final int RESOURCE_TYPE_USER = 1;

	public static final int RESOURCE_TYPE_DOMAIN = 2;
	
	
	public OpenshiftService() {
		super("OpenshiftService");
	}

	@Override
	protected void onHandleIntent(Intent requestIntent) {
		
		mOriginalRequestIntent = requestIntent;

		// Get request data from Intent
		String method = requestIntent.getStringExtra(OpenshiftService.METHOD_EXTRA);
		int resourceType = requestIntent.getIntExtra(OpenshiftService.RESOURCE_TYPE_EXTRA, -1);
		mCallback = requestIntent.getParcelableExtra(OpenshiftService.SERVICE_CALLBACK);

		switch (resourceType) {
		case RESOURCE_TYPE_USER:

			if (method.equalsIgnoreCase(RestMethod.GET.toString())) {
				UserProcessor processor = new UserProcessor(getApplicationContext());
				processor.getUserInformation(new OpenshiftCallback<UserResource>() {
					
					@Override
					public void send(Response<UserResource> response) {
						Bundle bundle = getOriginalIntentBundle();
						bundle.putSerializable(OpenshiftService.RESOURCE_RESPONSE_EXTRA, response);
						mCallback.send(response.getStatus(), bundle);
						
					}
				});
			} else {				
				mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
			}
			break;
		case RESOURCE_TYPE_DOMAIN:
			if (method.equalsIgnoreCase(RestMethod.GET.toString())) {
				DomainProcessor processor = new DomainProcessor(getApplicationContext());
				processor.getDomains(new OpenshiftCallback<OpenshiftDataList<DomainResource>>() {
					
					@Override
					public void send(Response<OpenshiftDataList<DomainResource>> response) {
						Bundle bundle = getOriginalIntentBundle();
						bundle.putSerializable(OpenshiftService.RESOURCE_RESPONSE_EXTRA, response);
						mCallback.send(response.getStatus(), bundle);
						
					}
				});
			} else {				
				mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
			}			
			break;
			
		default:
			mCallback.send(REQUEST_INVALID, getOriginalIntentBundle());
			break;
		}

		
	}
	
	protected Bundle getOriginalIntentBundle() {
		Bundle originalRequest = new Bundle();
		originalRequest.putParcelable(ORIGINAL_INTENT_EXTRA, mOriginalRequestIntent);
		return originalRequest;
	}


}
