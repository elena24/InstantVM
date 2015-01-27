package com.endava.cloudpractice.instantvm.repository.datamodel;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;


public class VMDefinition {

	private String name;
	private String description;

	public void setName(String name) {
		Preconditions.checkArgument(name != null && !name.isEmpty());
		this.name = name;
	}

	public VMDefinition withName(String name) {
		setName(name);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		Preconditions.checkArgument(description != null);
		this.description = description;
	}

	public VMDefinition withDescription(String description) {
		setDescription(description);
		return this;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final VMDefinition other = (VMDefinition) obj;
		return
			Objects.equal(this.name, other.name) &&
			Objects.equal(this.description, other.description);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(
			name,
			description);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.addValue(name)
			.addValue(description)
			.toString();
	}

}
