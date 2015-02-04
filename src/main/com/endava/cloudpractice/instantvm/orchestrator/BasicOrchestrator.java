package com.endava.cloudpractice.instantvm.orchestrator;

import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.endava.cloudpractice.instantvm.repository.VMDefinitionRepository;
import com.google.common.base.Preconditions;


public class BasicOrchestrator implements Orchestrator {

	private VMDefinitionRepository defRepository;
	private VMManager vmManager;


	public BasicOrchestrator(VMDefinitionRepository defRepository, VMManager vmManager) {
		Preconditions.checkArgument(defRepository != null);
		Preconditions.checkArgument(vmManager != null);

		this.defRepository = defRepository;
		this.vmManager = vmManager;
	}


	@Override
	public VMDefinitionRepository getVMDefinitionRepository() {
		return defRepository;
	}


	@Override
	public VMManager getVMManager() {
		return vmManager;
	}

}
