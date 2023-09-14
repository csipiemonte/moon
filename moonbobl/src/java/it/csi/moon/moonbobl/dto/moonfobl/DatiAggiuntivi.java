/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;
/*
 * DTO per dati aggiuntivi relativi a istanza
 * mapping su colonna XXXXX della tabella ISTANZE
 */

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DatiAggiuntivi {

	private Date dataOraLogin = null;
	private String provider = "N.D.";
	private DatiAggiuntiviHeaders headers = null;
	 
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
