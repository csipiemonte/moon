/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.wf;

import javax.ws.rs.core.MediaType;

import it.csi.apimint.cosmo.v1.dto.AggiornaEventoFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.AggiornaEventoFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.AvviaProcessoFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.AvviaProcessoFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.CreaDocumentiFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaEventoFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaEventoFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.CreaNotificaFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaNotificaFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.CreaPraticaFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.CreaPraticaFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.EliminaEventoFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.EsitoCreazioneDocumentiFruitore;
import it.csi.apimint.cosmo.v1.dto.FileUploadResult;
import it.csi.apimint.cosmo.v1.dto.GetPraticaFruitoreResponse;
import it.csi.apimint.cosmo.v1.dto.InviaSegnaleFruitoreRequest;
import it.csi.apimint.cosmo.v1.dto.InviaSegnaleFruitoreResponse;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO COSMO via servizi REST via API Manager Outer
 *
 * @author Laurent Pissard
 */
public interface CosmoDAO {

	// Documenti
	public EsitoCreazioneDocumentiFruitore creaDocumento(CreaDocumentiFruitoreRequest documento) throws DAOException;
	public byte[] getContenuto(String idPratica) throws DAOException;
	
	// Pratiche
	public GetPraticaFruitoreResponse getPratica(String idPratica) throws DAOException;
	public CreaPraticaFruitoreResponse creaPratica(CreaPraticaFruitoreRequest pratica) throws DAOException;
	
	// Eventi
	public CreaEventoFruitoreResponse creaEvento(CreaEventoFruitoreRequest evento) throws DAOException;
	public AggiornaEventoFruitoreResponse aggiornaEvento(String idEvento, AggiornaEventoFruitoreRequest evento) throws DAOException;
	public void eliminaEvento(String idEvento, EliminaEventoFruitoreRequest evento) throws DAOException;
	
	// Processi
	public AvviaProcessoFruitoreResponse avviaProcesso(AvviaProcessoFruitoreRequest processo) throws DAOException;
	public InviaSegnaleFruitoreResponse inviaSegnale(InviaSegnaleFruitoreRequest segnale, String idPratica) throws DAOException;
	
	// Notifiche
	public CreaNotificaFruitoreResponse creaNotifica(CreaNotificaFruitoreRequest notifica) throws DAOException;
	
	// File
	public FileUploadResult fileUpload(byte[] bytes, MediaType mediaType, String fileName) throws DAOException;
	
}
