/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.exceptions.service;

import java.io.Serializable;

/**
 * @author franc
 *
 */
public class ResourceNotFoundException extends ServiceException implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Errore: Risorsa non trovata";
	
	public ResourceNotFoundException() {
		super(MSG);
	}
	 public ResourceNotFoundException(String msg)
	 {
	  super(msg);
	 }
}
