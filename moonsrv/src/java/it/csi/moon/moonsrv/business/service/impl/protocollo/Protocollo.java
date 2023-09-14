/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.protocollo;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.commons.entity.ProtocolloRichiestaEntity;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.stardas.cxfclient.MetadatiType;

/**
 * Interface Protocollo
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface Protocollo {

	public String protocollaIstanza(Istanza istanza, ProtocolloParams params) throws BusinessException;
	public String protocollaIntegrazione(Istanza istanza, Long idStoricoWorkflow, ProtocolloParams params) throws BusinessException;
	public String protocollaFile(Istanza istanza, ProtocolloParams params) throws BusinessException;
	
	public ProtocolloParams readParams() throws BusinessException;
	
	//x Test, accesso diretto da API
	public MetadatiType _retrieveMetadati(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato) throws BusinessException;
	public MetadatiType _retrieveMetadatiAllegato(Istanza istanza, ProtocolloParams params, ProtocolloRichiestaEntity.TipoDoc tipoDoc, boolean isDocFirmato, AllegatoLazyEntity allegato) throws BusinessException;
}
