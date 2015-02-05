package com.endava.cloudpractice.instantvm.orchestrator;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.endava.cloudpractice.instantvm.repository.VMDefinitionRepository;
import com.google.common.collect.ImmutableList;

public class BasicOrchestratorTest {

	private VMDefinitionRepository defRepository;
	private VMManager vmManager;
	private BasicOrchestrator orchestrator;


	@Before
	public void before() {
		defRepository = Mockito.mock(VMDefinitionRepository.class);
		vmManager = Mockito.mock(VMManager.class);
		orchestrator = new BasicOrchestrator(defRepository, vmManager);
	}


	@After
	public void after() {
		defRepository = null;
		vmManager = null;
		orchestrator = null;
	}


	@Test
	public void addVMDefinition() {
		VMDefinition vmDefinition = new VMDefinition().withName("name");

		orchestrator.addVMDefinition(vmDefinition);

		Mockito.verify(defRepository).addVMDefinition(vmDefinition);
	}


	@Test
	public void removeVMDefinition() {
		String vmDefinitionName = "name";

		orchestrator.removeVMDefinition(vmDefinitionName);

		Mockito.verify(defRepository).removeVMDefinition(vmDefinitionName);
	}


	@Test
	public void listVMDefinition() {
		String vmDefinitionName = "name";
		VMDefinition vmDefinition = new VMDefinition().withName(vmDefinitionName);

		Mockito.when(defRepository.listVMDefinitions()).thenReturn(ImmutableList.of(vmDefinition));

		orchestrator.addVMDefinition(vmDefinition);
		List<VMDefinition> vmDefinitions = orchestrator.listVMDefinitions();

		Assert.assertEquals(1, vmDefinitions.size());
		Assert.assertEquals(vmDefinition, vmDefinitions.iterator().next());
	}


	@Test(expected=IllegalArgumentException.class)
	public void attemptLaunchUnknownVM() {
		String vmDefinitionName = "name";

		Mockito.when(defRepository.getVMDefinition(vmDefinitionName)).thenReturn(null);

		orchestrator.launchVM(vmDefinitionName);
	}


	@Test
	public void launchVM() {
		String vmDefinitionName = "name", vmId = "id";
		VMDefinition vmDefinition = new VMDefinition().withName(vmDefinitionName);
		VMStatus intendedVMStatus = new VMStatus().withId(vmId);

		Mockito.when(defRepository.getVMDefinition(vmDefinitionName)).thenReturn(vmDefinition);
		Mockito.when(vmManager.launchVM(vmDefinition)).thenReturn(intendedVMStatus);

		VMStatus actualVMStatus = orchestrator.launchVM(vmDefinitionName);

		Mockito.verify(vmManager).launchVM(vmDefinition);
		Assert.assertEquals(intendedVMStatus, actualVMStatus);
	}


	@Test
	public void terminateVM() {
		String vmId = "id";

		orchestrator.terminateVM(vmId);
	
		Mockito.verify(vmManager).terminateVM(vmId);
	}


	@Test
	public void listVMs() {
		String vmId = "id";
		VMStatus vmStatus = new VMStatus().withId(vmId);

		Mockito.when(vmManager.listVMs()).thenReturn(ImmutableList.of(vmStatus));

		List<VMStatus> vmStatuses = orchestrator.listVMs();

		Assert.assertEquals(1, vmStatuses.size());
		Assert.assertEquals(vmStatus, vmStatuses.iterator().next());
	}

}
