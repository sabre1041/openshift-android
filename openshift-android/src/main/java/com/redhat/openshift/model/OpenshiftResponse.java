package com.redhat.openshift.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;


public class OpenshiftResponse<T extends OpenshiftResource> implements OpenshiftResource  {
	@SerializedName("api_version")
	private String apiVersion;
	private String status;
	private String type;
	private String version;
	private List<OpenshiftMessage> messages = new ArrayList<OpenshiftMessage>();
	private T data;
	
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public List<OpenshiftMessage> getMessages() {
		return messages;
	}
	public void setMessages(List<OpenshiftMessage> messages) {
		this.messages = messages;
	}
	
	

}
