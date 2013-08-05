package com.openshift.android.model;

import java.util.List;

public class OpenshiftDataList<T extends OpenshiftResource> implements OpenshiftResource  {
	
	private List<T> list;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
