/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.processo;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.ProcessoEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.moonfobl.dto.moonfobl.CompieAzioneResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;

/**
 * Delegate su alcune operazioni di WorkflowServiceImpl che possono essere specializzate per Processo
 * Attualmente 1 metodo :
 * - compieAzione : particolarita dell'azione (al di fuori della reazione del record storicoWorkFlow rimasto in WorkflowServiceImpl)
 * 
 * @author laurent
 */
public interface ProcessoServiceDelegate {
	
	public CompieAzioneResponse compieAzione(UserInfo user, IstanzaEntity istanza, Azione azione, Workflow workflow, ProcessoEntity processo, StoricoWorkflowEntity newStoricoWfEntity) throws BusinessException;
	
	public DatiAzione getInitData(UserInfo user, Long idIstanza, Workflow workflow, String ipAdress)  throws BusinessException;
	
}
