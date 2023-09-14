/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonprint.exceptions.service;

import java.io.Serializable;
import java.io.StringWriter;

/**
 * @author franc
 * Eccezione rilanciata dal servizio
 * 
 */
public class ServiceException extends RuntimeException implements Serializable {
	/**
	 * 
	 */
	private String errCode = "ERR";
	
	private static final long serialVersionUID = 1L;

	public ServiceException()
	 {
	  super();
	 }
	
	 public ServiceException(String message, Throwable cause)
	 {
	  super(message, cause);
	 }
	 
	 public ServiceException(String message, Throwable cause,String code)
	 {
	  super(message, cause);
	  this.errCode = code;
	 }
	 
	 public ServiceException(Throwable cause)
	 {
	  super(cause);
	 }
	 
	 public ServiceException(String msg)
	 {
	  super(msg);
	 }
	 
	 public ServiceException(String msg,String code)
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
