package org.jasig.ssp.web.api.reports;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.jasig.ssp.model.external.Term;
import org.jasig.ssp.model.reference.Campus;
import org.jasig.ssp.security.permissions.Permission;
import org.jasig.ssp.service.EarlyAlertResponseService;
import org.jasig.ssp.service.EarlyAlertService;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.external.ExternalPersonService;
import org.jasig.ssp.service.external.RegistrationStatusByTermService;
import org.jasig.ssp.service.external.TermService;
import org.jasig.ssp.service.reference.CampusService;
import org.jasig.ssp.transferobject.reports.EarlyAlertTermCaseCountsTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/1/report/map")
public class MapReportsController extends ReportBaseController {
	private static String REPORT_URL_NUMBER_COURSES_IN_PLAN = "/reports/numberCoursesInPlan.jasper";
	private static String REPORT_FILE_TITLE_NUMBER_COURSES_IN_PLAN = "NUMBER_OF_COURSES_IN_PLAN";
	
	private static String REPORT_URL_NUMBER_PLANS_BY_ADVISOR = "/reports/numberPlansByAdvisor.jasper";
	private static String REPORT_FILE_TITLE_NUMBER_PLANS_BY_ADVISOR = "NUMBER_OF_PLANS_BY_ADVISOR";
	
	private static String REPORT_URL_NUMBER_STUDENTS_BY_STATUS = "/reports/numberStudentsByStatus.jasper";
	private static String REPORT_FILE_TITLE_NUMBER_STUDENTS_BY_STATUS = "NUMBER_OF_STUDENTS_BY_STATUS";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CaseloadActivityReportController.class);
	
	@Autowired
	protected transient TermService termService;
	

	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT,
				Locale.US);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}
	
	
	@RequestMapping(value = "/numbercourses", method = RequestMethod.GET)
	@PreAuthorize(Permission.SECURITY_REPORT_READ)
	public @ResponseBody
	void getNumberOfPlansByCourse(
			final HttpServletResponse response,		
			final @RequestParam(required = false) String courseNumber,
			final @RequestParam(required = false) String subjectAbbreviation,
			final @RequestParam(required = false) String termCode,			
			final @RequestParam(required = false, defaultValue = DEFAULT_REPORT_TYPE) String reportType)
			throws ObjectNotFoundException, JRException, IOException {
		
	
		List<Term> terms = SearchParameters.getTerms(Lists.newArrayList(termCode), termService);
		final Map<String, Object> parameters = Maps.newHashMap();
		
		SearchParameters.addTermsToMap( terms, parameters);
		generateReport( response,  parameters, null,  REPORT_URL_NUMBER_COURSES_IN_PLAN, 
				 reportType, REPORT_FILE_TITLE_NUMBER_COURSES_IN_PLAN);
	}
	
	@RequestMapping(value = "/numberplansbyadvisor", method = RequestMethod.GET)
	@PreAuthorize(Permission.SECURITY_REPORT_READ)
	public @ResponseBody
	void getNumberOfPlansByAdvisor(
			final HttpServletResponse response,		
			final @RequestParam(required = false) Date createDateFrom,
			final @RequestParam(required = false) Date createDateTo,
			final @RequestParam(required = false) String termCode,			
			final @RequestParam(required = false, defaultValue = DEFAULT_REPORT_TYPE) String reportType)
			throws ObjectNotFoundException, JRException, IOException {
		
	
				
		List<Term> terms = SearchParameters.getTerms(Lists.newArrayList(termCode), termService);
		final Map<String, Object> parameters = Maps.newHashMap();
		
		SearchParameters.addTermsToMap( terms, parameters);
		
		generateReport( response,  parameters, null,  REPORT_URL_NUMBER_PLANS_BY_ADVISOR, 
				 reportType, REPORT_FILE_TITLE_NUMBER_PLANS_BY_ADVISOR);
	}
	
	@RequestMapping(value = "/numberstudentsbystatus", method = RequestMethod.GET)
	@PreAuthorize(Permission.SECURITY_REPORT_READ)
	public @ResponseBody
	void getNumberOfStudentsByStatus(
			final HttpServletResponse response,		
			final @RequestParam(required = false) String courseNumber,
			final @RequestParam(required = false) String subjectAbbreviation,
			final @RequestParam(required = false) String planStatus,
			final @RequestParam(required = false) String termCode,			
			final @RequestParam(required = false, defaultValue = DEFAULT_REPORT_TYPE) String reportType)
			throws ObjectNotFoundException, JRException, IOException {
		
	
		List<Term> terms = SearchParameters.getTerms(Lists.newArrayList(termCode), termService);
		final Map<String, Object> parameters = Maps.newHashMap();
		
		SearchParameters.addTermsToMap(terms , parameters);
		generateReport( response,  parameters, null,  REPORT_URL_NUMBER_STUDENTS_BY_STATUS, 
				 reportType, REPORT_FILE_TITLE_NUMBER_STUDENTS_BY_STATUS);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}