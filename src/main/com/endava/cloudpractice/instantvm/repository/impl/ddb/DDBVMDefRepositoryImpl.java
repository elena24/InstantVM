package com.endava.cloudpractice.instantvm.repository.impl.ddb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.endava.cloudpractice.instantvm.repository.VMDefRepository;
import com.endava.cloudpractice.instantvm.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class DDBVMDefRepositoryImpl implements VMDefRepository {

	private static final String KEY = "Name";
	private static final String VALUE = "Data";

	private final String table;
	private final ObjectMapper mapper = new ObjectMapper();


	public DDBVMDefRepositoryImpl(String table) {
		Preconditions.checkArgument(table != null && !table.isEmpty());
		this.table = table;
	}


	@Override
	public VMDefinition readVMDefinition(String name) {
		Preconditions.checkArgument(name != null && !name.isEmpty());

		GetItemResult result = AWSClients.DDB.getItem(new GetItemRequest().withTableName(table)
			.withKey(ImmutableMap.of(KEY, new AttributeValue().withS(name))));
		return getVMDefinitionFromDDBItem(result.getItem());
	}


	@Override
	public void writeVMDefinition(VMDefinition def) {
		Preconditions.checkArgument(def != null);

		AWSClients.DDB.putItem(new PutItemRequest().withTableName(table)
			.withItem(getDDBItemFromVMDefinition(def)));
	}


	@Override
	public void deleteVMDefinition(String name) {
		Preconditions.checkArgument(name != null && !name.isEmpty());

		AWSClients.DDB.deleteItem(new DeleteItemRequest().withTableName(table)
			.withKey(ImmutableMap.of(KEY, new AttributeValue().withS(name))));
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

		String json = null;
		try {
			json = mapper.writeValueAsString(def);
		} catch(JsonProcessingException e) {
			Throwables.propagate(e);
		}

		return ImmutableMap.of(
			KEY, new AttributeValue().withS(def.getName()),
			VALUE, new AttributeValue().withS(json));
	}


	private VMDefinition getVMDefinitionFromDDBItem(Map<String, AttributeValue> item) {
		if(item == null) {
			return null;
		}

		String json = item.get(VALUE).getS();
		if (json == null) {
			return null;
		}

		VMDefinition def = null;
		try {
			def = mapper.readValue(json, VMDefinition.class);
		} catch(IOException e) {
			Throwables.propagate(e);
		}
		return def;
	}

}
