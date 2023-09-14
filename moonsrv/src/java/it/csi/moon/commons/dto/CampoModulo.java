/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Objects;

/**
 * Campo di un Modulo
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
public class CampoModulo {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
	private String label = null;
	private String key = null;
	private String type = null;
	private String fullKey = null;
	private String gridKey = null;
	private String gridFullKey = null;
	
	public CampoModulo() {
		super();
	}
	public CampoModulo(String label, String key, String type, String fullKey) {
		super();
		this.label = label;
		this.key = key;
		this.type = type;
		this.fullKey = fullKey;
	}
	public CampoModulo(String label, String key, String type, String fullKey, String gridKey, String gridFullKey) {
		super();
		this.label = label;
		this.key = key;
		this.type = type;
		this.fullKey = fullKey;
		this.gridKey = gridKey;
		this.gridFullKey = gridFullKey;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFullKey() {
		return fullKey;
	}
	public void setFullKey(String fullKey) {
		this.fullKey = fullKey;
	}
	public String getGridKey() {
		return gridKey;
	}
	public void setGridKey(String gridKey) {
		this.gridKey = gridKey;
	}
	public String getGridFullKey() {
		return gridFullKey;
	}
	public void setGridFullKey(String gridFullKey) {
		this.gridFullKey = gridFullKey;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    CampoModulo obj = (CampoModulo) o;
	    return 
	    	Objects.equals(label, obj.label) &&
	    	Objects.equals(key, obj.type) &&
	        Objects.equals(type, obj.type) &&
	        Objects.equals(fullKey, obj.fullKey) &&
	    	Objects.equals(gridKey, obj.gridKey) &&
	        Objects.equals(gridFullKey, obj.gridFullKey);
	}


  @Override
  public int hashCode() {
    return Objects.hash(label, key, type, fullKey, gridKey, gridFullKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CampoModulo {\n");
    
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    fullKey: ").append(toIndentedString(fullKey)).append("\n");
    sb.append("    gridKey: ").append(toIndentedString(gridKey)).append("\n");
    sb.append("    gridFullKey: ").append(toIndentedString(gridFullKey)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  protected String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

