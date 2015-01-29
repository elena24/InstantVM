package com.endava.cloudpractice.instantvm.repository.impl;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.endava.cloudpractice.instantvm.repository.VMDefRepository;
import com.endava.cloudpractice.instantvm.repository.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;


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
	public void writeVMDefinition(VMDefinition def) {
		Preconditions.checkArgument(def != null);

		String json = null;
		try {
			json = mapper.writeValueAsString(def);
		} catch(JsonProcessingException e) {
			Throwables.propagate(e);
		}

		AWSClients.DDB.putItem(new PutItemRequest().withTableName(table)
			.withItem(ImmutableMap.of(
				KEY, new AttributeValue().withS(def.getName()),
				VALUE, new AttributeValue().withS(json))));
	}

	@Override
	public VMDefinition readVMDefinition(String name) {
		Preconditions.checkArgument(name != null && !name.isEmpty());

		GetItemResult result = AWSClients.DDB.getItem(new GetItemRequest().withTableName(table)
			.withKey(ImmutableMap.of(KEY, new AttributeValue().withS(name))));
		String json = result.getItem().get(VALUE).getS();
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
