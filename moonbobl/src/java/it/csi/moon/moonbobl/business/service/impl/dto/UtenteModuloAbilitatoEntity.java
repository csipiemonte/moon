/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import it.csi.moon.moonbobl.dto.moonfobl.UtenteModuloAbilitato;

/**
 * Principalmente campi di Utente (moon_fo_t_utente) ma non tutti
 * e i 2 campi dell'abilitazione (dataUpd e attoreUpd)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class UtenteModuloAbilitatoEntity {
  
	private Long idUtente;
	private String identificativoUtente;
	private String nome;
	private String cognome;
//	private String username;
//	private String password;
	private String email;
	private String flAttivo;
	private Integer idTipoUtente;
//	private Date dataIns;
//	private Date dataUpd;
//	private String attoreUpd;
	private Date dataUpdAbilitazione;
	private String attoreUpdAbilitazione;
	
	public UtenteModuloAbilitatoEntity() {
		super();
	}
	
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
//	public String getUsername() {
//		return username;
//	}
//	public void setUsername(String username) {
//		this.username = username;
//	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFlAttivo() {
		return flAttivo;
	}
	public void setFlAttivo(String flAttivo) {
		this.flAttivo = flAttivo;
	}
	public Integer getIdTipoUtente() {
		return idTipoUtente;
	}
	public void setIdTipoUtente(Integer idTipoUtente) {
		this.idTipoUtente = idTipoUtente;
	}
//	public Date getDataIns() {
//		return dataIns;
//	}
//	public void setDataIns(Date dataIns) {
//		this.dataIns = dataIns;
//	}
//	public Date getDataUpd() {
//		return dataUpd;
//	}
//	public void setDataUpd(Date dataUpd) {
//		this.dataUpd = dataUpd;
//	}
//	public String getAttoreUpd() {
//		return attoreUpd;
//	}
//	public void setAttoreUpd(String attoreUpd) {
//		this.attoreUpd = attoreUpd;
//	}
	public Date getDataUpdAbilitazione() {
		return dataUpdAbilitazione;
	}
	public void setDataUpdAbilitazione(Date dataUpdAbilitazione) {
		this.dataUpdAbilitazione = dataUpdAbilitazione;
	}
	public String getAttoreUpdAbilitazione() {
		return attoreUpdAbilitazione;
	}
	public void setAttoreUpdAbilitazione(String attoreUpdAbilitazione) {
		this.attoreUpdAbilitazione = attoreUpdAbilitazione;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    UtenteModuloAbilitatoEntity utente = (UtenteModuloAbilitatoEntity) o;
	    return Objects.equals(idUtente, utente.idUtente) &&
	        Objects.equals(identificativoUtente, utente.identificativoUtente) &&
	        Objects.equals(nome, utente.nome) &&
	        Objects.equals(cognome, utente.cognome) &&
//	        Objects.equals(username, utente.username) &&
//	        Objects.equals(password, utente.password) &&
	        Objects.equals(email, utente.email) &&
	        Objects.equals(flAttivo, utente.flAttivo) &&
	        Objects.equals(idTipoUtente, utente.idTipoUtente) &&
//	        Objects.equals(dataIns, utente.dataIns) &&
//	        Objects.equals(dataUpd, utente.dataUpd) &&
//	        Objects.equals(attoreUpd, utente.attoreUpd) &&
	        Objects.equals(dataUpdAbilitazione, utente.dataUpdAbilitazione) &&
	        Objects.equals(attoreUpdAbilitazione, utente.attoreUpdAbilitazione);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idUtente, identificativoUtente, nome, cognome, /*username, password,*/ email, flAttivo, idTipoUtente, /*dataIns, dataUpd, attoreUpd*/ dataUpdAbilitazione, attoreUpdAbilitazione);
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Utente {\n");
	    
	    sb.append("    idUtente: ").append(toIndentedString(idUtente)).append("\n");
	    sb.append("    identificativoUtente: ").append(toIndentedString(identificativoUtente)).append("\n");
	    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
	    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
//	    sb.append("    username: ").append(toIndentedString(username)).append("\n");
//	    sb.append("    password: ").append(toIndentedString(password)).append("\n");
	    sb.append("    email: ").append(toIndentedString(email)).append("\n");
	    sb.append("    flAttivo: ").append(toIndentedString(flAttivo)).append("\n");
	    sb.append("    idTipoUtente: ").append(toIndentedString(idTipoUtente)).append("\n");
//	    sb.append("    dataIns: ").append(toIndentedString(dataIns)).append("\n");
//	    sb.append("    dataUpd: ").append(toIndentedString(dataUpd)).append("\n");
//	    sb.append("    attoreUpd: ").append(toIndentedString(attoreUpd)).append("\n");
	    sb.append("    dataUpd: ").append(toIndentedString(dataUpdAbilitazione)).append("\n");
	    sb.append("    attoreUpd: ").append(toIndentedString(attoreUpdAbilitazione)).append("\n");
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

