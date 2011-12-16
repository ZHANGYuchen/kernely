package org.kernely.holiday.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.kernely.holiday.model.HolidayRequestDetail;

@XmlRootElement
public class HolidayDetailDTO {

	public Date day;
	public boolean am;
	public boolean pm;
	
	public HolidayDetailDTO(){
		
	}
	
	public HolidayDetailDTO(HolidayRequestDetail detail){
		this.day = detail.getDay();
		this.pm = detail.isPm();
		this.am = detail.isAm();
	}
}
