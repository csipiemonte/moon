/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dto;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;
import it.csi.moon.commons.dto.UserInfo;

/**
 * Parametri payload per la richiesta di inizializzazione di un istanza per un determinato modulo identificato
 * <br>Servizio /istanze/init/{@code idModulo}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class AzioneInitParams   {

	private Long idIstanza;
	private String datiIstanza;
	private String codiceFiscale;
	private String cognome;
	private String nome;
	private UserInfo user;
	private String ipAddress;

	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	
	public String getDatiIstanza() {
		return datiIstanza;
	}
	public void setDatiIstanza(String datiIstanza) {
		this.datiIstanza = datiIstanza;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	/**
	 * codice fiscale dell&#39;utente
	 **/
	@ApiModelProperty(value = "codice fiscale dell'utente")
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * cognome dell&#39;utente
	 **/
	@ApiModelProperty(value = "cognome dell'utente")
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * nome dell&#39;utente
	 **/
	@ApiModelProperty(value = "nome dell'utente")
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AzioneInitParams objToCompare = (AzioneInitParams) o;
		return 
				Objects.equals(idIstanza, objToCompare.idIstanza) &&
				Objects.equals(codiceFiscale, objToCompare.codiceFiscale) &&
				Objects.equals(cognome, objToCompare.cognome) &&
				Objects.equals(nome, objToCompare.nome) &&
				Objects.equals(user, objToCompare.user) &&
				Objects.equals(ipAddress, objToCompare.ipAddress); 
	}



	@Override
	public int hashCode() {
		return Objects.hash(idIstanza, codiceFiscale, cognome, nome, user, ipAddress);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("AzioneInitParams {\n");
		sb.append("    idIstanza: ").append(toIndentedString(idIstanza)).append("\n");
		sb.append("    datiIstanza: ").append(toIndentedString(datiIstanza)).append("\n");
		sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
		sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
		sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
		sb.append("    user: ").append(toIndentedString(user)).append("\n");
		sb.append("    ipAddress: ").append(toIndentedString(ipAddress)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}

