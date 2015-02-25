package com.endava.cloudpractice.instantvm;

import java.util.Map;


public class Configuration {

	public static final String AWS_ACCESS_KEY_ID;
	public static final String AWS_SECRET_KEY;

	public static final String AWS_DDB_ENDPOINT = "dynamodb.us-west-2.amazonaws.com";
	public static final String AWS_EC2_ENDPOINT = "ec2.us-west-2.amazonaws.com";
	public static final String AWS_CLOUDFORMATION_ENDPOINT = "cloudformation.us-west-2.amazonaws.com";

	public static final String AWS_EC2_SECURITYGROUP_NAME = "CloudPractice.InstantVM.Default";
	public static final String AWS_EC2_KEYPAIR_NAME = "CloudPractice.InstantVM.Default";

	public static final String VMDEFNAME_ATTRIBUTE = "VMDefinition.Name";
	public static final String VMMANAGERTYPE_ATTRIBUTE = "VMManager.Type";

	static {
		Map<String, String> env = System.getenv();
		AWS_ACCESS_KEY_ID = env.get("AWS_ACCESS_KEY_ID");
		AWS_SECRET_KEY = env.get("AWS_SECRET_KEY");
		
		if(AWS_ACCESS_KEY_ID == null || AWS_ACCESS_KEY_ID.isEmpty()) {
			throw new RuntimeException("AWS_ACCESS_KEY_ID environment variable is not set.");
		}
		if(AWS_SECRET_KEY == null || AWS_SECRET_KEY.isEmpty()) {
			throw new RuntimeException("AWS_SECRET_KEY environment variable is not set.");
		}
	}

}
