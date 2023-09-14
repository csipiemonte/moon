/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Parametri payload per la richiesta di inizializzazione di un istanza per un determinato modulo identificato
 * <br>Servizio /istanze/init/{@code idModulo}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzaInitParams {

  private String ipAdress;
  private Long idModulo;
  private Long idVersioneModulo;
  private Long idEnte;
  private String codiceFiscale;
  private String cognome;
  private String nome;
  @Deprecated
  private String jwt;
  private UserInfo userInfo;
  private String shibIrideIdentitaDigitale;
  private IstanzaInitBLParams blParams;
  private Boolean flagAnprSpento;
  private Boolean flagCompilaBo;

  /**
   * ipAdress dell&#39;utente (String)
   **/
  public String getIpAdress() {
    return ipAdress;
  }
  public void setIpAdress(String ipAdress) {
    this.ipAdress = ipAdress;
  }

  /**
   * l&#39;identificativo dell&#39;modulo (Long)
   **/
  public Long getIdModulo() {
    return idModulo;
  }
  public void setIdModulo(Long idModulo) {
    this.idModulo = idModulo;
  }

  /**
   * l&#39;identificativo dell&#39;modulo Versione (Long)
   **/
  public Long getIdVersioneModulo() {
    return idVersioneModulo;
  }
  public void setIdVersioneModulo(Long idVersioneModulo) {
    this.idVersioneModulo = idVersioneModulo;
  }

  /**
   * l&#39;identificativo dell&#39;ente (Long)
   **/
  public Long getIdEnte() {
    return idEnte;
  }
  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }
  
  /**
   * codice fiscale dell&#39;utente
   **/
  @ApiModelProperty(value = "codice fiscale dell'utente")
  // nome originario nello yaml: codFisc 
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
  // nome originario nello yaml: cognome 
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
  // nome originario nello yaml: nome 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * JWT dell&#39;utente
   **/
  
  @ApiModelProperty(value = "JWT dell'utente")

  // nome originario nello yaml: jwt 
  public String getJwt() {
	if (this.jwt == null && this.userInfo != null) {
		return this.userInfo.getJwt();
	}
    return jwt;
  }
  @Deprecated
  public void setJwt(String jwt) {
    this.jwt = jwt;
  }
	
	public Boolean getFlagAnprSpento() {
		return flagAnprSpento;
	}
	public void setFlagAnprSpento(Boolean flagAnprSpento) {
		this.flagAnprSpento = flagAnprSpento;
	}
	public IstanzaInitBLParams getBlParams() {
		return blParams;
	}
	public void setBlParams(IstanzaInitBLParams blParams) {
		this.blParams = blParams;
	}
	public String getShibIrideIdentitaDigitale() {
		return shibIrideIdentitaDigitale;
	}
	public void setShibIrideIdentitaDigitale(String shibIrideIdentitaDigitale) {
		this.shibIrideIdentitaDigitale = shibIrideIdentitaDigitale;
	}
	public Boolean getFlagCompilaBo() {
		return flagCompilaBo;
	}
	public void setFlagCompilaBo(Boolean flagCompilaBo) {
		this.flagCompilaBo = flagCompilaBo;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class IstanzaInitParams {\n");
		sb.append("    ipAdress: ").append(toIndentedString(ipAdress)).append("\n");
		sb.append("    idModulo: ").append(toIndentedString(idModulo)).append("\n");
		sb.append("    idModuloVersione: ").append(toIndentedString(idVersioneModulo)).append("\n");
		sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
		sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
		sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
		sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
		sb.append("    shibIrideIdentitaDigitale: ").append(toIndentedString(shibIrideIdentitaDigitale)).append("\n");
		sb.append("    blParams: ").append(toIndentedString(blParams)).append("\n");
		sb.append("    flagAnprSpento: ").append(toIndentedString(flagAnprSpento)).append("\n");
		sb.append("    flagCompilaBo: ").append(toIndentedString(flagCompilaBo)).append("\n");
		sb.append("    userInfo: ").append(toIndentedString(userInfo)).append("\n");
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

