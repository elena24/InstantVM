package com.endava.cloudpractice.instantvm.repository.impl.ddb;

import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.endava.cloudpractice.instantvm.repository.VMDefRepository;
import com.endava.cloudpractice.instantvm.repository.datamodel.VMDefinition;
import com.endava.cloudpractice.instantvm.util.AWSClients;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;


public class DDBVMDefRepositoryImpl implements VMDefRepository {

	private final String table;
	private final ObjectMapper mapper = new ObjectMapper();

	public DDBVMDefRepositoryImpl(String table) {
		Preconditions.checkArgument(table != null && !table.isEmpty());
		this.table = table;
	}

	@Override
	public void writeVMDefinition(VMDefinition def) {
		Preconditions.checkArgument(def != null);
		try {
			String json = mapper.writeValueAsString(def);
			AWSClients.DDB.putItem(new PutItemRequest().withTableName(table).withItem(
				ImmutableMap.of()));
		} catch(JsonProcessingException e) {
			Throwables.propagate(e);
		}
	}

	@Override
	public VMDefinition readVMDefinition(String name) {
		Preconditions.checkArgument(name != null && !name.isEmpty());
		return null;
	}

}
