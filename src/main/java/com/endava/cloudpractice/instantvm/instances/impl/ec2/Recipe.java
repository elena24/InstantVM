package com.endava.cloudpractice.instantvm.instances.impl.ec2;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;


class Recipe {

	private String type;
	private String image;

	
	public void setType(String type) {
		Preconditions.checkArgument(type != null && !type.isEmpty());
		this.type = type;
	}

	public Recipe withType(String type) {
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

	public Recipe withImage(String image) {
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
		final Recipe other = (Recipe) obj;
		return
			Objects.equal(this.type, other.type) &&
			Objects.equal(this.image, other.image);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(
			type,
			image);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.addValue(type)
			.addValue(image)
			.toString();
	}

}
