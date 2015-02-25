package com.endava.cloudpractice.instantvm.instances.impl.cloudformation;

import java.util.List;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.google.common.base.Preconditions;


public class CloudFormationVMManagerImpl implements VMManager {

	@Override
	public VMStatus launchVM(VMDefinition def) {
		Preconditions.checkArgument(def != null);
		Preconditions.checkArgument(def.getRecipe() != null);

		throw new UnsupportedOperationException("Not yet implemented.");
	}


	@Override
	public void terminateVM(String id) {
		Preconditions.checkArgument(id != null && !id.isEmpty());

		throw new UnsupportedOperationException("Not yet implemented.");
	}


	@Override
	public List<VMStatus> listVMs() {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

}
