/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.exceptions.business;

public class ItemNotFoundDAOException extends DAOException {
	private static final long serialVersionUID = 1L;
	private final static String DEFAULT_MSG = "Elemento non trovato su db";
	public ItemNotFoundDAOException() {
		super(DEFAULT_MSG);
		// TODO Auto-generated constructor stub
	}
	public ItemNotFoundDAOException(String msg) {
		super(DEFAULT_MSG + " - "+ msg);
		// TODO Auto-generated constructor stub
	}
	public ItemNotFoundDAOException(Throwable msg) {
		super(DEFAULT_MSG + " - "+msg.getMessage());
		// TODO Auto-generated constructor stub
	}

}
