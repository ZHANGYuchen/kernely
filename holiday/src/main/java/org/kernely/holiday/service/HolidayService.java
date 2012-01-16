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

package org.kernely.holiday.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.kernely.core.dto.UserDTO;
import org.kernely.core.service.AbstractService;
import org.kernely.core.service.user.UserService;
import org.kernely.holiday.dto.HolidayCreationRequestDTO;
import org.kernely.holiday.dto.HolidayDTO;
import org.kernely.holiday.dto.HolidayUpdateRequestDTO;
import org.kernely.holiday.model.HolidayType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

/**
 * The service for holiday pages 
 * @author b.grandperret
 *
 */
@Singleton
public class HolidayService extends AbstractService {
	
	@Inject
	UserService userService;
	
	@Inject
	HolidayBalanceService balanceService;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Gets the lists of all groups contained in the database.
	 * 
	 * @return the list of all groups contained in the database.
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public List<HolidayDTO> getAllHoliday() {

		Query query = em.get().createQuery("SELECT e FROM HolidayType e ORDER BY Name");
		List<HolidayType> collection = (List<HolidayType>) query.getResultList();
		List<HolidayDTO> dtos = new ArrayList<HolidayDTO>();
		log.debug("HolidayService found {} holiday types", collection.size());
		for (HolidayType holiday : collection) {
			dtos.add(new HolidayDTO(holiday.getName(), holiday.getQuantity(), holiday.getPeriodUnit(), holiday.getId(), holiday.isAnticipated(),
					holiday.getEffectiveMonth(), holiday.getColor()));
			log.debug("Creation of Holiday Type {}", holiday.getName());
		}
		return dtos;
	}

	/**
	 * Gets the holiday DTO for the holiday type with the id passed in parameter.
	 * 
	 * @param id
	 *            The id of the holiday typ
	 * @return the holiday type dto.
	 */
	@Transactional
	public HolidayDTO getHolidayDTO(int id) {
		Query query = em.get().createQuery("SELECT  h from HolidayType h WHERE  h.id=:id");
		query.setParameter("id", id);
		HolidayType holiday = (HolidayType) query.getSingleResult();
		HolidayDTO hdto = new HolidayDTO(holiday.getName(), holiday.getQuantity(), holiday.getPeriodUnit(), holiday.getId(), holiday.isAnticipated(),
				holiday.getEffectiveMonth(), holiday.getColor());

		return hdto;
	}

	/**
	 * Delete an existing holiday in database
	 * 
	 * @param id
	 *            The id of the group to delete
	 */
	@Transactional
	public void deleteHoliday(int id) {
		HolidayType holiday = em.get().find(HolidayType.class, id);
		em.get().remove(holiday);
	}

	/**
	 * Create a new Holiday in database
	 * 
	 * @param request
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public HolidayDTO createHoliday(HolidayCreationRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request cannot be null ");
		}

		if (request.type == null) {
			throw new IllegalArgumentException("holiday type cannot be null ");
		}
		if ("".equals(request.type.trim())) {
			throw new IllegalArgumentException("holiday type cannot be space character only ");
		}
		String type = request.type;
		Query verifExist = em.get().createQuery("SELECT g FROM HolidayType g WHERE name=:name");
		verifExist.setParameter("name", type);
		List<HolidayType> list = (List<HolidayType>) verifExist.getResultList();
		if (!list.isEmpty()) {
			throw new IllegalArgumentException("Another holiday with this name already exists");
		}

		HolidayType holiday = new HolidayType();
		holiday.setName(request.type.trim());
		holiday.setQuantity(request.quantity);
		holiday.setPeriodUnit(request.unity);
		holiday.setEffectiveMonth(request.effectiveMonth);
		holiday.setAnticipated(request.anticipation);
		holiday.setColor(request.color);

		em.get().persist(holiday);
		
		
		// Create a balance with this holiday type, for each user
		int holidayId = this.getHolidayDTO(request.type.trim()).id;
		
		for (UserDTO user : userService.getAllUsers()){
			balanceService.createHolidayBalance(user.id, holidayId);
		}
		
		return new HolidayDTO(holiday);
	}

	/**
	 * Get a holiday type with its name
	 * @name The name of the holiday
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public HolidayDTO getHolidayDTO(String holidayName){
		Query verifExist = em.get().createQuery("SELECT h FROM HolidayType h WHERE name=:name");
		verifExist.setParameter("name", holidayName);
		
		HolidayType holiday= (HolidayType) verifExist.getSingleResult();
		return new HolidayDTO(holiday);
	}
	
	/**
	 * Update an existing holiday in database
	 * 
	 * @param request
	 *            The request, containing group name and id of the needed group
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateHoliday(HolidayUpdateRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request cannot be null ");
		}

		if (request.type == null) {
			throw new IllegalArgumentException("holiday cannot be null ");
		}
		if (request.frequency < 0) {
			throw new IllegalArgumentException("holiday frequency cannot be under 0 ");
		}
		if ("".equals(request.type.trim())) {
			throw new IllegalArgumentException("holiday type cannot be space character only ");
		}

		String name = request.type;
		int id = request.id;

		// Check if the user wants to change the name
		HolidayType holiday = em.get().find(HolidayType.class, id);
		if (!holiday.getName().equals(name)) {
			// If the new name already exists, don't update
			Query verifExist = em.get().createQuery("SELECT g FROM HolidayType g WHERE name=:name");
			verifExist.setParameter("name", name);
			List<HolidayType> list = (List<HolidayType>) verifExist.getResultList();
			if (!list.isEmpty()) {
				return;
			}
		}

		holiday.setName(request.type);
		holiday.setQuantity(request.quantity);
		holiday.setPeriodUnit(request.unity);
		holiday.setEffectiveMonth(request.effectiveMonth);
		holiday.setAnticipated(request.anticipation);
		holiday.setColor(request.color);
		em.get().merge(holiday);
	}

}