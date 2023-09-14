/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dto;

public class LogonModeEntity {

	private String codiceModulo;
	private String nomePortale;
	private String logonMode;
	private String filtro;
	
	public String getCodiceModulo() {
		return codiceModulo;
	}
	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}
	public String getNomePortale() {
		return nomePortale;
	}
	public void setNomePortale(String nomePortale) {
		this.nomePortale = nomePortale;
	}
	public String getLogonMode() {
		return logonMode;
	}
	public void setLogonMode(String loginMode) {
		this.logonMode = loginMode;
	}
	
	public String getFiltro() {
		return filtro;
	}
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codiceModulo == null) ? 0 : codiceModulo.hashCode());
		result = prime * result + ((filtro == null) ? 0 : filtro.hashCode());
		result = prime * result + ((logonMode == null) ? 0 : logonMode.hashCode());
		result = prime * result + ((nomePortale == null) ? 0 : nomePortale.hashCode());
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
		LogonModeEntity other = (LogonModeEntity) obj;
		if (codiceModulo == null) {
			if (other.codiceModulo != null)
				return false;
		} else if (!codiceModulo.equals(other.codiceModulo))
			return false;
		if (filtro == null) {
			if (other.filtro != null)
				return false;
		} else if (!filtro.equals(other.filtro))
			return false;
		if (logonMode == null) {
			if (other.logonMode != null)
				return false;
		} else if (!logonMode.equals(other.logonMode))
			return false;
		if (nomePortale == null) {
			if (other.nomePortale != null)
				return false;
		} else if (!nomePortale.equals(other.nomePortale))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "LogonModeEntity [codiceModulo=" + codiceModulo + ", nomePortale=" + nomePortale + ", logonMode="
				+ logonMode + ", filtro=" + filtro + "]";
	}
	
}
