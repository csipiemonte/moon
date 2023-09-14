/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;

public class EmailRequestValidator {

	private static final String CLASS_NAME = "EmailRequestValidator";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
    public static final String EMAIL_VALIDATE_REGEX =
            "(([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4}))((([;,]){1}"
            +"([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})){0,10})";
    
	/**
     * Valida Request to, cc, bcc, subject, text
     * @param request
     */
    public void valida(EmailRequest request) throws BusinessException {
    	StringBuilder sb = new StringBuilder();
		if (request==null) {
			LOG.error("[" + CLASS_NAME + "::validaEmailRequest] request null.");
			throw new BusinessException("ValidaEmailRequestException");
		}
		//
		if (!isValidEmail(request.getTo())) {
			sb.append("\nTo:").append(request.getTo()).append(" is not valide.");
		}
		if (StringUtils.isNotEmpty(request.getCc()) && !isValidEmail(request.getCc())) {
			sb.append("\nCc:").append(request.getCc()).append(" is presente and not valide.");
		}
		if (StringUtils.isNotEmpty(request.getBcc()) && !isValidEmail(request.getBcc())) {
			sb.append("\nBcc:").append(request.getBcc()).append(" is presente and not valide.");
		}
		if (StringUtils.isEmpty(request.getSubject())) {
			sb.append("\nSubject:").append(request.getSubject()).append(" is empty.");
		}
		if (StringUtils.isEmpty(request.getText())) {
			sb.append("\nText:").append(request.getText()).append(" is empty.");
		}
		//
		if (sb.length() != 0) {
			LOG.error("[" + CLASS_NAME + "::validaEmailRequest] " + sb.toString());
			throw new BusinessException("ValidaEmailRequestException");
		}
	}
    public static boolean isValidEmail(String email) {
        return email!=null?Pattern.matches(EMAIL_VALIDATE_REGEX, email.replaceAll("\\s", "")):false;
    }
    
}
