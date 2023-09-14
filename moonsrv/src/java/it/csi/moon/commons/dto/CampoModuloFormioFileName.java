/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

/**
 * Campo di un Modulo con list of formIoFileNames
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 31/08/2021 - versione iniziale
 */
public class CampoModuloFormioFileName extends CampoModulo {
  
	private String formIoFileName;
	
	public CampoModuloFormioFileName() {
		super();
	}
	public CampoModuloFormioFileName(CampoModulo cm, String formIoFileName) {
		super();
		setLabel(cm.getLabel());
		setKey(cm.getKey());
		setType(cm.getType());
		setFullKey(cm.getFullKey());
		setGridKey(cm.getGridKey());
		setGridFullKey(cm.getGridFullKey());
		this.formIoFileName = formIoFileName;
	}
	
	public String getFormIoFileName() {
		return formIoFileName;
	}
	public void setFormIoFileName(String formIoFileName) {
		this.formIoFileName = formIoFileName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((formIoFileName == null) ? 0 : formIoFileName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampoModuloFormioFileName other = (CampoModuloFormioFileName) obj;
		if (formIoFileName == null) {
			if (other.formIoFileName != null)
				return false;
		} else if (!formIoFileName.equals(other.formIoFileName))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CampoModuloFormioFileName {\n");
		sb.append("    super: ").append(super.toString()).append("\n");
		sb.append("    formIoFileName: ").append(toIndentedString(formIoFileName)).append("\n");
		sb.append("}");
		return sb.toString();
	}
  
}

