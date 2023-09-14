/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.exceptions.service;

public class FormatException extends ServiceException {
	private static final long serialVersionUID = 1L;
	private final static String MSG = "Errore: Formato parametro non valido";
	
	public FormatException() {
		super(MSG);
	}
	 public FormatException(String msg)
	 {
	  super(msg);
	 }
}
