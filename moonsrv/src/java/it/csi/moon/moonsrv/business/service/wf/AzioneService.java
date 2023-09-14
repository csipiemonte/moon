/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.wf;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Delegate delle azioni che possono essere specializzate per Modulo
 * Attualmente 1 metodo :
 * - salvaProtocollo : registra il numero di protocollo
 * 
 * @author Alberto
 *
 */
public interface AzioneService {

	public void inserisciProtocollo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity);
	
	public void azioneSenzaDati(UserInfo user, Azione azione, Long idIstanza) throws BusinessException;

}
