package com.endava.cloudpractice.instantvm.repository;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import java.util.List;


public interface VMDefinitionRepository {

	VMDefinition readVMDefinition(String name);
	void writeVMDefinition(VMDefinition def);
	void deleteVMDefinition(String name);
	List<VMDefinition> listVMDefinitions();

}
