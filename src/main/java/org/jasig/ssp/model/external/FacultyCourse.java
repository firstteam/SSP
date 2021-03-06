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
package org.jasig.ssp.model.external;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * FacultyCourse external data object.
 */
@Entity
@Immutable
@Table(name = "v_external_faculty_course")
public class FacultyCourse extends AbstractExternalData implements
		Serializable, ExternalData {

	private static final long serialVersionUID = 5820559696467468171L;

	@Column(nullable = false, length = 50)
	@NotNull
	@NotEmpty
	@Size(max = 50)
	private String facultySchoolId;

	@Column(nullable = false, length = 25)
	@NotNull
	@NotEmpty
	@Size(max = 25)
	private String termCode;

	@Column(nullable = false, length = 35)
	@NotNull
	@NotEmpty
	@Size(max = 35)
	private String formattedCourse;

	@Column(nullable = false, length = 100)
	@NotNull
	@NotEmpty
	@Size(max = 100)
	private String title;
	
	@Column(nullable = false, length = 128)
	@NotNull
	@NotEmpty
	@Size(max = 128)
	private String sectionCode;
	
	@Column(nullable = false, length = 10)
	@NotNull
	@NotEmpty
	@Size(max = 10)
	private String sectionNumber;

	public String getFacultySchoolId() {
		return facultySchoolId;
	}

	/**
	 * @param facultySchoolId
	 *            the facultySchoolId
	 */
	public void setFacultySchoolId(final String facultySchoolId) {
		this.facultySchoolId = facultySchoolId;
	}

	public String getTermCode() {
		return termCode;
	}

	/**
	 * @param termCode
	 *            the termCode
	 */
	public void setTermCode(final String termCode) {
		this.termCode = termCode;
	}

	public String getFormattedCourse() {
		return formattedCourse;
	}

	/**
	 * @param formattedCourse
	 *            the formattedCourse
	 */
	public void setFormattedCourse(final String formattedCourse) {
		this.formattedCourse = formattedCourse;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @return the sectionCode
	 */
	public String getSectionCode() {
		return sectionCode;
	}

	/**
	 * @param sectionCode the sectionCode to set
	 */
	public void setSectionCode(final String sectionCode) {
		this.sectionCode = sectionCode;
	}

	/**
	 * @return the sectionNumber
	 */
	public String getSectionNumber() {
		return sectionNumber;
	}

	/**
	 * @param sectionNumber the sectionNumber to set
	 */
	public void setSectionNumber(final String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}
}