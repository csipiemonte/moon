/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Objects;

public class NuovaVersioneModuloRequest {

	private String versione = null;
	private Long idVersionePartenza = null;
	
	public String getVersione() {
		return versione;
	}
	public void setVersione(String versione) {
		this.versione = versione;
	}
	public Long getIdVersionePartenza() {
		return idVersionePartenza;
	}
	public void setIdVersionePartenza(Long idVersionePartenza) {
		this.idVersionePartenza = idVersionePartenza;
	}
	
	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    NuovaVersioneModuloRequest nvmReq = (NuovaVersioneModuloRequest) o;
	    return 
	    	Objects.equals(versione, nvmReq.versione) && 
	    	Objects.equals(idVersionePartenza, nvmReq.idVersionePartenza);
	  }


	  @Override
	  public int hashCode() {
	    return Objects.hash(versione, idVersionePartenza);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class NuovaVersioneModuloRequest {\n");
	    
	    sb.append("    versione: ").append(toIndentedString(versione)).append("\n");    
	    sb.append("    idVersionePartenza: ").append(toIndentedString(idVersionePartenza)).append("\n"); 
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
