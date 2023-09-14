/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import it.csi.moon.commons.dto.ReportVerificaFirma;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
/**
 * Interfaccia servizi di verifica firma digitale
 * mediante i servizi esposti da Dosign
 * 
 * @author franc
 *
 */
public interface FirmaDigitaleService {
	
	public ReportVerificaFirma checkDocumentSignatureByIdAllegato(Long idAllegato) throws BusinessException;
	public ReportVerificaFirma checkDocumentSignatureByIdFile(Long idFile) throws BusinessException;
	public ReportVerificaFirma checkDocumentSignatureByFile(MultipartFormDataInput file) throws BusinessException;
	public ReportVerificaFirma checkDocumentSignatureByBytes(byte[] bytes) throws BusinessException;
	
	public boolean testResources() throws BusinessException;

 	public String retrieveContentType(MultipartFormDataInput file) throws BusinessException;
}
