/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Date;

import it.csi.moon.moonbobl.business.service.impl.dto.AreaEntity;

/**
 * Entity della tabella di decodifica delle area di un ente su quale sara collocato i moduli
 * <br>Un modulo potra essere solo su un area di un ente, ma potra essere su piu area di diversi enti.
 * <br>
 * <br>Tabella moon_fo_d_area
 * <br>PK: idArea
 * <br>AK: idEnte,codiceArea
 * 
 * @see AreaEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Area {

	private Long idArea;
	private Long idEnte;
	private String codiceArea;
	private String nomeArea;
	private Date dataUpd;
	private String attoreUpd;
	
	public Area() {	
	}
	
	public Area(Long idArea, Long idEnte, String codiceArea, String nomeArea, Date dataUpd, String attoreUpd) {
		this.idArea = idArea;
		this.idEnte = idEnte;
		this.codiceArea = codiceArea;
		this.nomeArea = nomeArea;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public String getCodiceArea() {
		return codiceArea;
	}
	public void setCodiceArea(String codiceArea) {
		this.codiceArea = codiceArea;
	}
	public String getNomeArea() {
		return nomeArea;
	}
	public void setNomeArea(String nomeArea) {
		this.nomeArea = nomeArea;
	}
	public Date getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Date dataUpd) {
		this.dataUpd = dataUpd;
	}
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attoreUpd == null) ? 0 : attoreUpd.hashCode());
		result = prime * result + ((codiceArea == null) ? 0 : codiceArea.hashCode());
		result = prime * result + ((dataUpd == null) ? 0 : dataUpd.hashCode());
		result = prime * result + ((idArea == null) ? 0 : idArea.hashCode());
		result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
		result = prime * result + ((nomeArea == null) ? 0 : nomeArea.hashCode());
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
		Area other = (Area) obj;
		if (attoreUpd == null) {
			if (other.attoreUpd != null)
				return false;
		} else if (!attoreUpd.equals(other.attoreUpd))
			return false;
		if (codiceArea == null) {
			if (other.codiceArea != null)
				return false;
		} else if (!codiceArea.equals(other.codiceArea))
			return false;
		if (dataUpd == null) {
			if (other.dataUpd != null)
				return false;
		} else if (!dataUpd.equals(other.dataUpd))
			return false;
		if (idArea == null) {
			if (other.idArea != null)
				return false;
		} else if (!idArea.equals(other.idArea))
			return false;
		if (idEnte == null) {
			if (other.idEnte != null)
				return false;
		} else if (!idEnte.equals(other.idEnte))
			return false;
		if (nomeArea == null) {
			if (other.nomeArea != null)
				return false;
		} else if (!nomeArea.equals(other.nomeArea))
			return false;
		return true;
	}

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Area {\n");
	    sb.append("    idArea: ").append(toIndentedString(idArea)).append("\n");
	    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
	    sb.append("    codiceArea: ").append(toIndentedString(codiceArea)).append("\n");
	    sb.append("    nomeArea: ").append(toIndentedString(nomeArea)).append("\n");
	    sb.append("    dataUpd: ").append(toIndentedString(dataUpd)).append("\n");
	    sb.append("    attoreUpd: ").append(toIndentedString(attoreUpd)).append("\n");
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
