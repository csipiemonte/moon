/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * Entity della tabella utente 
 * <br>
 * <br>Tabella:  <code>moon_fo_t_utente</code>
 * <br>PK:  <code>idUtente</code>
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class UtenteEntity {
	
	private Long idUtente;
	private String identificativoUtente;
	private String nome;
	private String cognome;
	private String username;
	private String password;
	private String email;
	private String flAttivo;
	private Integer idTipoUtente;
	private Date dataIns;
	private Date dataUpd;
	private String attoreIns;
	private String attoreUpd;
	
	public UtenteEntity() {
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

	public Date getDataIns() {
		return dataIns;
	}

	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}

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

}
