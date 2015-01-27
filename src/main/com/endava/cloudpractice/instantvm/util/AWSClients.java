package com.endava.cloudpractice.instantvm.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.endava.cloudpractice.instantvm.Configuration;


public class AWSClients {

	private static final AWSCredentials AWS_CREDENTIALS = new BasicAWSCredentials(
			Configuration.AWS_ACCESS_KEY_ID, Configuration.AWS_SECRET_KEY);
	
	public static final AmazonDynamoDBClient DDB = new AmazonDynamoDBClient(AWS_CREDENTIALS);

	static {
		DDB.setEndpoint(Configuration.AWS_DDB_ENDPOINT);
	}

}
