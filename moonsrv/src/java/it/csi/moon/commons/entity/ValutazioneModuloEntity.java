/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

import it.csi.moon.commons.dto.Valutazione;

/**
 * Entity della tabella delle valutazione utente dei moduli
 * <br>
 * <br>Tabella:  <code>moon_fo_t_valutazione_modulo</code>
 * <br>NO PK.
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ValutazioneModuloEntity {

	private Long idIstanza;
	private Long idModulo;
	private Long idVersioneModulo;
	private Date dataIns;
	private Integer idValutazione;
	private Valutazione valutazione;
	
	public ValutazioneModuloEntity() {
		super();
	}
	
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
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
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	public Integer getIdValutazione() {
		return idValutazione;
	}
	public void setIdValutazione(Integer idValutazione) {
		this.idValutazione = idValutazione;
	}
	public Valutazione getValutazione() {
		return valutazione;
	}
	public void setValutazione(Valutazione valutazione) {
		this.valutazione = valutazione;
	}

	@Override
	public String toString() {
		return "ValutazioneModuloEntity [idIstanza=" + idIstanza + ", idModulo=" + idModulo + ", idVersioneModulo="
				+ idVersioneModulo + ", dataIns=" + dataIns + ", idValutazione=" + idValutazione + ", valutazione="
				+ valutazione + "]";
	}

}
