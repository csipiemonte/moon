/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.exceptions.business;
/*
 * Eccezione base sollevata dal layer persistenza
 * 
 */
public class DAOException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String DEFAULT_MSG = "Riscontrata eccezione durante operazione db";
	public DAOException() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DAOException(String msg) {
		super(DEFAULT_MSG + " - "+ msg);
		// TODO Auto-generated constructor stub
	}
	public DAOException(Throwable msg) {
		super(DEFAULT_MSG + " - "+msg.getMessage());
		// TODO Auto-generated constructor stub
	}
	
	

}
