/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Processi da assegnare ai moduli (moon_wf_d_processo)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Processo {

	private Long idProcesso = null;
	private String codiceProcesso = null;
	private String nomeProcesso = null;
	private String descProcesso = null;
	private String flagAttivo = null;
	private Date dataUpd = null;
	private String attoreUpd = null;
	private List<Workflow> workflows = null;
	
	public Processo() {
	}
	
	public Processo(Long idProcesso, String codiceProcesso, String nomeProcesso, String descProcesso) {
		this.idProcesso = idProcesso;
		this.codiceProcesso = codiceProcesso;
		this.nomeProcesso = nomeProcesso;
		this.descProcesso = descProcesso;
		this.flagAttivo = "S";
	}
	
	public Processo(Long idProcesso, String codiceProcesso, String nomeProcesso, String descProcesso, String flagAttivo, Date dataUpd, String attoreUpd) {
		this.idProcesso = idProcesso;
		this.codiceProcesso = codiceProcesso;
		this.nomeProcesso = nomeProcesso;
		this.descProcesso = descProcesso;
		this.flagAttivo = flagAttivo;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Long getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}
	public String getCodiceProcesso() {
		return codiceProcesso;
	}
	public void setCodiceProcesso(String codiceProcesso) {
		this.codiceProcesso = codiceProcesso;
	}
	public String getNomeProcesso() {
		return nomeProcesso;
	}
	public void setNomeProcesso(String nomeProcesso) {
		this.nomeProcesso = nomeProcesso;
	}
	public String getDescProcesso() {
		return descProcesso;
	}
	public void setDescProcesso(String descProcesso) {
		this.descProcesso = descProcesso;
	}
	public String getFlagAttivo() {
		return flagAttivo;
	}
	public void setFlagAttivo(String flagAttivo) {
		this.flagAttivo = flagAttivo;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
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
	public List<Workflow> getWorkflows() {
		return workflows;
	}
	public void setWorkflows(List<Workflow> workflows) {
		this.workflows = workflows;
	}

	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Processo obj = (Processo) o;
	    return 
	    	Objects.equals(idProcesso, obj.idProcesso) &&
	    	Objects.equals(codiceProcesso, obj.codiceProcesso) &&
	    	Objects.equals(nomeProcesso, obj.nomeProcesso) &&
	    	Objects.equals(descProcesso, obj.descProcesso) &&
	    	Objects.equals(flagAttivo, obj.flagAttivo) &&
	    	Objects.equals(dataUpd, obj.dataUpd) &&
	    	Objects.equals(attoreUpd, obj.attoreUpd) ;
	  }


	  @Override
	  public int hashCode() {
	    return Objects.hash(idProcesso, codiceProcesso, nomeProcesso, descProcesso, flagAttivo, dataUpd, attoreUpd);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Processo {\n");
	    
	    sb.append("    idProcesso: ").append(toIndentedString(idProcesso)).append("\n");
	    sb.append("    codiceProcesso: ").append(toIndentedString(codiceProcesso)).append("\n");
	    sb.append("    nomeProcesso: ").append(toIndentedString(nomeProcesso)).append("\n");
	    sb.append("    descProcesso: ").append(toIndentedString(descProcesso)).append("\n");
	    sb.append("    flagAttivo: ").append(toIndentedString(flagAttivo)).append("\n");
	    sb.append("    dataUpd: ").append(toIndentedString(dataUpd)).append("\n");
	    sb.append("    attoreUpd: ").append(toIndentedString(attoreUpd)).append("\n");
	    sb.append("    workflows: ").append(toIndentedString(workflows)).append("\n");
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
