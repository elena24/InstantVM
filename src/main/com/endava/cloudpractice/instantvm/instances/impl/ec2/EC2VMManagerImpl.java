package com.endava.cloudpractice.instantvm.instances.impl.ec2;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.endava.cloudpractice.instantvm.Configuration;
import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.google.common.base.Preconditions;
import java.util.List;


public class EC2VMManagerImpl implements VMManager {

	@Override
	public String startInstance(VMDefinition vmDefinition) {
		Preconditions.checkArgument(vmDefinition != null);
		Preconditions.checkArgument(vmDefinition.getType() != null);
		Preconditions.checkArgument(vmDefinition.getImage() != null);

		RunInstancesRequest request = new RunInstancesRequest()
			.withInstanceType(vmDefinition.getType())
			.withMinCount(1).withMaxCount(1)
			.withImageId(vmDefinition.getImage())
			.withKeyName(Configuration.AWS_EC2_KEYPAIR_NAME)
			.withSecurityGroups(Configuration.AWS_EC2_SECURITYGROUP_NAME);
		RunInstancesResult response = AWSClients.EC2.runInstances(request);

		List<Instance> instances = response.getReservation().getInstances();
		String id = null;
		if (!instances.isEmpty()) {
			id = instances.iterator().next().getInstanceId();
		}
		return id;
	}

	@Override
	public void terminateInstance(String id) {
		Preconditions.checkArgument(id != null && !id.isEmpty());

		AWSClients.EC2.terminateInstances(new TerminateInstancesRequest()
			.withInstanceIds(id));
	}

}