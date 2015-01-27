package com.endava.cloudpractice.instantvm.repository;

import com.endava.cloudpractice.instantvm.repository.datamodel.VMDefinition;


public interface VMDefRepository {

	void writeVMDefinition(VMDefinition def);
	VMDefinition readVMDefinition(String name);

}
