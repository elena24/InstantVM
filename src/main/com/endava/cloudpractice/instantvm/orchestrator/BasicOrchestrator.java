package com.endava.cloudpractice.instantvm.orchestrator;

import java.util.List;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
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


	@Override
	public void addVMDefinition(VMDefinition vmDefinition) {
		defRepository.addVMDefinition(vmDefinition);
	}


	@Override
	public void removeVMDefinition(String vmDefinitionName) {
		defRepository.removeVMDefinition(vmDefinitionName);
	}


	@Override
	public List<VMDefinition> listVMDefinitions() {
		return defRepository.listVMDefinitions();
	}


	@Override
	public VMStatus launchVM(String vmDefinitionName) {
		VMDefinition vmDefinition = defRepository.getVMDefinition(vmDefinitionName);
		if(vmDefinition == null) {
			throw new IllegalArgumentException(String.format("Unknown VM definition: <%1$s>", vmDefinitionName));
		}
		VMStatus status = vmManager.launchVM(vmDefinition);
		return status;
	}


	@Override
	public void terminateVM(String vmId) {
		//TODO: Add implementation
		throw new UnsupportedOperationException();
	}


	@Override
	public List<VMStatus> listVMs() {
		//TODO: Add implementation
		throw new UnsupportedOperationException();
	}

}
