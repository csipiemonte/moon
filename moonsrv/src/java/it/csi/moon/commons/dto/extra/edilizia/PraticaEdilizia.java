/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.edilizia;

import java.util.List;

public class PraticaEdilizia {

	private String codice;
	private String primoIntestatario;
	private String dataPresentazione;
	private String descrizioneOpera;
	private List<String> elencoIndirizzi;
	private List<String> elencoProvvedimenti;
	private String numeroProgressivoConservazione;
	private String identificativoRepCartellini;
	private String dataProtocollo;
	private String indirizzi;
	private String provvedimenti;
	private String protocollo;
	private String esito;


	public PraticaEdilizia() {
		super();
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the primoIntestatario
	 */
	public String getPrimoIntestatario() {
		return primoIntestatario;
	}

	/**
	 * @return the dataPresentazione
	 */
	public String getDataPresentazione() {
		return dataPresentazione;
	}

	/**
	 * @return the descrizioneOpera
	 */
	public String getDescrizioneOpera() {
		return descrizioneOpera;
	}

	/**
	 * @return the elencoIndirizzi
	 */
	public List<String> getElencoIndirizzi() {
		return elencoIndirizzi;
	}

	/**
	 * @return the elencoProvvedimenti
	 */
	public List<String> getElencoProvvedimenti() {
		return elencoProvvedimenti;
	}

	/**
	 * @return the numeroProgressivoConservazione
	 */
	public String getNumeroProgressivoConservazione() {
		return numeroProgressivoConservazione;
	}

	/**
	 * @return the identificativoRepCartellini
	 */
	public String getIdentificativoRepCartellini() {
		return identificativoRepCartellini;
	}

	/**
	 * @return the dataProtocollo
	 */
	public String getDataProtocollo() {
		return dataProtocollo;
	}


	/**
	 * @return the indirizzi
	 */
	public String getIndirizzi() {
		if (this.elencoIndirizzi != null && !this.elencoIndirizzi.isEmpty() )
		{
			return String.join("\n", this.elencoIndirizzi);
		}		
		else return " ";
	}

	/**
	 * @return the provvedimenti
	 */
	public String getProvvedimenti() {
		if (this.elencoProvvedimenti != null && !this.elencoProvvedimenti.isEmpty() )
		{
			return String.join("\n", this.elencoProvvedimenti);
		}		
		else return " ";
	}

	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param primoIntestatario the primoIntestatario to set
	 */
	public void setPrimoIntestatario(String primoIntestatario) {
		this.primoIntestatario = primoIntestatario;
	}

	/**
	 * @param dataPresentazione the dataPresentazione to set
	 */
	public void setDataPresentazione(String dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	/**
	 * @param descrizioneOpera the descrizioneOpera to set
	 */
	public void setDescrizioneOpera(String descrizioneOpera) {
		this.descrizioneOpera = descrizioneOpera;
	}

	/**
	 * @param elencoIndirizzi the elencoIndirizzi to set
	 */
	public void setElencoIndirizzi(List<String> elencoIndirizzi) {
		this.elencoIndirizzi = elencoIndirizzi;
	}

	/**
	 * @param elencoProvvedimenti the elencoProvvedimenti to set
	 */
	public void setElencoProvvedimenti(List<String> elencoProvvedimenti) {
		this.elencoProvvedimenti = elencoProvvedimenti;
	}

	/**
	 * @param numeroProgressivoConservazione the numeroProgressivoConservazione to set
	 */
	public void setNumeroProgressivoConservazione(String numeroProgressivoConservazione) {
		this.numeroProgressivoConservazione = numeroProgressivoConservazione;
	}

	/**
	 * @param identificativoRepCartellini the identificativoRepCartellini to set
	 */
	public void setIdentificativoRepCartellini(String identificativoRepCartellini) {
		this.identificativoRepCartellini = identificativoRepCartellini;
	}

	/**
	 * @param dataProtocollo the dataProtocollo to set
	 */
	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	/**
	 * @param indirizzi the indirizzi to set
	 */
	public void setIndirizzi(String indirizzi) {
		this.indirizzi = indirizzi;
	}

	/**
	 * @param provvedimenti the provvedimenti to set
	 */
	public void setProvvedimenti(String provvedimenti) {
		this.provvedimenti = provvedimenti;
	}
	
	/**
	 * @return the protocollo
	 */
	public String getProtocollo() {
		return protocollo;
	}

	/**
	 * @param protocollo the protocollo to set
	 */
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	/**
	 * @return the elencoEsiti
	 */
	
	/**
	 * @return the esito
	 */
	public String getEsito() {
		return esito;
	}

	/**
	 * @param esito the esito to set
	 */
	public void setEsito(String esito) {
		this.esito = esito;
	}
	




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result + ((dataPresentazione == null) ? 0 : dataPresentazione.hashCode());
		result = prime * result + ((dataProtocollo == null) ? 0 : dataProtocollo.hashCode());
		result = prime * result + ((descrizioneOpera == null) ? 0 : descrizioneOpera.hashCode());
		result = prime * result + ((elencoIndirizzi == null) ? 0 : elencoIndirizzi.hashCode());
		result = prime * result + ((elencoProvvedimenti == null) ? 0 : elencoProvvedimenti.hashCode());
		result = prime * result + ((identificativoRepCartellini == null) ? 0 : identificativoRepCartellini.hashCode());
		result = prime * result + ((indirizzi == null) ? 0 : indirizzi.hashCode());
		result = prime * result
				+ ((numeroProgressivoConservazione == null) ? 0 : numeroProgressivoConservazione.hashCode());
		result = prime * result + ((primoIntestatario == null) ? 0 : primoIntestatario.hashCode());
		result = prime * result + ((provvedimenti == null) ? 0 : provvedimenti.hashCode());
		result = prime * result + ((esito == null) ? 0 : esito.hashCode());
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
		PraticaEdilizia other = (PraticaEdilizia) obj;
		if (codice == null) {
			if (other.codice != null)
				return false;
		} else if (!codice.equals(other.codice))
			return false;
		if (dataPresentazione == null) {
			if (other.dataPresentazione != null)
				return false;
		} else if (!dataPresentazione.equals(other.dataPresentazione))
			return false;
		if (dataProtocollo == null) {
			if (other.dataProtocollo != null)
				return false;
		} else if (!dataProtocollo.equals(other.dataProtocollo))
			return false;
		if (descrizioneOpera == null) {
			if (other.descrizioneOpera != null)
				return false;
		} else if (!descrizioneOpera.equals(other.descrizioneOpera))
			return false;
		if (elencoIndirizzi == null) {
			if (other.elencoIndirizzi != null)
				return false;
		} else if (!elencoIndirizzi.equals(other.elencoIndirizzi))
			return false;
		if (elencoProvvedimenti == null) {
			if (other.elencoProvvedimenti != null)
				return false;
		} else if (!elencoProvvedimenti.equals(other.elencoProvvedimenti))
			return false;
		if (identificativoRepCartellini == null) {
			if (other.identificativoRepCartellini != null)
				return false;
		} else if (!identificativoRepCartellini.equals(other.identificativoRepCartellini))
			return false;
		if (indirizzi == null) {
			if (other.indirizzi != null)
				return false;
		} else if (!indirizzi.equals(other.indirizzi))
			return false;
		if (numeroProgressivoConservazione == null) {
			if (other.numeroProgressivoConservazione != null)
				return false;
		} else if (!numeroProgressivoConservazione.equals(other.numeroProgressivoConservazione))
			return false;
		if (primoIntestatario == null) {
			if (other.primoIntestatario != null)
				return false;
		} else if (!primoIntestatario.equals(other.primoIntestatario))
			return false;
		if (provvedimenti == null) {
			if (other.provvedimenti != null)
				return false;
		} else if (!provvedimenti.equals(other.provvedimenti))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PraticaEdilizia [codice=" + codice + ", primoIntestatario=" + primoIntestatario + ", dataPresentazione="
				+ dataPresentazione + ", descrizioneOpera=" + descrizioneOpera + ", elencoIndirizzi=" + elencoIndirizzi
				+ ", elencoProvvedimenti=" + elencoProvvedimenti + ", numeroProgressivoConservazione="
				+ numeroProgressivoConservazione + ", identificativoRepCartellini=" + identificativoRepCartellini
				+ ", dataProtocollo=" + dataProtocollo + ", indirizzi=" + indirizzi + ", provvedimenti=" + provvedimenti
				+ ", esito=" + esito+ "]";
	}



	

	
}
