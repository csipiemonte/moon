/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.wf;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.Azione;
import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.LogEmailEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.util.decodifica.DecodificaTipoLogEmail;
import it.csi.moon.moonsrv.business.service.impl.dao.AzioneDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaPdfDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.MoonprintDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.WorkflowDAO;
import it.csi.moon.moonsrv.business.service.mail.EmailService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;



/**
 * Azione_Default - Azioni di default per i moduli che non necessitano specializzazione.
 * 
 * @author laurent
 *
 */
public class Azione_Default implements AzioneService {
	
	private static final String CLASS_NAME = "Azione_Default";
	protected Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	MoonprintDAO moonprintDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	NotificaDAO notificaDAO;
	@Autowired
	LogEmailDAO logEmailDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	IstanzaPdfDAO istanzaPdfDAO;
	@Autowired
	EmailService emailService;
	@Autowired
	AzioneDAO azioneDAO;
	@Autowired
	WorkflowDAO workflowDAO;
	
	public Azione_Default() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	/*
	 * Il comportamento di default prevede il salvataggio dei dati di protocollo 
	 * 
	 */
	@Override
	public void inserisciProtocollo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {
		
		// i dati di protocollo in uscita vengono salvati in storicoEntity al passo precedente

	}

	/*
	 * (non-Javadoc)
	 * @see it.csi.moon.moonsrv.business.service.wf.AzioneService#azioneSenzaDati(it.csi.moon.commons.dto.UserInfo, it.csi.moon.commons.dto.Azione, java.lang.Long)
	 * TODO: da implementare in base alla necessità
	 */
	public void azioneSenzaDati(UserInfo user, Azione azione, Long idIstanza) throws DAOException, BusinessException {
		try {
			StoricoWorkflowEntity lastStoricoWf = storicoWorkflowDAO.findLastStorico(idIstanza)
					.orElseThrow(ItemNotFoundBusinessException::new);
			if (lastStoricoWf.getIdFileRendering()==null) {
				LOG.error("[" + CLASS_NAME + "::azioneSenzaDati] IdFileRendering NULL on lastStoricoWf = "+lastStoricoWf);
				throw new BusinessException();
			}
				
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::azioneSenzaDati] Exception" + e.getMessage());
			throw new BusinessException();
		}
	}

	
	private void sendLogEmail(Istanza istanza, String emailDestinatario, EmailRequest request, DecodificaTipoLogEmail tipologia) {	
		ExecutorService ex = Executors.newSingleThreadExecutor();
		Runnable runnable = () -> {
			String logEsito = sendEmail(request);
			logEmail(istanza, emailDestinatario, logEsito, tipologia);
		};
		ex.submit(runnable);	
	}
	
	private String sendEmail(EmailRequest request) {
		String logEsito = "OK";
		try {
			emailService.sendMailWithAttach(request);
			LOG.debug("[" + CLASS_NAME + "::sendEmail] : moonsrvDAO.sendEmail() " );
		} catch (DAOException e) {
    		logEsito = "KO";
		}
		return logEsito;
	}

	private void logEmail(Istanza istanza, String emailDestinatario, String logEsito, DecodificaTipoLogEmail tipologia) {
		// 5. LogEmail 
		LogEmailEntity logEmail = new LogEmailEntity();
		logEmail.setIdTipologia(tipologia.getId()); // 2 MOOnBO-invio richiesta integrazione
		logEmail.setIdEnte(istanza.getIdEnte());
		logEmail.setIdModulo(istanza.getModulo().getIdModulo());
		logEmail.setIdIstanza(istanza.getIdIstanza());
		logEmail.setEmailDestinatario(emailDestinatario);
		logEmail.setTipoEmail("text-attach");
		logEmail.setEsito(logEsito);
		logEmailDAO.insert(logEmail);
	}


	
	/**
	 * Valida che bytes inizia con %PDF
	 * Commentato il controllo sulla fine del file, sembra che venga generato con un file DOS con \n in piu final
	 * @param bytes
	 * @return bytes se inizia con %PDF altrimenti Exception
	 */
	private byte[] validaPdf(byte[] bytes) {
		byte[] header = new byte[] { bytes[0], bytes[1], bytes[2], bytes[3] };
		LOG.info("[" + CLASS_NAME + "::validaPdf] header: " + header + "  =>  " + new String(header) + " expected %PDF");
		var isHeaderValid = header[0] == 0x25 && header[1] == 0x50 && header[2] == 0x44 && header[3] == 0x46; //%PDF

		if (!(isHeaderValid /*&& isTrailerValid*/)) {
			LOG.error("[" + CLASS_NAME + "::validaPdf] NOT_VALID_PDF  isHeaderValid=" + isHeaderValid /*+ "  isTrailerValid=" + isTrailerValid*/);
			throw new BusinessException("NOT_VALID_PDF");
		}
		return bytes;
	}

	
}
