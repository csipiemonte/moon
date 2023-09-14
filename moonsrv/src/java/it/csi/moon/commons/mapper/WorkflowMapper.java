/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.StoricoWorkflow;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.DatiAzioneEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.entity.WorkflowEntity;

/**
 * @author franc
 *
 */
public class WorkflowMapper {
	
	
	public static Workflow buildFromWorkflowEntity(WorkflowEntity entity) {
		
		Workflow workflow = new Workflow();
		
		workflow.setIdWorkflow(entity.getIdWorkflow());	
		workflow.setIdAzione(entity.getIdAzione());
		workflow.setIdProcesso(entity.getIdProcesso());
		
		workflow.setIdStatoWfArrivo(entity.getIdStatoWfArrivo());
		workflow.setIdStatoWfPartenza(entity.getIdStatoWfPartenza());		
		workflow.setNomeAzione(entity.getNomeAzione());
		workflow.setCodiceAzione(entity.getCodiceAzione());
		workflow.setCampoCondizione(entity.getCampoCondizione());
		workflow.setValoreCondizione(entity.getValoreCondizione());
		
		Boolean isAnnullabile = (entity.getFlagAnnullabile().equals("S")) ? true : false;
		Boolean isAutomatico = (entity.getFlagAutomatico().equals("S")) ? true : false;
		workflow.setIsAnnullabile(isAnnullabile);
		workflow.setIsAutomatico(isAutomatico);
		
		workflow.setEmailDestinatario(entity.getEmailDestinatario());
		workflow.setIdGruppoUtentiDestinatari(entity.getIdGruppoUtentiDestinatari());
		workflow.setIdTipoUtenteDestinatario(entity.getIdTipoUtenteDestinatario());
		workflow.setIdUtenteDestinatario(entity.getIdUtenteDestinatario());
	
		workflow.setIdDatiAzione(entity.getIdDatiazione());
		Boolean isAzioneConDati = (entity.getIdDatiazione() != null) ? true : false;
		workflow.setIsAzioneConDati(isAzioneConDati);
		
		workflow.setIdCondition(entity.getIdCondition());
		
		return workflow;
	}
	
	public static StoricoWorkflow buildFromStoricoWorkflowEntity(StoricoWorkflowEntity entity) {
		
		StoricoWorkflow storicoWorkflow = new StoricoWorkflow();
		
		storicoWorkflow.setIdIstanza(entity.getIdIstanza());
		storicoWorkflow.setDataFine(entity.getDataFine());
		storicoWorkflow.setDataInizio(entity.getDataInizio());
		storicoWorkflow.setDatiAzione(entity.getDatiAzione());
		storicoWorkflow.setDescDestinatario(entity.getDescDestinatario());
		storicoWorkflow.setIdAzione(entity.getIdAzione());
		storicoWorkflow.setIdStoricoWorkflow(entity.getIdStoricoWorkflow());
		storicoWorkflow.setNomeAzione(entity.getNomeAzione());
		storicoWorkflow.setNomeStatoWfArrivo(entity.getNomeStatoWfArrivo());
		storicoWorkflow.setNomeStatoWfPartenza(entity.getNomeStatoWfPartenza());
		storicoWorkflow.setIdProcesso(entity.getIdProcesso());
		storicoWorkflow.setIdStatoWfPartenza(entity.getIdStatoWfPartenza());
		storicoWorkflow.setIdStatoWfArrivo(entity.getIdStatoWfArrivo());
		storicoWorkflow.setContieneDati((entity.getDatiAzione() != null && !entity.getDatiAzione().equals("")) ? true : false);
		
		return storicoWorkflow;
	}

	public static DatiAzione buildFromDatiAzioneEntity(DatiAzioneEntity entity) {
		
		DatiAzione datiAzione = new DatiAzione();
		
		datiAzione.setCodiceDatiAzione(entity.getCodiceDatiazione());
		datiAzione.setIdDatiAzione(entity.getIdDatiazione());
		datiAzione.setDescrizioneDatiAzione(entity.getDescrizioneDatiazione());
		datiAzione.setStruttura(entity.getStruttura());
		datiAzione.setVersioneDatiAzione(entity.getVersioneDatiazione());
		
		return datiAzione;
	}
	
}
