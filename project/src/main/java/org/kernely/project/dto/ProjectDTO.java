package org.kernely.project.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The project DTO
 */
@XmlRootElement
public class ProjectDTO {
	
	/**
	 * The id of the project
	 */
	public int id;
	
	/**
	 * The  name of the project
	 */
	public String name;
	
	/**
	 * Default constructor
	 */
	public ProjectDTO(){
		
	}
	
	/**
	 * Constructor
	 */
	public ProjectDTO(String newName, int newId){
		this.id = newId;
		this.name = newName;
	}
}
