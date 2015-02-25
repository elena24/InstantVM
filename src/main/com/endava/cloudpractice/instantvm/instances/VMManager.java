package com.endava.cloudpractice.instantvm.instances;

import java.util.List;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;


public interface VMManager {
	
	VMStatus launchVM(VMDefinition def);
	void terminateVM(String id);
	List<VMStatus> listVMs();

}
