/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * DTO dei log delle chiamate ai servizi COSMO (moon_cs_t_servizio)
 * 
/**
 * Entity della tabella dei log delle chiamate ai servizi COSMO
 * <br>
 * <br>Tabella moon_cs_t_servizio
 * <br>PK: idLogServizio
 * 
 * @author Laurent
 */
public class CosmoLogServizioEntity {

	private Long idLogServizio;
	private Long idLogPratica;
	private String idPratica;
	private Long idIstanza;
	private Long idModulo;
	private String servizio;
	private Date dataIns;
	private Date dataUpd;
	private String richiesta;
	private String risposta;
	private String errore;
	
	public Long getIdLogServizio() {
		return idLogServizio;
	}
	public void setIdLogServizio(Long idLogServizio) {
		this.idLogServizio = idLogServizio;
	}
	public Long getIdLogPratica() {
		return idLogPratica;
	}
	public void setIdLogPratica(Long idLogPratica) {
		this.idLogPratica = idLogPratica;
	}
	public String getIdPratica() {
		return idPratica;
	}
	public void setIdPratica(String idPratica) {
		this.idPratica = idPratica;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public String getServizio() {
		return servizio;
	}
	public void setServizio(String servizio) {
		this.servizio = servizio;
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
	public String getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
	}
	public String getRisposta() {
		return risposta;
	}
	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}
	public String getErrore() {
		return errore;
	}
	public void setErrore(String errore) {
		this.errore = errore;
	}

}
