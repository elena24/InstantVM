package com.endava.cloudpractice.instantvm.orchestrator;

import java.util.List;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.endava.cloudpractice.instantvm.repository.VMDefinitionRepository;


public interface Orchestrator {
	
	VMDefinitionRepository getVMDefinitionRepository();
	VMManager getVMManager();

	void addVMDefinition(VMDefinition vmDefinition);
	void removeVMDefinition(String vmDefinitionName);
	List<VMDefinition> listVMDefinitions();

	VMStatus launchVM(String vmDefinitionName);
	void terminateVM(String vmId);
	List<VMStatus> listVMs();

}
