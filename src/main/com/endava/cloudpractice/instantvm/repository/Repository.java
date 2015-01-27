package com.endava.cloudpractice.instantvm.repository;

import com.endava.cloudpractice.instantvm.repository.datamodel.VMDefinition;


public interface Repository {

	void writeVMDefinition(VMDefinition def);
	VMDefinition readVMDefinition(String name);

}
