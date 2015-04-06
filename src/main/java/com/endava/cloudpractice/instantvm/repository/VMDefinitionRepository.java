package com.endava.cloudpractice.instantvm.repository;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import java.util.List;


public interface VMDefinitionRepository {

	VMDefinition getVMDefinition(String defName);
	void addVMDefinition(VMDefinition def);
	void removeVMDefinition(String defName);
	List<VMDefinition> listVMDefinitions();

}
