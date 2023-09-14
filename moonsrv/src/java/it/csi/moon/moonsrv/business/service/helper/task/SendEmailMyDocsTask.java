/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.helper.task;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.ContextRequest;
import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.extra.doc.RichiestaPubblicazioneMyDocs;
import it.csi.moon.commons.entity.LogEmailEntity;
import it.csi.moon.commons.entity.MyDocsRichiestaEntity;
import it.csi.moon.commons.util.decodifica.DecodificaTipoLogEmail;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonsrv.business.service.mail.EmailService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EmailRequestValidator;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * SendEmailMyDocsTask
 *
 * @author Danilo
 *
 */
public class SendEmailMyDocsTask implements Callable<String> {

	private static final String CLASS_NAME = "SendEmailDichiaranteIstanzaTask";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private Istanza istanza;
	private MyDocsRichiestaEntity richiesta;

	private EmailRequest emailRequest;
	private Integer idTipologiaLogEmail = DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE.getId();
	private static final String SUPPORTO_MOON = "supporto.moon@csi.it";
	private static final String SUBJECT = "Errore in pubblicazione verso MyDocs";
	private static final String TEXT = "La pubblicazione verso Mydocs";

	@Autowired
	EmailService emailService;
	@Autowired
	LogEmailDAO logEmailDAO;
	@Autowired
	IstanzeService istanzeService;
	
    public SendEmailMyDocsTask(MyDocsRichiestaEntity richiesta, Istanza istanza) {
		super();
		
		this.richiesta = richiesta;
		this.istanza = istanza;
		
		LOG.debug("[" + CLASS_NAME + "::SendEmailMyDocsTask] id istanza): " + getIstanza().getIdIstanza());

        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
    
	private Istanza getIstanza() {
		return istanza;
	}

    /**
     * Invia una email di notifica errore pubblicazione verso MyDocs
     *
     */
	public String call() throws BusinessException {
		try {
			String result = null;
			LOG.debug("[" + CLASS_NAME + "::call] Started Task...");

			// 1. Invio Email supporto.moon@csi.it
			String logEsito = "";

			LOG.debug("[" + CLASS_NAME + "::call] moonsrvDAO.sendEmail() verso mydocs...");
			try {
				makeValidaSendEmail(SUPPORTO_MOON);
				result = "Un email di notifica di errore di pubblicazione verso mydocs è stato mandato all'indirizzo "
						+ SUPPORTO_MOON;
				logEsito += "OK";
			} catch (BusinessException e) {
				result = "Non è stato possibile inviare l'email di notifica all'indirizzo " + SUPPORTO_MOON;
				logEsito += "KO";
			}

			// 2. LogEmail
			LogEmailEntity logEmail = new LogEmailEntity();
			logEmail.setIdTipologia(this.idTipologiaLogEmail);
			logEmail.setIdEnte(getIstanza().getIdEnte());
			logEmail.setIdModulo(getIstanza().getModulo().getIdModulo());
			logEmail.setIdIstanza(getIstanza().getIdIstanza());
			logEmail.setEmailDestinatario(SUPPORTO_MOON);
			logEmail.setTipoEmail("text-attach");
			logEmail.setEsito(logEsito);
			logEmailDAO.insert(logEmail);

			LOG.debug("[" + CLASS_NAME + "::call] Started Completed. " + result);
			return result;
		} catch (DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::call] ERROR ");
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::call] ERROR ");
			throw be;			
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::call] ERROR ", e);
			throw new BusinessException();
		}
	}

	private void makeValidaSendEmail(String emailSupportoMoon) {
		EmailRequest request = makeEmailRequest(emailSupportoMoon);
		new EmailRequestValidator().valida(request);
		String response = "OK";
		emailService.sendMail(request); // moonsrvDAO.sendEmail(request);
		LOG.debug("[" + CLASS_NAME + "::makeValidaSendEmail] moonsrvDAO.sendEmail() response=" + response);
	}

	private EmailRequest makeEmailRequest(String emailSupportoMoon) {
		if (emailRequest!=null) {
			emailRequest.setTo(emailSupportoMoon);
		} else {
			emailRequest = new EmailRequest();
			emailRequest.setTo(emailSupportoMoon);
			emailRequest.setCc("");
			emailRequest.setBcc("");
			emailRequest.setSubject(retrieveSubject());
			emailRequest.setText(retrieveText());
			emailRequest.setHtml("");			
			emailRequest.setContext(retrieveContext());
		}
		return emailRequest;
	}

	private ContextRequest retrieveContext() {
		ContextRequest context = new ContextRequest();
		context.setIdModulo(getIstanza().getModulo().getIdModulo());
		context.setIdIstanza(getIstanza().getIdIstanza());
		return context;
	}

	private String retrieveSubject() {
    	LOG.debug("[" + CLASS_NAME + "::retrieveSubject] BEGIN");
    	
    	String result = SUBJECT;
        if (RichiestaPubblicazioneMyDocs.TipoDoc.DOCUMENTO_PA.getId().equals(richiesta.getTipoDoc())) {        	
        	result = result.concat(" "+RichiestaPubblicazioneMyDocs.TipoDoc.DOCUMENTO_PA.getCodiceTipologiaMydocs());
        } else if (RichiestaPubblicazioneMyDocs.TipoDoc.ISTANZA.getId().equals(richiesta.getTipoDoc())) {
        	result = result.concat(" "+RichiestaPubblicazioneMyDocs.TipoDoc.ISTANZA.getCodiceTipologiaMydocs());        	
        }
	    
    	LOG.debug("[" + CLASS_NAME + "::retrieveSubject] END " + result);
		return result;
	}

	private String retrieveText() {
    	LOG.debug("[" + CLASS_NAME + "::retrieveText] BEGIN");
		String result = TEXT;
		result = result.concat(" "+"riferito all' id richiesta "+richiesta.getIdRichiesta()+" "+"per l' istanza"+" "+richiesta.getIdIstanza()+" "+"non è andata a buon fine");
		LOG.debug("[" + CLASS_NAME + "::retrieveText] END " + result);
		return result;
	}

}
