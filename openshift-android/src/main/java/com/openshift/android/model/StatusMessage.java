package com.openshift.android.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class StatusMessage implements Serializable {
	
	@SerializedName("gear_id")
	private String gearId;
	
	private String message;
	
	public String getGearId() {
		return gearId;
	}

	public void setGearId(String gearId) {
		this.gearId = gearId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
