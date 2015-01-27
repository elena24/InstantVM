package com.endava.cloudpractice.instantvm.repository.datamodel;

import com.google.common.base.Preconditions;


public class VMDefinition {

	private String name;
	private String description;

	public void setName(String name) {
		Preconditions.checkArgument(name != null && !name.isEmpty());
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		Preconditions.checkArgument(description != null);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
