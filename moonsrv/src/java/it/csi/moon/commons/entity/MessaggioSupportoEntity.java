/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella degli Messaggi di supporto (dettaglio della Chat : Richiesta Supporto)
 * <br>
 * <br>Tabella:  <code>moon_fo_t_messaggio_supporto</code>
 * <br>PK:  <code>idMessaggioSupporto</code>
 * 
 * @author Laurent
 *
 * @version 1.0.0 - 21/07/2020 - versione iniziale
 */
public class MessaggioSupportoEntity {
	
	private Long idMessaggioSupporto;
	private Long idRichiestaSupporto;
	private String contenuto;
	private String provenienza; // FO/BO
	private Date dataIns;
	private String attoreIns;
	
	public MessaggioSupportoEntity() {
		super();
	}

	public MessaggioSupportoEntity(Long idMessaggioSupporto, Long idRichiestaSupporto, String contenuto,
			String provenienza, Date dataIns, String attoreIns) {
		super();
		this.idMessaggioSupporto = idMessaggioSupporto;
		this.idRichiestaSupporto = idRichiestaSupporto;
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
	public Long getIdRichiestaSupporto() {
		return idRichiestaSupporto;
	}
	public void setIdRichiestaSupporto(Long idRichiestaSupporto) {
		this.idRichiestaSupporto = idRichiestaSupporto;
	}
	public String getContenuto() {
		return contenuto;
	}
	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}
	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
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

	@Override
	public String toString() {
		return "MessaggioSupporto [idMessaggioSupporto=" + idMessaggioSupporto + ", idRichiestaSupporto="
				+ idRichiestaSupporto + ", contenuto=" + contenuto + ", provenienza=" + provenienza + ", dataIns="
				+ dataIns + ", attoreIns=" + attoreIns + "]";
	}
	
}