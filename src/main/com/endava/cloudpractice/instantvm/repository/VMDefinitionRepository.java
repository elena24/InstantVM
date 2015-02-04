package com.endava.cloudpractice.instantvm.repository;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import java.util.List;


public interface VMDefinitionRepository {

	VMDefinition getVMDefinition(String vmDefinitionName);
	void addVMDefinition(VMDefinition vmDefinition);
	void removeVMDefinition(String vmDefinitionName);
	List<VMDefinition> listVMDefinitions();

}
