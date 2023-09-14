/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.exceptions.business;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MSG = "Errore generico ";
	private String code = "MOONFOBL-00000";
	private String title = "ERRORE";
    
	public BaseException() {
		super(DEFAULT_MSG);
	}

	//
	public BaseException(String msg) {
		super(msg);
	}
	public BaseException(String msg, String code) {
		super(msg);
		this.code = code;
	}
	public BaseException(String msg, String code, String title) {
		super(msg);
		this.code = code;
		this.title = title;
	}

	//
	public BaseException(Throwable msg) {
		super(msg);
	}
	public BaseException(Throwable msg, String code) {
		super(msg);
		this.code = code;
	}
	public BaseException(Throwable msg, String code, String title) {
		super(msg);
		this.code = code;
		this.title = title;
	}
	
	//
	public String getCode() {
		return code;
	}
	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "BaseException [msg=" + getMessage() + " , code=" + code + " , title=" + title + "]";
	}

	public static enum ErrType {
		SOGGETTO_NON_RESIDENTE ("MASCH-00110", "NON RISULTI RESIDENTE A TORINO, SE DI RECENTE HAI FATTO UNA PRATICA DI IMMIGRAZIONE RIVOLGITI AL CALL CENTER"),
		DOCUMENTO_NON_CONGRUENTI ("MASCH-00111", "NUMERO O DATA DOCUMENTO NON CONGRUENTI RIVOLGITI AL CALL CENTER"),
		DOMANDA_GIA_PRESENTATA ("MDLSTC-00112", "UN COMPONENTE DEL TUO NUCLEO FAMILIARE HA GIA’ PRESENTATO LA DOMANDA"), 
		CREDENZIALI_NON_VALIDE ("MDLSTC-00113", "CREDENZIALI LOGIN/PIN NON VALIDE"), 
		RICHIESTA_E_TOKEN_NON_VALIDE ("MDLSTC-00114", "RICHIESTA NON VALIDA CON IL TOKEN"),
		;
		
	    private final String code;
	    private final String msg;
	    ErrType(String code, String msg) {
	        this.code = code;
	        this.msg = msg;
	    }
	    public String getCode() {
	    	return code;
	    }
	    public String getMsg() {
	    	return msg;
	    }
	    public String toString() {
	    	return code + " - " + msg;
	    }
	}
	
}
