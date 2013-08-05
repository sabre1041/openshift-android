package com.openshift.android.model;

public class DomainResource implements OpenshiftResource {
	
	private String id;
	private String suffix;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	@Override
	public String toString() {
		return this.getId();
	}

}
