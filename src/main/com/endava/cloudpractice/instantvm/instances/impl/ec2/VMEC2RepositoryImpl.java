package com.endava.cloudpractice.instantvm.instances.impl.ec2;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.endava.cloudpractice.instantvm.Configuration;
import com.endava.cloudpractice.instantvm.instances.VMEC2Repository;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class VMEC2RepositoryImpl implements VMEC2Repository {

	private static final String VM_IMAGE_ID_KEY = "vmImageId";
	private static final String VM_INSTANCE_TYPE_KEY = "vmInstanceType";

	private final CreateSecurityGroupRequest createSecurityGroupRequest = new CreateSecurityGroupRequest();
	private final IpPermission ipPermission = new IpPermission();
	private final AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest = new AuthorizeSecurityGroupIngressRequest();
	private final CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();
	private CreateKeyPairResult createKeyPairResult;
	private KeyPair keyPair = new KeyPair();


	public VMEC2RepositoryImpl() {
	}

	@Override
	public void createSecurityGroup() {
		createSecurityGroupRequest.withGroupName(
				Configuration.SECURITY_GROUP_NAME).withDescription(
				Configuration.SECURITY_GROUP_DESCRIPTION);
		AWSClients.EC2_CLIENT
				.createSecurityGroup(createSecurityGroupRequest);

		ipPermission.withIpRanges(Configuration.IP_RANGE_1)
				.withIpProtocol(Configuration.IP_PROTOCOL)
				.withFromPort(Configuration.PORT)
				.withToPort(Configuration.PORT);

		authorizeSecurityGroupIngressRequest.withGroupName(
				Configuration.SECURITY_GROUP_NAME).withIpPermissions(
				ipPermission);

		AWSClients.EC2_CLIENT
				.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);

	}

	public void createKeyPair() {
		createKeyPairRequest.withKeyName(Configuration.KEY_NAME);
		createKeyPairResult = AWSClients.EC2_CLIENT
				.createKeyPair(createKeyPairRequest);

		keyPair = createKeyPairResult.getKeyPair();

	}

	@Override
	public List<String> startEC2Instance(String instanceType, String imageId) {

		List<String> runninginstanceIDs = new ArrayList<String>();
		RunInstancesRequest request = new RunInstancesRequest();

		request.setInstanceType(instanceType);
		request.setMinCount(Configuration.MIN_NO_INSTANCES);
		request.setMaxCount(Configuration.MAX_NO_INSTANCES);
		Placement p = new Placement();
		p.setAvailabilityZone(Configuration.AWS_ZONE);
		request.setPlacement(p);
		request.setImageId(imageId);
		request.setKeyName(keyPair.getKeyName());

		RunInstancesResult runInstancesRes = AWSClients.EC2_CLIENT
				.runInstances(request);
		// Getting the list of running instances according with our request
		Reservation reservation = runInstancesRes.getReservation();
		List<Instance> instances = reservation.getInstances();
		if (!instances.isEmpty()) {
			Iterator<Instance> instIterator = instances.iterator();
			while (instIterator.hasNext()) {
				Instance runningInst = instIterator.next();
				String uniqueID = runningInst.getInstanceId();
				runninginstanceIDs.add(uniqueID);
			}
		}
		return runninginstanceIDs;
	}

	public void terminateAMI(String instanceId) throws AmazonServiceException,
			Exception {
		TerminateInstancesRequest rq = new TerminateInstancesRequest();
		rq.getInstanceIds().add(instanceId);
		AWSClients.EC2_CLIENT.terminateInstances(rq);
	}

}
