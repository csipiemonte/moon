/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

import java.util.Objects;

/**
 * Richiesta di login
 * <br>Tutti attributi sono String, non c'è controllo ne della tipologia del dato, ne dell'obbligatorieta al livello di API
 * <br>
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 31/03/2020 - Version Initiale
 */
public class LoginRequest {
	
	public static enum TipoDocumento { 
		CARTA_IDENTITA(1), 
		PERMESSO_SOGGIORNO(2),
		;
		private final Integer id;
		private TipoDocumento(Integer id2) { id=id2; }
		public Integer getId() { return id; }
	};
	
	private String logonMode;
	private String codiceModulo;
	private String codiceFiscale;
	private String cognome;
	private String nome;
	private Integer tipoDocumento;
	private String numeroDocumento;
	private String dataRilascioDocumento;
	private String login;
	private String password;
	private String pin;
	// valorixxati dalla componente Bl
	private String provider;
	private String nomePortale;
	private Integer idFruitore; // xMOOn Embedded
	
	private String socialUser;
	
	public String getSocialUser() {
		return socialUser;
	}
	public void setSocialUser(String socialUser) {
		this.socialUser = socialUser;
	}
	public String getLogonMode() {
		return logonMode;
	}
	public void setLogonMode(String logonMode) {
		this.logonMode = logonMode;
	}
	public String getCodiceModulo() {
		return codiceModulo;
	}
	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(Integer tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	
	public String getDataRilascioDocumento() {
		return dataRilascioDocumento;
	}
	public void setDataRilascioDocumento(String dataRilascioDocumento) {
		this.dataRilascioDocumento = dataRilascioDocumento;
	}

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getNomePortale() {
		return nomePortale;
	}
	public void setNomePortale(String nomePortale) {
		this.nomePortale = nomePortale;
	}
	public Integer getIdFruitore() {
		return idFruitore;
	}
	public void setIdFruitore(Integer idFruitore) {
		this.idFruitore = idFruitore;
	}
	
	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    LoginRequest r = (LoginRequest) o;
	    return Objects.equals(logonMode, r.logonMode) &&
	    	Objects.equals(codiceModulo, r.codiceModulo) &&
	    	Objects.equals(codiceFiscale, r.codiceFiscale) &&
			Objects.equals(cognome, r.cognome) &&
			Objects.equals(nome, r.nome) &&
			Objects.equals(tipoDocumento, r.tipoDocumento) &&
			Objects.equals(numeroDocumento, r.numeroDocumento) &&
			Objects.equals(dataRilascioDocumento, r.dataRilascioDocumento) &&
			Objects.equals(login, r.login) &&
			Objects.equals(password, r.password) &&
			Objects.equals(pin, r.pin) &&
			Objects.equals(idFruitore, r.idFruitore);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(logonMode, codiceModulo, codiceFiscale, cognome, nome, tipoDocumento, numeroDocumento, dataRilascioDocumento, login, password, pin);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("LoginRequest {\n");
	    sb.append("    logonMode: ").append(toIndentedString(logonMode)).append("\n");
	    sb.append("    codiceModulo: ").append(toIndentedString(codiceModulo)).append("\n");
	    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
	    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
	    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
	    sb.append("    tipoDocumento: ").append(toIndentedString(tipoDocumento)).append("\n");
	    sb.append("    numeroDocumento: ").append(toIndentedString(numeroDocumento)).append("\n");
	    sb.append("    dataRilascioDocumento: ").append(toIndentedString(dataRilascioDocumento)).append("\n");
	    sb.append("    login: ").append(toIndentedString(login)).append("\n");
	    sb.append("    password: ").append(toIndentedString(password)).append("\n");
	    sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
	    sb.append("    nome portale: ").append(toIndentedString(nomePortale)).append("\n");
	    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
	    sb.append("    idFruitore: ").append(toIndentedString(idFruitore)).append("\n");
	    sb.append("}");
	    return sb.toString();
	  }

	  /**
	   * Convert the given object to string with each line indented by 4 spaces
	   * (except the first line).
	   */
	  protected String toIndentedString(Object o) {
	    if (o == null) {
	      return "null";
	    }
	    return o.toString().replace("\n", "\n    ");
	  }

}
