/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import it.csi.moon.moonbobl.util.decodifica.DecodificaLogonMode;

/**
 * Portali di pubblicazione dei moduli su modulistica (moon_ml_d_logon_mode)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class LogonMode {

	private Integer idLogonMode;
	private String codiceLogonMode;
	private String descLogonMode;

	
	public LogonMode() {
		super();
	}
	public LogonMode(Integer idLogonMode, String codiceLogonMode, String descLogonMode) {
		super();
		this.idLogonMode = idLogonMode;
		this.codiceLogonMode = codiceLogonMode;
		this.descLogonMode = descLogonMode;
	}
	public LogonMode(DecodificaLogonMode dLogonMode) {
		super();
		this.idLogonMode = dLogonMode.getId();
		this.codiceLogonMode = dLogonMode.getCodice();
		this.descLogonMode = dLogonMode.getDescrizione();
	}

	public Integer getIdLogonMode() {
		return idLogonMode;
	}
	public void setIdLogonMode(Integer idLogonMode) {
		this.idLogonMode = idLogonMode;
	}
	public String getCodiceLogonMode() {
		return codiceLogonMode;
	}
	public void setCodiceLogonMode(String codiceLogonMode) {
		this.codiceLogonMode = codiceLogonMode;
	}
	public String getDescLogonMode() {
		return descLogonMode;
	}
	public void setDescLogonMode(String descLogonMode) {
		this.descLogonMode = descLogonMode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idLogonMode == null) ? 0 : idLogonMode.hashCode());
		result = prime * result + ((codiceLogonMode == null) ? 0 : codiceLogonMode.hashCode());
		result = prime * result + ((descLogonMode == null) ? 0 : descLogonMode.hashCode());
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
		LogonMode other = (LogonMode) obj;
		if (idLogonMode == null) {
			if (other.idLogonMode != null)
				return false;
		} else if (!idLogonMode.equals(other.idLogonMode))
			return false;
		if (codiceLogonMode == null) {
			if (other.codiceLogonMode != null)
				return false;
		} else if (!codiceLogonMode.equals(other.codiceLogonMode))
			return false;
		if (descLogonMode == null) {
			if (other.descLogonMode != null)
				return false;
		} else if (!descLogonMode.equals(other.descLogonMode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Portale [idLogonMode=" + idLogonMode + ", codiceLogonMode=" + codiceLogonMode + ", descLogonMode="
				+ descLogonMode + "]";
	}

}
