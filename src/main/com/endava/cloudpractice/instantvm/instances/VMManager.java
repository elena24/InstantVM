package com.endava.cloudpractice.instantvm.instances;

import com.amazonaws.AmazonServiceException;
import java.util.List;


public interface VMManager {
	
	void createSecurityGroup();
	void createKeyPair();
	List<String> startEC2Instance(String instanceType, String imageId);
	void terminateAMI(String instanceId) throws AmazonServiceException,Exception;
}
