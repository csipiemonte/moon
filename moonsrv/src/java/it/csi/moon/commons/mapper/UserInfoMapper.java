/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.UtenteEntity;

/**
 * @author franc
 *
 */
public class UserInfoMapper {
	
	public static UserInfo buildFromIstanzaEntity(UtenteEntity entity) {
		
		UserInfo user = new UserInfo();
		
		user.setIdentificativoUtente(entity.getIdentificativoUtente());
		user.setCognome(entity.getCognome());
		user.setNome(entity.getNome());

		return user;
	}
	

}
