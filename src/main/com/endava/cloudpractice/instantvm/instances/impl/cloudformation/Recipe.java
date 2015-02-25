package com.endava.cloudpractice.instantvm.instances.impl.cloudformation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;


class Recipe {

	private String template;

	
	public void setTemplate(String template) {
		Preconditions.checkArgument(template != null && !template.isEmpty());
		this.template = template;
	}

	public Recipe withTemplate(String template) {
		setTemplate(template);
		return this;
	}

	public String getTemplate() {
		return template;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Recipe other = (Recipe) obj;
		return
			Objects.equal(this.template, other.template);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(
			template);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.addValue(template)
			.toString();
	}

}
