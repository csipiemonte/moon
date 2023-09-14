/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.exceptions.business;

public class ItemNotFoundDAOException extends DAOException {
	
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MSG = "Elemento non trovato su db";
	
	public ItemNotFoundDAOException() {
		super(DEFAULT_MSG);
	}
	
	//
	public ItemNotFoundDAOException(String msg) {
		super(msg);
	}
	public ItemNotFoundDAOException(String msg, String code) {
		super(msg,code);	
	}
	public ItemNotFoundDAOException(String msg, String code, String title) {
		super(msg,code,title);	
	}

	//
	public ItemNotFoundDAOException(Throwable thr) {
		super(thr.getMessage());
	}
	public ItemNotFoundDAOException(Throwable msg, String code) {
		super(msg,code);	
	}
	public ItemNotFoundDAOException(Throwable msg, String code, String title) {
		super(msg,code,title);	
	}

}
