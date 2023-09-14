/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso a tag_istanza
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public interface TagIstanzaDAO {
	
	int updateEsito(Long idTag, Long idIstanza, String esito) throws DAOException;	
	
}
