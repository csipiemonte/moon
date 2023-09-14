/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella dei dati di un instanze 
 * <br>Almeno un record presente per ogni istanza
 * <br>Un solo record presente per ogni istanza/cronologia
 * <br>
 * <br>Tabella moon_fo_t_dati_istanza
 * <br>PK: idDatiIstanza
 * <br>AK: idIstanza,idCronologiaStati
 * 
 * @see IstanzaEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzaDatiEntity {

	private Long idDatiIstanza;
	private Long idCronologiaStati;
	private Long idIstanza;
	private String datiIstanza;
	private Integer idTipoModifica;
	private Integer idStepCompilazione;
	private Date dataUpd;
	private String attoreUpd;
	
	public Long getIdDatiIstanza() {
		return idDatiIstanza;
	}
	public void setIdDatiIstanza(Long idDatiIstanza) {
		this.idDatiIstanza = idDatiIstanza;
	}
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
	public String getDatiIstanza() {
		return datiIstanza;
	}
	public void setDatiIstanza(String datiIstanza) {
		this.datiIstanza = datiIstanza;
	}
	public Integer getIdTipoModifica() {
		return idTipoModifica;
	}
	public void setIdTipoModifica(Integer idTipoModificaDati) {
		this.idTipoModifica = idTipoModificaDati;
	}
	public Integer getIdStepCompilazione() {
		return idStepCompilazione;
	}
	public void setIdStepCompilazione(Integer idStepCompilazione) {
		this.idStepCompilazione = idStepCompilazione;
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
	
	@Override
	public String toString() {
		return "IstanzaDatiEntity [idDatiIstanza=" + idDatiIstanza + ", idCronologiaStati=" + idCronologiaStati
				+ ", idIstanza=" + idIstanza + ", datiIstanza=" + datiIstanza + ", idTipoModifica=" + idTipoModifica
				+ ", idStepCompilazione=" + idStepCompilazione + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd
				+ "]";
	}
	
}
