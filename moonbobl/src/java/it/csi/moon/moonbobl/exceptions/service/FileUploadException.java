/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.exceptions.service;

import org.apache.commons.lang.StringUtils;

import it.csi.moon.moonbobl.exceptions.business.BaseException;

public class FileUploadException extends ServiceException {
	
	private static final long serialVersionUID = 1L;
	private final static String MSG = "Errore: Caricamento file";

	public FileUploadException() {
		super(MSG);
	}
	public FileUploadException(String msg) {
		super((StringUtils.isNotEmpty(msg))?msg:MSG);
	}
	public FileUploadException(BaseException be) {
		super(be);
	}
	public FileUploadException(BaseException be, String msg) {
		super((StringUtils.isNotEmpty(msg))?msg:be.getMessage(), be.getCode(), be.getTitle());
	}
}
