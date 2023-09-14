/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.epay.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.commons.entity.EpayNotificaPagamentoEntity;
import it.csi.moon.commons.entity.EpayNotificaPagamentoTestataEntity;
import it.csi.moon.commons.entity.EpayRicevutaTelematicaEntity;
import it.csi.moon.commons.entity.EpayRicevutaTelematicaTestataEntity;
import it.csi.moon.commons.entity.EpayRichiestaEntity;
import it.csi.moon.commons.util.MoonJmsTypes;
import it.csi.moon.moonsrv.business.service.WorkflowService;
import it.csi.moon.moonsrv.business.service.epay.EpayServiceCallback;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayNotificaPagamentoDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayNotificaPagamentoTestataDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayRicevutaTelematicaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayRicevutaTelematicaTestataDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonsrv.business.service.mail.EmailService;
import it.csi.moon.moonsrv.business.ws.epay.type.DatiTransazionePSPType;
import it.csi.moon.moonsrv.business.ws.epay.type.NotificaPagamentoType;
import it.csi.moon.moonsrv.business.ws.epay.type.RTType;
import it.csi.moon.moonsrv.business.ws.epay.type.ResponseType;
import it.csi.moon.moonsrv.business.ws.epay.type.ResultType;
import it.csi.moon.moonsrv.business.ws.epay.type.SoggettoType;
import it.csi.moon.moonsrv.business.ws.epay.type.TestataNotifichePagamentoType;
import it.csi.moon.moonsrv.business.ws.epay.type.TestataRTType;
import it.csi.moon.moonsrv.business.ws.epay.type.TrasmettiNotifichePagamentoRequest;
import it.csi.moon.moonsrv.business.ws.epay.type.TrasmettiRTRequest;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Profile("epay")
@Component
public class EpayServiceCallbackImpl implements EpayServiceCallback {

	private static final String CLASS_NAME = "EpayServiceCallbackImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private ObjectMapper mapper;
	
	@Autowired
	EpayRicevutaTelematicaTestataDAO ricevutaTelematicaTestataDAO;
	@Autowired
	EpayRicevutaTelematicaDAO ricevutaTelematicaDAO;
	@Autowired
	EpayNotificaPagamentoTestataDAO notificaPagamentoTestataDAO;
	@Autowired
	EpayNotificaPagamentoDAO notificaPagamentoDAO;
	@Autowired
	EpayRichiestaDAO richiestaDAO;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	EmailService emailService;
	@Autowired
	private JmsTemplate jmsTemplate;
	
	public EpayServiceCallbackImpl() {
		super();
		System.out.println("-------------------------------------------------------------------");
		System.out.println("EpayServiceCallbackImpl actived. @Profile(\"epay\") presente.");
		LOG.info("[EpayServiceCallbackImpl::EpayServiceCallbackImpl] EpayServiceCallbackImpl actived. @Profile(\"epay\") presente.");
	}

	//
	// RT - RicevutaTelematica
	//
	@Override
	public ResponseType riceviRicevutaTelematica(TrasmettiRTRequest ricevutaTelematica) {
        try {
            if (LOG.isDebugEnabled()) {
            	LOG.debug("[" + CLASS_NAME + "::riceviRicevutaTelematica] IN ricevutaTelematica = " + ricevutaTelematica);
            }
	        //
	        EpayRicevutaTelematicaTestataEntity rtTestataE = remapAndSaveRicevutaTelematicaTestata(ricevutaTelematica.getTestata());
	        //
	        for (RTType rt : ricevutaTelematica.getCorpoRT().getElencoRT().getRT()) {
	        	EpayRicevutaTelematicaEntity rtE = remapAndSaveRicevutaTelematica(rtTestataE.getIdRicevutaTelemTestata(), rt);
	        	elaboraRicevutaTelematica(rtE);
	        }
        } catch (DAOException daoe) {
        	LOG.error("[" + CLASS_NAME + "::riceviRicevutaTelematica] DAOException ", daoe);
        } catch (Exception e) {
        	LOG.error("[" + CLASS_NAME + "::riceviRicevutaTelematica] Exception ", e);
		}
		return buildResponseOK();
	}

	private EpayRicevutaTelematicaTestataEntity remapAndSaveRicevutaTelematicaTestata(TestataRTType testata) throws DAOException {
		EpayRicevutaTelematicaTestataEntity result = buildRTTestataEntity(testata);
        Long idRicevutaTelematicaTestata = ricevutaTelematicaTestataDAO.insert(result);
        result.setIdRicevutaTelemTestata(idRicevutaTelematicaTestata);
		return result;
	}

	private EpayRicevutaTelematicaEntity remapAndSaveRicevutaTelematica(Long idRicevutaTelematicaTestata, RTType rt) throws DAOException {
		EpayRicevutaTelematicaEntity result = buildRicevutaTelematicaEntity(rt, idRicevutaTelematicaTestata);
		Long idRicevutaTelematica = ricevutaTelematicaDAO.insert(result);
		result.setIdRicevutaTelemematica(idRicevutaTelematica);
		return result;
	}
	private void elaboraRicevutaTelematica(EpayRicevutaTelematicaEntity rtEntity) {
        if (LOG.isDebugEnabled()) {
        	LOG.debug("[" + CLASS_NAME + "::elaboraRicevutaTelematica] IN rtEntity = " + rtEntity);
        }
        // SE esito POSITIVO
		// Ricercare EpayRichiesta sulla base deti dati del XML rtEntity.getXml() ed assegnare idRicevutaTelematicaPositiva
	}
	
	//
	// RT - Mappers
	private EpayRicevutaTelematicaTestataEntity buildRTTestataEntity(TestataRTType testata) {
		EpayRicevutaTelematicaTestataEntity result = new EpayRicevutaTelematicaTestataEntity();
		result.setIdMessaggio(testata.getIdMessaggio());
		result.setCfEnteCreditore(testata.getCFEnteCreditore());
		result.setCodiceVersamento(testata.getCodiceVersamento());
		result.setNumeroRt(testata.getNumeroRT());
		result.setDataIns(new Date());
		return result;
	}
	private EpayRicevutaTelematicaEntity buildRicevutaTelematicaEntity(RTType rt, Long idRicevutaTelematicaTestata) {
		EpayRicevutaTelematicaEntity result = new EpayRicevutaTelematicaEntity();
		result.setIdRicevutaTelemTestata(idRicevutaTelematicaTestata);
		result.setId(rt.getId());
//        if (LOG.isDebugEnabled()) {
//        	LOG.debug("[" + CLASS_NAME + "::buildRicevutaTelematicaEntity] Base64.getDecoder().decode(rt.getXML())  encoded = " + new String(rt.getXML()));
//        }
//	    final byte[] xmlBytesDecoded = Base64.getDecoder().decode(rt.getXML());
//	    final String xmlDecoded = new String(xmlBytesDecoded, StandardCharsets.UTF_8);
//		result.setXml(xmlDecoded);
		result.setXml(new String(rt.getXML()));
		result.setDataIns(new Date());
		return result;
	}

	//
	// NOTIFICA PAGAMENTO
	//
	@Override
	public ResponseType riceviNotificaPagamento(TrasmettiNotifichePagamentoRequest notifichePagamento) {
		try {
//	        if (LOG.isDebugEnabled()) {
	        	LOG.info("[" + CLASS_NAME + "::riceviNotificaPagamento] IN notifichePagamento = " + notifichePagamento);
//	        }
	        //
	        EpayNotificaPagamentoTestataEntity notificaTestataE = remapAndSaveNotificaPagamentoTestata(notifichePagamento.getTestata());
	        //
	        notifichePagamento.getCorpoNotifichePagamento().getElencoNotifichePagamento().getNotificaPagamento().stream()
	        	.forEach(np -> elaboraNotificaPagamento(remapAndSaveNotificaPagamento(notificaTestataE.getIdNotificaPagamTestata(), np)));
	        //
			return buildResponseOK();
        } catch (DAOException daoe) {
        	LOG.error("[" + CLASS_NAME + "::riceviNotificaPagamento] DAOException ", daoe);
        	segnalaErroreBloccante("riceviNotificaPagamento");
        	return buildResponseBLOCCANTE();
        } catch (Exception e) {
        	LOG.error("[" + CLASS_NAME + "::riceviNotificaPagamento] Exception ", e);
        	segnalaErroreBloccante("riceviNotificaPagamento");
        	return buildResponseBLOCCANTE();
		}
	}

	private EpayNotificaPagamentoTestataEntity remapAndSaveNotificaPagamentoTestata(TestataNotifichePagamentoType testata) {
		EpayNotificaPagamentoTestataEntity result = buildNotificaPagamentoTestataEntity(testata);
        Long idNotificaPagamentoTestata = notificaPagamentoTestataDAO.insert(result);
        result.setIdNotificaPagamTestata(idNotificaPagamentoTestata);
		return result;
	}

	private EpayNotificaPagamentoEntity remapAndSaveNotificaPagamento(Long idNotificaPagamentoTestata, NotificaPagamentoType np) {
		EpayNotificaPagamentoEntity result = buildNotificaPagamentoEntity(np, idNotificaPagamentoTestata);
		Long idNotificaPagamento = notificaPagamentoDAO.insert(result);
		result.setIdNotificaPagamento(idNotificaPagamento);
		return result;
	}
	/**
	 * elaboraNotificaPagamento = salva su DB e invia su coda JMS una notifica di pagamento
	 * @param notificaE
	 */
	private void elaboraNotificaPagamento(EpayNotificaPagamentoEntity notificaE) {
        if (LOG.isDebugEnabled()) {
        	LOG.debug("[" + CLASS_NAME + "::elaboraNotificaPagamento] IN notificaPagamentoEntity = " + notificaE);
        }
        richiestaDAO.updateNotifica(notificaE.getCodiceAvviso(), notificaE.getIdNotificaPagamento(), notificaE.getDataEsitoPagamento());
        //TODO rendere ASYNC le azione seguente
        try {
	        compieAzioneWfIfNecessario(notificaE.getCodiceAvviso());
	        sendMessageToMoonFoUnderJmsTopic(notificaE);
        } catch (Exception e) {
        	LOG.debug("[" + CLASS_NAME + "::elaboraNotificaPagamento] Errore su elaborazione opzionale (azione e email) del codiceAvviso: " + notificaE.getCodiceAvviso());
		}
	}
	
	private void compieAzioneWfIfNecessario(String codiceAvviso) {
        if (LOG.isDebugEnabled()) {
        	LOG.debug("[" + CLASS_NAME + "::compieAzioneWfIfNecessario] IN codiceAvviso = " + codiceAvviso);
        }
		EpayRichiestaEntity richiestaEpay = richiestaDAO.findByCodiceAvviso(codiceAvviso);
        if (LOG.isDebugEnabled()) {
        	LOG.debug("[" + CLASS_NAME + "::compieAzioneWfIfNecessario] richiestaEpay = " + richiestaEpay);
        }
        workflowService.compieAzioneAutomaticaIfPresente(richiestaEpay.getIdIstanza());
	}

	//
	// NP - Mappers
	private EpayNotificaPagamentoTestataEntity buildNotificaPagamentoTestataEntity(TestataNotifichePagamentoType testata) {
		EpayNotificaPagamentoTestataEntity result = new EpayNotificaPagamentoTestataEntity();
		result.setIdMessaggio(testata.getIdMessaggio());
		result.setCfEnteCreditore(testata.getCFEnteCreditore());
		result.setCodiceVersamento(testata.getCodiceVersamento());
		result.setPagamentiSpontanei(testata.isPagamentiSpontanei()?"S":"N");
		result.setNumeroPagamenti(testata.getNumeroPagamenti());
		result.setImportoTotalePagamenti(testata.getImportoTotalePagamenti());
		result.setDataIns(new Date());
		return result;
	}
	private EpayNotificaPagamentoEntity buildNotificaPagamentoEntity(NotificaPagamentoType np, Long idNotificaPagamentoTesta) {
		EpayNotificaPagamentoEntity result = new EpayNotificaPagamentoEntity();
		result.setIdNotificaPagamTesta(idNotificaPagamentoTesta);
		//
		result.setIdPosizioneDebitoria(np.getIdPosizioneDebitoria());
		//
		result.setAnnoDiRiferimento(np.getAnnoDiRiferimento());
		result.setCodiceAvviso(np.getCodiceAvviso());
		result.setIuv(np.getIUV());
		result.setImportoPagato(np.getImportoPagato());
		//
		result.setDataScadenza(remapDate(np.getDataScadenza()));
		//
		result.setDescCausaleVersamento(np.getDescrizioneCausaleVersamento());
		result.setDataEsitoPagamento(remapDate(np.getDataEsitoPagamento()));
		result.setSoggettoDebitore(remapSoggetto(np.getSoggettoDebitore()));
		//
		result.setSoggettoVersante(remapSoggetto(np.getSoggettoVersante()));
		result.setDatiTransazionePsp(remapDatiTransazionePsp(np.getDatiTransazionePSP()));
		//
		result.setDatiSpecificiRiscossione(np.getDatiSpecificiRiscossione());
		//
		result.setNote(np.getNote());
		result.setDataIns(new Date());
		return result;
	}

	/*
     * Converts XMLGregorianCalendar to java.util.Date in Java
     */
    public Date remapDate(XMLGregorianCalendar calendar){
        if(calendar == null) {
            return null;
        }
        return calendar.toGregorianCalendar().getTime();
    }
    /*
     * Convert SoggettoType to JSON String
     */
    private String remapSoggetto(SoggettoType soggettoXML) {
    	try {
    		if (soggettoXML==null) {
        		return null;
        	}
			return getMapper().writeValueAsString(soggettoXML);
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::remapSoggetto] IN soggettoXML = " + soggettoXML);
			throw new BusinessException("Impossibile convertire il SoggettoType sulla Notifica Pagamento", "MOONSRV-30820");
		}
	}
    /*
     * Convert SoggettoType to JSON String
     */
    private String remapDatiTransazionePsp(DatiTransazionePSPType datiXML) {
    	try {
    		if (datiXML==null) {
        		return null;
        	}
			return getMapper().writeValueAsString(datiXML);
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::remapDatiTransazionePsp] IN datiXML = " + datiXML);
			throw new BusinessException("Impossibile convertire il DatiTransazionePSPType sulla Notifica Pagamento", "MOONSRV-30821");
		}
	}

    
    private ObjectMapper getMapper() {
    	if (mapper==null) {
    		mapper = new ObjectMapper();
    	}
    	return mapper;
    }
	
    
    private void sendMessageToMoonFoUnderJmsTopic(EpayNotificaPagamentoEntity npe) throws BusinessException {
    	try {
	    	LOG.info("[" + CLASS_NAME + "::sendMessageToMoonFoUnderJmsTopic] IUV = " + npe.getIuv());
	    	jmsTemplate.setPubSubDomain(true);
	    	String payload = getMapper().writeValueAsString(npe);
		    jmsTemplate.convertAndSend(getNotificaPagamentoJmsName(), payload, msg -> {
		    	msg.setStringProperty("iuv", npe.getIuv());
		    	msg.setJMSType(MoonJmsTypes.EPAY_NOTIFICATION);
		    	return msg;
		    });
		} catch (Throwable e) {
			LOG.error("[" + CLASS_NAME + "::sendMessageToMoonFoUnderJmsTopic] Errore invio messaggio coda JMS for iuv=" + npe.getIuv() + " " + e.getMessage());
			throw new BusinessException("Errore invio messaggio coda JMS","MOONSRV-");
		}
    }
    
	private static String getNotificaPagamentoJmsName() {
		return EnvProperties.readFromFile(EnvProperties.MOONFO_JMS_NAME);
	}
	
	//
	//
	//
	private ResponseType buildResponseOK() {
        ResponseType result = new ResponseType();
        ResultType res = new ResultType();
        res.setCodice("000");
        res.setMessaggio("L’invocazione del servizio si è conclusa correttamente.");
        result.setResult(res);
        return result;
	}
	
	private ResponseType buildResponseErroreNonBloccante() {
        ResponseType result = new ResponseType();
        ResultType res = new ResultType();
        res.setCodice("100");
        res.setMessaggio("Errore applicativo generico");
        result.setResult(res);
        return result;
	}
	
	private ResponseType buildResponseBLOCCANTE() {
        ResponseType result = new ResponseType();
        ResultType res = new ResultType();
        res.setCodice("200");
        res.setMessaggio("Errore generico di sistema");
        result.setResult(res);
        return result;
	}
	
	//
	//
	private void segnalaErroreBloccante(String methodName) {
		try {
			LOG.info("[" + CLASS_NAME + "::segnalaErroreBloccante] IN methodName = " + methodName);
			EmailRequest emailRequest = makeEmailRequest(methodName);
			emailService.sendMail(emailRequest);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::segnalaErroreBloccante] Exception Impossibile segnalare l'errore BLOCCANTE di " + methodName);
		} // NON TORNARE Exception
	}
	
	private EmailRequest makeEmailRequest(String methodName) {
		EmailRequest emailRequest = new EmailRequest();
		emailRequest = new EmailRequest();
		emailRequest.setTo("supporto.moon@csi.it");
		emailRequest.setSubject(retrieveSubject());
		emailRequest.setText(retrieveText(methodName));
		return emailRequest;
	}

	private String retrieveSubject() {
		return EnvProperties.readFromFile(EnvProperties.TARGET_LINE) + " - EpayServiceCallbackImpl";
	}

	private String retrieveText(String methodName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
		return "Errore bloccante sul metodo " + methodName + "\r\nVerificare lo stato delle code con PiemontePAY.\r\n" + sdf.format(new Date());
	}
	
}
