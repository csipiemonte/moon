/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

import it.csi.moon.moonbobl.dto.moonfobl.Valutazione;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataIns == null) ? 0 : dataIns.hashCode());
		result = prime * result + ((idIstanza == null) ? 0 : idIstanza.hashCode());
		result = prime * result + ((idModulo == null) ? 0 : idModulo.hashCode());
		result = prime * result + ((idValutazione == null) ? 0 : idValutazione.hashCode());
		result = prime * result + ((idVersioneModulo == null) ? 0 : idVersioneModulo.hashCode());
		result = prime * result + ((valutazione == null) ? 0 : valutazione.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValutazioneModuloEntity other = (ValutazioneModuloEntity) obj;
		if (dataIns == null) {
			if (other.dataIns != null)
				return false;
		} else if (!dataIns.equals(other.dataIns))
			return false;
		if (idIstanza == null) {
			if (other.idIstanza != null)
				return false;
		} else if (!idIstanza.equals(other.idIstanza))
			return false;
		if (idModulo == null) {
			if (other.idModulo != null)
				return false;
		} else if (!idModulo.equals(other.idModulo))
			return false;
		if (idValutazione == null) {
			if (other.idValutazione != null)
				return false;
		} else if (!idValutazione.equals(other.idValutazione))
			return false;
		if (idVersioneModulo == null) {
			if (other.idVersioneModulo != null)
				return false;
		} else if (!idVersioneModulo.equals(other.idVersioneModulo))
			return false;
		if (valutazione == null) {
			if (other.valutazione != null)
				return false;
		} else if (!valutazione.equals(other.valutazione))
			return false;
		return true;
	}
	
}
