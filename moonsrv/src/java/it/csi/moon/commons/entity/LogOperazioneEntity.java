/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella logOperazione 
 * <br>
 * <br>Tabella moon_fo_t_log_operazione
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class LogOperazioneEntity {
	private Long idTraccia;
	private Date dataOra;
	private String ipAddress;
	private String utente;
	private String codiceApp;
	private String operazione;
	private String dettaglio;
	private String codiceRuolo;
	
	public LogOperazioneEntity() {
		super();
	}

	public LogOperazioneEntity(Long idTraccia, Date dataOra, String ipAddress, String utente, String codiceApp,
			String operazione, String dettaglio, String codiceRuolo) {
		super();
		this.idTraccia = idTraccia;
		this.dataOra = dataOra;
		this.ipAddress = ipAddress;
		this.utente = utente;
		this.codiceApp = codiceApp;
		this.operazione = operazione;
		this.dettaglio = dettaglio;
		this.codiceRuolo = codiceRuolo;
	}

	public Long getIdTraccia() {
		return idTraccia;
	}

	public void setIdTraccia(Long idTraccia) {
		this.idTraccia = idTraccia;
	}

	public Date getDataOra() {
		return dataOra;
	}

	public void setDataOra(Date dataOra) {
		this.dataOra = dataOra;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getCodiceApp() {
		return codiceApp;
	}

	public void setCodiceApp(String codiceApp) {
		this.codiceApp = codiceApp;
	}

	public String getOperazione() {
		return operazione;
	}

	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}

	public String getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(String dettaglio) {
		this.dettaglio = dettaglio;
	}

	public String getCodiceRuolo() {
		return codiceRuolo;
	}

	public void setCodiceRuolo(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}
	
	
	
}
