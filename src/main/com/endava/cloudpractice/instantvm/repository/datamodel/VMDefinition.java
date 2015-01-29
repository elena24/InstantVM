package com.endava.cloudpractice.instantvm.repository.datamodel;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;


public class VMDefinition {

	private String name;
	private String description;
	private String type;
	private String image;

	
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

	public void setType(String type) {
		Preconditions.checkArgument(type != null && !type.isEmpty());
		this.type = type;
	}

	public VMDefinition withType(String type) {
		setType(type);
		return this;
	}

	public String getType() {
		return type;
	}

	public void setImage(String image) {
		Preconditions.checkArgument(image != null && !image.isEmpty());
		this.image = image;
	}

	public VMDefinition withImage(String image) {
		setImage(image);
		return this;
	}

	public String getImage() {
		return image;
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
			Objects.equal(this.type, other.type) &&
			Objects.equal(this.image, other.image);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(
			name,
			description,
			type,
			image);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.addValue(name)
			.addValue(description)
			.addValue(type)
			.addValue(image)
			.toString();
	}

}
