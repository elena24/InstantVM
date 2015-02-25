package com.endava.cloudpractice.instantvm.datamodel;

import com.google.common.base.Preconditions;


public enum VMManagerType {

	BARE_EC2("BareEC2"),
	CLOUDFORMATION("CloudFormation"),
	UNDEFINED("Unknown");


	private final String text;

	private VMManagerType(String text) {
		Preconditions.checkArgument(text != null && !text.isEmpty());
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public static VMManagerType fromString(String text) {
		Preconditions.checkArgument(text != null && !text.isEmpty());
		for(VMManagerType vMManagerType : values()) {
			if(text.equalsIgnoreCase(vMManagerType.text)) {
				return vMManagerType;
			}
		}
		throw new IllegalArgumentException(text);
	}

}
