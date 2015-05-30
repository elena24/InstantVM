package com.endava.cloudpractice.instantvm.instances.impl.ec2;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.endava.cloudpractice.instantvm.Configuration;
import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMManagerType;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


public class EC2VMManagerImplIntegrationTest {

	private static EC2VMManagerImpl manager;


	@Before
	public void before() {
		manager = new EC2VMManagerImpl();
	}


	@After
	public void after() {
		manager = null;
	}


	@Test
	public void cycle() {
		String defName = UUID.randomUUID().toString();
		VMDefinition def = new VMDefinition()
			.withName(defName)
			.withDescription("description")
			.withManager(VMManagerType.BARE_EC2)
			.withRecipe("{\"type\":\"t2.micro\",\"image\":\"ami-dfc39aef\"}");

		VMStatus status = manager.launchVM(def);
		Assert.assertNotNull(status);

		manager.terminateVM(status.getId());

		Assert.assertNotNull(status.getId());
		Assert.assertNotNull(status.getAttributes());
		Assert.assertEquals(defName, status.getAttributes().get(Configuration.VMDEFNAME_ATTRIBUTE));
		Assert.assertEquals(def.getManager().toString(), status.getAttributes().get(Configuration.VMMANAGERTYPE_ATTRIBUTE));
	}


	@Test
	public void list() {
		final String defName = UUID.randomUUID().toString();
		VMDefinition def = new VMDefinition()
			.withName(defName)
			.withManager(VMManagerType.BARE_EC2)
			.withDescription("description")
			.withRecipe("{\"type\":\"t2.micro\",\"image\":\"ami-dfc39aef\"}");

		VMStatus status = manager.launchVM(def);
		Assert.assertNotNull(status);

		List<VMStatus> statuses = manager.listVMs();

		manager.terminateVM(status.getId());

		Assert.assertTrue(statuses.size() >= 1);
		Assert.assertNotNull(Iterables.find(statuses, new Predicate<VMStatus>() {
			@Override
			public boolean apply(VMStatus status) {
				return defName.equals(status.getAttributes().get(Configuration.VMDEFNAME_ATTRIBUTE));
			}}, null));
	}

}
