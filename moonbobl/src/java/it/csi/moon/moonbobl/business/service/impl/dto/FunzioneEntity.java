/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * Entity della tabella funzione
 * <br>
 * <br>Tabella:  <code>moon_fo_d_funzione</code>
 * <br>PK:  <code>idFunzione</code>
 * <br>AK:  <code>codiceFunzione</code>
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class FunzioneEntity {
	
	private Integer idFunzione;
	private String codiceFunzione;
	private String nomeFunzione;
	private String descrizioneFunzione;
	private String flAttivo;
	private Date dataUpd;
	private String attoreUpd;
	
	public FunzioneEntity() {
		super();
	}

	public FunzioneEntity(Integer idFunzione, String codiceFunzione, String nomeFunzione, String descrizioneFunzione,
			String flAttivo, Date dataUpd, String attoreUpd) {
		super();
		this.idFunzione = idFunzione;
		this.codiceFunzione = codiceFunzione;
		this.nomeFunzione = nomeFunzione;
		this.descrizioneFunzione = descrizioneFunzione;
		this.flAttivo = flAttivo;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Integer getIdFunzione() {
		return idFunzione;
	}

	public void setIdFunzione(Integer idFunzione) {
		this.idFunzione = idFunzione;
	}

	public String getCodiceFunzione() {
		return codiceFunzione;
	}

	public void setCodiceFunzione(String codiceFunzione) {
		this.codiceFunzione = codiceFunzione;
	}

	public String getNomeFunzione() {
		return nomeFunzione;
	}

	public void setNomeFunzione(String nomeFunzione) {
		this.nomeFunzione = nomeFunzione;
	}

	public String getDescrizioneFunzione() {
		return descrizioneFunzione;
	}

	public void setDescrizioneFunzione(String descrizioneFunzione) {
		this.descrizioneFunzione = descrizioneFunzione;
	}

	public String getFlAttivo() {
		return flAttivo;
	}

	public void setFlAttivo(String flAttivo) {
		this.flAttivo = flAttivo;
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
