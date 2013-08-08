package com.openshift.android.service;

import com.openshift.android.model.OpenshiftResource;
import com.openshift.android.rest.RestRequest;


/**
 * Definition of a callback which is invoked after a rest request has completed
 * 
 * @author Andrew Block
 *
 */
public interface OpenshiftCallback {
	
	void send(int statusCode, RestRequest<? extends OpenshiftResource> response);

}
