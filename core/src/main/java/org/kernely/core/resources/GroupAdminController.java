package org.kernely.core.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.kernely.core.dto.GroupCreationRequestDTO;
import org.kernely.core.dto.GroupDTO;
import org.kernely.core.dto.UserDTO;
import org.kernely.core.service.user.GroupService;
import org.kernely.core.service.user.UserService;
import org.kernely.core.template.TemplateRenderer;

import com.google.inject.Inject;

@Path("/admin/groups")
public class GroupAdminController extends AbstractController {

	@Inject
	private TemplateRenderer templateRenderer;
	
	@Inject
	private GroupService groupService;
	
	@Inject
	private UserService userService;
	
	/**
	 * Get all existing groups in the database
	 * @return A list of all DTO associated to the existing groups in the database
	 */
	@GET
	@Path("/all")
	@Produces({"application/json"})
	public List<GroupDTO> displayAllGroups()
	{
		if (userService.currentUserIsAdministrator()){
			log.debug("Call to GET on all users");
			List<GroupDTO> groups = groupService.getAllGroups();
			return groups;
		}
		return null;
	}
	
	/**
	 * Display the group administration page
	 * @return the group administration page
	 */
	@GET
	@Produces( { MediaType.TEXT_HTML })
	public String displayPage()
	{
		if (userService.currentUserIsAdministrator()){
			return templateRenderer.create("/templates/gsp/administration/group_admin.gsp").asAdminPage().render() ;
		}
		return templateRenderer.create("/templates/gsp/home.gsp").render();
	}
	
	/**
	 * Create a new group with the given informations
	 * @param group The DTO containing all informations about the new group
	 * @return A JSON string containing the result of the operation
	 */
	@POST
	@Path("/create")
	@Produces({"application/json"})
	public String create(GroupCreationRequestDTO group)
	{
		
		if (userService.currentUserIsAdministrator()){
			try{
				log.debug("Create a user");
				if(group.id == 0){
					groupService.createGroup(group);
				}
				else{
					groupService.updateGroup(group);
				}
				return "{\"result\":\"ok\"}";
			} catch (IllegalArgumentException iae) {
				log.debug(iae.getMessage());
				return "{\"result\":\""+iae.getMessage()+"\"}";
			}
		}
		return null;
	}
	
	/**
	 * Delete the group which has the id 'id'
	 * @param id The id of the group to delete
	 * @return The result of the operation
	 */
	@GET
	@Path("/delete/{id}")
	@Produces( { MediaType.TEXT_HTML })
	public String lock(@PathParam("id") int id){
		if (userService.currentUserIsAdministrator()){
			groupService.deleteGroup(id);
			return "Ok";
		}
		return null;
	}
	
	/**
	 * Get all users associated to the group which has the id 'id'
	 * @param id The id of the group
	 * @return A list of all DTO associated to the users contained in this group
	 */
	@GET
	@Path("/{id}/users")
	@Produces({"application/json"})
	public List<UserDTO> getGroupUsers(@PathParam("id") int id){
		if (userService.currentUserIsAdministrator()){
			return groupService.getGroupUsers(id);
		}
		return null;
	}

}
