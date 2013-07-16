package com.redhat.openshift.model;

public class UserResource implements OpenshiftResource {
	
	private String login;
	private String maxGears;
	private String planState;
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getMaxGears() {
		return maxGears;
	}
	public void setMaxGears(String maxGears) {
		this.maxGears = maxGears;
	}
	public String getPlanState() {
		return planState;
	}
	public void setPlanState(String planState) {
		this.planState = planState;
	}

	
}
