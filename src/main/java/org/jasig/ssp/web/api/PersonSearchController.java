/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.web.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.ssp.factory.PersonSearchRequestTOFactory;
import org.jasig.ssp.factory.PersonSearchResult2TOFactory;
import org.jasig.ssp.factory.PersonSearchResultTOFactory;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.PersonSearchRequest;
import org.jasig.ssp.model.PersonSearchResult;
import org.jasig.ssp.model.PersonSearchResult2;
import org.jasig.ssp.model.reference.ProgramStatus;
import org.jasig.ssp.security.permissions.Permission;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonSearchService;
import org.jasig.ssp.service.RequestTrustService;
import org.jasig.ssp.service.SecurityService;
import org.jasig.ssp.service.reference.ProgramStatusService;
import org.jasig.ssp.transferobject.PagedResponse;
import org.jasig.ssp.transferobject.PersonSearchResult2TO;
import org.jasig.ssp.transferobject.PersonSearchResultTO;
import org.jasig.ssp.transferobject.jsonserializer.DateOnlyFormatting;
import org.jasig.ssp.util.security.DynamicPermissionChecking;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortDirection;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import au.com.bytecode.opencsv.CSVWriter;

@Controller
@RequestMapping("/1/person")
public class PersonSearchController extends AbstractBaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PersonSearchController.class);

	@Autowired
	private transient PersonSearchService service;

	@Autowired
	private transient ProgramStatusService programStatusService;

	@Autowired
	private transient PersonSearchResultTOFactory factory;
	
	@Autowired
	private transient PersonSearchResult2TOFactory factory2;

	@Autowired
	private transient SecurityService securityService;

	@Autowired
	private transient RequestTrustService requestTrustService;
	
	@Autowired
	private transient PersonSearchRequestTOFactory personSearchRequestFactory;

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@DynamicPermissionChecking
	@RequestMapping(value="/search", method = RequestMethod.GET)
	public @ResponseBody
	PagedResponse<PersonSearchResultTO> search(
			final @RequestParam String searchTerm,
			final @RequestParam(required = false) UUID programStatusId,
			// ignored if programStatusId is non-null
			final @RequestParam(required = false) Boolean requireProgramStatus,
			final @RequestParam(required = false) Boolean outsideCaseload,
			final @RequestParam(required = false) ObjectStatus status,
			final @RequestParam(required = false) Integer start,
			final @RequestParam(required = false) Integer limit,
			final @RequestParam(required = false) String sort,
			final @RequestParam(required = false) String sortDirection,
			final HttpServletRequest request)
			throws ObjectNotFoundException, ValidationException {

		assertSearchApiAuthorization(request);

		ProgramStatus programStatus = null;
		if (null != programStatusId) {
			programStatus = programStatusService.get(programStatusId);
		}

		final PagingWrapper<PersonSearchResult> results = service.searchBy(
				programStatus, requireProgramStatus, outsideCaseload, searchTerm,
				securityService.currentUser().getPerson(),
				SortingAndPaging.createForSingleSortWithPaging(status, start, limit,
						sort, sortDirection, null));

		return new PagedResponse<PersonSearchResultTO>(true,
				results.getResults(), factory.asTOList(results.getRows()));
	}
	
    protected CSVWriter initCsvWriter(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response)
    throws IOException {
        return new CSVWriter(response.getWriter());
    }		
	@DynamicPermissionChecking
	@ResponseBody
	@RequestMapping(value="/students/search", method = RequestMethod.GET)
	PagedResponse<PersonSearchResult2TO>  search2(	
	 final @RequestParam(required = false) String studentId,
	 final @RequestParam(required = false) String programStatus,
	 final @RequestParam(required = false) String coachId,
	 final @RequestParam(required = false) String declaredMajor,
	 final @RequestParam(required = false) BigDecimal hoursEarnedMin,
	 final @RequestParam(required = false) BigDecimal hoursEarnedMax,
	 final @RequestParam(required = false) BigDecimal gpaEarnedMin,
	 final @RequestParam(required = false) BigDecimal gpaEarnedMax,
	 final @RequestParam(required = false) Boolean currentlyRegistered,
	 final @RequestParam(required = false) String earlyAlertResponseLate,
	 final @RequestParam(required = false) String sapStatusCode,
	 final @RequestParam(required = false) String specialServiceGroup,
	 // mapStatus has to be preserved for backward compat.
	 // in /directoryperson it's been replaced with planStatus
	 final @RequestParam(required = false) String mapStatus,

	 // planStatus semantics have to be preserved for backward compat.
	 // in /directoryperson it's different: planStatus means the
	 // same thing as mapStatus here and a new param - planExists -
	 // replaces planStatus
	 final @RequestParam(required = false) String planStatus,
	 final @RequestParam(required = false) Boolean myCaseload,
	 final @RequestParam(required = false)  Boolean myPlans,
	 final @RequestParam(required = false)  Boolean myWatchList,
	 final @RequestParam(required = false) @DateTimeFormat(pattern=DateOnlyFormatting.DEFAULT_DATE_PATTERN) Date birthDate,
     final @RequestParam(required = false) String actualStartTerm,
	 final @RequestParam(required = false) ObjectStatus status,
	 final @RequestParam(required = false) Integer start,
	 final @RequestParam(required = false) Integer limit,
	 final @RequestParam(required = false) String sort,
	 final @RequestParam(required = false) String sortDirection,
	 final HttpServletRequest request) throws ObjectNotFoundException
	 {
		assertSearchApiAuthorization(request);
		PersonSearchRequest form = personSearchRequestFactory.from(studentId,
				null, 
				null,
				programStatus,
				specialServiceGroup, 
				coachId,
				declaredMajor,
				hoursEarnedMin,
				hoursEarnedMax,
				gpaEarnedMin,
				gpaEarnedMax,
				currentlyRegistered,
				earlyAlertResponseLate,
				sapStatusCode,
				mapStatus,
				planStatus,
				myCaseload,
				myPlans,
				myWatchList,
				birthDate, 
                                actualStartTerm);
		
		
		String sortConfigured = sort == null ? "lastName":sort;
		if(sortConfigured.equals("coach")){
			sortConfigured = "coachFirstName";
		}
		SortingAndPaging sortAndPage = SortingAndPaging
		.createForSingleSortWithPaging(ObjectStatus.ALL, start, limit, sortConfigured,
				sortDirection, null);
		if(sortConfigured.equals("coachFirstName")){
			sortAndPage.prependSortField("coachLastName", SortDirection.valueOf(sortDirection));
		}
		
		form.setSortAndPage(sortAndPage);
		final PagingWrapper<PersonSearchResult2> models = service.search2(form);
		
		return new PagedResponse<PersonSearchResult2TO>(true,
				models.getResults(), factory2.asTOList(models.getRows()));	
	}
	
	@DynamicPermissionChecking
	@ResponseBody
	@RequestMapping(value="/directoryperson/search", method = RequestMethod.GET)
	PagedResponse<PersonSearchResult2TO>  personDirectorySearch(	
	 final @RequestParam(required = false) String schoolId,
	 final @RequestParam(required = false) String firstName,
	 final @RequestParam(required = false) String lastName,
	 final @RequestParam(required = false) String programStatus,
	 final @RequestParam(required = false) String coachId,
	 final @RequestParam(required = false) String declaredMajor,
	 final @RequestParam(required = false) BigDecimal hoursEarnedMin,
	 final @RequestParam(required = false) BigDecimal hoursEarnedMax,
	 final @RequestParam(required = false) BigDecimal gpaEarnedMin,
	 final @RequestParam(required = false) BigDecimal gpaEarnedMax,
	 final @RequestParam(required = false) Boolean currentlyRegistered,
	 final @RequestParam(required = false) String earlyAlertResponseLate,
	 final @RequestParam(required = false) String sapStatusCode,
	 final @RequestParam(required = false) String specialServiceGroup,
	 // Semantics of planStatus in /directoryperson changed w/r/t /students/search
	 // see notes in that handler
	 final @RequestParam(required = false) String planStatus,
	 // planExists here is essentially a rename of of planStatus /students/search
	 // see notes in that handler
	 final @RequestParam(required = false) String planExists,
	 final @RequestParam(required = false) Boolean myCaseload,
	 final @RequestParam(required = false) Boolean myPlans,
	 final @RequestParam(required = false) Boolean myWatchList,
	 final @RequestParam(required = false) @DateTimeFormat(pattern=DateOnlyFormatting.DEFAULT_DATE_PATTERN) Date birthDate,
     final @RequestParam(required = false) String actualStartTerm,
	 final @RequestParam(required = false) String personTableType,
	 final @RequestParam(required = false) Integer start,
	 final @RequestParam(required = false) Integer limit,
	 final @RequestParam(required = false) String sort,
	 final @RequestParam(required = false) String sortDirection,
	 final HttpServletRequest request) throws ObjectNotFoundException
	 {
		assertSearchApiAuthorization(request);
		SortingAndPaging sortAndPage = buildSortAndPage( limit,  start,  sort,  sortDirection);
		final PagingWrapper<PersonSearchResult2> models = service.searchPersonDirectory(personSearchRequestFactory.from(schoolId,
				firstName, lastName, 
				programStatus,specialServiceGroup, 
				coachId,declaredMajor,
				hoursEarnedMin,hoursEarnedMax,
				gpaEarnedMin,gpaEarnedMax,
				currentlyRegistered,earlyAlertResponseLate,
				sapStatusCode,
				planStatus,planExists,
				myCaseload,myPlans,myWatchList, birthDate, actualStartTerm, personTableType, sortAndPage));
		return new PagedResponse<PersonSearchResult2TO>(true,
				models.getResults(), factory2.asTOList(models.getRows()));	
	}
	@DynamicPermissionChecking
	@ResponseBody
	@RequestMapping(value="/directoryperson/search/count", method = RequestMethod.GET)
	Long  personDirectorySearchCount(	
	 final @RequestParam(required = false) String schoolId,
	 final @RequestParam(required = false) String firstName,
	 final @RequestParam(required = false) String lastName,
	 final @RequestParam(required = false) String programStatus,
	 final @RequestParam(required = false) String coachId,
	 final @RequestParam(required = false) String declaredMajor,
	 final @RequestParam(required = false) BigDecimal hoursEarnedMin,
	 final @RequestParam(required = false) BigDecimal hoursEarnedMax,
	 final @RequestParam(required = false) BigDecimal gpaEarnedMin,
	 final @RequestParam(required = false) BigDecimal gpaEarnedMax,
	 final @RequestParam(required = false) Boolean currentlyRegistered,
	 final @RequestParam(required = false) String earlyAlertResponseLate,
	 final @RequestParam(required = false) String sapStatusCode,
	 final @RequestParam(required = false) String specialServiceGroup,
	 // Semantics of planStatus in /directoryperson changed w/r/t /students/search
	 // see notes in that handler
	 final @RequestParam(required = false) String planStatus,
	 // planExists here is essentially a rename of of planStatus /students/search
	 // see notes in that handler
	 final @RequestParam(required = false) String planExists,
	 final @RequestParam(required = false) Boolean myCaseload,
	 final @RequestParam(required = false) Boolean myPlans,
	 final @RequestParam(required = false) Boolean myWatchList,
	 final @RequestParam(required = false) @DateTimeFormat(pattern=DateOnlyFormatting.DEFAULT_DATE_PATTERN) Date birthDate,
     final @RequestParam(required = false) String actualStartTerm,
	 final @RequestParam(required = false) String personTableType,
	 final @RequestParam(required = false) Integer start,
	 final @RequestParam(required = false) Integer limit,
	 final @RequestParam(required = false) String sort,
	 final @RequestParam(required = false) String sortDirection,
	 final HttpServletRequest request) throws ObjectNotFoundException
	 {
		assertSearchApiAuthorization(request);
		SortingAndPaging sortAndPage = buildSortAndPage( limit,  start,  sort,  sortDirection);
		return  service.searchPersonDirectoryCount(personSearchRequestFactory.from(schoolId,
				firstName, lastName,
				programStatus,specialServiceGroup,
				coachId,declaredMajor,
				hoursEarnedMin,hoursEarnedMax,
				gpaEarnedMin,gpaEarnedMax,
				currentlyRegistered,earlyAlertResponseLate,
				sapStatusCode,
				planStatus,planExists,
				myCaseload,myPlans,myWatchList, birthDate, actualStartTerm, personTableType, sortAndPage));
	}
		
	private SortingAndPaging buildSortAndPage(Integer limit, Integer start, String sort, String sortDirection){
		String sortConfigured = sort == null ? "dp.lastName":"dp."+ sort;
		if(sortConfigured.equals("dp.coach")){
			sortConfigured = "dp.coachLastName";
		}else if(sortConfigured.equals("dp.currentProgramStatusName")){
			sortConfigured = "dp.programStatusName";
		}else if(sortConfigured.equals("dp.numberOfEarlyAlerts")){
			sortConfigured = "dp.activeAlertsCount";
		}else if(sortConfigured.equals("dp.studentType")){
			sortConfigured = "dp.studentTypeName";
		}
		SortingAndPaging sortAndPage = SortingAndPaging
		.createForSingleSortWithPaging(ObjectStatus.ALL, start, limit, sortConfigured,
				sortDirection, null);
		if(sortConfigured.equals("dp.coachLastName")){
			sortAndPage.prependSortField("dp.coachFirstName", SortDirection.getSortDirection(sortDirection));
		}
		return sortAndPage;
	}

	private void assertSearchApiAuthorization(HttpServletRequest request)
			throws AccessDeniedException {
		if ( securityService.hasAuthority(Permission.PERSON_READ) ||
				securityService.hasAuthority("ROLE_PERSON_SEARCH_READ")) {
			return;
		}
		try {
			requestTrustService.assertHighlyTrustedRequest(request);
		} catch ( AccessDeniedException e ) {
			throw new AccessDeniedException("Untrusted request with"
					+ " insufficient permissions.", e);
		}
	}
}
