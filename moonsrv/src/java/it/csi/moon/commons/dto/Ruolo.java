/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Ruolo (moon_fo_d_ruolo)
 * <br>Utilizzato per la gestione degli accessi delle componente che lo richiedono : moonbuilder, moonbo
 * <br>
 * <br>Tabella:  <code>moon_fo_d_ruolo</code>
 * <br>PK:  <code>idRuolo</code>
 * <br>AK:  <code>codiceRuolo</code>
 * <br>
 * <br>Esempi:
 * <br>1 - ADMIN - Amministratore del sistema
 * <br>2 - RESP	- Responsabile di un ente
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Ruolo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  
	private Integer idRuolo = null;
	private String codice = null;
	private String nome = null;
	private String descrizione = null;
	private Boolean flagAttivo = null;
	private Date dataUpd = null;
	private String attoreUpd = null;
	
	public Integer getIdRuolo() {
		return idRuolo;
	}
	public void setIdRuolo(Integer idRuolo) {
		this.idRuolo = idRuolo;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Boolean getFlagAttivo() {
		return flagAttivo;
	}
	public void setFlagAttivo(Boolean flagAttivo) {
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
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Ruolo ruolo = (Ruolo) o;
	    return Objects.equals(idRuolo, ruolo.idRuolo) &&
	        Objects.equals(codice, ruolo.codice) &&
	        Objects.equals(nome, ruolo.nome) &&
	        Objects.equals(descrizione, ruolo.descrizione) &&
	        Objects.equals(flagAttivo, ruolo.flagAttivo) &&
	        Objects.equals(dataUpd, ruolo.dataUpd) &&
	        Objects.equals(attoreUpd, ruolo.attoreUpd);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(idRuolo, codice, nome, descrizione, flagAttivo, dataUpd, attoreUpd);
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Ruolo {\n");
	    sb.append("    idRuolo: ").append(toIndentedString(idRuolo)).append("\n");
	    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
	    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
	    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
	    sb.append("    flagAttivo: ").append(toIndentedString(flagAttivo)).append("\n");
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

