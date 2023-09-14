/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;
/*
 * DTO per dati aggiuntivi relativi a istanza
 * mapping su colonna XXXXX della tabella ISTANZE
 */

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DatiAggiuntivi implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date dataOraLogin = null;
	private String provider = "N.D.";
	private DatiAggiuntiviHeaders headers = null;
	
	public DatiAggiuntivi() {
		super();
	}
	public DatiAggiuntivi(DatiAggiuntivi datiAggiuntiviToClone) {
		super();
		this.dataOraLogin = datiAggiuntiviToClone.getDataOraLogin();
		this.provider = datiAggiuntiviToClone.getProvider();
		this.headers = datiAggiuntiviToClone.getHeaders()==null?null:new DatiAggiuntiviHeaders(datiAggiuntiviToClone.getHeaders());
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")  
	public Date getDataOraLogin() {
		return dataOraLogin;
	}
	public void setDataOraLogin(Date dataOraLogin) {
		this.dataOraLogin = dataOraLogin;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public DatiAggiuntiviHeaders getHeaders() {
		return headers;
	}
	public void setHeaders(DatiAggiuntiviHeaders headers) {
		this.headers = headers;
	}
	  
}
