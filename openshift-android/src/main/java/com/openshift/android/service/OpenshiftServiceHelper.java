package com.openshift.android.service;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.google.gson.reflect.TypeToken;
import com.openshift.android.model.ApplicationResource;
import com.openshift.android.model.DomainResource;
import com.openshift.android.model.OpenshiftDataList;
import com.openshift.android.model.OpenshiftResource;
import com.openshift.android.model.OpenshiftResponse;
import com.openshift.android.model.UserResource;
import com.openshift.android.processor.OpenshiftActions;
import com.openshift.android.rest.RestMethod;
import com.openshift.android.rest.RestRequest;
import com.openshift.android.security.AuthorizationManager;

/**
 * Helper class to facilitate the invocation of the Openshift Service. Each type of Rest Based request
 * will have a method defined within this class
 * 
 * @author Andrew Block
 * 
 * @see OpenshiftService
 *
 */
public class OpenshiftServiceHelper {
	private Context ctx;
	private static OpenshiftServiceHelper instance;
	
	public static String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
	public static String EXTRA_RESULT_CODE = "EXTRA_RESULT_CODE";
	public static String EXTRA_RESULT_DATA = "EXTRA_RESULT_DATA";

	private OpenshiftServiceHelper(Context ctx) {
		this.ctx = ctx;
	}
	
	/**
	 * @param ctx The Android {@link Context}
	 * @return An instance of the {@link OpenshiftServiceHelper}
	 */
	public static OpenshiftServiceHelper getInstance(Context ctx) {
		if(instance==null){
			instance = new OpenshiftServiceHelper(ctx);
		}
		return instance;
	}
	
	/**
	 * Method used to invoke the Openshift Service to list all domains attributed to a user
	 * 
	 * @see DomainResource
	 */
	public void listDomains() {
		Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<DomainResource>>>() {}.getType();

		RestRequest<OpenshiftResponse<OpenshiftDataList<DomainResource>>> restRequest = new RestRequest<OpenshiftResponse<OpenshiftDataList<DomainResource>>>();
		restRequest.setMethod(RestMethod.GET);
		restRequest.setType(type);
		restRequest.setIntentActionName(OpenshiftActions.LIST_DOMAINS);
		restRequest.setUrl(AuthorizationManager.getInstance(ctx).getOpenshiftUrl()+"domains");
		initService(restRequest);
	}


	/**
	 * Method used to invoke the Openshift service to list all applications within the given domain name
	 * 
	 * @param domainName The domain name
	 * 
	 * @see ApplicationResource
	 */
	public void listApplications(String domainName) {
		Type type = new TypeToken<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>>() {}.getType();

		RestRequest<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>> restRequest = new RestRequest<OpenshiftResponse<OpenshiftDataList<ApplicationResource>>>();
		restRequest.setMethod(RestMethod.GET);
		restRequest.setType(type);
		restRequest.setIntentActionName(OpenshiftActions.LIST_APPLICATIONS);
		restRequest.setUrl(AuthorizationManager.getInstance(ctx).getOpenshiftUrl()+"domains/"+domainName+"/applications");
		initService(restRequest);
	}	
	
	/**
	 * Method used to invoke the Openshfit service to stop a given application within the given domain anme
	 * 
	 * @param domainName the name of the domain
	 * @param application the name of the application
	 * 
	 * @see ApplicationResource
	 */
	public void stopApplication(String domainName, String application) {
		Type type = new TypeToken<OpenshiftResponse<ApplicationResource>>() {}.getType();

		RestRequest<OpenshiftResponse<ApplicationResource>> restRequest = new RestRequest<OpenshiftResponse<ApplicationResource>>();
		restRequest.setMethod(RestMethod.POST);
		restRequest.getInputParameters().put("event", "stop");
		restRequest.setType(type);
		restRequest.setIntentActionName(OpenshiftActions.STOP_APPLICATION);
		restRequest.setUrl(AuthorizationManager.getInstance(ctx).getOpenshiftUrl()+"domains/"+domainName+"/applications/"+application+"/events");
		initService(restRequest);
	}	

	/**
	 * Method used to invoke the Openshfit service to start a given application within the given domain anme
	 * 
	 * @param domainName the name of the domain
	 * @param application the name of the application
	 * 
	 * @see ApplicationResource
	 */
	public void startApplication(String domainName, String application) {
		Type type = new TypeToken<OpenshiftResponse<ApplicationResource>>() {}.getType();

		RestRequest<OpenshiftResponse<ApplicationResource>> restRequest = new RestRequest<OpenshiftResponse<ApplicationResource>>();
		restRequest.setMethod(RestMethod.POST);
		restRequest.getInputParameters().put("event", "start");
		restRequest.setType(type);
		restRequest.setIntentActionName(OpenshiftActions.START_APPLICATION);
		restRequest.setUrl(AuthorizationManager.getInstance(ctx).getOpenshiftUrl()+"domains/"+domainName+"/applications/"+application+"/events");
		initService(restRequest);
	}
	
	/**
	 * Method used to invoke the Openshfit service to restart a given application within the given domain anme
	 * 
	 * @param domainName the name of the domain
	 * @param application the name of the application
	 * 
	 * @see ApplicationResource
	 */
	public void restartApplication(String domainName, String application) {
		Type type = new TypeToken<OpenshiftResponse<ApplicationResource>>() {}.getType();

		RestRequest<OpenshiftResponse<ApplicationResource>> restRequest = new RestRequest<OpenshiftResponse<ApplicationResource>>();
		restRequest.setMethod(RestMethod.POST);
		restRequest.getInputParameters().put("event", "restart");
		restRequest.setType(type);
		restRequest.setIntentActionName(OpenshiftActions.RESTART_APPLICATION);
		restRequest.setUrl(AuthorizationManager.getInstance(ctx).getOpenshiftUrl()+"domains/"+domainName+"/applications/"+application+"/events");
		initService(restRequest);
	}
	
	/**
	 * Method used to invoke the Openshfit service to delete a given application within the given domain anme
	 * 
	 * @param domainName the name of the domain
	 * @param application the name of the application
	 * 
	 * @see ApplicationResource
	 */
	public void deleteApplication(String domainName, String application) {

		RestRequest<OpenshiftResponse<ApplicationResource>> restRequest = new RestRequest<OpenshiftResponse<ApplicationResource>>();
		restRequest.setMethod(RestMethod.DELETE);
		restRequest.setIntentActionName(OpenshiftActions.DELETE_APPLICATION);
		restRequest.setUrl(AuthorizationManager.getInstance(ctx).getOpenshiftUrl()+"domains/"+domainName+"/applications/"+application);
		initService(restRequest);
	}

	
	/**
	 * Method used to invoke the Openshfit service to retrieve information for the current user
	 * 
	 * @see UserResource
	 * 
	 */
	public void getUserInformation() {

		Type type = new TypeToken<OpenshiftResponse<UserResource>>() {}.getType();

		RestRequest<OpenshiftResponse<UserResource>> restRequest = new RestRequest<OpenshiftResponse<UserResource>>();
		restRequest.setMethod(RestMethod.GET);
		restRequest.setType(type);
		restRequest.setIntentActionName(OpenshiftActions.USER_DETAIL);
		restRequest.setUrl(AuthorizationManager.getInstance(ctx).getOpenshiftUrl()+"user");
		initService(restRequest);

		
	}
	
	/**
	 * Performs the invocation of the {@link OpenshiftService}
	 * 
	 * @param restRequest the {@link RestRequest} to perform
	 * 
	 * @see ResultReceiver
	 */
	private void initService(final RestRequest<? extends OpenshiftResource> restRequest) {
		ResultReceiver serviceCallback = new ResultReceiver(null){

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				Intent origIntent = (Intent)resultData.getParcelable(OpenshiftService.ORIGINAL_INTENT_EXTRA);
				
				if(origIntent != null){


					Intent resultBroadcast = new Intent(restRequest.getIntentActionName());
					resultBroadcast.putExtra(EXTRA_RESULT_CODE, resultCode);
					resultBroadcast.putExtra(EXTRA_RESULT_DATA, resultData.getSerializable(OpenshiftService.RESOURCE_RESPONSE_EXTRA));
					
					ctx.sendBroadcast(resultBroadcast);
				}
			}

		};

		Intent intent = new Intent(this.ctx, OpenshiftService.class);
		intent.putExtra(OpenshiftService.SERVICE_CALLBACK, serviceCallback);		
		intent.putExtra(OpenshiftService.RESOURCE_REQUEST_EXTRA, restRequest);
		

		this.ctx.startService(intent);

	}
	
}
