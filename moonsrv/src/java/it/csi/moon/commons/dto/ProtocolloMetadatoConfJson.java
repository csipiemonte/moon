/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Map;

public class ProtocolloMetadatoConfJson {
	
	private String type;
	private Map<String,String> metadati;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String,String> getMetadati() {
		return metadati;
	}
	public void setMetadati(Map<String,String> metadati) {
		this.metadati = metadati;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((metadati == null) ? 0 : metadati.hashCode());
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
		ProtocolloMetadatoConfJson other = (ProtocolloMetadatoConfJson) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (metadati == null) {
			if (other.metadati != null)
				return false;
		} else if (!metadati.equals(other.metadati))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ProtocolloMetadatoConfJson [type=" + type + ", metadati=" + metadati + "]";
	}
	
}