/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Richiesta di supporto (Chat) (moon_fo_t_richiesta_supporto)
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
public class RichiestaSupporto {
	
	private Long idRichiestaSupporto;
	private Long idIstanza;
	private Long idModulo;
	private String flagInAttesaDiRisposta;
	private String descMittente;
	private String emailMittente;
	private Date dataIns;
	private String attoreIns;
	private Date dataUpd;
	private String attoreUpd;
	private List<MessaggioSupporto> messaggi;
	
	public RichiestaSupporto() {
		super();
	}

	public RichiestaSupporto(Long idRichiestaSupporto, Long idIstanza, Long idModulo,
			String flagInAttesaDiRisposta, String descMittente, String emailMittente,
			Date dataIns, String attoreIns, Date dataUpd, String attoreUpd) {
		super();
		this.idRichiestaSupporto = idRichiestaSupporto;
		this.idIstanza = idIstanza;
		this.idModulo = idModulo;
		this.flagInAttesaDiRisposta = flagInAttesaDiRisposta;
		this.descMittente = descMittente;
		this.emailMittente = emailMittente;
		this.dataIns = dataIns;
		this.attoreIns = attoreIns;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Long getIdRichiestaSupporto() {
		return idRichiestaSupporto;
	}
	public void setIdRichiestaSupporto(Long idRichiestaSupporto) {
		this.idRichiestaSupporto = idRichiestaSupporto;
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
	public String getFlagInAttesaDiRisposta() {
		return flagInAttesaDiRisposta;
	}
	public void setFlagInAttesaDiRisposta(String flagInAttesaDiRisposta) {
		this.flagInAttesaDiRisposta = flagInAttesaDiRisposta;
	}
	public String getDescMittente() {
		return descMittente;
	}
	public void setDescMittente(String descMittente) {
		this.descMittente = descMittente;
	}
	public String getEmailMittente() {
		return emailMittente;
	}
	public void setEmailMittente(String emailMittente) {
		this.emailMittente = emailMittente;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	public String getAttoreIns() {
		return attoreIns;
	}
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
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
	public List<MessaggioSupporto> getMessaggi() {
		return messaggi;
	}
	public void setMessaggi(List<MessaggioSupporto> messaggi) {
		this.messaggi = messaggi;
	}

	@Override
	public String toString() {
		return "RichiestaSupporto [idRichiestaSupporto=" + idRichiestaSupporto + ", idIstanza=" + idIstanza
			+ ", idModulo=" + idModulo + ", flagInAttesaDiRisposta=" + flagInAttesaDiRisposta + ", descMittente="
			+ descMittente + ", emailMittente=" + emailMittente + ", dataIns=" + dataIns + ", attoreIns="
			+ attoreIns + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd
			+ ", messaggi=" + messaggi + "]";
	}
	
}