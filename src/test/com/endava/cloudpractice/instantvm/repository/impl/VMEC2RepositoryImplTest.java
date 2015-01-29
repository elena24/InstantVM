package com.endava.cloudpractice.instantvm.repository.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.endava.cloudpractice.instantvm.repository.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.repository.impl.DDBVMDefRepositoryImpl;
import com.endava.cloudpractice.instantvm.repository.impl.VMEC2RepositoryImpl;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.google.common.util.concurrent.Uninterruptibles;

public class VMEC2RepositoryImplTest {

	private static final Logger LOGGER = Logger
			.getLogger(VMEC2RepositoryImplTest.class.getName());

	private static String table;
	private static DDBVMDefRepositoryImpl repository;
	private static VMEC2RepositoryImpl ec2Repository;
	private List<String> instanceIds;

	@BeforeClass
	public static void beforeClass() {
		table = "InstantVM-test-" + UUID.randomUUID().toString();
		AWSClients.DDB.createTable(new CreateTableRequest()
				.withTableName(table)
				.withAttributeDefinitions(
						new AttributeDefinition().withAttributeName("Name")
								.withAttributeType("S"))
				.withKeySchema(
						new KeySchemaElement().withAttributeName("Name")
								.withKeyType("HASH"))
				.withProvisionedThroughput(
						new ProvisionedThroughput().withReadCapacityUnits(1L)
								.withWriteCapacityUnits(1L)));
		DescribeTableResult result;
		do {
			Uninterruptibles.sleepUninterruptibly(1L, TimeUnit.SECONDS);
			result = AWSClients.DDB.describeTable(table);
		} while (!"ACTIVE".equals(result.getTable().getTableStatus()));
	}

	@AfterClass
	public static void afterClass() {
		AWSClients.DDB.deleteTable(table);
		table = null;
	}

	@Before
	public void before() {
		repository = new DDBVMDefRepositoryImpl(table);
		ec2Repository = new VMEC2RepositoryImpl(table);
	}

	@After
	public void after() {
		for (String id : instanceIds) {
			try {
				ec2Repository.terminateAMI(id);
			} catch (AmazonServiceException e) {
				LOGGER.warning(e.getMessage());
			} catch (Exception e) {
				LOGGER.warning(e.getMessage());
			}
		}
		repository = null;
		ec2Repository = null;
	}

	@Test
	public void startInstanceTest() throws AmazonServiceException, Exception {
		VMDefinition def1, def2;

		def1 = new VMDefinition().withName("vmInstanceType").withDescription(
				"t2.micro");
		def2 = new VMDefinition().withName("vmImageId").withDescription(
				"ami-b5a7ea85");
		repository.writeVMDefinition(def1);
		repository.writeVMDefinition(def2);

		ec2Repository.createSecurityGroup();
		ec2Repository.createKeyPair();
		instanceIds = ec2Repository.startEC2Instance();
	}
}
