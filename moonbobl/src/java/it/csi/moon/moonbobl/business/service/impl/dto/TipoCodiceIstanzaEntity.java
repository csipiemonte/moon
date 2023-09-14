/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

/**
 * Entity della tabella di decodifica delle tipologie di progressivo codice_istanza
 * <br>Viene usato al primo salvataggio di un instanza per generare il codice_istanza univoco
 * <br>
 * <br>Tabella moon_io_d_tipocodiceistanza
 * <br>PK: idTipoCodiceIstanza
 * 
 * @since 1.0.0
 */
public class TipoCodiceIstanzaEntity {

	private Integer idTipoCodiceIstanza = null;
	private String descCodice = null;
	private String descrizioneTipo = null;
	
	public TipoCodiceIstanzaEntity() {
		
	}
	
	public TipoCodiceIstanzaEntity(Integer idTipoCodiceIstanza, String descCodice, String descrizioneTipo) {
		this.idTipoCodiceIstanza = idTipoCodiceIstanza;
		this.descCodice = descCodice;
		this.descrizioneTipo = descrizioneTipo;
	}

	public Integer getIdTipoCodiceIstanza() {
		return idTipoCodiceIstanza;
	}

	public void setIdTipoCodiceIstanza(Integer idTipoCodiceIstanza) {
		this.idTipoCodiceIstanza = idTipoCodiceIstanza;
	}

	public String getDescCodice() {
		return descCodice;
	}

	public void setDescCodice(String descCodice) {
		this.descCodice = descCodice;
	}

	public String getDescrizioneTipo() {
		return descrizioneTipo;
	}

	public void setDescrizioneTipo(String descrizioneTipo) {
		this.descrizioneTipo = descrizioneTipo;
	}
	
}
