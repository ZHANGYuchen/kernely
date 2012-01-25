package org.kernely.holiday.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kernely.core.controller.AbstractController;
import org.kernely.core.service.user.UserService;
import org.kernely.core.template.TemplateRenderer;
import org.kernely.holiday.dto.CalendarRequestDTO;
import org.kernely.holiday.dto.HolidayDetailDTO;
import org.kernely.holiday.dto.HolidayRequestDTO;
import org.kernely.holiday.model.HolidayRequest;
import org.kernely.holiday.service.HolidayRequestService;

import com.google.inject.Inject;

/**
 * Controller for the  manager  request view
 * @author b.grandperret
 *
 */
@Path("/holiday/managers/request")
public class HolidayManagerRequestController extends AbstractController {
	@Inject
	private TemplateRenderer templateRenderer;
	
	@Inject
	private UserService userService;
	
	@Inject
	private HolidayRequestService holidayRequestService ; 
	
	/**
	 * Set the template
	 * @return the page 
	 */
	@GET
	@Produces( { MediaType.TEXT_HTML })
	public Response getHolidayRequestPage(){
		return ok(templateRenderer.create("/templates/gsp/holiday_manager_request.gsp").addCss("/css/holiday_manager_request.css"));
	}
	
	/**
	 * Get all existing holiday request  pending in the database
	 * @return A list of all DTO associated to the existing holiday requests in the database
	 */
	@GET
	@Path("/all/pending")
	@Produces({MediaType.APPLICATION_JSON})
	public List<HolidayRequestDTO> displayAllHolidayRequestPending()
	{
		if (userService.isManager(userService.getAuthenticatedUserDTO().username)){
			log.debug("Call to GET on all holiday request pending");
			return holidayRequestService.getSpecificRequestsForManagers(HolidayRequest.PENDING_STATUS);
		}
		return null;
	}
	
	/**
	 * Get all existing holiday request with a status in the database
	 * @return A list of all DTO associated to the existing holiday requests in the database
	 */
	@GET
	@Path("/all/status")
	@Produces({MediaType.APPLICATION_JSON})
	public List<HolidayRequestDTO> displayAllHolidayRequestStatus()
	{
		if (userService.isManager(userService.getAuthenticatedUserDTO().username)){
			log.debug("Call to GET on all holiday request accepted or denied");
			List<HolidayRequestDTO> lhr = new ArrayList<HolidayRequestDTO>();
			lhr.addAll(holidayRequestService.getSpecificRequestsForManagers(HolidayRequest.ACCEPTED_STATUS));
			lhr.addAll(holidayRequestService.getSpecificRequestsForManagers(HolidayRequest.DENIED_STATUS));
			return lhr; 
		}
		return null;
	}
	
	/**
	 * Accept a request
	 * @param idRequest
	 * @return ok
	 */
	@GET
	@Path("/accept/{id}")
	@Produces({MediaType.TEXT_HTML})
	public String acceptHoliday(@PathParam("id")int idRequest){
		if (userService.isManager(userService.getAuthenticatedUserDTO().username)){
			holidayRequestService.acceptRequest(idRequest);
			return "{\"result\":\"Ok\"}"; 			
		}
		return null;
	}
	
	/**
	 * deny a request
	 * @param idRequest
	 * @return ok
	 */
	@GET
	@Path("/deny/{id}")
	@Produces({MediaType.TEXT_HTML})
	public String denyHoliday(@PathParam("id")int idRequest){
		if (userService.isManager(userService.getAuthenticatedUserDTO().username)){
			holidayRequestService.denyRequest(idRequest);
			return "{\"result\":\"Ok\"}"; 			
		}
		return null;
	}
	
	/**
	 * comment a request
	 * @param idRequest
	 * @param the comment of the manager
	 * @return ok
	 */
	@GET
	@Path("/comment/{id}/{comment}")
	@Produces({MediaType.TEXT_HTML})
	public String managerCommentHoliday(@PathParam("id")int idRequest, @PathParam("comment")String managerComment){
		if (userService.isManager(userService.getAuthenticatedUserDTO().username)){
			holidayRequestService.addManagerCommentary(idRequest, managerComment);
			return "{\"result\":\"Ok\"}"; 			
		}
		return null;
	}
	
	/**
	 * Get the first and last date of the holiday request
	 * @param idRequest
	 * @return the firs element is the first date, the second is the second date
	 */
	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<HolidayDetailDTO> getDetailsInterval(@PathParam("id")int idRequest){
		if (userService.isManager(userService.getAuthenticatedUserDTO().username)){
			List<HolidayDetailDTO> hddto =  holidayRequestService.getHolidayRequestDetails(idRequest);
			Date firstDay = hddto.get(0).day;
			Date lastDay = hddto.get(0).day;
			HolidayDetailDTO firstHoliday = hddto.get(0);
			HolidayDetailDTO lastHoliday = hddto.get(0);
			
			for (HolidayDetailDTO holiday : hddto){
				if (holiday.day.equals(firstHoliday.day) && (holiday.am == true)){
						firstHoliday = holiday;
				}
				if (holiday.day.before(firstDay)){
					firstDay = holiday.day;
					firstHoliday = holiday;
				}
				if (holiday.day.equals(lastHoliday.day) && (holiday.pm == true)){
						lastHoliday = holiday;
				}
				if (holiday.day.after(lastDay)){
					lastDay = holiday.day;
					lastHoliday = holiday;
				}
			}
			hddto.clear();
			hddto.add(firstHoliday);
			hddto.add(lastHoliday);
			return hddto;
		}
		return null;
	}
	
	/**
	 * Get the holiday request details by order (first date to last date)
	 * @param idRequest
	 * @return The list ordered of the holiday request details
	 */
	@GET
	@Path("/details/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<HolidayDetailDTO> getDetails(@PathParam("id")int idRequest){
		if (userService.isManager(userService.getAuthenticatedUserDTO().username)){
			return holidayRequestService.getHolidayRequestDetailsByOrder(idRequest);
		}
		return null;
	}
	
	/**
	 * Construct the calendar in function of the begin date and end date   
	 * @param dateBegin
	 * @param dateEnd
	 * @return Calendar request
	 */
	@GET
	@Path("construct/{dateBegin}/{dateEnd}")
	@Produces(MediaType.APPLICATION_JSON)
	public CalendarRequestDTO constructCalendar(@PathParam("dateBegin")String dateBegin,@PathParam("dateEnd")String dateEnd){
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime d1 = DateTime.parse(dateBegin, fmt);
		DateTime d2 = DateTime.parse(dateEnd, fmt);
		return holidayRequestService.getCalendarRequest(d1, d2);
	}
}
