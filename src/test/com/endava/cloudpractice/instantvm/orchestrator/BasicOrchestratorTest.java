package com.endava.cloudpractice.instantvm.orchestrator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.endava.cloudpractice.instantvm.repository.VMDefinitionRepository;

public class BasicOrchestratorTest {

	private BasicOrchestrator orchestrator;


	@Before
	public void before() {
		VMDefinitionRepository defRepository = Mockito.mock(VMDefinitionRepository.class);
		VMManager vmManager = Mockito.mock(VMManager.class);
		orchestrator = new BasicOrchestrator(defRepository, vmManager);
	}


	@After
	public void after() {
		orchestrator = null;
	}


	@Test
	public void dummy() {
	}

}
