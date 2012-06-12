package org.jasig.ssp.service.reference;

import java.util.UUID;

import org.jasig.ssp.model.reference.EducationLevel;
import org.jasig.ssp.service.AuditableCrudService;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.jasig.ssp.web.api.validation.ValidationException;

/**
 * EducationLevel service
 */
public interface EducationLevelService extends
		AuditableCrudService<EducationLevel> {

	@Override
	PagingWrapper<EducationLevel> getAll(SortingAndPaging sAndP);

	@Override
	EducationLevel get(UUID id) throws ObjectNotFoundException;

	@Override
	EducationLevel create(EducationLevel obj) throws ObjectNotFoundException,
			ValidationException;

	@Override
	EducationLevel save(EducationLevel obj) throws ObjectNotFoundException,
			ValidationException;

	@Override
	void delete(UUID id) throws ObjectNotFoundException;
}