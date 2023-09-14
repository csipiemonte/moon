/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import it.csi.moon.commons.util.decodifica.DecodificaStatoIstanza;

/**
 * Entity della tabella di decodifica degli stati delle istanze
 * <br>Viene salavato in ogni cronologia di istanza e riportato anche sull'istanza stessa
 * <br> 
 * <br>Tabella moon_wf_d_stato
 * <br>PK: idStatoWf
 * <br>Usato prevalentamente da enum DecodificaStatoIstanza
 * 
 * @see DecodificaStatoIstanza
 * @see IstanzaCronologiaStatiEntity#getIdStatoWf()
 * @see IstanzaEntity#getIdStatoWf()
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class StatoEntity {

	private Integer idStatoWf = null;
	private String codiceStatoWf = null;
	private String nomeStatoWf = null;
	private String descStatoWf = null;
	private Integer idTabFo = null;
	
	public StatoEntity() {
	}
	
	public StatoEntity(Integer idStatoWf, String nomeStatoWf, String descStatoWf) {
		this.idStatoWf = idStatoWf;
		this.nomeStatoWf = nomeStatoWf;
		this.descStatoWf = descStatoWf;
	}
	public StatoEntity(Integer idStatoWf, String codiceStatoWf, String nomeStatoWf, String descStatoWf, Integer idTabFo) {
		this.idStatoWf = idStatoWf;
		this.codiceStatoWf = codiceStatoWf;
		this.nomeStatoWf = nomeStatoWf;
		this.descStatoWf = descStatoWf;
		this.idTabFo = idTabFo;
	}
	
	public Integer getIdStatoWf() {
		return idStatoWf;
	}
	public void setIdStatoWf(Integer idStatoWf) {
		this.idStatoWf = idStatoWf;
	}
	public String getCodiceStatoWf() {
		return codiceStatoWf;
	}
	public void setCodiceStatoWf(String codiceStatoWf) {
		this.codiceStatoWf = codiceStatoWf;
	}
	public String getNomeStatoWf() {
		return nomeStatoWf;
	}
	public void setNomeStatoWf(String nomeStatoWf) {
		this.nomeStatoWf = nomeStatoWf;
	}
	public String getDescStatoWf() {
		return descStatoWf;
	}
	public void setDescStatoWf(String descStatoWf) {
		this.descStatoWf = descStatoWf;
	}
	public Integer getIdTabFo() {
		return idTabFo;
	}
	public void setIdTabFo(Integer idTabFo) {
		this.idTabFo = idTabFo;
	}
	
}
