/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import it.csi.moon.moonbobl.dto.moonprint.MoonPrintDocumentoAccoglimento;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocument;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoDiniego;
import it.csi.moon.moonbobl.dto.moonprint.MoonprintDocumentoRicevuta;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

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
	public byte[] printPdf(MoonprintDocumentoRicevuta moonPrintParam) throws DAOException;
	public byte[] printPdf(MoonprintDocumentoDiniego moonprintDocument) throws DAOException;
	public byte[] printPdf(MoonPrintDocumentoAccoglimento moonprintDocument) throws DAOException;
}
