/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import it.csi.moon.commons.util.decodifica.DecodificaTipoModificaDati;

/**
 * Entity della tabella di decodifica delle tipologie delle modifiche dati 
 * <br>Viene salavato in ogni dati delle instanze
 * <br>Durante la compilazione in bozza e fino all'invio viene valorizzato a 1-INI
 * <br>
 * <br>Tabella moon_fo_d_tipo_modifica_dati
 * <br>PK: idTipoModifica
 * <br>Usato prevalentamente da enum DecodificaTipoModificaDati
 * 
 * @see DecodificaTipoModificaDati
 * @see IstanzaDatiEntity#getIdTipoModifica()
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class TipoModificaDatiEntity {

	private Integer idTipoModifica = null;
	private String codTipoModificaDati = null;
	private String descrizioneTipoModificaTipoDati = null;
	private String flAttivo = null;
	
	public TipoModificaDatiEntity() {
		
	}
	
	public TipoModificaDatiEntity(Integer idTipoModifica, String codTipoModificaDati, String descrizioneTipoModificaTipoDati, String flAttivo) {
		this.idTipoModifica = idTipoModifica;
		this.codTipoModificaDati = codTipoModificaDati;
		this.descrizioneTipoModificaTipoDati = descrizioneTipoModificaTipoDati;
		this.flAttivo = flAttivo;
	}

	public Integer getIdTipoModifica() {
		return idTipoModifica;
	}
	public void setIdTipoModifica(Integer idTipoModifica) {
		this.idTipoModifica = idTipoModifica;
	}

	public String getCodTipoModificaDati() {
		return codTipoModificaDati;
	}
	public void setCodTipoModificaDati(String codTipoModificaDati) {
		this.codTipoModificaDati = codTipoModificaDati;
	}

	public String getDescrizioneTipoModificaTipoDati() {
		return descrizioneTipoModificaTipoDati;
	}
	public void setDescrizioneTipoModificaTipoDati(String descrizioneTipoModificaTipoDati) {
		this.descrizioneTipoModificaTipoDati = descrizioneTipoModificaTipoDati;
	}

	public String getFlAttivo() {
		return flAttivo;
	}
	public void setFlAttivo(String flAttivo) {
		this.flAttivo = flAttivo;
	}

}
