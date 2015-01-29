package com.endava.cloudpractice.instantvm.instances;

import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;


public interface VMManager {
	
	String startInstance(VMDefinition vmDefinition);
	void terminateInstance(String id);

}
