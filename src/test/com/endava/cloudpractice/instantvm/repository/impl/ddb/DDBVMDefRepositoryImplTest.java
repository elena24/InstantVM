package com.endava.cloudpractice.instantvm.repository.impl.ddb;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.google.common.util.concurrent.Uninterruptibles;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class DDBVMDefRepositoryImplTest {

	private static String table;

	@BeforeClass
	public static void setUp() {
		table = "InstantVM-test-" + UUID.randomUUID().toString();
		AWSClients.DDB.createTable(new CreateTableRequest()
			.withTableName(table)
			.withAttributeDefinitions(new AttributeDefinition()
				.withAttributeName("Name").withAttributeType("S"))
			.withKeySchema(new KeySchemaElement()
				.withAttributeName("Name").withKeyType("HASH"))
			.withProvisionedThroughput(new ProvisionedThroughput()
				.withReadCapacityUnits(1L).withWriteCapacityUnits(1L)));
		DescribeTableResult result;
		do {
			Uninterruptibles.sleepUninterruptibly(1L, TimeUnit.SECONDS);
			result = AWSClients.DDB.describeTable(table);
		} while(!"ACTIVE".equals(result.getTable().getTableStatus()));
	}

	@AfterClass
	public static void tearDown() {
		AWSClients.DDB.deleteTable(table);
		table = null;
	}

	@Test
	public void dummy() {
	}

}
