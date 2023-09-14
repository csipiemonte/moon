/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

/**
 * Entity della tabella di decodifica delle tipologie delle email
 * <br>Viene salavato in ogni dati delle instanze
 * <br>Durante la compilazione in bozza e fino all'invio viene valorizzato a 1-INI
 * <br>
 * <br>Tabella moon_fo_d_tipo_email
 * <br>PK: idTipoEmail
 * <br>Usato prevalentamente da enum DecodificaTipoEmail
 * 
 * @see DecodificaTipoEmail
 * @see LogEmailEntity#getIdTipologia()
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class TipoEmailEntity {

	private Integer idTipoEmail = null;
	private String codTipoTipoEmail = null;
	private String descrizioneTipoEmail = null;
	private String flAttivo = null;
	
	public TipoEmailEntity() {
	}
	
	public TipoEmailEntity(Integer idTipoEmail, String codTipoTipoEmail, String descrizioneTipoEmail, String flAttivo) {
		this.idTipoEmail = idTipoEmail;
		this.codTipoTipoEmail = codTipoTipoEmail;
		this.descrizioneTipoEmail = descrizioneTipoEmail;
		this.flAttivo = flAttivo;
	}

	public Integer getIdTipoEmail() {
		return idTipoEmail;
	}
	public void setIdTipoEmail(Integer idTipoEmail) {
		this.idTipoEmail = idTipoEmail;
	}

	public String getCodTipoTipoEmail() {
		return codTipoTipoEmail;
	}
	public void setCodTipoTipoEmail(String codTipoTipoEmail) {
		this.codTipoTipoEmail = codTipoTipoEmail;
	}

	public String getDescrizioneTipoEmail() {
		return descrizioneTipoEmail;
	}
	public void setDescrizioneTipoEmail(String descrizioneTipoEmail) {
		this.descrizioneTipoEmail = descrizioneTipoEmail;
	}

	public String getFlAttivo() {
		return flAttivo;
	}
	public void setFlAttivo(String flAttivo) {
		this.flAttivo = flAttivo;
	}

}
