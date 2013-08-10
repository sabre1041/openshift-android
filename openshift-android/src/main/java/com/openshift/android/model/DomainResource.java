package com.openshift.android.model;


/**
 * Model class representing an Openshift Domain
 * 
 * @author Andrew Block
 * 
 * @see OpenshiftResource
 *
 */
public class DomainResource implements OpenshiftResource {
	
	private String name;
	private String suffix;
	
	public String getName() {
		return name;
	}
	public void setId(String id) {
		this.name = id;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

}
