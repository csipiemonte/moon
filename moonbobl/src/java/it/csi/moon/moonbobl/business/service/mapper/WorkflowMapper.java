/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper;

import java.util.regex.Pattern;

import it.csi.moon.moonbobl.business.service.impl.dto.DatiAzioneEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowEntity;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAzione;
import it.csi.moon.moonbobl.dto.moonfobl.StoricoWorkflow;
import it.csi.moon.moonbobl.dto.moonfobl.Workflow;

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

		workflow.setIdStatoWfPartenza(entity.getIdStatoWfPartenza());
		workflow.setIdStatoWfArrivo(entity.getIdStatoWfArrivo());		
		workflow.setNomeAzione(entity.getNomeAzione());
		workflow.setCodiceAzione(entity.getCodiceAzione());
		workflow.setCampoCondizione(entity.getCampoCondizione());
		workflow.setValoreCondizione(entity.getValoreCondizione());
	
		workflow.setEmailDestinatario(entity.getEmailDestinatario());
		workflow.setIdUtenteDestinatario(entity.getIdUtenteDestinatario());
		workflow.setIdTipoUtenteDestinatario(entity.getIdTipoUtenteDestinatario());
		workflow.setIdGruppoUtentiDestinatari(entity.getIdGruppoUtentiDestinatari());
		
		workflow.setIsAnnullabile("S".equalsIgnoreCase(entity.getFlagAnnullabile())?true:false);
		workflow.setIsAutomatico("S".equalsIgnoreCase(entity.getFlagAutomatico())?true:false);
		
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
		if (entity.getDatiAzione() != null)
		{
			String datiAzione = modificaContesto(entity.getDatiAzione());
			storicoWorkflow.setDatiAzione(datiAzione);
		}
		else 
		{
			storicoWorkflow.setDatiAzione(entity.getDatiAzione());
		}
		
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
		storicoWorkflow.setIdFileRendering(entity.getIdFileRendering());
		storicoWorkflow.setAttoreUpd(entity.getAttoreUpd());
		
		return storicoWorkflow;
	}

	public static DatiAzione buildFromDatiAzioneEntity(DatiAzioneEntity entity) {
		
		DatiAzione datiAzione = new DatiAzione();
		
		datiAzione.setCodiceDatiAzione(entity.getCodiceDatiAzione());
		datiAzione.setIdDatiAzione(entity.getIdDatiAzione());
		datiAzione.setDescrizioneDatiAzione(entity.getDescrizioneDatiAzione());
		datiAzione.setStruttura(entity.getStruttura());
		datiAzione.setVersioneDatiAzione(entity.getVersioneDatiAzione());
		
		return datiAzione;
	}
	
	public static String modificaContesto(String testo) 
	{

		// modifica il contesto
	    Pattern p = Pattern.compile("moonfobl", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	    
	    String result = p.matcher(testo).replaceAll("moonbobl");
	    
	    Pattern pm = Pattern.compile("modulistica", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	    
	    result = pm.matcher(result).replaceAll("moonbobl");
	    
	    // converte in url relativa
	    String REGEX = "https://[a-zA-Z0-9\\-]+\\.patrim\\.csi\\.it/"; 
	    Pattern pvh = Pattern.compile(REGEX, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	    result = pvh.matcher(result).replaceAll("/");
	    
	    String REGEX2 = "https://[a-zA-Z0-9\\-]+\\.csi\\.it/";
		Pattern pvh2 = Pattern.compile(REGEX2, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		result = pvh2.matcher(result).replaceAll("/");
	    
	    return result;
	}

	public static WorkflowEntity buildFromObj(Workflow obj) {
		WorkflowEntity entity = new WorkflowEntity();
		
		entity.setIdWorkflow(obj.getIdWorkflow());	
		entity.setIdAzione(obj.getIdAzione());
		entity.setIdProcesso(obj.getIdProcesso());
		
		entity.setIdStatoWfArrivo(obj.getIdStatoWfArrivo());
		entity.setIdStatoWfPartenza(obj.getIdStatoWfPartenza());		
		entity.setNomeAzione(obj.getNomeAzione());
		entity.setCodiceAzione(obj.getCodiceAzione());
		entity.setCampoCondizione(obj.getCampoCondizione());
		entity.setValoreCondizione(obj.getValoreCondizione());
		
		entity.setFlagAnnullabile(obj.getIsAnnullabile()?"S":"N");
		entity.setFlagAutomatico(obj.getIsAutomatico()?"S":"N");
		entity.setFlagArchiviabile("N");
		entity.setFlagApi("N");
		entity.setFlagStatoIstanza("N");
		
		entity.setEmailDestinatario(obj.getEmailDestinatario());
		entity.setIdGruppoUtentiDestinatari(obj.getIdGruppoUtentiDestinatari());
		entity.setIdTipoUtenteDestinatario(obj.getIdTipoUtenteDestinatario());
		entity.setIdUtenteDestinatario(obj.getIdUtenteDestinatario());
//		entity.setIdDatiAzione(obj.getIdDatiazione()); // TODO	
		entity.setIdCondition(obj.getIdCondition());
		
		return entity;
	}
}
