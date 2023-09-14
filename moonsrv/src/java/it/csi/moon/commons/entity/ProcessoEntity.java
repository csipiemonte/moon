/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

import it.csi.moon.commons.util.decodifica.DecodificaProcessoIstanza;

/**
 * Entity della tabella di decodifica degli processi delle istanze
 * <br>Viene salavato in ogni storico workflow di istanza dall'invio da parte del cittadino
 * <br> 
 * <br>Tabella moon_wf_d_processo
 * <br>PK: idStatoWf
 * <br>Usato prevalentamente da enum DecodificaProcessoIstanza
 * 
 * @see DecodificaProcessoIstanza
 * @see StoricoWorkflowEntity#getIdProcesso()
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ProcessoEntity {

	private Long idProcesso = null;
	private String codiceProcesso = null;
	private String nomeProcesso = null;
	private String descrizioneProcesso = null;
	private String flagAttivo = null;
	private Date dataUpd = null;
	private String attoreUpd = null;
	
	public ProcessoEntity() {
	}
	
	public ProcessoEntity(Long idProcesso, String codiceProcesso, String nomeProcesso, String descrizioneProcesso) {
		this.idProcesso = idProcesso;
		this.codiceProcesso = codiceProcesso;
		this.nomeProcesso = nomeProcesso;
		this.descrizioneProcesso = descrizioneProcesso;
		this.flagAttivo = "S";
	}
	
	public ProcessoEntity(Long idProcesso, String codiceProcesso, String nomeProcesso, String descrizioneProcesso, String flagAttivo, Date dataUpd, String attoreUpd) {
		this.idProcesso = idProcesso;
		this.codiceProcesso = codiceProcesso;
		this.nomeProcesso = nomeProcesso;
		this.descrizioneProcesso = descrizioneProcesso;
		this.flagAttivo = flagAttivo;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Long getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}
	public String getCodiceProcesso() {
		return codiceProcesso;
	}
	public void setCodiceProcesso(String codiceProcesso) {
		this.codiceProcesso = codiceProcesso;
	}
	public String getNomeProcesso() {
		return nomeProcesso;
	}
	public void setNomeProcesso(String nomeProcesso) {
		this.nomeProcesso = nomeProcesso;
	}
	public String getDescrizioneProcesso() {
		return descrizioneProcesso;
	}
	public void setDescrizioneProcesso(String descrizioneProcesso) {
		this.descrizioneProcesso = descrizioneProcesso;
	}
	public String getFlagAttivo() {
		return flagAttivo;
	}
	public void setFlagAttivo(String flagAttivo) {
		this.flagAttivo = flagAttivo;
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
	
}
