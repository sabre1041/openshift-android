package com.openshift.android.rest;

/**
 * Raw Rest Client Response. Data is returned in the form of a {@link String} prior to Serialization
 * 
 * @author Andrew Block
 *
 */
public class RestResponse {
	
	private int statusCode;
	private String errorMessage;
	private String data;
	
	public RestResponse() {}
	
	public RestResponse(int statusCode, String errorMessage, String data) {
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
		this.data = data;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

}
