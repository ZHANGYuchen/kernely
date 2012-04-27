package org.kernely.timesheet.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kernely.controller.AbstractController;
import org.kernely.core.service.UserService;
import org.kernely.template.SobaTemplateRenderer;
import org.kernely.timesheet.dto.TimeSheetCalendarDTO;
import org.kernely.timesheet.dto.TimeSheetDayDTO;
import org.kernely.timesheet.dto.TimeSheetDetailDTO;
import org.kernely.timesheet.dto.TimeSheetMonthDTO;
import org.kernely.timesheet.service.TimeSheetService;

import com.google.inject.Inject;

/**
 * Main controller for timesheet
 */
@Path("/timesheet")
public class TimeSheetController extends AbstractController {
	@Inject
	private SobaTemplateRenderer templateRenderer;

	@Inject
	private UserService userService;

	@Inject
	private TimeSheetService timeSheetService;

	/**
	 * Set the template
	 * 
	 * @return the main time sheet page
	 */
	@GET
	@Produces( { MediaType.TEXT_HTML })
	public Response getTimeSheetPanel() {
		return Response.ok(templateRenderer.render("templates/timesheet_main_page.html")).build();
	}
	
	

	/**
	 * Gets days associated to the current week.
	 * 
	 * @return A JSON String containing the rights of all users for the project
	 */
	@GET
	@Path("/calendar")
	@Produces( { MediaType.APPLICATION_JSON })
	public TimeSheetCalendarDTO getTimeSheetForWeekOfYear(@QueryParam("week") int week, @QueryParam("year") int year ) {
		if(week == 0 || year == 0){
			week = DateTime.now().getWeekOfWeekyear();
			year = DateTime.now().getYear();
		}
		
		return timeSheetService.getTimeSheetCalendar(week, year, userService.getAuthenticatedUserDTO().id);
		
	}
	
	@GET
	@Path("/day")
	@Produces( { MediaType.APPLICATION_JSON })
	public TimeSheetDayDTO getDayForTimeSheet(@QueryParam("day") String day) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yy");
		DateTime d1 = DateTime.parse(day, fmt).toDateMidnight().toDateTime();
		return timeSheetService.getTimeSheetDayDTO(d1.toDate());
	}
	
	@GET
	@Path("/month")
	@Produces( { MediaType.APPLICATION_JSON })
	public TimeSheetMonthDTO getMonthCalendarForUser(@QueryParam("month") int month, @QueryParam("year") int year) {
		if(month == 0 || year == 0){
			month = DateTime.now().getMonthOfYear();
			year = DateTime.now().getYear();
		}
		
		return timeSheetService.getTimeSheetCalendars(month, year, userService.getAuthenticatedUserDTO().id);
	}
	
	/**
	 * Set the template of month visualization
	 * 
	 * @return the monthly time sheet page
	 */
	@GET
	@Produces( { MediaType.TEXT_HTML })
	@Path("/view")
	public Response getTimeSheetVisualizationPanel() {
		return Response.ok(templateRenderer.render("templates/timesheet_view.html")).build();
	}
	
	/**
	 * Update a day in a timesheet
	 * 
	 * @return A JSON String containing the updated day detail
	 */
	@POST
	@Path("/update")
	@Consumes( { MediaType.APPLICATION_JSON })
	@Produces( { MediaType.APPLICATION_JSON })
	public TimeSheetDetailDTO updateTimeSheet(TimeSheetDetailDTO timeSheetDay) {
		return timeSheetService.createOrUpdateDayAmountForProject(timeSheetDay);
	}
	
	/**
	 * Remove a line in a time sheet
	 * @return 
	 */
	@GET
	@Path("/removeline")
	public String deleteLineFromTimeSheet(@QueryParam("timeSheetUniqueId") long timeSheetId, @QueryParam("projectUniqueId") long projectId) {
		timeSheetService.removeLine(timeSheetId, projectId);
		return "{\"result\":\"Ok\"}";
	}
	
	/**
	 * Validate a whole month of days in timesheets.
	 * @return 
	 */
	@GET
	@Path("/validate")
	public String validateDays(@QueryParam("month") int month, @QueryParam("year") int year) {
		timeSheetService.validateMonth(month, year, userService.getAuthenticatedUserDTO().id);
		return "{\"result\":\"Ok\"}";
	}
}
