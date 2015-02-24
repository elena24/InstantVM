package com.endava.cloudpractice.instantvm.repository.impl.ddb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.endava.cloudpractice.instantvm.repository.VMDefinitionRepository;
import com.endava.cloudpractice.instantvm.datamodel.BuilderType;
import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;


public class DDBVMDefinitionRepositoryImpl implements VMDefinitionRepository {

	private static final String NAME = "Name";
	private static final String DESCRIPTION = "Description";
	private static final String BUILDER = "Builder";
	private static final String RECIPE = "Recipe";

	private final String table;


	public DDBVMDefinitionRepositoryImpl(String table) {
		Preconditions.checkArgument(table != null && !table.isEmpty());
		this.table = table;
	}


	@Override
	public VMDefinition getVMDefinition(String vmDefinitionName) {
		Preconditions.checkArgument(vmDefinitionName != null && !vmDefinitionName.isEmpty());

		GetItemResult result = AWSClients.DDB.getItem(new GetItemRequest().withTableName(table)
			.withKey(ImmutableMap.of(NAME, new AttributeValue().withS(vmDefinitionName))));
		return getVMDefinitionFromDDBItem(result.getItem());
	}


	@Override
	public void addVMDefinition(VMDefinition vmDefinition) {
		Preconditions.checkArgument(vmDefinition != null);

		AWSClients.DDB.putItem(new PutItemRequest().withTableName(table)
			.withItem(getDDBItemFromVMDefinition(vmDefinition)));
	}


	@Override
	public void removeVMDefinition(String vmDefinitionName) {
		Preconditions.checkArgument(vmDefinitionName != null && !vmDefinitionName.isEmpty());

		AWSClients.DDB.deleteItem(new DeleteItemRequest().withTableName(table)
			.withKey(ImmutableMap.of(NAME, new AttributeValue().withS(vmDefinitionName))));
	}


	@Override
	public List<VMDefinition> listVMDefinitions() {
		List<VMDefinition> defs = Lists.newLinkedList();
		Map<String, AttributeValue> startKey = null;
		do {
			ScanResult result = AWSClients.DDB.scan(new ScanRequest()
				.withTableName(table).withExclusiveStartKey(startKey));
			defs.addAll(Lists.transform(
				result.getItems(),
				new Function<Map<String, AttributeValue>, VMDefinition>() {
					@Override
					public VMDefinition apply(Map<String, AttributeValue> item) {
						return getVMDefinitionFromDDBItem(item);
					}
				}));
			startKey = result.getLastEvaluatedKey();
		} while(startKey != null);
		return defs;
	}


	private Map<String, AttributeValue> getDDBItemFromVMDefinition(VMDefinition def) {
		if(def == null) {
			return ImmutableMap.of();
		}

		return ImmutableMap.of(
			NAME, new AttributeValue().withS(def.getName()),
			DESCRIPTION, new AttributeValue().withS(def.getDescription()),
			BUILDER, new AttributeValue().withS(def.getBuilder().toString()),
			RECIPE, new AttributeValue().withS(def.getRecipe()));
	}


	private VMDefinition getVMDefinitionFromDDBItem(Map<String, AttributeValue> item) {
		if(item == null) {
			return null;
		}

		VMDefinition def = new VMDefinition();
		if(item.get(NAME) != null) {
			def.setName(item.get(NAME).getS());
		}
		if(item.get(DESCRIPTION) != null) {
			def.setDescription(item.get(DESCRIPTION).getS());
		}
		if(item.get(BUILDER) != null) {
			def.setBuilder(BuilderType.fromString(item.get(BUILDER).getS()));
		}
		if(item.get(RECIPE) != null) {
			def.setRecipe(item.get(RECIPE).getS());
		}
		return def;
	}

}
