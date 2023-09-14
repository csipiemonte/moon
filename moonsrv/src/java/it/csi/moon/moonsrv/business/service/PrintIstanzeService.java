/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service;

import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.moonprint.MoonprintDocument;
import it.csi.moon.commons.entity.IstanzaPdfEntity;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.util.decodifica.DecodificaTipoRepositoryFile;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Metodi di business relativi alla stampa delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface PrintIstanzeService {
	public byte[] generaSalvaPdf(Long idIstanza);
	public byte[] getPdfById(Long idIstanza);
	public byte[] getNotificaById(Long idIstanza);
	public IstanzaPdfEntity getIstanzaPdfEntityById(Long idIstanza);
	public RepositoryFileEntity getRenderedFileEntityByIdSw(Long idIstanza, Long idStoricoWorkflow, DecodificaTipoRepositoryFile dTipoRepositoryFile);
	public byte[] printPdf(Long idIstanza);	
	public byte[] printPdfClassLoader(Long idIstanza);	
	public byte[] printPdf(Istanza istanza);
	// for test
	public MoonprintDocument remap(Long idIstanza);
	
	//
	public byte[] printPdfIntegrazione(Long idIstanza, Long idStoricoWorkflow);
	public Documento getFirstDocumentoNotificaByIdIstanza(Long idIstanza) throws BusinessException;
	public byte[] getDocumentoByFormioNameFile(String formioNameFile) throws BusinessException;
	public byte[] getDocumentoByIdFile(Long idFile) throws BusinessException;
	
	public String getPrintMapperName(String codiceModulo, String versioneModulo) throws BusinessException;
	
}
