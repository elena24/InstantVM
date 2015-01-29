package com.endava.cloudpractice.instantvm.repository;

import java.util.List;

import com.amazonaws.AmazonServiceException;

public interface VMEC2Repository {
	
	void createSecurityGroup();
	void createKeyPair();
	List<String> startEC2Instance();
	void terminateAMI(String instanceId) throws AmazonServiceException,Exception;
}
