/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.ProtocolloParametro;
import it.csi.moon.commons.dto.ResponseOperazioneMassiva;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.stardas.cxfclient.MetadatiType;

/**
 * Metodi di business relativi alla protocollazione delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ProtocolloService {
	// Protocollazione delle istanze
	public void protocollaIstanza(Long idIstanza) throws BusinessException;	
	public void protocollaIstanza(Istanza istanza) throws BusinessException;
	// Protocollazione di un integrazione e suoi allegati
	public void protocollaIntegrazione(Long idIstanza, Long idStoricoWorkflow) throws BusinessException;
	//
	public ResponseOperazioneMassiva protocollaMassivo(Long idTag);
	
	// Protocolla File (Ricevuta da BO)
	public void protocollaFile(Long idFile);
	
	public List<ProtocolloParametro> getProtocolloParametri(Long idModulo);
	
	//xTest
	public MetadatiType retrieveMetadatiIstanza(Long idIstanza) throws BusinessException;
	public MetadatiType retrieveMetadatiAllegato(Long idIstanza, Long idAllegato) throws BusinessException;
	
	public String getProtocolloManagerName(String codiceProtocolloSistema, String codiceModulo) throws BusinessException;
	public String getProtocolloManagerName(String codiceModulo) throws BusinessException;
}
