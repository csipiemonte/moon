/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Messaggio di supporto (moon_fo_t_messaggio_supporto)
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
public class MessaggioSupporto {
	
	private Long idMessaggioSupporto;
	private String contenuto;
	private String provenienza; // FO/BO
	private Date dataIns;
	private String attoreIns;
	private List<AllegatoMessaggioSupporto> allegati;
	
	public MessaggioSupporto() {
		super();
	}

	public MessaggioSupporto(Long idMessaggioSupporto, Long idRichiestaSupporto, String contenuto,
			String provenienza, Date dataIns, String attoreIns) {
		super();
		this.idMessaggioSupporto = idMessaggioSupporto;
		this.contenuto = contenuto;
		this.provenienza = provenienza;
		this.dataIns = dataIns;
		this.attoreIns = attoreIns;
	}

	public Long getIdMessaggioSupporto() {
		return idMessaggioSupporto;
	}
	public void setIdMessaggioSupporto(Long idMessaggioSupporto) {
		this.idMessaggioSupporto = idMessaggioSupporto;
	}
	public String getContenuto() {
		return contenuto;
	}
	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}
	/**
	 * Codice che indica la tipologia di attore del messaggio : FO o BO
	 * @return
	 */
	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
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
	public List<AllegatoMessaggioSupporto> getAllegati() {
		return allegati;
	}
	public void setAllegati(List<AllegatoMessaggioSupporto> allegati) {
		this.allegati = allegati;
	}
	
	@Override
	public String toString() {
		return "MessaggioSupporto [idMessaggioSupporto=" + idMessaggioSupporto 
				+ ", contenuto=" + contenuto + ", provenienza=" + provenienza + ", dataIns="
				+ dataIns + ", attoreIns=" + attoreIns + ", allegati=" + allegati + "]";
	}
	
}