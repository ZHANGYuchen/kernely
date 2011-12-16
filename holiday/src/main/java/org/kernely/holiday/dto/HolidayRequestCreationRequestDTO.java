package org.kernely.holiday.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HolidayRequestCreationRequestDTO {

	public String requesterComment;
	public int typeId;
	public List<HolidayDetailCreationRequestDTO> details;
	
	public HolidayRequestCreationRequestDTO(){
		
	}
	
	
}
