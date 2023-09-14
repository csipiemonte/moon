/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.dto;

import java.util.Date;

import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;

/**
 * Entity della tabella di decodifica degli stati esteri
 * <br>Viene salavato in ogni cronologia di istanza
 * <br>
 * <br>Tabella moon_ext_anpr_c_stato_estero 
 * <br>PK: idStatoEstero
 * 
 * @see DecodificaAzione
 * @see IstanzaCronologiaStatiEntity#getIdAzioneSvolta()
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class AnprStatoEsteroEntity {

	private Integer idStatoEstero = null;
	private String denominazione = null;
	private String denominazioneIstat = null;
	private String denominazioneIstatEn = null;
	private Date dtInizioValidita = null;
	private Date dtFineValidita = null;
	private String codIso31661Alpha3 = null;
	private String codMae = null;
	private String codMin = null;
	private String codAt = null;
	private String codIstat = null;
	private String cittadinanza = null;
	private String nascita = null;
	private String residenza = null;
	private String fonte = null;
	private String tipo = null;
	private String codIsoSovrano = null;
	private String motivo = null;
	/*
	id_stato_estero,denominazione,denominazione_istat,denominazione_istat_en,dt_inizio_validita,dt_fine_validita,
	cod_iso_3166_1_alpha3,cod_mae,cod_min,cod_at,cod_istat,cittadinanza,nascita,residenza,fonte,tipo,cod_iso_sovrano,motivo
	*/
	
	public AnprStatoEsteroEntity() {	
	}
	
	public AnprStatoEsteroEntity(Integer idStatoEstero, String denominazione, String codIstat, String cittadinanza, String nascita, String residenza) {
		this.idStatoEstero = idStatoEstero;
		this.denominazione = denominazione;
		this.codIstat = codIstat;
		this.cittadinanza = cittadinanza;
		this.nascita = nascita;
		this.residenza = residenza;
	}

	public Integer getIdStatoEstero() {
		return idStatoEstero;
	}
	public void setIdStatoEstero(Integer idStatoEstero) {
		this.idStatoEstero = idStatoEstero;
	}

	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getDenominazioneIstat() {
		return denominazioneIstat;
	}
	public void setDenominazioneIstat(String denominazioneIstat) {
		this.denominazioneIstat = denominazioneIstat;
	}

	public String getDenominazioneIstatEn() {
		return denominazioneIstatEn;
	}
	public void setDenominazioneIstatEn(String denominazioneIstatEn) {
		this.denominazioneIstatEn = denominazioneIstatEn;
	}

	public Date getDtInizioValidita() {
		return dtInizioValidita;
	}
	public void setDtInizioValidita(Date dtInizioValidita) {
		this.dtInizioValidita = dtInizioValidita;
	}

	public Date getDtFineValidita() {
		return dtFineValidita;
	}
	public void setDtFineValidita(Date dtFineValidita) {
		this.dtFineValidita = dtFineValidita;
	}

	public String getCodIso31661Alpha3() {
		return codIso31661Alpha3;
	}
	public void setCodIso31661Alpha3(String codIso31661Alpha3) {
		this.codIso31661Alpha3 = codIso31661Alpha3;
	}

	public String getCodMae() {
		return codMae;
	}
	public void setCodMae(String codMae) {
		this.codMae = codMae;
	}

	public String getCodMin() {
		return codMin;
	}
	public void setCodMin(String codMin) {
		this.codMin = codMin;
	}

	public String getCodAt() {
		return codAt;
	}
	public void setCodAt(String codAt) {
		this.codAt = codAt;
	}

	public String getCodIstat() {
		return codIstat;
	}
	public void setCodIstat(String codIstat) {
		this.codIstat = codIstat;
	}

	public String getCittadinanza() {
		return cittadinanza;
	}
	public void setCittadinanza(String cittadinanza) {
		this.cittadinanza = cittadinanza;
	}

	public String getNascita() {
		return nascita;
	}
	public void setNascita(String nascita) {
		this.nascita = nascita;
	}

	public String getResidenza() {
		return residenza;
	}
	public void setResidenza(String residenza) {
		this.residenza = residenza;
	}

	public String getFonte() {
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCodIsoSovrano() {
		return codIsoSovrano;
	}
	public void setCodIsoSovrano(String codIsoSovrano) {
		this.codIsoSovrano = codIsoSovrano;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cittadinanza == null) ? 0 : cittadinanza.hashCode());
		result = prime * result + ((codIso31661Alpha3 == null) ? 0 : codIso31661Alpha3.hashCode());
		result = prime * result + ((codIsoSovrano == null) ? 0 : codIsoSovrano.hashCode());
		result = prime * result + ((codIstat == null) ? 0 : codIstat.hashCode());
		result = prime * result + ((denominazione == null) ? 0 : denominazione.hashCode());
		result = prime * result + ((dtFineValidita == null) ? 0 : dtFineValidita.hashCode());
		result = prime * result + ((dtInizioValidita == null) ? 0 : dtInizioValidita.hashCode());
		result = prime * result + ((fonte == null) ? 0 : fonte.hashCode());
		result = prime * result + ((idStatoEstero == null) ? 0 : idStatoEstero.hashCode());
		result = prime * result + ((nascita == null) ? 0 : nascita.hashCode());
		result = prime * result + ((residenza == null) ? 0 : residenza.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		AnprStatoEsteroEntity other = (AnprStatoEsteroEntity) obj;
		if (cittadinanza == null) {
			if (other.cittadinanza != null)
				return false;
		} else if (!cittadinanza.equals(other.cittadinanza))
			return false;
		if (codIso31661Alpha3 == null) {
			if (other.codIso31661Alpha3 != null)
				return false;
		} else if (!codIso31661Alpha3.equals(other.codIso31661Alpha3))
			return false;
		if (codIsoSovrano == null) {
			if (other.codIsoSovrano != null)
				return false;
		} else if (!codIsoSovrano.equals(other.codIsoSovrano))
			return false;
		if (codIstat == null) {
			if (other.codIstat != null)
				return false;
		} else if (!codIstat.equals(other.codIstat))
			return false;
		if (denominazione == null) {
			if (other.denominazione != null)
				return false;
		} else if (!denominazione.equals(other.denominazione))
			return false;
		if (dtFineValidita == null) {
			if (other.dtFineValidita != null)
				return false;
		} else if (!dtFineValidita.equals(other.dtFineValidita))
			return false;
		if (dtInizioValidita == null) {
			if (other.dtInizioValidita != null)
				return false;
		} else if (!dtInizioValidita.equals(other.dtInizioValidita))
			return false;
		if (fonte == null) {
			if (other.fonte != null)
				return false;
		} else if (!fonte.equals(other.fonte))
			return false;
		if (idStatoEstero == null) {
			if (other.idStatoEstero != null)
				return false;
		} else if (!idStatoEstero.equals(other.idStatoEstero))
			return false;
		if (nascita == null) {
			if (other.nascita != null)
				return false;
		} else if (!nascita.equals(other.nascita))
			return false;
		if (residenza == null) {
			if (other.residenza != null)
				return false;
		} else if (!residenza.equals(other.residenza))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AnprStatoEsteroEntity [idStatoEstero=" + idStatoEstero + ", denominazione=" + denominazione
				+ ", codIso31661Alpha3=" + codIso31661Alpha3 + ", codIstat=" + codIstat + ", cittadinanza="
				+ cittadinanza + ", nascita=" + nascita + ", residenza=" + residenza + ", fonte=" + fonte
				+ ", codIsoSovrano=" + codIsoSovrano + "]";
	}
	
	
}
