/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Objects;

public class Fruitore {
	
	private Integer idFruitore = null;
	private Long idModulo = null;
		
	public Integer getIdFruitore() {
		return idFruitore;
	}

	public void setIdFruitore(Integer idFruitore) {
		this.idFruitore = idFruitore;
	}

	public Long getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}

	
	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Fruitore obj = (Fruitore) o;
	    return Objects.equals(idFruitore, obj.idFruitore) &&
	        Objects.equals(idModulo, obj.idModulo); 
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(idFruitore, idModulo);
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Fruitore {\n");	    
	    sb.append("    idFruitore: ").append(toIndentedString(idFruitore)).append("\n");
	    sb.append("    idModulo: ").append(toIndentedString(idModulo)).append("\n");
	    sb.append("}");
	    return sb.toString();
	}
	
	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
	
}
