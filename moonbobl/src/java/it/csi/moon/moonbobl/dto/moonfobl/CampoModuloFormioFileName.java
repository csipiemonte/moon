/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

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
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CampoModuloFormioFileNames {\n");
    sb.append("    super: ").append(super.toString()).append("\n");
    sb.append("    formIoFileName: ").append(toIndentedString(formIoFileName)).append("\n");
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

