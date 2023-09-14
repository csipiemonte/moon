/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

public class IndexRichiestaEntity {

	private Long idRichiesta;
	private Date dataInizio;
	private Date dataFine;
	private Long idModulo;
	private Date dataFilter;
	private int istanzeProc;
	
	public Long getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
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
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Date getDataFilter() {
		return dataFilter;
	}
	public void setDataFilter(Date dataFilter) {
		this.dataFilter = dataFilter;
	}
	public int getIstanzeProc() {
		return istanzeProc;
	}
	public void setIstanzeProc(int istanzeProc) {
		this.istanzeProc = istanzeProc;
	}
	
	
}
