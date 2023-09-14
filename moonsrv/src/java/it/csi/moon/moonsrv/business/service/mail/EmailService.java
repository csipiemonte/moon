/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mail;

import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

public interface EmailService {

	public void sendMail(EmailRequest emailRequest) throws BusinessException;
	public void sendMailWithAttach(EmailRequest emailRequest) throws BusinessException;
	public void sendMailHtmlAndText(EmailRequest emailRequest) throws BusinessException;
	public void sendMailHtmlAndTextWithAttach(EmailRequest emailRequest) throws BusinessException;
	public void sendMailForApi(EmailRequest emailRequest) throws BusinessException;
	
}
