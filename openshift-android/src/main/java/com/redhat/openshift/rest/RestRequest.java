package com.redhat.openshift.rest;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import android.os.ResultReceiver;

import com.redhat.openshift.model.OpenshiftResource;

public class RestRequest<T extends OpenshiftResource> implements Serializable {

	private OpenshiftResource response;
	private String intentActionName;
	private Type type;
	private RestMethod method;
	private String url;
	private String message;
	private int status;
	private ResultReceiver resultReceiver;
	private Map<String, String> inputParameters = new HashMap<String,String>();
	
	public RestRequest(OpenshiftResource response, RestMethod method, String url) {
		this.response = response;
		this.method = method;
		this.url = url;
	}
	
	public RestRequest() {}
	
	public OpenshiftResource getResponse() {
		return response;
	}
	public void setResponse(OpenshiftResource response) {
		this.response = response;
	}
	public RestMethod getMethod() {
		return method;
	}
	public void setMethod(RestMethod method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ResultReceiver getResultReceiver() {
		return resultReceiver;
	}

	public void setResultReceiver(ResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

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

	public String getIntentActionName() {
		return intentActionName;
	}

	public void setIntentActionName(String intentActionName) {
		this.intentActionName = intentActionName;
	}

	public Map<String, String> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(Map<String, String> inputParameters) {
		this.inputParameters = inputParameters;
	}
	
}
