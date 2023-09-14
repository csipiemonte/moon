/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

public class EmbeddedOptions {
	
	private String urlEsciModulo = null;
	private String  urlTornaIstanze = null;
	private String  urlErrore= null;
//	private List<String> tabIstanze = null;

	public EmbeddedOptions() {
		super();
	}
	public EmbeddedOptions(EmbeddedOptions embeddedOptionsToClone) {
		super();
		this.urlEsciModulo = embeddedOptionsToClone.getUrlEsciModulo();
		this.urlTornaIstanze = embeddedOptionsToClone.getUrlTornaIstanze();
		this.urlErrore= embeddedOptionsToClone.getUrlErrore();
	}
	
	/**
	 * @return the urlEsciModulo
	 */
	public String getUrlEsciModulo() {
		return urlEsciModulo;
	}

	/**
	 * @return the urlTornaIstanze
	 */
	public String getUrlTornaIstanze() {
		return urlTornaIstanze;
	}

	/**
	 * @return the urlErrore
	 */
	public String getUrlErrore() {
		return urlErrore;
	}

	/**
	 * @return the tabIstanze
	 */
//	public List<String> getTabIstanze() {
//		return tabIstanze;
//	}

	/**
	 * @param urlEsciModulo the urlEsciModulo to set
	 */
	public void setUrlEsciModulo(String urlEsciModulo) {
		this.urlEsciModulo = urlEsciModulo;
	}

	/**
	 * @param urlTornaIstanze the urlTornaIstanze to set
	 */
	public void setUrlTornaIstanze(String urlTornaIstanze) {
		this.urlTornaIstanze = urlTornaIstanze;
	}

	/**
	 * @param urlErrore the urlErrore to set
	 */
	public void setUrlErrore(String urlErrore) {
		this.urlErrore = urlErrore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((urlErrore == null) ? 0 : urlErrore.hashCode());
		result = prime * result + ((urlEsciModulo == null) ? 0 : urlEsciModulo.hashCode());
		result = prime * result + ((urlTornaIstanze == null) ? 0 : urlTornaIstanze.hashCode());
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
		EmbeddedOptions other = (EmbeddedOptions) obj;
		if (urlErrore == null) {
			if (other.urlErrore != null)
				return false;
		} else if (!urlErrore.equals(other.urlErrore))
			return false;
		if (urlEsciModulo == null) {
			if (other.urlEsciModulo != null)
				return false;
		} else if (!urlEsciModulo.equals(other.urlEsciModulo))
			return false;
		if (urlTornaIstanze == null) {
			if (other.urlTornaIstanze != null)
				return false;
		} else if (!urlTornaIstanze.equals(other.urlTornaIstanze))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmbeddedOptions [urlEsciModulo=" + urlEsciModulo + ", urlTornaIstanze=" + urlTornaIstanze
				+ ", urlErrore=" + urlErrore + "]";
	}

	/**
	 * @param tabIstanze the tabIstanze to set
	 */
//	public void setTabIstanze(List<String> tabIstanze) {
//		this.tabIstanze = tabIstanze;
//	}


}
