/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella modulo
 * <br>Deve essere presente almeno un record di Cronologia, Struttura, Progresso 
 * <br>
 * <br>Tabella moon_io_d_modulo
 * <br>PK: idModulo
 * 
 * @see moon_io_r_cronologia_statomodulo
 * @see ModuloStrutturaEntity
 * @see ModuloProgressivoEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloEntity {
	
	private Long idModulo;
	private String codiceModulo;
	private String oggettoModulo;
	private String descrizioneModulo;
	private Date dataIns;
	private Date dataUpd;
	private String flagIsRiservato;
	private Integer idTipoCodiceIstanza;
	private String flagProtocolloIntegrato;
	private String attoreUpd;
	
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public String getCodiceModulo() {
		return codiceModulo;
	}
	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}
	public String getOggettoModulo() {
		return oggettoModulo;
	}
	public void setOggettoModulo(String oggettoModulo) {
		this.oggettoModulo = oggettoModulo;
	}
	public String getDescrizioneModulo() {
		return descrizioneModulo;
	}
	public void setDescrizioneModulo(String descrizioneModulo) {
		this.descrizioneModulo = descrizioneModulo;
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
	public String getFlagIsRiservato() {
		return flagIsRiservato;
	}
	public void setFlagIsRiservato(String flagIsRiservato) {
		this.flagIsRiservato = flagIsRiservato;
	}
	public Integer getIdTipoCodiceIstanza() {
		return idTipoCodiceIstanza;
	}
	public void setIdTipoCodiceIstanza(Integer idTipoCodiceIstanza) {
		this.idTipoCodiceIstanza = idTipoCodiceIstanza;
	}
	public String getFlagProtocolloIntegrato() {
		return flagProtocolloIntegrato;
	}
	public void setFlagProtocolloIntegrato(String flagProtocolloIntegrato) {
		this.flagProtocolloIntegrato = flagProtocolloIntegrato;
	}
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}

}
