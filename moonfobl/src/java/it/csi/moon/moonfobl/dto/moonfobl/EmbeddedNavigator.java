/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

import it.csi.moon.moonfobl.util.decodifica.DecodificaEmbeddedService;

public class EmbeddedNavigator {

	private String service;
	private EmbeddedOptions options;
	private EmbeddedNavParams params;
	private String token;

	public EmbeddedNavigator() {
		super();
	}
	public EmbeddedNavigator(String service, EmbeddedOptions options, EmbeddedNavParams params) {
		super();
		this.service = service;
		this.options = options;
		this.params = params;
	}
	public EmbeddedNavigator(EmbeddedNavigator embeddedNavigatorToClone) {
		super();
		this.service = embeddedNavigatorToClone.getService();
		this.options = embeddedNavigatorToClone.getOptions()==null?null:new EmbeddedOptions(embeddedNavigatorToClone.getOptions());
		this.params = embeddedNavigatorToClone.getParams()==null?null:new EmbeddedNavParams(embeddedNavigatorToClone.getParams());
		this.token = embeddedNavigatorToClone.getToken();
	}
	public EmbeddedNavigator(DecodificaEmbeddedService embeddedService) {
		this.service = embeddedService.getCodice();
	}
	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}
	/**
	 * @return the options
	 */
	public EmbeddedOptions getOptions() {
		return options;
	}
	/**
	 * @return the params
	 */
	public EmbeddedNavParams getParams() {
		return params;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(EmbeddedOptions options) {
		this.options = options;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(EmbeddedNavParams params) {
		this.params = params;
	}
	
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmbeddedNavigator other = (EmbeddedNavigator) obj;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmbeddedNavigator [service=" + service + ", options=" + options + ", params=" + params + ", token="
				+ token + "]";
	}
	



}
