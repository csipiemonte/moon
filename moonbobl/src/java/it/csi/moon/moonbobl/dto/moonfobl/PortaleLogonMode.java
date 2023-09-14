/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

/**
 * Portali di pubblicazione dei moduli su modulistica (moon_ml_d_logon_mode)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class PortaleLogonMode {

	private Long idModulo;
	private Portale portale;
	private LogonMode logonMode;
	private String filtro;

	public PortaleLogonMode() {
		super();
	}
	public PortaleLogonMode(Long idModulo, Portale portale, LogonMode logonMode, String filtro) {
		super();
		this.idModulo = idModulo;
		this.portale = portale;
		this.logonMode = logonMode;
		this.filtro = filtro;
	}

	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Portale getPortale() {
		return portale;
	}
	public void setPortale(Portale portale) {
		this.portale = portale;
	}
	public LogonMode getLogonMode() {
		return logonMode;
	}
	public void setLogonMode(LogonMode logonMode) {
		this.logonMode = logonMode;
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
		result = prime * result + ((idModulo == null) ? 0 : idModulo.hashCode());
		result = prime * result + ((portale == null) ? 0 : portale.hashCode());
		result = prime * result + ((logonMode == null) ? 0 : logonMode.hashCode());
		result = prime * result + ((filtro == null) ? 0 : filtro.hashCode());
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
		PortaleLogonMode other = (PortaleLogonMode) obj;
		if (idModulo == null) {
			if (other.idModulo != null)
				return false;
		} else if (!idModulo.equals(other.idModulo))
			return false;
		if (portale == null) {
			if (other.portale != null)
				return false;
		} else if (!portale.equals(other.portale))
			return false;
		if (logonMode == null) {
			if (other.logonMode != null)
				return false;
		} else if (!logonMode.equals(other.logonMode))
			return false;
		if (filtro == null) {
			if (other.filtro != null)
				return false;
		} else if (!filtro.equals(other.filtro))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PortaleLogonMode [idModulo=" + idModulo + ", portale=" + portale + ", logonMode=" + logonMode
				+ ", filtro=" + filtro + "]";
	}

}
