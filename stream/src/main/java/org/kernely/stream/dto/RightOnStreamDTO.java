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
package org.kernely.stream.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * the stream dto for the write
 * @author b.grandperret
 *
 */
@XmlRootElement
public class RightOnStreamDTO {

	/**
	 * The user or group id 
	 */
	public int id;

	/**
	 * The type of id : "group" or "user"
	 */
	public String idType;
	
	/**
	 * The type of permission for the user 
	 */
	public String permission;

	
	/**
	 * Default constructor
	 */
	public RightOnStreamDTO() {

	}

	/**
	 * Creates a RightOnStreamDTO
	 * 
	 * @param id
	 *            Id of the user or group
	 * @param idType
	 *            The type of id : "group" or "user"
	 * @param permission
	 *            Permission granted to the user ou group
	 */
	public RightOnStreamDTO(int id, String idType, String permission) {
		this.id = id;
		this.permission = permission;
		this.idType=idType;
	}

}
