package com.redhat.openshift.rest;

import java.io.Serializable;

import com.redhat.openshift.model.OpenshiftResource;

public class Response<T extends OpenshiftResource> implements Serializable {

	private int status;
	private String message;
	private T resource;
	
	public Response(int status, String message, T resource) {
		this.status = status;
		this.message = message;
		this.resource = resource;
	}
	
	public Response() {};
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getResource() {
		return resource;
	}
	public void setResource(T resource) {
		this.resource = resource;
	}
	
}
