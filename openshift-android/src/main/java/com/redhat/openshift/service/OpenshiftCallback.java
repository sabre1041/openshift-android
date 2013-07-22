package com.redhat.openshift.service;

import com.redhat.openshift.model.OpenshiftResource;
import com.redhat.openshift.rest.RestRequest;


public interface OpenshiftCallback {
	
	void send(int statusCode, RestRequest<? extends OpenshiftResource> response);

}
