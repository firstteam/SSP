package org.jasig.ssp.service.reference;

import java.util.UUID;

import org.jasig.ssp.model.reference.EducationGoal;
import org.jasig.ssp.service.AuditableCrudService;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.jasig.ssp.web.api.validation.ValidationException;

/**
 * EducationGoal service
 */
public interface EducationGoalService extends
		AuditableCrudService<EducationGoal> {

	@Override
	PagingWrapper<EducationGoal> getAll(SortingAndPaging sAndP);

	@Override
	EducationGoal get(UUID id) throws ObjectNotFoundException;

	@Override
	EducationGoal create(EducationGoal obj) throws ObjectNotFoundException,
			ValidationException;

	@Override
	EducationGoal save(EducationGoal obj) throws ObjectNotFoundException,
			ValidationException;

	@Override
	void delete(UUID id) throws ObjectNotFoundException;
}