/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.dto;

public class DipendentiRPEntity {
	
	private Long idDipendente;
	private String cognome;
	private String nome;
	private String matricola;
	private String codiceFiscale;
	private String categoria;
	private String tipoRapportoLavoro;
	private String posizioneTipo;
	private String posizioneTipoDirig;
	private String raggruppOrganico;
	private String ruoloEsterno;
	private String statoAssegnazione;
	private String direzione;
	private String settore;
	private String uoCodiceRegionale;


	public DipendentiRPEntity() {
		super();
	}

	public DipendentiRPEntity(Long idDipendente, String cognome, String nome, String matricola, String codiceFiscale, String categoria,
			 String tipoRapportoLavoro, String posizioneTipo, String posizioneTipoDirig, String raggruppOrganico, String ruoloEsterno,
			 String statoAssegnazione,String direzione,String settore,String uoCodiceRegionale) {
		super(); 
		this.idDipendente = idDipendente;
		this.cognome = cognome;
		this.nome = nome;
		this.matricola = matricola;
		this.codiceFiscale = codiceFiscale;
		this.categoria = categoria;
		this.tipoRapportoLavoro = tipoRapportoLavoro;
		this.posizioneTipo = posizioneTipo;
		this.posizioneTipoDirig = posizioneTipoDirig;
		this.raggruppOrganico = raggruppOrganico;
		this.ruoloEsterno = ruoloEsterno;
		this.statoAssegnazione = statoAssegnazione;
		this.direzione = direzione;
		this.settore = settore;
		this.uoCodiceRegionale = uoCodiceRegionale;
		
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

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getTipoRapportoLavoro() {
		return tipoRapportoLavoro;
	}

	public void setTipoRapportoLavoro(String tipoRapportoLavoro) {
		this.tipoRapportoLavoro = tipoRapportoLavoro;
	}

	public String getPosizioneTipo() {
		return posizioneTipo;
	}

	public void setPosizioneTipo(String posizioneTipo) {
		this.posizioneTipo = posizioneTipo;
	}

	public String getPosizioneTipoDirig() {
		return posizioneTipoDirig;
	}

	public void setPosizioneTipoDirig(String posizioneTipoDirig) {
		this.posizioneTipoDirig = posizioneTipoDirig;
	}

	public String getRaggruppOrganico() {
		return raggruppOrganico;
	}

	public void setRaggruppOrganico(String raggruppOrganico) {
		this.raggruppOrganico = raggruppOrganico;
	}

	public String getRuoloEsterno() {
		return ruoloEsterno;
	}

	public void setRuoloEsterno(String ruoloEsterno) {
		this.ruoloEsterno = ruoloEsterno;
	}

	public String getStatoAssegnazione() {
		return statoAssegnazione;
	}

	public void setStatoAssegnazione(String statoAssegnazione) {
		this.statoAssegnazione = statoAssegnazione;
	}

	public String getDirezione() {
		return direzione;
	}

	public void setDirezione(String direzione) {
		this.direzione = direzione;
	}

	public String getSettore() {
		return settore;
	}

	public void setSettore(String settore) {
		this.settore = settore;
	}

	public String getUoCodiceRegionale() {
		return uoCodiceRegionale;
	}

	public void setUoCodiceRegionale(String uoCodiceRegionale) {
		this.uoCodiceRegionale = uoCodiceRegionale;
	}


}
