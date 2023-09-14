/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.dto;

public class DipendentiCsiEntity {
	
	private Long idDipendente;
	private String cognome;
	private String nome;
	private String matricola;
	private String codiceFiscale;
	private String fo_i_liv;
	private String resp_fo_i_liv;	
	private String fo_ii_liv;
	private String fo_iii_liv;
	private String fo_iv_liv;
	private String funzione_diretta;
	private String resp_funz_diretta;
	private String email;
	private String indirizzo;
	private Integer	gg_standard;
	private Integer	gg_aggiuntivi;
	private Integer	gg_limite;
	private String tipoOrario ;
	private String numeroOrePartTime;
	private String profiloOrarioPartTime;
	private String categoria ;
	private String legge104;
	private String certmedico;
	private String telelavoro;
	private String giornitelelavoro;

	public DipendentiCsiEntity() {
		super();
	}

	public DipendentiCsiEntity(Long idDipendente, String cognome, String nome, String matricola, String codiceFiscale, 
			String fo_i_liv, String resp_fo_i_liv, String fo_ii_liv, String fo_iii_liv, String fo_iv_liv, String funzione_diretta,
			 String resp_funz_diretta,String email,String indirizzo, 
			 Integer gg_standard,Integer gg_aggiuntivi,Integer gg_limite,
			 String tipoOrario, String numeroOrePartTime,String profiloOrarioPartTime, String categoria,
			 String legge104, String certmedico, String telelavoro, String giornitelelavoro) {
		super(); 
		this.idDipendente = idDipendente;
		this.cognome = cognome;
		this.nome = nome;
		this.matricola = matricola;
		this.codiceFiscale = codiceFiscale;
		this.fo_i_liv = fo_i_liv;
		this.resp_fo_i_liv = resp_fo_i_liv;
		this.fo_ii_liv = fo_ii_liv;
		this.fo_iii_liv = fo_iii_liv;
		this.fo_iv_liv = fo_iv_liv;
		this.funzione_diretta = funzione_diretta;
		this.resp_funz_diretta = resp_funz_diretta;
		this.email = email;
		this.indirizzo = indirizzo;
		this.gg_standard = gg_standard;
		this.gg_aggiuntivi = gg_aggiuntivi;
		this.gg_limite = gg_limite;
		this.tipoOrario = tipoOrario;
		this.numeroOrePartTime = numeroOrePartTime;
		this.profiloOrarioPartTime = profiloOrarioPartTime;
		this.categoria = categoria;
		this.legge104 = legge104;
		this.certmedico = certmedico;
		this.telelavoro = telelavoro;
		this.giornitelelavoro = giornitelelavoro;
	}

	public Long getIdDipendente() {
		return idDipendente;
	}

	public void setIdDipendente(Long idDipendente) {
		this.idDipendente = idDipendente;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getFo_i_liv() {
		return fo_i_liv;
	}

	public void setFo_i_liv(String fo_i_liv) {
		this.fo_i_liv = fo_i_liv;
	}

	public String getResp_fo_i_liv() {
		return resp_fo_i_liv;
	}

	public void setResp_fo_i_liv(String resp_fo_i_liv) {
		this.resp_fo_i_liv = resp_fo_i_liv;
	}

	public String getFo_ii_liv() {
		return fo_ii_liv;
	}

	public void setFo_ii_liv(String fo_ii_liv) {
		this.fo_ii_liv = fo_ii_liv;
	}

	public String getFo_iii_liv() {
		return fo_iii_liv;
	}

	public void setFo_iii_liv(String fo_iii_liv) {
		this.fo_iii_liv = fo_iii_liv;
	}

	public String getFo_iv_liv() {
		return fo_iv_liv;
	}

	public void setFo_iv_liv(String fo_iv_liv) {
		this.fo_iv_liv = fo_iv_liv;
	}

	public String getFunzione_diretta() {
		return funzione_diretta;
	}

	public void setFunzione_diretta(String funzione_diretta) {
		this.funzione_diretta = funzione_diretta;
	}

	public String getResp_funz_diretta() {
		return resp_funz_diretta;
	}

	public void setResp_funz_diretta(String resp_funz_diretta) {
		this.resp_funz_diretta = resp_funz_diretta;
	}

	public Integer getGg_standard() {
		return gg_standard;
	}

	public void setGg_standard(Integer gg_standard) {
		this.gg_standard = gg_standard;
	}

	public Integer getGg_aggiuntivi() {
		return gg_aggiuntivi;
	}

	public void setGg_aggiuntivi(Integer gg_aggiuntivi) {
		this.gg_aggiuntivi = gg_aggiuntivi;
	}

	public Integer getGg_limite() {
		return gg_limite;
	}

	public void setGg_limite(Integer gg_limite) {
		this.gg_limite = gg_limite;
	}

	public String getTipoOrario() {
		return tipoOrario;
	}

	public void setTipoOrario(String tipoOrario) {
		this.tipoOrario = tipoOrario;
	}

	public String getNumeroOrePartTime() {
		return numeroOrePartTime;
	}

	public void setNumeroOrePartTime(String numeroOrePartTime) {
		this.numeroOrePartTime = numeroOrePartTime;
	}

	public String getProfiloOrarioPartTime() {
		return profiloOrarioPartTime;
	}

	public void setProfiloOrarioPartTime(String profiloOrarioPartTime) {
		this.profiloOrarioPartTime = profiloOrarioPartTime;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getLegge104() {
		return legge104;
	}

	public void setLegge104(String legge104) {
		this.legge104 = legge104;
	}

	public String getCertmedico() {
		return certmedico;
	}

	public void setCertmedico(String certmedico) {
		this.certmedico = certmedico;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getTelelavoro() {
		return telelavoro;
	}

	public void setTelelavoro(String telelavoro) {
		this.telelavoro = telelavoro;
	}

	public String getGiornitelelavoro() {
		return giornitelelavoro;
	}

	public void setGiornitelelavoro(String giornitelelavoro) {
		this.giornitelelavoro = giornitelelavoro;
	}




}
