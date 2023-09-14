/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.task;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.WorkflowService;
import it.csi.moon.moonbobl.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowFilter;
import it.csi.moon.moonbobl.dto.moonfobl.Azione;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAzione;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;


public class WfAzioneIstanzaTask implements Callable<String> {

	private final static String CLASS_NAME = "WfAzioneIstanzaTask";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	private UserInfo user;
	private Istanza istanzaSaved;
	private String confAzione;
	private String ipAddress;
	
	@Autowired
	WorkflowService workflowService;
	@Autowired
	WorkflowDAO workflowDAO;
	
    public WfAzioneIstanzaTask(UserInfo user, Istanza istanzaSaved, String confAzione, String ipAddress) {
		super();
		this.user = user;
		this.istanzaSaved = istanzaSaved;
		this.confAzione = confAzione;
		this.ipAddress = ipAddress;
		log.debug("[" + CLASS_NAME + "::WFAzioneIstanzaTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): "+istanzaSaved.getIdIstanza());
		
	    //autowiring support
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
    
    /**
     * Richiede l'esecuzione dell'azione de workflow successiva allo stato corrente dell'istanza
     * 
     */
    public String call() throws BusinessException {
    	try {
	    	String result = null;
	        log.debug("[" + CLASS_NAME + "::call] Started Task...");

	        Thread.sleep(3000);
	        
	        WorkflowEntity w = identificaAzioneWorkflowDaEseguire(); // lastStoricoWf
    		if (w!=null) {
	    		Azione azione = new Azione();
	    		azione.setIdAzione(w.getIdAzione());
	    		azione.setIdIstanza(istanzaSaved.getIdIstanza());
	    		azione.setIdWorkflow(w.getIdWorkflow());
				azione.setDatiAzione(initDatiAzioneIfNecessary(w));
	    		
	    		Azione azioneCompiuta = compieAzione(azione);
    		}
    		
	        log.debug("[" + CLASS_NAME + "::call] Started Completed. " + result);
	        return result;
		} catch (BusinessException be) {
			log.warn("[" + CLASS_NAME + "::call] ERROR ");
			throw be;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::call] ERROR ", e);
			throw new BusinessException();
		}
    }

	protected WorkflowEntity identificaAzioneWorkflowDaEseguire() {
		WorkflowFilter filter = new WorkflowFilter();
		filter.setIdModulo(istanzaSaved.getModulo().getIdModulo());
		filter.setIdStatoWfPartenza(istanzaSaved.getStato().getIdStato()); // INVIATO
		filter.setEscludiAzioniUtenteCompilante(true);
		filter.setEscludiAzioniDiSistema(true);
		//List<Workflow> elenco = workflowService.getElencoAzioniPossibili(user, istanzaSaved.getIdIstanza(), filter); // Possible effetto indesiderabile se fosse anche con flag_automatico, verrebbe esseguita 2 volte
		List<WorkflowEntity> elenco = workflowDAO.find(filter);
		if (elenco==null) {
			log.error("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguire] NESSUN Workflow Azioni possibile per id:odulo:" + 
					istanzaSaved.getModulo().getIdModulo() + " idStatoPartenza:" + istanzaSaved.getStato().getIdStato());
			return null;
		}
		if (elenco.size()>1) {
			log.error("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguire] "+elenco.size()+" is more than one Workflow Azioni possibile per idModulo:" + 
				istanzaSaved.getModulo().getIdModulo() + " idStatoPartenza:" + istanzaSaved.getStato().getIdStato());
			return null;
		}
		WorkflowEntity result = elenco.get(0);
		log.info("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguire] result = " + result);
		return result;
	}
    
	private Azione compieAzione(Azione azione) {
		log.info("[" + CLASS_NAME + "::compieAzione] IN azione = " + azione);
		Azione result = workflowService.compieAzione(user, istanzaSaved.getIdIstanza(), azione, this.ipAddress);
		log.info("[" + CLASS_NAME + "::compieAzione] result = " + result);
		return result;
	}

	private String initDatiAzioneIfNecessary(WorkflowEntity w) {
		if (w.getIdDatiazione()==null) {
			log.debug("[" + CLASS_NAME + "::initDatiAzioneIfNecessary] NO DatiAzione Requested.");
			return null;
		}
		log.debug("[" + CLASS_NAME + "::initDatiAzioneIfNecessary] IN idDatiAzione = " + w.getIdDatiazione());
		String result = null;
		DatiAzione datiAzione = workflowService.initDatiAzione(user, istanzaSaved.getIdIstanza(), w.getIdWorkflow(), ipAddress);
		log.debug("[" + CLASS_NAME + "::initDatiAzioneIfNecessary] datiAzione = " + datiAzione);
		result = (String)datiAzione.getData();
		log.debug("[" + CLASS_NAME + "::initDatiAzioneIfNecessary] result = " + result);
		return result;
	}
	
}