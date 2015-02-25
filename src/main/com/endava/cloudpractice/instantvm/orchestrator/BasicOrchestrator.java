package com.endava.cloudpractice.instantvm.orchestrator;

import java.util.List;
import java.util.Map;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMManagerType;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.endava.cloudpractice.instantvm.repository.VMDefinitionRepository;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


public class BasicOrchestrator implements Orchestrator {

	private VMDefinitionRepository repo;
	private Map<VMManagerType, VMManager> managers;


	public BasicOrchestrator(VMDefinitionRepository repo, Map<VMManagerType, VMManager> managers) {
		Preconditions.checkArgument(repo != null);
		Preconditions.checkArgument(managers != null);

		this.repo = repo;
		this.managers = managers;
	}


	@Override
	public VMDefinitionRepository getVMDefinitionRepository() {
		return repo;
	}


	@Override
	public Map<VMManagerType, VMManager> getVMManagers() {
		return managers;
	}


	@Override
	public void addVMDefinition(VMDefinition def) {
		repo.addVMDefinition(def);
	}


	@Override
	public void removeVMDefinition(String defName) {
		repo.removeVMDefinition(defName);
	}


	@Override
	public List<VMDefinition> listVMDefinitions() {
		return repo.listVMDefinitions();
	}


	@Override
	public VMStatus launchVM(String defName) {
		VMDefinition def = repo.getVMDefinition(defName);
		if(def == null) {
			throw new IllegalArgumentException(String.format("Unknown VM definition: <%1$s>", defName));
		}
		if(managers.get(def.getManager()) == null) {
			throw new IllegalStateException(String.format("Undefined VM manager: <%1$s>", def.getManager()));
		}
		VMStatus status = managers.get(def.getManager()).launchVM(def);
		return status;
	}


	@Override
	public void terminateVM(String id) {
		for(VMManager manager : managers.values()) {
			manager.terminateVM(id);
		}
	}


	@Override
	public List<VMStatus> listVMs() {
		List<VMStatus> status = Lists.newArrayList();
		for(VMManager manager : managers.values()) {
			status.addAll(manager.listVMs());
		}
		return status;
	}

}
