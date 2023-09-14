/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper.task;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.WorkflowEntity;
import it.csi.moon.commons.entity.WorkflowFilter;
import it.csi.moon.moonfobl.business.service.WorkflowService;
import it.csi.moon.moonfobl.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonfobl.dto.moonfobl.CompieAzioneResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class WfAzioneIstanzaTask implements Callable<String> {

	private static final String CLASS_NAME = "WfAzioneIstanzaTask";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
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
		LOG.debug("[" + CLASS_NAME + "::WFAzioneIstanzaTask] CONSTRUCTOR istanzaSaved.getIdIstanza(): "+istanzaSaved.getIdIstanza());
		
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
	        LOG.debug("[" + CLASS_NAME + "::call] Started Task...");

	        sleepMilliseconds(3000);
	        
	        WorkflowEntity w = identificaAzioneWorkflowDaEseguire(); // lastStoricoWf
    		if (w!=null) {
	    		Azione azione = new Azione();
	    		azione.setIdAzione(w.getIdAzione());
	    		azione.setIdIstanza(istanzaSaved.getIdIstanza());
	    		azione.setIdWorkflow(w.getIdWorkflow());
				azione.setDatiAzione(initDatiAzioneIfNecessary(w));
	    		
	    		CompieAzioneResponse azioneCompiuta = compieAzione(azione);
    		}
    		
	        LOG.debug("[" + CLASS_NAME + "::call] Started Completed. " + result);
	        return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::call] ERROR ");
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::call] ERROR ", e);
			throw new BusinessException();
		}
    }
    
    public static void sleepMilliseconds(int val) {
        try { 
            TimeUnit.MILLISECONDS.sleep(val);
        } catch (InterruptedException e) {
        	LOG.error("[" + CLASS_NAME + "::sleepMilliseconds] Thread interrupted");
        	Thread.currentThread().interrupt();
        }
    }
    
	protected WorkflowEntity identificaAzioneWorkflowDaEseguire() {
		WorkflowFilter filter = new WorkflowFilter();
		filter.setIdModulo(istanzaSaved.getModulo().getIdModulo());
		filter.setIdStatoWfPartenza(istanzaSaved.getStato().getIdStato()); // INVIATO
		filter.setEscludiAzioniUtenteCompilante(true);
		//List<Workflow> elenco = workflowService.getElencoAzioniPossibili(user, istanzaSaved.getIdIstanza(), filter); // Possible effetto indesiderabile se fosse anche con flag_automatico, verrebbe esseguita 2 volte
		List<WorkflowEntity> elenco = workflowDAO.find(filter);
		if (elenco==null) {
			LOG.error("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguire] NESSUN Workflow Azioni possibile per id:odulo:" + 
					istanzaSaved.getModulo().getIdModulo() + " idStatoPartenza:" + istanzaSaved.getStato().getIdStato());
			return null;
		}
		if (elenco.size()>1) {
			LOG.error("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguire] "+elenco.size()+" is more than one Workflow Azioni possibile per idModulo:" + 
				istanzaSaved.getModulo().getIdModulo() + " idStatoPartenza:" + istanzaSaved.getStato().getIdStato());
			return null;
		}
		WorkflowEntity result = elenco.get(0);
		LOG.info("[" + CLASS_NAME + "::identificaAzioneWorkflowDaEseguire] result = " + result);
		return result;
	}
    
	private CompieAzioneResponse compieAzione(Azione azione) {
		LOG.info("[" + CLASS_NAME + "::compieAzione] IN azione = " + azione);
		CompieAzioneResponse result = workflowService.compieAzione(user, istanzaSaved.getIdIstanza(), azione);
		LOG.info("[" + CLASS_NAME + "::compieAzione] result = " + result);
		return result;
	}

	private String initDatiAzioneIfNecessary(WorkflowEntity w) {
		if (w.getIdDatiazione()==null) {
			LOG.debug("[" + CLASS_NAME + "::initDatiAzioneIfNecessary] NO DatiAzione Requested.");
			return null;
		}
		LOG.debug("[" + CLASS_NAME + "::initDatiAzioneIfNecessary] IN idDatiAzione = " + w.getIdDatiazione());
		String result = null;
		DatiAzione datiAzione = workflowService.initDatiAzione(user, istanzaSaved.getIdIstanza(), w.getIdWorkflow(), ipAddress);
		LOG.debug("[" + CLASS_NAME + "::initDatiAzioneIfNecessary] datiAzione = " + datiAzione);
		result = (String)datiAzione.getData();
		LOG.debug("[" + CLASS_NAME + "::initDatiAzioneIfNecessary] result = " + result);
		return result;
	}
	
}
