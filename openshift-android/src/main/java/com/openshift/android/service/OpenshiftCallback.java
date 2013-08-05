package com.openshift.android.service;

import com.openshift.android.model.OpenshiftResource;
import com.openshift.android.rest.RestRequest;


public interface OpenshiftCallback {
	
	void send(int statusCode, RestRequest<? extends OpenshiftResource> response);

}
