package org.jasig.ssp.dao;

import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.reference.ProgramStatus;
import org.jasig.ssp.service.reference.ConfigService;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

/**
 * PersonSearch DAO
 */
@Repository
public class PersonSearchDao extends AbstractDao<Person> {

	public PersonSearchDao() {
		super(Person.class);
	}

	@Autowired
	private transient ConfigService configService;

	/**
	 * Search people by the specified terms.
	 * 
	 * @param programStatus
	 *            program status filter
	 * @param outsideCaseload
	 *            false allows searches without checking the Coach (advisor)
	 *            property; defaults to true
	 * @param searchTerm
	 *            Search term that search first and last name and school ID;
	 *            required
	 * @param advisor
	 *            required if outsideCaseload is not {@link Boolean#FALSE}.
	 * @param sAndP
	 *            Sorting and paging parameters
	 * @return List of people that match the specified filters
	 */
	public PagingWrapper<Person> searchBy(
			@NotNull final ProgramStatus programStatus,
			final Boolean outsideCaseload, @NotNull final String searchTerm,
			final Person advisor, final SortingAndPaging sAndP) {

		if (!StringUtils.isNotBlank(searchTerm)) {
			throw new IllegalArgumentException("search term must be specified");
		}
		if (programStatus == null) {
			throw new IllegalArgumentException(
					"program status must be specified");
		}

		final Criteria query = createCriteria();

		query.createAlias("programStatuses", "personProgramStatus")
				.add(Restrictions.eq("personProgramStatus.programStatus",
						programStatus));

		if (Boolean.FALSE.equals(outsideCaseload)) {
			query.add(Restrictions.eq("coach", advisor));
		}

		// searchTerm : Can be firstName, lastName, studentId or firstName + ' '
		// + lastName
		final Disjunction terms = Restrictions.disjunction();

		final String searchTermLowercase = searchTerm.toLowerCase(Locale
				.getDefault());
		terms.add(Restrictions.ilike("firstName", searchTermLowercase,
				MatchMode.ANYWHERE));
		terms.add(Restrictions.ilike("lastName", searchTermLowercase,
				MatchMode.ANYWHERE));
		terms.add(Restrictions.ilike("schoolId", searchTermLowercase,
				MatchMode.ANYWHERE));

		terms.add(Restrictions
				.sqlRestriction(
						"lower({alias}.first_name) "
								+ configService.getDatabaseConcatOperator()
								+ " ' ' "
								+ configService.getDatabaseConcatOperator()
								+ " lower({alias}.last_name) like ? ",
						searchTermLowercase, new StringType()));

		query.add(terms);

		// eager load program status
		query.setFetchMode("personProgramStatus", FetchMode.JOIN);
		query.setFetchMode("personProgramStatus.programStatus", FetchMode.JOIN);

		final PagingWrapper<Object[]> results = processCriteriaWithSortingAndPaging(
				query, sAndP, true);

		final List<Person> people = Lists.newArrayList();
		for (Object[] personAndProgramStatus : results) {
			if ((personAndProgramStatus != null)
					&& (personAndProgramStatus.length > 0)) {
				if (personAndProgramStatus[0] instanceof Person) {
					people.add((Person) personAndProgramStatus[0]);
				} else if (personAndProgramStatus[1] instanceof Person) {
					people.add((Person) personAndProgramStatus[1]);
				}
			}
		}

		return new PagingWrapper<Person>(results.getResults(), people);
	}
}