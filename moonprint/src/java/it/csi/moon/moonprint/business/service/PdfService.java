/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.business.service;

import it.csi.moon.moonprint.exceptions.service.ServiceException;

public interface PdfService {

	public byte[] getPdfMock() throws ServiceException;

	public byte[] getPdf(String body) throws ServiceException;
	public byte[] getPdf(String template, String body) throws ServiceException;

}
