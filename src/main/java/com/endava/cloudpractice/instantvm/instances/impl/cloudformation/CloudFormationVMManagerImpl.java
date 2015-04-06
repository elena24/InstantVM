package com.endava.cloudpractice.instantvm.instances.impl.cloudformation;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.cloudformation.model.CreateStackRequest;
import com.amazonaws.services.cloudformation.model.DeleteStackRequest;
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest;
import com.amazonaws.services.cloudformation.model.DescribeStacksResult;
import com.amazonaws.services.cloudformation.model.Stack;
import com.amazonaws.services.cloudformation.model.Tag;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class CloudFormationVMManagerImpl implements VMManager {

	private static final String ID_PREFIX = "cfm-";

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();


	@Override
	public VMStatus launchVM(VMDefinition def) {
		Preconditions.checkArgument(def != null);
		Preconditions.checkArgument(def.getRecipe() != null);

		Recipe recipe = deserializeRecipe(def.getRecipe());

		String id = ID_PREFIX + UUID.randomUUID().toString().replace("-", "");
		CreateStackRequest request = new CreateStackRequest()
			.withStackName(id)
			.withTemplateURL(recipe.getTemplate())
			.withTags(ImmutableList.of(
					new Tag().withKey(Configuration.VMDEFNAME_ATTRIBUTE).withValue(def.getName()),
					new Tag().withKey(Configuration.VMMANAGERTYPE_ATTRIBUTE).withValue(VMManagerType.CLOUDFORMATION.toString())));
		AWSClients.CLOUDFORMATION.createStack(request);

		return new VMStatus()
			.withId(id)
			.withAttributes(ImmutableMap.of(
					Configuration.VMDEFNAME_ATTRIBUTE, def.getName(),
					Configuration.VMMANAGERTYPE_ATTRIBUTE, VMManagerType.CLOUDFORMATION.toString()));
	}


	@Override
	public void terminateVM(String id) {
		Preconditions.checkArgument(id != null && !id.isEmpty());

		if(!id.startsWith(ID_PREFIX)) {
			return;
		}
		AWSClients.CLOUDFORMATION.deleteStack(new DeleteStackRequest()
			.withStackName(id));
	}


	@Override
	public List<VMStatus> listVMs() {
		List<VMStatus> stats = Lists.newLinkedList();
		String nextToken = null;
		do {
			DescribeStacksResult result = AWSClients.CLOUDFORMATION.describeStacks(new DescribeStacksRequest()
				.withNextToken(nextToken));
			stats.addAll(Lists.transform(result.getStacks(), new Function<Stack, VMStatus>() {
				@Override
				public VMStatus apply(Stack stack) {
					return getVMStatusFromCloudFormationStack(stack);
				}}));
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


	private static VMStatus getVMStatusFromCloudFormationStack(Stack stack) {
		if(stack == null) {
			return null;
		}
		
		VMStatus status = new VMStatus();
		
		status.setId(stack.getStackName());
		Map<String, String> attributes = Maps.newHashMap();
		for(Tag tag : stack.getTags()) {
			attributes.put(tag.getKey(), tag.getValue());
		}
		status.setAttributes(attributes);

		return status;
	}

}
