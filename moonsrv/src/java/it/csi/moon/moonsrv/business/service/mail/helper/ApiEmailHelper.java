/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mail.helper;

import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.EmailAttachment;
import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.api.PostAzioneSendMail;
import it.csi.moon.commons.dto.api.PostAzioneSendMailAllOfAttachments;
import it.csi.moon.moonsrv.business.service.helper.StrReplaceHelper;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class ApiEmailHelper {

	private static final String CLASS_NAME = "ApiEmailHelper";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private Istanza istanza;
	private Long idStoricoWorkflow;
	private StrReplaceHelper strReplaceHelper;

	public ApiEmailHelper(Istanza istanza, Long idStoricoWorkflow) {
		 this.istanza = istanza;
		 this.idStoricoWorkflow = idStoricoWorkflow;
		 this.strReplaceHelper = new StrReplaceHelper(istanza);
	}

	public EmailRequest buildEmailRequest(PostAzioneSendMail postAzione) {
		EmailRequest  emailRequest = new EmailRequest();
		emailRequest.setTo(replaceDinamici(postAzione.getTo()));
		emailRequest.setCc(replaceDinamici(postAzione.getCc()));
		emailRequest.setBcc(replaceDinamici(postAzione.getBcc()));
		emailRequest.setSubject(replaceDinamici(postAzione.getSubject()));
		emailRequest.setText(replaceDinamici(postAzione.getText()));
		emailRequest.setAttachment(retrieveEmailAttachment(postAzione));
		return emailRequest;
	}

	private String replaceDinamici(String str) {
		return strReplaceHelper.replaceDinamici(str, istanza);
	}
	
	private EmailAttachment retrieveEmailAttachment(PostAzioneSendMail postAzioneSendMail) {		
		PostAzioneSendMailAllOfAttachments att =  postAzioneSendMail.getAttachments();
		EmailAttachment result = null;
		if (att != null) {
			result = new EmailAttachment(istanza.getIdIstanza(), idStoricoWorkflow,att.getIstanza(),att.getAllegati(),att.getAllegatiAzione());		
		}
		return result;
	}

}
