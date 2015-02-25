package com.endava.cloudpractice.instantvm.datamodel;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;


public class VMDefinition {

	private String name;
	private String description;
	private VMManagerType manager;
	private String recipe;

	
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

	public void setManager(VMManagerType builder) {
		Preconditions.checkArgument(builder != null);
		this.manager = builder;
	}

	public VMDefinition withManager(VMManagerType manager) {
		setManager(manager);
		return this;
	}

	public VMManagerType getManager() {
		return manager;
	}

	public void setRecipe(String recipe) {
		Preconditions.checkArgument(recipe != null && !recipe.isEmpty());
		this.recipe = recipe;
	}

	public VMDefinition withRecipe(String recipe) {
		setRecipe(recipe);
		return this;
	}

	public String getRecipe() {
		return recipe;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final VMDefinition other = (VMDefinition) obj;
		return
			Objects.equal(this.name, other.name) &&
			Objects.equal(this.description, other.description) &&
			Objects.equal(this.manager, other.manager) &&
			Objects.equal(this.recipe, other.recipe);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(
			name,
			description,
			manager,
			recipe);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.addValue(name)
			.addValue(description)
			.addValue(manager)
			.addValue(recipe)
			.toString();
	}

}
