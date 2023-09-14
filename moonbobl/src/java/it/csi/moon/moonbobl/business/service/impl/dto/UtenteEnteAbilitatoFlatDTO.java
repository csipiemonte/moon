/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * DTO di query specifica Utente/Area/Ruoli su un ente
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class UtenteEnteAbilitatoFlatDTO {
	
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
	private String attoreUpd;
	//
	private Long idEnte;
	private String codiceEnte;
	private String nomeEnte;
	private String descrizioneEnte;
	private Integer idTipoEnte;
	//
	private Long idArea = null;
	private String codiceArea = null;
	private String nomeArea = null;
	private Integer idRuolo = null;
	private String codiceRuolo = null;
	private String nomeRuolo = null;
	//
	private Date dataUpdAbilitazione;
	private String attoreUpdAbilitazione;
	
	public UtenteEnteAbilitatoFlatDTO() {
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
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}

	//
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public String getCodiceEnte() {
		return codiceEnte;
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public String getNomeEnte() {
		return nomeEnte;
	}
	public void setNomeEnte(String nomeEnte) {
		this.nomeEnte = nomeEnte;
	}
	public String getDescrizioneEnte() {
		return descrizioneEnte;
	}
	public void setDescrizioneEnte(String descrizioneEnte) {
		this.descrizioneEnte = descrizioneEnte;
	}
	public Integer getIdTipoEnte() {
		return idTipoEnte;
	}
	public void setIdTipoEnte(Integer idTipoEnte) {
		this.idTipoEnte = idTipoEnte;
	}
	// Area
	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
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
	// Ruolo
	public Integer getIdRuolo() {
		return idRuolo;
	}
	public void setIdRuolo(Integer idRuolo) {
		this.idRuolo = idRuolo;
	}
	public String getCodiceRuolo() {
		return codiceRuolo;
	}
	public void setCodiceRuolo(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}
	public String getNomeRuolo() {
		return nomeRuolo;
	}
	public void setNomeRuolo(String nomeRuolo) {
		this.nomeRuolo = nomeRuolo;
	}

	// UAR
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
	
}
