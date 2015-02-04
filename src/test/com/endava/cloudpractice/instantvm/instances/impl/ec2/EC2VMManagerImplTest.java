package com.endava.cloudpractice.instantvm.instances.impl.ec2;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.endava.cloudpractice.instantvm.Configuration;
import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


public class EC2VMManagerImplTest {

	private static EC2VMManagerImpl vmManager;


	@Before
	public void before() {
		vmManager = new EC2VMManagerImpl();
	}


	@After
	public void after() {
		vmManager = null;
	}


	@Test
	public void cycle() {
		String vmDefinitionName = UUID.randomUUID().toString();
		VMDefinition vmDefinition = new VMDefinition()
			.withName(vmDefinitionName)
			.withDescription("description")
			.withType("t2.micro")
			.withImage("ami-b5a7ea85");

		VMStatus vmStatus = vmManager.launchVM(vmDefinition);
		Assert.assertNotNull(vmStatus);

		vmManager.terminateVM(vmStatus.getId());

		Assert.assertNotNull(vmStatus.getId());
		Assert.assertNotNull(vmStatus.getAttributes());
		Assert.assertEquals(vmDefinitionName, vmStatus.getAttributes().get(Configuration.VMDEFNAME_ATTRIBUTE));
	}


	@Test
	public void list() {
		String vmDefinitionName = UUID.randomUUID().toString();
		VMDefinition vmDefinition = new VMDefinition()
			.withName(vmDefinitionName)
			.withDescription("description")
			.withType("t2.micro")
			.withImage("ami-b5a7ea85");

		VMStatus vmStatus = vmManager.launchVM(vmDefinition);
		Assert.assertNotNull(vmStatus);
		
		List<VMStatus> vmStatuses = vmManager.listVMs();

		vmManager.terminateVM(vmStatus.getId());

		Assert.assertTrue(vmStatuses.size() >= 1);
		Assert.assertNotNull(Iterables.find(vmStatuses, new Predicate<VMStatus>() {
			@Override
			public boolean apply(VMStatus vmStatus) {
				return vmDefinitionName.equals(vmStatus.getAttributes().get(Configuration.VMDEFNAME_ATTRIBUTE));
			}}, null));
	}

}
