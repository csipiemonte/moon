/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Utente (moon_fo_t_utente)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Utente {
  
	private Long idUtente = null;
	private String identificativoUtente = null;
	private String nome = null;
	private String cognome = null;
	private String username = null;
	private String password = null;
	private String email = null;
	private Boolean flagAttivo = null;
	private TipoUtente tipoUtente = null;
	private Date dataIns = null;
	private Date dataUpd = null;
	private String attoreIns = null;
	private String attoreUpd = null;
	
	public Long getIdUtente() {
		return idUtente;
	}
	public void setIdUtente(Long idUtente) {
		this.idUtente = idUtente;
	}
	public String getIdentificativoUtente() {
		return identificativoUtente;
	}
	public void setIdentificativoUtente(String identificativoUtente) {
		this.identificativoUtente = identificativoUtente;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getFlagAttivo() {
		return flagAttivo;
	}
	public void setFlagAttivo(Boolean flagAttivo) {
		this.flagAttivo = flagAttivo;
	}
	public TipoUtente getTipoUtente() {
		return tipoUtente;
	}
	public void setTipoUtente(TipoUtente tipoUtente) {
		this.tipoUtente = tipoUtente;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Date dataUpd) {
		this.dataUpd = dataUpd;
	}
	public String getAttoreIns() {
		return attoreIns;
	}
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
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
	    Utente utente = (Utente) o;
	    return Objects.equals(idUtente, utente.idUtente) &&
	        Objects.equals(identificativoUtente, utente.identificativoUtente) &&
	        Objects.equals(nome, utente.nome) &&
	        Objects.equals(cognome, utente.cognome) &&
	        Objects.equals(username, utente.username) &&
	        Objects.equals(password, utente.password) &&
	        Objects.equals(email, utente.email) &&
	        Objects.equals(flagAttivo, utente.flagAttivo) &&
	        Objects.equals(tipoUtente, utente.tipoUtente) &&
	        Objects.equals(dataIns, utente.dataIns) &&
	        Objects.equals(dataUpd, utente.dataUpd) &&
	        Objects.equals(attoreIns, utente.attoreIns) &&
	        Objects.equals(attoreUpd, utente.attoreUpd);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idUtente, identificativoUtente, nome, cognome, username, password, email, flagAttivo, tipoUtente, dataIns, dataUpd, attoreIns, attoreUpd);
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Utente {\n");
	    
	    sb.append("    idUtente: ").append(toIndentedString(idUtente)).append("\n");
	    sb.append("    identificativoUtente: ").append(toIndentedString(identificativoUtente)).append("\n");
	    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
	    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
	    sb.append("    username: ").append(toIndentedString(username)).append("\n");
	    sb.append("    password: ").append(toIndentedString(password)).append("\n");
	    sb.append("    email: ").append(toIndentedString(email)).append("\n");
	    sb.append("    flagAttivo: ").append(toIndentedString(flagAttivo)).append("\n");
	    sb.append("    tipoUtente: ").append(toIndentedString(tipoUtente)).append("\n");
	    sb.append("    dataIns: ").append(toIndentedString(dataIns)).append("\n");
	    sb.append("    dataUpd: ").append(toIndentedString(dataUpd)).append("\n");
	    sb.append("    attoreIns: ").append(toIndentedString(attoreIns)).append("\n");
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

