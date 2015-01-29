package com.endava.cloudpractice.instantvm.instances;


public interface VMManager {
	
	String startInstance(String type, String image);
	void terminateInstance(String id);

}
