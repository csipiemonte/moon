/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.dto;
/*
 * Rappresenta Access Token per
 * accedere ai serivi esposti da APIMINT
 * https://gitlab.ecosis.csi.it/technical-components/tecnologie/tecnologie-integrazione/wso2apimanager/howtos/blob/master/Linee_guida_APIMINT.md#appendice_a_token_api
 * 
 */
public class ApimintToken {
	
	private String access_token;
	private String scope;
	private String token_type;
	private Long expires_in;
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public Long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}

}
