package org.studentsuccessplan.ssp.transferobject.reference;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.studentsuccessplan.ssp.model.reference.AbstractReference;
import org.studentsuccessplan.ssp.transferobject.AuditableTO;
import org.studentsuccessplan.ssp.transferobject.NamedTO;

public abstract class AbstractReferenceTO<T extends AbstractReference>
		extends AuditableTO<T> implements NamedTO {

	@NotNull
	@NotEmpty
	private String name;

	private String description;

	public AbstractReferenceTO() {
		super();
	}

	public AbstractReferenceTO(final UUID id) {
		super(id);
	}

	public AbstractReferenceTO(final UUID id, final String name) {
		super(id);
		this.name = name;
	}

	public AbstractReferenceTO(final UUID id, final String name,
			final String description) {
		super(id);
		this.name = name;
		this.description = description;
	}

	@Override
	public void fromModel(final T model) {
		super.fromModel(model);

		name = model.getName();
		description = model.getDescription();
	}

	@Override
	public T addToModel(final T model) {
		super.addToModel(model);

		model.setName(getName());
		model.setDescription(getDescription());

		return model;
	}

	@Override
	public abstract T asModel();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

}
