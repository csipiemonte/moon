/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.doc;

import java.util.List;

import it.csi.apimint.mydocs.be.v1.dto.AmbitoResponse;
import it.csi.apimint.mydocs.be.v1.dto.DocumentiResponse;
import it.csi.apimint.mydocs.be.v1.dto.FiltroDocumento;
import it.csi.apimint.mydocs.be.v1.dto.TipologiaResponse;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ProtocolloParametro;
import it.csi.moon.commons.entity.MyDocsRichiestaEntity;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

public interface MyDocsService {

	public String ping() throws BusinessException;
	
	//
	// AMBITI
	//
	public List<AmbitoResponse> listAmbitiByIdEnte(Long idEnte) throws BusinessException;
	public List<AmbitoResponse> listAmbitiByCodiceEnte(String codiceEnte) throws BusinessException;
	public List<AmbitoResponse> listAmbitiByCodiceIpaEnte(String codiceIpaEnte) throws BusinessException;
	public AmbitoResponse getAmbitoById(Long idEnte, Long idAmbito) throws BusinessException;

	//
	// TIPOLOGIE
	//
	public List<TipologiaResponse> listTipologieByIdEnte(Long idEnte) throws BusinessException;
	public List<TipologiaResponse> listTipologieByCodiceEnte(String codiceEnte) throws BusinessException;
	public List<TipologiaResponse> listTipologieByCodiceIpaEnte(String codiceIpaEnte) throws BusinessException;
	public TipologiaResponse getTipologiaById(Long idEnte, Long idTipologia) throws BusinessException;
	
	//
	// DOCUMENTI
	// generici
	public DocumentiResponse findDocumenti(Long idEnte, FiltroDocumento filtro) throws BusinessException;
//	public String pubblicaDocumento(RichiestaPubblicazioneMyDocs richiesta) throws BusinessException;
	// specifici
//	public String pubblicaIstanza(Long idIstanza) throws BusinessException;
//	public String pubblicaIstanza(Istanza istanza) throws BusinessException;
//	public String pubblicaFile(Long idFile) throws BusinessException;
	
	public String pubblicaIstanza(Long idIstanza,  Long idRichiesta) throws BusinessException;
	public String pubblicaIstanza(Istanza istanza, Long idRichiesta) throws BusinessException;
	public String pubblicaFile(Long idFile, Long idRichiesta) throws BusinessException;
	
//	public String pubblicaFile(Long idFile, Long idIstanza, Long idStoricoWorkflow) throws BusinessException;
	public String pubblicaMyDocs(Long idIstanza, Long idStoricoWorkflow) throws BusinessException;
	
	public List<ProtocolloParametro> getMyDocsParametri(Long idModulo);
	
	// LOG RICHIESTA
	public List<MyDocsRichiestaEntity> findByIdIstanza(Long idIstanza) throws BusinessException;
	
}
