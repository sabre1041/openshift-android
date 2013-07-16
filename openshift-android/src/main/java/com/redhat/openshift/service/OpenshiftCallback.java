package com.redhat.openshift.service;

import com.redhat.openshift.model.OpenshiftResource;
import com.redhat.openshift.rest.Response;


public interface OpenshiftCallback<T extends OpenshiftResource> {
	
	void send(Response<T> response);

}
