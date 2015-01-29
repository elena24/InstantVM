package com.endava.cloudpractice.instantvm.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.endava.cloudpractice.instantvm.Configuration;


public class AWSClients {

	private static final AWSCredentials AWS_CREDENTIALS = new BasicAWSCredentials(
			Configuration.AWS_ACCESS_KEY_ID, Configuration.AWS_SECRET_KEY);
	
	public static final AmazonDynamoDBClient DDB = new AmazonDynamoDBClient(AWS_CREDENTIALS);
	public static final AmazonEC2Client EC2 = new AmazonEC2Client(AWS_CREDENTIALS);

	static {
		DDB.setEndpoint(Configuration.AWS_DDB_ENDPOINT);
		EC2.setEndpoint(Configuration.AWS_EC2_ENDPOINT);
	}

}
