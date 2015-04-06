package com.endava.cloudpractice.instantvm.orchestrator;

import java.util.List;
import java.util.Map;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMManagerType;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.endava.cloudpractice.instantvm.repository.VMDefinitionRepository;


public interface Orchestrator {
	
	VMDefinitionRepository getVMDefinitionRepository();
	Map<VMManagerType, VMManager> getVMManagers();

	void addVMDefinition(VMDefinition def);
	void removeVMDefinition(String defName);
	List<VMDefinition> listVMDefinitions();

	VMStatus launchVM(String defName);
	void terminateVM(String id);
	List<VMStatus> listVMs();

}
