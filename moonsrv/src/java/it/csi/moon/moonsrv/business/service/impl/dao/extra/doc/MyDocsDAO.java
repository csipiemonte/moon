/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.doc;

import java.util.List;

import it.csi.apimint.mydocs.be.v1.dto.AmbitoResponse;
import it.csi.apimint.mydocs.be.v1.dto.DocumentiResponse;
import it.csi.apimint.mydocs.be.v1.dto.Documento;
import it.csi.apimint.mydocs.be.v1.dto.FiltroDocumento;
import it.csi.apimint.mydocs.be.v1.dto.Tipologia;
import it.csi.apimint.mydocs.be.v1.dto.TipologiaResponse;
import it.csi.moon.commons.dto.Ambito;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

public interface MyDocsDAO {
	
	//
	// PING
	public String ping() throws DAOException;
	
	//
	// AMBITI
	//
	public List<AmbitoResponse> listAmbiti(String ente) throws DAOException;
	public AmbitoResponse getAmbitoById(String ente, Long idAmbito) throws DAOException;
	public AmbitoResponse insertAmbito(String ente, Ambito ambito) throws DAOException;

	//
	// TIPOLOGIE
	//
	public List<TipologiaResponse> listTipologie(String ente) throws DAOException;
	public TipologiaResponse getTipologiaById(String ente, Long idTipologia) throws DAOException;
	public TipologiaResponse insertTipologia(String ente, Tipologia tipologia) throws DAOException;
	
	//
	// DOCUMENTI
	public DocumentiResponse findDocumenti(String ente, FiltroDocumento filtro) throws DAOException;
	public String insertDocumento(String ente, Documento documento) throws DAOException;
//	public int getDocumentoById(SignedBuffer inContent) throws DAOException;
//	public int updateDocumentoById(SignedBuffer inContent) throws DAOException;

}
