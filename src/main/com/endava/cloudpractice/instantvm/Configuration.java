package com.endava.cloudpractice.instantvm;

import java.util.Map;
import java.util.Random;


public class Configuration {

	public static final String AWS_ACCESS_KEY_ID;//="AKIAJWIGIABNOMXZYF5Q";
	public static final String AWS_SECRET_KEY;//="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";

	public static final String AWS_DDB_ENDPOINT = "dynamodb.us-west-2.amazonaws.com";
	public static final String AWS_EC2_ENDPOINT = "ec2.us-west-2.amazonaws.com";
	
	public static final String AWS_ZONE = "us-west-2b";

	public static final String SECURITY_GROUP_NAME = "securityGroup-"+(new Random()).nextInt();
	public static final String SECURITY_GROUP_DESCRIPTION = "My group description";
	
	public static final String IP_RANGE_1 = "0.0.0.0/0";
	public static final String IP_PROTOCOL = "tcp";
	public static final int PORT = 22;
	public static final Integer MIN_NO_INSTANCES = 1;
	public static final Integer MAX_NO_INSTANCES = 1;
	public static String KEY_NAME = "ec2-"+(new Random()).nextInt();
	
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
