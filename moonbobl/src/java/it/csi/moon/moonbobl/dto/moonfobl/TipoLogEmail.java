/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import it.csi.moon.moonbobl.business.service.impl.dto.TipoEmailEntity;

public class TipoLogEmail {
	
	private String codice = null;
	private String descrizione = null;
	
	public TipoLogEmail() {
	}
	
	public TipoLogEmail(TipoEmailEntity entity) {
		this.codice = entity.getCodTipoTipoEmail();
		this.descrizione = entity.getDescrizioneTipoEmail();
	}

	public String getCodice() {
		return codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
