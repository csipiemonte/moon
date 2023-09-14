/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.coto;

import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
* DAO Pratiche edilizie
* 
* @author Danilo
* @since 1.0.0
*/
public interface GestionePraticheApiRestDAO {

	public  String getJsonPratica(Integer registro, Integer progressivo, Integer anno ) throws DAOException;


}
 