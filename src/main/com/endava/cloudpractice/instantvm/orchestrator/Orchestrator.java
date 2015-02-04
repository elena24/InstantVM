package com.endava.cloudpractice.instantvm.orchestrator;

import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.endava.cloudpractice.instantvm.repository.VMDefinitionRepository;


public interface Orchestrator {
	
	VMDefinitionRepository getVMDefinitionRepository();
	VMManager getVMManager();

}
