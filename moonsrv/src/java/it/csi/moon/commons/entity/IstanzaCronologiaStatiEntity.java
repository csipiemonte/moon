/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella della cronologia delle instanze 
 * <br>Almeno un record presente per ogni istanza
 * <br>Solo un record per instanza con dataFine NULL
 * <br>
 * <br>Tabella moon_fo_t_cronologia_stati
 * <br>PK: idCronologiaStati
 * <br>AK: idIstanza,dataFine NULL  per identificare l'ultimo stato di un istanza
 * 
 * @see IstanzaEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzaCronologiaStatiEntity {

	private Long idCronologiaStati;
	private Long idIstanza;
	private Integer idStatoWf;
	private Long idAzioneSvolta;
	private Date dataInizio;
	private Date dataFine;
	private String attoreIns;
	private String attoreUpd;
	
	public Long getIdCronologiaStati() {
		return idCronologiaStati;
	}
	public void setIdCronologiaStati(Long idCronologiaStati) {
		this.idCronologiaStati = idCronologiaStati;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Integer getIdStatoWf() {
		return idStatoWf;
	}
	public void setIdStatoWf(Integer idStatoWf) {
		this.idStatoWf = idStatoWf;
	}
	public Long getIdAzioneSvolta() {
		return idAzioneSvolta;
	}
	public void setIdAzioneSvolta(Long idAzioneSvolta) {
		this.idAzioneSvolta = idAzioneSvolta;
	}
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
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
