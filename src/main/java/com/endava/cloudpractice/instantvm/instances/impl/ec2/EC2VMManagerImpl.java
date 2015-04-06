package com.endava.cloudpractice.instantvm.instances.impl.ec2;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.endava.cloudpractice.instantvm.Configuration;
import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.datamodel.VMManagerType;
import com.endava.cloudpractice.instantvm.datamodel.VMStatus;
import com.endava.cloudpractice.instantvm.instances.VMManager;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class EC2VMManagerImpl implements VMManager {

	private static final String ID_PREFIX = "ec2-";
	private static final String TAG_KEY_FILTER_NAME = "tag-key";
	private static final String TAG_VALUE_FILTER_NAME = "tag-value";

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();


	@Override
	public VMStatus launchVM(VMDefinition def) {
		Preconditions.checkArgument(def != null);
		Preconditions.checkArgument(def.getRecipe() != null);

		Recipe recipe = deserializeRecipe(def.getRecipe());

		RunInstancesRequest request = new RunInstancesRequest()
			.withInstanceType(recipe.getType())
			.withMinCount(1).withMaxCount(1)
			.withImageId(recipe.getImage())
			.withKeyName(Configuration.AWS_EC2_KEYPAIR_NAME)
			.withSecurityGroups(Configuration.AWS_EC2_SECURITYGROUP_NAME);
		RunInstancesResult response = AWSClients.EC2.runInstances(request);
		List<Instance> instances = response.getReservation().getInstances();
		if(instances.isEmpty()) {
			return null;
		}
		String id = instances.iterator().next().getInstanceId();

		AWSClients.EC2.createTags(new CreateTagsRequest(
				ImmutableList.of(id),
				ImmutableList.of(
						new Tag(Configuration.VMDEFNAME_ATTRIBUTE, def.getName()),
						new Tag(Configuration.VMMANAGERTYPE_ATTRIBUTE, VMManagerType.BARE_EC2.toString()))));

		return new VMStatus()
			.withId(getCustomIDFromEC2ID(id))
			.withAttributes(ImmutableMap.of(
					Configuration.VMDEFNAME_ATTRIBUTE, def.getName(),
					Configuration.VMMANAGERTYPE_ATTRIBUTE, VMManagerType.BARE_EC2.toString()));
	}


	@Override
	public void terminateVM(String id) {
		Preconditions.checkArgument(id != null && !id.isEmpty());

		if(!id.startsWith(ID_PREFIX)) {
			return;
		}
		AWSClients.EC2.terminateInstances(new TerminateInstancesRequest()
			.withInstanceIds(getEC2IDFromCustomID(id)));
	}


	@Override
	public List<VMStatus> listVMs() {
		List<VMStatus> stats = Lists.newLinkedList();
		String nextToken = null;
		do {
			DescribeInstancesResult result = AWSClients.EC2.describeInstances(new DescribeInstancesRequest()
				.withFilters(
						new Filter().withName(TAG_KEY_FILTER_NAME).withValues(Configuration.VMMANAGERTYPE_ATTRIBUTE),
						new Filter().withName(TAG_VALUE_FILTER_NAME).withValues(VMManagerType.BARE_EC2.toString()))
				.withNextToken(nextToken));
			stats.addAll(ImmutableList.copyOf(Iterables.transform(
					Iterables.concat(
							Lists.transform(result.getReservations(), new Function<Reservation, List<Instance>>() {
							@Override
							public List<Instance> apply(Reservation reservation) {
								return reservation.getInstances();
							}})),
					new Function<Instance, VMStatus>() {
						@Override
						public VMStatus apply(Instance instance) {
							return getVMStatusFromEC2Instance(instance);
						}})));
			nextToken = result.getNextToken();
		} while(nextToken != null);
		return stats;
	}


	private static Recipe deserializeRecipe(String json) {
		if(json == null) {
			return null;
		}
		Recipe recipe = null;
		try {
			recipe = JSON_MAPPER.readValue(json, Recipe.class);
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		return recipe;
	}


	private static VMStatus getVMStatusFromEC2Instance(Instance instance) {
		if(instance == null) {
			return null;
		}

		VMStatus status = new VMStatus();

		status.setId(getCustomIDFromEC2ID(instance.getInstanceId()));
		Map<String, String> attributes = Maps.newHashMap();
		for(Tag tag : instance.getTags()) {
			attributes.put(tag.getKey(), tag.getValue());
		}
		status.setAttributes(attributes);

		return status;
	}


	private static String getCustomIDFromEC2ID(String id) {
		if(id == null) {
			return null;
		}
		return id.replaceAll("i-", ID_PREFIX);
	}


	private static String getEC2IDFromCustomID(String id) {
		if(id == null) {
			return null;
		}
		return id.replaceAll(ID_PREFIX, "i-");
	}

}
