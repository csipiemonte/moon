/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.apipostazione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

import it.csi.moon.commons.dto.EmailAttachment;
import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.api.PostAzione;
import it.csi.moon.commons.dto.api.PostAzioneSendMail;
import it.csi.moon.commons.dto.api.PostAzioneSendMailAllOfAttachments;
import it.csi.moon.commons.entity.LogEmailEntity;
import it.csi.moon.commons.util.decodifica.DecodificaTipoLogEmail;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonsrv.business.service.mail.EmailService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.EmailRequestValidator;

public class ApiPostAzioneDefault extends BaseApiPostAzione implements ApiPostAzione {

	private static final String CLASS_NAME = "ApiPostAzioneDefault";

	private static final String OK = "OK";
	private static final String KO = "KO";

    @Autowired
	EmailService emailService;
	@Autowired
	LogEmailDAO logEmailDAO;
	
	public ApiPostAzioneDefault(String codiceAzione, Istanza istanza, Long idStoricoWorkflow) {
		super();
        
		this.codiceAzione = codiceAzione;
        this.istanza = istanza;
        this.idStoricoWorkflow = idStoricoWorkflow;
        this.strReplaceHelper = new StrReplaceHelper(this.istanza);
	}
	
	@Override
	public void execute(List<PostAzione> postAzioni) {
		for (PostAzione postAzione : postAzioni) {
			executeAzione(postAzione);
		}
	}

	protected void executeAzione(PostAzione postAzione) {
		try {
			switch(postAzione.getPostAzioneType().name()) {
				case "SEND_EMAIL":
					sendEmail(postAzione);
					break;
				default:
					LOG.error("[" + CLASS_NAME + "::execute] UNKOWN PostAzione " + postAzione.getPostAzioneType().name());
			}
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::executeAzione] BusinessException", e);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::executeAzione] Exception", e);
		}
	}
	
	//
	//
	@Override
	public String sendEmail(PostAzione postAzione) {
		String esito = KO;
		EmailRequestValidator validator = new EmailRequestValidator();
		try {
			if (postAzione != null) {
				conf = retrieveAPAEmailConfByAzione(istanza);
				EmailRequest request = buildEmailRequest((PostAzioneSendMail) postAzione);
				validator.valida(request);
				try {
					emailService.sendMailForApi(request);
					esito = OK;
				} catch (BusinessException e) {
					LOG.error("[" + CLASS_NAME + "::sendEmail] BusinessException sendEmail - " + getIdentificativoIstanza());
				}
				logEmail(request, istanza, "text-attach", esito);

			} else {
				LOG.info("[" + CLASS_NAME + "::sendEmail - invio di default]" + getIdentificativoIstanza());
				conf = retrieveAPAEmailConfByKey(istanza, codiceAzione);
				JsonNode invioEmailDefault = retrieveAPAEmailConfByKey(istanza, "INVIO_EMAIL_DEFAULT");
				if (invioEmailDefault != null && invioEmailDefault.asText().equals("S")) {
					if (conf == null) {
						conf = retrieveAPAEmailConfByKey(istanza, "DEFAULT");
					}
					if (conf != null) {
						EmailRequest request = buildEmailRequestFromConf(conf);
						validator.valida(request);
						try {
							LOG.info("[" + CLASS_NAME + "::sendEmail - invio di default - validazione ok ]"
									+ getIdentificativoIstanza());
							emailService.sendMailForApi(request);
							esito = OK;
						} catch (BusinessException e) {
							esito = KO;
							LOG.error("[" + CLASS_NAME + "::sendEmail] BusinessException sendEmail - "
									+ getIdentificativoIstanza());
						}
						logEmail(request, istanza, "text-attach", esito);
					} else {						
						EmailRequest  request = new EmailRequest();
						request.setTo("");
						logEmail(request, istanza, "text-attach", esito);
						validator.valida(request);
					}
				}
			}
			return esito;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::sendEmail] BusinessException " + getIdentificativoIstanza(), e);
			return esito;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::sendEmail] Exception " + getIdentificativoIstanza(), e);
			return esito;
		}
	}

	private String getIdentificativoIstanza() {
		return istanza.getIdIstanza() + "-" + istanza.getCodiceIstanza();
	}
	
	public EmailRequest buildEmailRequest(PostAzioneSendMail postAzione) {
		EmailRequest  emailRequest = new EmailRequest();
		emailRequest.setTo(replaceDinamici(postAzione.getTo(), ConfAPAEmailKeys.TO));
		emailRequest.setCc(replaceDinamici(postAzione.getCc(), ConfAPAEmailKeys.CC));
		emailRequest.setBcc(replaceDinamici(postAzione.getBcc(), ConfAPAEmailKeys.BCC));
		emailRequest.setSubject(replaceDinamici(postAzione.getSubject(), ConfAPAEmailKeys.SUBJECT));
		emailRequest.setText(replaceDinamici(postAzione.getText(), ConfAPAEmailKeys.TEXT));
		emailRequest.setAttachment(buildEmailAttachment(postAzione));
		return emailRequest;
	}
	
	public EmailRequest buildEmailRequestFromConf(JsonNode conf) {
		EmailRequest  emailRequest = new EmailRequest();
		emailRequest.setTo(replaceDinamici(conf.has("to") ? conf.get("to").asText():ConfAPAEmailKeys.TO.getTextDefaultValue(), ConfAPAEmailKeys.TO));
		emailRequest.setCc(replaceDinamici(conf.has("cc") ? conf.get("cc").asText():ConfAPAEmailKeys.CC.getTextDefaultValue(), ConfAPAEmailKeys.CC));
		emailRequest.setBcc(replaceDinamici(conf.has("bcc") ? conf.get("bcc").asText():ConfAPAEmailKeys.BCC.getTextDefaultValue(), ConfAPAEmailKeys.BCC));
		emailRequest.setSubject(replaceDinamici(conf.has("subject") ? conf.get("subject").asText():ConfAPAEmailKeys.SUBJECT.getTextDefaultValue(), ConfAPAEmailKeys.SUBJECT));
		emailRequest.setText(replaceDinamici(conf.has("text") ? conf.get("text").asText():ConfAPAEmailKeys.TEXT.getTextDefaultValue(), ConfAPAEmailKeys.TEXT));
//		emailRequest.setAttachment(buildEmailAttachment(postAzione));
		return emailRequest;
	}
	private EmailAttachment buildEmailAttachment(PostAzioneSendMail postAzione) {		
		PostAzioneSendMailAllOfAttachments att =  postAzione.getAttachments();
		EmailAttachment result = null;
		if (att != null) {
			result = new EmailAttachment(istanza.getIdIstanza(), idStoricoWorkflow, 
				retrieveBooleanValue(att.getIstanza(), ConfAPAEmailKeys.INCLUDE_ISTANZA),
				retrieveBooleanValue(att.getAllegati(), ConfAPAEmailKeys.INCLUDE_ALLEGATI),
				retrieveBooleanValue(att.getAllegatiAzione(), ConfAPAEmailKeys.INCLUDE_ALLEGATI_AZIONE));		
		}
		else {
			result = new EmailAttachment(istanza.getIdIstanza(), idStoricoWorkflow, 
					retrieveBooleanValue(null, ConfAPAEmailKeys.INCLUDE_ISTANZA),
					retrieveBooleanValue(null, ConfAPAEmailKeys.INCLUDE_ALLEGATI),
					retrieveBooleanValue(null, ConfAPAEmailKeys.INCLUDE_ALLEGATI_AZIONE));	
		}
		return result;
	}
	
	private EmailAttachment buildEmailAttachmentFromConf(JsonNode conf) {

		EmailAttachment result = null;
		JsonNode att = conf.has("attachments") ? conf.get("attachments") : null;

		result = new EmailAttachment(istanza.getIdIstanza(), idStoricoWorkflow,
				retrieveBooleanValue(
						att != null && att.has("istanza") ? att.get("istanza").asBoolean()
								: ConfAPAEmailKeys.INCLUDE_ISTANZA.getBooleanDefaultValue(),
						ConfAPAEmailKeys.INCLUDE_ISTANZA),
				retrieveBooleanValue(
						att != null && att.has("allegati") ? att.get("istanza").asBoolean()
								: ConfAPAEmailKeys.INCLUDE_ALLEGATI.getBooleanDefaultValue(),
						ConfAPAEmailKeys.INCLUDE_ALLEGATI),
				retrieveBooleanValue(
						att != null && att.has("allegatiAzione") ? att.get("istanza").asBoolean()
								: ConfAPAEmailKeys.INCLUDE_ALLEGATI_AZIONE.getBooleanDefaultValue(),
						ConfAPAEmailKeys.INCLUDE_ALLEGATI_AZIONE));

		return result;
	}
    
	private void logEmail(EmailRequest request, Istanza istanza, String tipoEmail, String esito) {
		LogEmailEntity logEmail = new LogEmailEntity();
		if (this.codiceAzione != null) {
			switch (this.codiceAzione) {
			case "NOTIFICA_FRUITORE":
				logEmail.setIdTipologia(DecodificaTipoLogEmail.API_ALLEGATO_NOTIFICA.getId());
				break;
			default:
				logEmail.setIdTipologia(DecodificaTipoLogEmail.API_ALLEGATO_AZIONE_CAMBIO_STATO.getId());
			}
		}
		logEmail.setIdEnte(istanza.getIdEnte());
		logEmail.setIdModulo(istanza.getModulo().getIdModulo());
		logEmail.setIdIstanza(istanza.getIdIstanza());
		logEmail.setEmailDestinatario(request.getTo());
		logEmail.setTipoEmail(tipoEmail);
		logEmail.setEsito(esito);
		logEmail.setIdStoricoWorkflow(this.idStoricoWorkflow);
		logEmailDAO.insert(logEmail);
	}

}
