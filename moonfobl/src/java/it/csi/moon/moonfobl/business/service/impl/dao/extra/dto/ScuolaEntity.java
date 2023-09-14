/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.dto;


/**
 * Entity della tabella moon_ext_regp_scuole_voucher
 * <br>
 * <br>Tabella moon_ext_regp_scuole_voucher 
 * <br>PK: id
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class ScuolaEntity {
	
	private Integer id;
	private Integer idScuola;
	private String codiceIstat;
	private String denominazione;
    private Integer idOrdineScuola;
    private Integer IdTipoScuola;
    private String annoScolastico;


	public ScuolaEntity() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdScuola() {
		return idScuola;
	}

	public void setIdScuola(Integer idScuola) {
		this.idScuola = idScuola;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public Integer getIdOrdineScuola() {
		return idOrdineScuola;
	}

	public void setIdOrdineScuola(Integer idOrdineScuole) {
		this.idOrdineScuola = idOrdineScuole;
	}

	public Integer getIdTipoScuola() {
		return IdTipoScuola;
	}

	public void setIdTipoScuola(Integer idTipoScuola) {
		IdTipoScuola = idTipoScuola;
	}
	
	public String getAnnoScolastico() {
		return annoScolastico;
	}

	public void setAnnoScolastico(String annoScolastico) {
		this.annoScolastico = annoScolastico;
	}


	@Override
	public String toString() {
		return "ScuolaEntity [id=" + id + ", idScuola=" + idScuola + ", codiceIstat=" + codiceIstat + ", denominazione="
				+ denominazione + ", idOrdineScuole=" + idOrdineScuola + ", IdTipoScuola=" + IdTipoScuola + ", annoScolastico=" + annoScolastico+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IdTipoScuola == null) ? 0 : IdTipoScuola.hashCode());
		result = prime * result + ((codiceIstat == null) ? 0 : codiceIstat.hashCode());
		result = prime * result + ((denominazione == null) ? 0 : denominazione.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idOrdineScuola == null) ? 0 : idOrdineScuola.hashCode());
		result = prime * result + ((idScuola == null) ? 0 : idScuola.hashCode());
		result = prime * result + ((annoScolastico == null) ? 0 : annoScolastico.hashCode());
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
		ScuolaEntity other = (ScuolaEntity) obj;
		if (IdTipoScuola == null) {
			if (other.IdTipoScuola != null)
				return false;
		} else if (!IdTipoScuola.equals(other.IdTipoScuola))
			return false;
		if (codiceIstat == null) {
			if (other.codiceIstat != null)
				return false;
		} else if (!codiceIstat.equals(other.codiceIstat))
			return false;
		if (denominazione == null) {
			if (other.denominazione != null)
				return false;
		} else if (!denominazione.equals(other.denominazione))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idOrdineScuola == null) {
			if (other.idOrdineScuola != null)
				return false;
		} else if (!idOrdineScuola.equals(other.idOrdineScuola))
			return false;
		if (idScuola == null) {
			if (other.idScuola != null)
				return false;
		} else if (!idScuola.equals(other.idScuola))
			return false;
		if (annoScolastico == null) {
			if (other.annoScolastico != null)
				return false;
		} else if (!annoScolastico.equals(other.annoScolastico))
			return false;		
		return true;
	}

}
