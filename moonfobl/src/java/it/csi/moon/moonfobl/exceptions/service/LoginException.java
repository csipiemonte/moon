/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.exceptions.service;

import java.io.Serializable;
import java.io.StringWriter;

/**
 * Eccezione rilanciata dal servizio
 * @author Laurent
 */
public class LoginException extends RuntimeException implements Serializable {
	/**
	 * 
	 */
	private String errCode = "ERR";
	
	private static final long serialVersionUID = 1L;

	public LoginException()
	 {
	  super();
	 }
	
	 public LoginException(String message, Throwable cause)
	 {
	  super(message, cause);
	 }
	 
	 public LoginException(String message, Throwable cause, String code)
	 {
	  super(message, cause);
	  this.errCode = code;
	 }
	 
	 public LoginException(Throwable cause)
	 {
	  super(cause);
	 }
	 
	 public LoginException(String msg)
	 {
	  super(msg);
	 }
	 
	 public LoginException(String msg,String code)
	 {
	  super(msg);
	  this.errCode = code;
	 }
	 
	 
	 public String getInternalErrorMessage()
	 {
	  Throwable cause = this.getCause();
	  if(cause != null)
	  {
	   StringWriter sw = new StringWriter();
	   return sw.toString();
	  }
	  else return getMessage();
		  
	 }

	public String getErrCode() {
		return errCode;
	}
	 
	 
}
