/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.exceptions.business;
/*
 * Eccezione base sollevata dal layer persistenza
 * 
 */
public class DAOException extends BaseException {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MSG = "Riscontrata eccezione durante operazione dao";
	private int status = -1;
	
	public DAOException() {
		super(DEFAULT_MSG);
	}
	
	//
	public DAOException(String msg) {
		super(msg);
	}
	public DAOException(String msg, String code) {
		super(msg,code);	
	}
	public DAOException(String msg, String code, String title) {
		super(msg,code,title);	
	}
	public DAOException(int status, String msg ) {
		super(msg);
		this.status = status;		
	}

	//
	public DAOException(Throwable thr) {
		super(thr.getMessage());
	}
	public DAOException(Throwable msg, String code) {
		super(msg,code);	
	}
	public DAOException(Throwable msg, String code, String title) {
		super(msg,code,title);	
	}

	//
	public int getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		return "DAOException [status=" + status + " , msg=" + getMessage() + " , code=" + getCode() + " , title=" + getTitle() + "]";
	}
}
