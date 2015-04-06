package com.endava.cloudpractice.instantvm.datamodel;

import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;


public class VMStatus {
	
	String id;
	Map<String, String> attributes = ImmutableMap.of();


	public void setId(String id) {
		Preconditions.checkArgument(id != null && !id.isEmpty());
		this.id = id;
	}

	public VMStatus withId(String id) {
		setId(id);
		return this;
	}

	public String getId() {
		return id;
	}

	public void setAttributes(Map<String, String> attributes) {
		Preconditions.checkArgument(attributes != null);
		this.attributes = ImmutableMap.copyOf(attributes);
	}

	public VMStatus withAttributes(Map<String, String> attributes) {
		setAttributes(attributes);
		return this;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final VMStatus other = (VMStatus) obj;
		return
			Objects.equal(this.id, other.id) &&
			Objects.equal(this.attributes, other.attributes);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(
			id,
			attributes);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.addValue(id)
			.addValue(attributes)
			.toString();
	}

}