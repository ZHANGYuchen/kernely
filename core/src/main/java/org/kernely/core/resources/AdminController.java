/*
Copyright 2011 Prometil SARL

This file is part of Kernely.

Kernely is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of
the License, or (at your option) any later version.

Kernely is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public
License along with Kernely.
If not, see <http://www.gnu.org/licenses/>.
*/
package org.kernely.core.resources;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.kernely.core.dto.UserCreationRequestDTO;
import org.kernely.core.dto.UserDTO;
import org.kernely.core.dto.UserDetailsDTO;
import org.kernely.core.plugin.AbstractPlugin;
import org.kernely.core.plugin.PluginsLoader;
import org.kernely.core.service.user.UserService;
import org.kernely.core.template.TemplateRenderer;

import com.google.inject.Inject;

/**
 * Controller of the administration.
 */
@Path("/admin")
public class AdminController  extends AbstractController{
	
	

	@Inject
	private TemplateRenderer templateRenderer;
	
	@Inject
	private UserService userService;

	@Inject
	private PluginsLoader pluginsLoader;
	
	/**
	 * Display the administration panel.
	 * @return The html content to display the administration.
	 */
	@GET
	@Produces( { MediaType.TEXT_HTML })
	public String getText()
	{
		HashMap<String, String> plugins = new HashMap<String, String>();
		for (AbstractPlugin plugin : pluginsLoader.getPlugins()) {
			plugins.put(plugin.getName(), "");
		}
		String page;
		// Display the admin page only if the user is admin.
		if (userService.currentUserIsAdministrator()){
			page = templateRenderer.create("/templates/gsp/admin.gsp").with("plugins",plugins).render();
		} else{
			page = templateRenderer.create("/templates/gsp/home.gsp").render();
		}
		return page;
	}

}
