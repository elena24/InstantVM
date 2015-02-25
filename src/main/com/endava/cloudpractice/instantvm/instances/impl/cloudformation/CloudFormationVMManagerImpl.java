package com.endava.cloudpractice.instantvm.instances.impl.cloudformation;

import java.util.List;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;


public class CloudFormationVMManagerImpl implements VMManager {

	@Override
	public VMStatus launchVM(VMDefinition def) {
		Preconditions.checkArgument(def != null);
		Preconditions.checkArgument(def.getRecipe() != null);

		//TODO: Add implementation
		return null;
	}


	@Override
	public void terminateVM(String id) {
		Preconditions.checkArgument(id != null && !id.isEmpty());

		//TODO: Add implementation
	}


	@Override
	public List<VMStatus> listVMs() {
		//TODO: Add implementation
		return ImmutableList.of();
	}

}
