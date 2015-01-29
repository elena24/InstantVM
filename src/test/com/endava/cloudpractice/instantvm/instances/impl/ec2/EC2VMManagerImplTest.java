package com.endava.cloudpractice.instantvm.instances.impl.ec2;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


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
		VMDefinition vmDef = new VMDefinition()
			.withName("name")
			.withDescription("description")
			.withType("t2.micro")
			.withImage("ami-b5a7ea85");

		String id = vmManager.startInstance(vmDef);
		Assert.assertNotNull(id);
		vmManager.terminateInstance(id);
	}

}
