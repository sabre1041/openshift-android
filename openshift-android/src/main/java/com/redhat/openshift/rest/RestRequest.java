package com.redhat.openshift.rest;

public class RestRequest {

	private String data;
	private RestMethod method;
	private String contextPath;
	
	public RestRequest(String data, RestMethod method, String contextPath) {
		this.data = data;
		this.method = method;
		this.setContextPath(contextPath);
	}
	
	public RestRequest() {}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public RestMethod getMethod() {
		return method;
	}
	public void setMethod(RestMethod method) {
		this.method = method;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
}
