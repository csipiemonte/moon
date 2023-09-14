/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso alla componente MOOnPrint per la stampa pdf
 * 
 * @see MoonprintDocument
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface MoonprintDAO {

	public String pingMoonprint() throws DAOException;
	
	public byte[] printPdf(MoonprintDocument moonPrintParam) throws DAOException;
	
}
