package com.endava.cloudpractice.instantvm.datamodel;

import com.google.common.base.Preconditions;


public enum BuilderType {

	BARE_EC2("BareEC2"),
	CLOUDFORMATION("CloudFormation"),
	UNDEFINED("Undefined");


	private final String text;

	private BuilderType(String text) {
		Preconditions.checkArgument(text != null && !text.isEmpty());
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public static BuilderType fromString(String text) {
		Preconditions.checkArgument(text != null && !text.isEmpty());
		for(BuilderType builderType : values()) {
			if(text.equalsIgnoreCase(builderType.text)) {
				return builderType;
			}
		}
		throw new IllegalArgumentException(text);
	}

}
