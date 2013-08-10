package com.openshift.android.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;



/**
 * Model class representing an OpenShift User
 * 
 * @author Andrew Block
 * 
 * @see OpenshiftResource
 *
 */
public class UserResource implements OpenshiftResource {
	
	@SerializedName("consumed_gears")
	private String consumedGears;
	
	@SerializedName("created_at")
	private Date createdAt;
	
	private String id;
	
	private String login;
	
	@SerializedName("max_gears")
	private String maxGears;
	
	public String getConsumedGears() {
		return consumedGears;
	}
	public void setConsumedGears(String consumedGears) {
		this.consumedGears = consumedGears;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getUsageAccountId() {
		return usageAccountId;
	}
	public void setUsageAccountId(String usageAccountId) {
		this.usageAccountId = usageAccountId;
	}
	@SerializedName("plan_id")
	private String planId;
	
	@SerializedName("plan_state")
	private String planState;
	
	@SerializedName("usage_account_id")
	private String usageAccountId; 
	
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
