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

package org.kernely.holiday.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.kernely.holiday.model.HolidayBalance;


/**
 * dto for holiday  
 * @author b.grandperret
 *
 */
@XmlRootElement
public class HolidayBalanceDTO {

	public int id;
	public float availableBalance;
	public float futureBalance;
	
	public HolidayBalanceDTO(){
		
	}

	public HolidayBalanceDTO(HolidayBalance balance){
		this.id = balance.getId() ; 
		
		// Divide balances by 12 because in database, balances are in twelths of days.
		this.availableBalance = ((float) balance.getAvailableBalance()) / 12.0F;
		this.futureBalance = ((float) balance.getFutureBalance()) / 12.0F;
	}
	
}
