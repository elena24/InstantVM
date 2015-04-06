package com.endava.cloudpractice.instantvm.repository.impl.ddb;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.endava.cloudpractice.instantvm.datamodel.VMManagerType;
import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.google.common.util.concurrent.Uninterruptibles;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class DDBVMDefinitionRepositoryImplIntegrationTest {

	private static String table;
	private DDBVMDefinitionRepositoryImpl repository;


	@BeforeClass
	public static void beforeClass() {
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
	public static void afterClass() {
		AWSClients.DDB.deleteTable(table);
		table = null;
	}


	@Before
	public void before() {
		repository = new DDBVMDefinitionRepositoryImpl(table);
		for(VMDefinition vmDefinition : repository.listVMDefinitions()) {
			repository.removeVMDefinition(vmDefinition.getName());
		}
	}


	@After
	public void after() {
		repository = null;
	}


	@Test
	public void roundtrip() {
		String name = UUID.randomUUID().toString();
		VMDefinition def1, def2;

		def1 = new VMDefinition()
			.withName(name)
			.withDescription("description")
			.withManager(VMManagerType.UNDEFINED)
			.withRecipe("recipe");

		repository.addVMDefinition(def1);
		def2 = repository.getVMDefinition(name);

		Assert.assertEquals(def1, def2);
	}


	@Test
	public void delete() {
		String name = UUID.randomUUID().toString();
		VMDefinition def1, def2;

		def1 = new VMDefinition()
			.withName(name)
			.withDescription("description")
			.withManager(VMManagerType.UNDEFINED)
			.withRecipe("recipe");

		repository.addVMDefinition(def1);
		repository.removeVMDefinition(name);
		def2 = repository.getVMDefinition(name);

		Assert.assertNull(def2);
	}


	@Test
	public void scan() {
		String name = UUID.randomUUID().toString();
		VMDefinition def1 = new VMDefinition()
			.withName(name)
			.withDescription("description")
			.withManager(VMManagerType.UNDEFINED)
			.withRecipe("recipe");
		repository.addVMDefinition(def1);

		List<VMDefinition> defs = repository.listVMDefinitions();

		Assert.assertEquals(1, defs.size());
		VMDefinition def2 = defs.iterator().next();
		Assert.assertEquals(def1, def2);
	}


	@Test
	public void scanEmpty() {
		List<VMDefinition> defs = repository.listVMDefinitions();
		Assert.assertTrue(defs.isEmpty());
	}

}
