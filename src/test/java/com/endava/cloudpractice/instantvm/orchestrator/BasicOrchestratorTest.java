package com.endava.cloudpractice.instantvm.orchestrator;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMManagerType;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.endava.cloudpractice.instantvm.repository.VMDefinitionRepository;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class BasicOrchestratorTest {

	private VMDefinitionRepository repo;
	private Map<VMManagerType, VMManager> managers;
	private BasicOrchestrator orchestrator;


	@Before
	public void before() {
		repo = Mockito.mock(VMDefinitionRepository.class);
		managers = ImmutableMap.of(
				VMManagerType.BARE_EC2, Mockito.mock(VMManager.class),
				VMManagerType.CLOUDFORMATION, Mockito.mock(VMManager.class));
		orchestrator = new BasicOrchestrator(repo, managers);
	}


	@After
	public void after() {
		repo = null;
		managers = null;
		orchestrator = null;
	}


	@Test
	public void addVMDefinition() {
		VMDefinition def = new VMDefinition().withName("name");

		orchestrator.addVMDefinition(def);

		Mockito.verify(repo).addVMDefinition(def);
	}


	@Test
	public void removeVMDefinition() {
		String defName = "name";

		orchestrator.removeVMDefinition(defName);

		Mockito.verify(repo).removeVMDefinition(defName);
	}


	@Test
	public void listVMDefinition() {
		String defName = "name";
		VMDefinition def = new VMDefinition().withName(defName);

		Mockito.when(repo.listVMDefinitions()).thenReturn(ImmutableList.of(def));

		orchestrator.addVMDefinition(def);
		List<VMDefinition> defs = orchestrator.listVMDefinitions();

		Assert.assertEquals(1, defs.size());
		Assert.assertEquals(def, defs.iterator().next());
	}


	@Test(expected=IllegalArgumentException.class)
	public void attemptLaunchUnknownVM() {
		String defName = "name";

		Mockito.when(repo.getVMDefinition(defName)).thenReturn(null);

		orchestrator.launchVM(defName);
	}


	@Test
	public void launchVM() {
		String defName = "name", id = "id";
		VMDefinition def = new VMDefinition().withName(defName).withManager(VMManagerType.BARE_EC2);
		VMStatus intendedStatus = new VMStatus().withId(id);

		Mockito.when(repo.getVMDefinition(defName)).thenReturn(def);
		Mockito.when(managers.get(VMManagerType.BARE_EC2).launchVM(def)).thenReturn(intendedStatus);

		VMStatus actualStatus = orchestrator.launchVM(defName);

		Mockito.verify(managers.get(VMManagerType.BARE_EC2)).launchVM(def);
		Assert.assertEquals(intendedStatus, actualStatus);
	}


	@Test
	public void terminateVM() {
		String id = "id";

		orchestrator.terminateVM(id);
	
		Mockito.verify(managers.get(VMManagerType.BARE_EC2)).terminateVM(id);
		Mockito.verify(managers.get(VMManagerType.CLOUDFORMATION)).terminateVM(id);
	}


	@Test
	public void listVMs() {
		String id1 = "id1", id2 = "id2";
		VMStatus status1 = new VMStatus().withId(id1), status2 = new VMStatus().withId(id2);

		Mockito.when(managers.get(VMManagerType.BARE_EC2).listVMs()).thenReturn(ImmutableList.of(status1));
		Mockito.when(managers.get(VMManagerType.CLOUDFORMATION).listVMs()).thenReturn(ImmutableList.of(status2));

		List<VMStatus> status = orchestrator.listVMs();

		Assert.assertEquals(2, status.size());
		Assert.assertEquals(ImmutableSet.of(status1, status2), ImmutableSet.copyOf(status));
	}

}
