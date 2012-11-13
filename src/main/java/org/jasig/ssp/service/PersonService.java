/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.reference.SpecialServiceGroup;
import org.jasig.ssp.security.exception.UnableToCreateAccountException;
import org.jasig.ssp.service.tool.IntakeService;
import org.jasig.ssp.transferobject.reports.AddressLabelSearchTO;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.springframework.security.core.GrantedAuthority;

import javax.portlet.PortletRequest;

/**
 * Person service
 */
public interface PersonService extends AuditableCrudService<Person> {

	@Override
	PagingWrapper<Person> getAll(SortingAndPaging sAndP);

	/**
	 * Retrieves the specified Person.
	 * 
	 * @param id
	 *            Required identifier for the Person to retrieve. Can not be
	 *            null.
	 * @exception ObjectNotFoundException
	 *                If the supplied identifier does not exist in the database.
	 * @return The specified Person instance.
	 */
	@Override
	Person get(UUID id) throws ObjectNotFoundException;

	Person personFromUsername(String username) throws ObjectNotFoundException;

	/**
	 * Creates a new Person instance based on the supplied model.
	 * 
	 * @param obj
	 *            Model instance
	 */
	@Override
	Person create(Person obj);

	/**
	 * Updates values of direct Person properties, but not any associated
	 * children or collections.
	 * 
	 * WARNING: Copies system-only (based on business logic rules) properties,
	 * so ensure that the incoming values have already been sanitized.
	 * 
	 * @param obj
	 *            Model instance from which to copy the simple properties.
	 * @see IntakeService
	 */
	@Override
	Person save(Person obj) throws ObjectNotFoundException;

	/**
	 * Mark a Person as deleted.
	 * 
	 * Does not remove them from persistent storage, but marks their status flag
	 * to {@link ObjectStatus#INACTIVE}.
	 */
	@Override
	void delete(UUID id) throws ObjectNotFoundException;

	/**
	 * Return a person object for every personId where available.
	 * 
	 * @param personIds
	 * @param sAndP
	 * @return A person object for every personId where available
	 */
	List<Person> peopleFromListOfIds(List<UUID> personIds,
			SortingAndPaging sAndP);

	/**
	 * Retrieves the specified Person by their Student ID (school_id).
	 * 
	 * @param studentId
	 *            Required school identifier for the Person to retrieve. Can not
	 *            be null.
	 *            Also searches the External Database for the identifier,
	 *            creating a Person if an ExternalPerson record exists..
	 * @exception ObjectNotFoundException
	 *                If the supplied identifier does not exist in the database.
	 * @return The specified Person instance.
	 */
	Person getBySchoolId(String studentId) throws ObjectNotFoundException;

	/**
	 * Gets a list of {@link Person} objects based on specified criteria
	 * 
	 * @param addressLabelSearchTO
	 *            criteria
	 * @param sAndP
	 *            Sorting and paging parameters
	 * @return List of person objects based on specified criteria
	 * @throws ObjectNotFoundException
	 */
	List<Person> peopleFromCriteria(AddressLabelSearchTO addressLabelSearchTO,
			final SortingAndPaging sAndP) throws ObjectNotFoundException;

	/**
	 * Gets a list of {@link Person} objects based on the specified criteria and
	 * {@link SpecialServiceGroup} identifiers.
	 * 
	 * @param specialServiceGroupIDs
	 *            list of {@link SpecialServiceGroup} service group identifiers
	 * @param createForSingleSort
	 * @return A list of {@link Person} objects based on the specified criteria
	 *         and special service groups.
	 * @throws ObjectNotFoundException
	 *             If any of the special service groups could not be found.
	 */
	List<Person> peopleFromSpecialServiceGroups(
			List<UUID> specialServiceGroupIDs,
			SortingAndPaging createForSingleSort)
			throws ObjectNotFoundException;

	/**
	 * Get a list of all Coaches
	 * 
	 * @param sAndP
	 *            Sorting and paging parameters
	 * @return List of all coaches
	 */
	PagingWrapper<Person> getAllCoaches(SortingAndPaging sAndP);

	/**
	 * Gets all coaches assigned to local Person records regardless of current
	 * permissions. This is as opposed to
	 * {@link #getAllCoaches(org.jasig.ssp.util.sort.SortingAndPaging)} which
	 * is intended to just return "official" coaches, i.e. users known to
	 * act as coaches, regardless of whether they have any assignments at all.
	 *
	 * @param sAndP
	 *            Sorting and paging parameters
	 * @return List of all <em>assigned</em> coaches
	 */
	PagingWrapper<Person> getAllAssignedCoaches(SortingAndPaging sAndP);

	Person load(UUID id);

	Person createUserAccount(String username,
			Collection<GrantedAuthority> authorities);

	void setPersonAttributesService(
			final PersonAttributesService personAttributesService);

	/**
	 * Attempt to create a new user account using JSR-168/286
	 * portlet user attributes from the given request.
	 *
	 *
	 * @param  username the login under which to create the account. Potentially
	 *                  null or empty, in which case the implementation can
	 *                  attempt to determine the username on its own, probably
	 *                  from user attributes on <code>portletRequest</code>, but
	 *                  under current usage the caller typically expects a
	 *                  certain username for the new account, so it is exposed
	 *                  as a param here.
	 * @param portletRequest current portlet request (note that associated
	 *                       user attributes map is potentially null or empty)
	 * @return the created Person, never null
	 * @throws UnableToCreateAccountException typically caused by the presence
	 *   of an existing account with conflicting keys or missing required
	 *   user attributes on <code>portletRequest</code>
	 */
	Person createUserAccountForCurrentPortletUser(String username,
			PortletRequest portletRequest)
			throws UnableToCreateAccountException;


}