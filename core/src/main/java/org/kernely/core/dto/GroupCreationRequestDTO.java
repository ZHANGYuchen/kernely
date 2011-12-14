/**
 * Copyright 2011 Prometil SARL
 *
 * This file is part of Kernely.
 *
 * Kernely is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Kernely is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with Kernely.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package org.kernely.core.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GroupCreationRequestDTO {

	public int id;
	public String name;
	public List<UserDTO> users;

	/**
	 * Default Constructor
	 */
	public GroupCreationRequestDTO() {

	}

	/**
	 * GroupCreationRequestDTO constructor
	 * 
	 * @param id
	 *            : the id of the group updated. Null in the case of a new group
	 * @param name
	 *            : name of the new group/ group updated
	 * @param users
	 *            : users associated to the current group
	 */
	public GroupCreationRequestDTO(int id, String name, List<UserDTO> users) {
		this.id = id;
		this.name = name;
		this.users = users;
	}

}
