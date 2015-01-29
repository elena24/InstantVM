package com.endava.cloudpractice.instantvm.instances.impl.ec2;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.endava.cloudpractice.instantvm.repository.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.google.common.util.concurrent.Uninterruptibles;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class EC2VMManagerImplTest {

	private static final Logger LOGGER = Logger.getLogger(EC2VMManagerImplTest.class.getName());

	private static String table;
	private static EC2VMManagerImpl vmManager;
	private List<String> instanceIds;

	@Before
	public void before() {
		vmManager = new EC2VMManagerImpl();
	}

	@After
	public void after() {
		for (String id : instanceIds) {
			try {
				vmManager.terminateAMI(id);
			} catch (AmazonServiceException e) {
				LOGGER.warning(e.getMessage());
			} catch (Exception e) {
				LOGGER.warning(e.getMessage());
			}
		}
		vmManager = null;
	}

	@Test
	public void startInstanceTest() throws AmazonServiceException, Exception {
		vmManager.createSecurityGroup();
		vmManager.createKeyPair();
		instanceIds = vmManager.startEC2Instance("t2.micro", "ami-b5a7ea85");
	}

}
