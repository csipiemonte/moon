/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * InitModuloCambiaStato
 * 
 * @author Laurent
 *
 */
public class InitModuloCambiaStato   {

	private Long idModulo = null;
	private Long idVersioneModulo = null;
	private String codiceModulo = null;
	private List<StatoModulo> cronologia = null;
	private Date dataMinCambioStato = null;

	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Long getIdVersioneModulo() {
		return idVersioneModulo;
	}
	public void setIdVersioneModulo(Long idVersioneModulo) {
		this.idVersioneModulo = idVersioneModulo;
	}
	public String getCodiceModulo() {
		return codiceModulo;
	}
	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}
	public List<StatoModulo> getCronologia() {
		return cronologia;
	}
	public void setCronologia(List<StatoModulo> cronologia) {
		this.cronologia = cronologia;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataMinCambioStato() {
		return dataMinCambioStato;
	}
	public void setDataMinCambioStato(Date dataMinCambioStato) {
		this.dataMinCambioStato = dataMinCambioStato;
	}
	
}

