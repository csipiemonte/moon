/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;




public class UserInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private String nome = null;
  private String cognome = null;
  private String identificativoUtente = null;
  private String mail = null;
  private boolean isMultiEntePortale;
  private Ente ente = null;
  private TipoUtente tipoUtente = null;
  private List<EnteAreeRuoli> entiAreeRuoli = null;
  private List<Ruolo> ruoli = null;
  private List<String> funzioni = null;
  private List<String> gruppi = null;
  private String idIride = null;
  private String idMoonToken = null;
  private String jwt = null;
  private Date dataOraLogin = null;
  private DatiAggiuntivi datiAggiuntivi = null;
  private String provider = "N.D.";
  private String portalName = null;

  public UserInfo() {
	  super();
  }
  public UserInfo(String identificativoUtente) {
	  this.identificativoUtente = identificativoUtente;
  }
  public UserInfo(String identificativoUtente, String cognome, String nome) {
	  this.identificativoUtente = identificativoUtente;
	  this.cognome = cognome;
	  this.nome = nome;
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
   * codice fiscale dell&#39;utente
   **/
  @ApiModelProperty(value = "identificativo utente")
  public String getIdentificativoUtente() {
    return identificativoUtente;
  }
  public void setIdentificativoUtente(String identificativoUtente) {
    this.identificativoUtente = identificativoUtente;
  }

  /**
   * mail dell&#39;utente preso dall'identificazione
   **/
  @ApiModelProperty(value = "mail dell'utente preso dall'identificazione")
  public String getMail() {
    return mail;
  }
  public void setMail(String mail) {
    this.mail = mail;
  }
  
  
  /**
   * flag se utente is logged on multiEnte Portale
   **/
  @ApiModelProperty(value = "flag se utente is logged on multiEnte Portale")
  public boolean isMultiEntePortale() {
	return isMultiEntePortale;
  }
  public void setMultiEntePortale(boolean isMultiEntePortale) {
	this.isMultiEntePortale = isMultiEntePortale;
  }
  
  /**
   * ID ente corrente dell&#39;ente legato ai moduli/istanze
   **/
//  @ApiModelProperty(value = "ID ente dell'ente corrente legato ai moduli/istanze")
//  public Long getIdEnte() {
//    return idEnte;
//  }
//  public void setIdEnte(Long idEnte) {
//    this.idEnte = idEnte;
//  }
  
  /**
   * ente corrente dell&#39;ente legato ai moduli/istanze
   **/
  @ApiModelProperty(value = "ente corrente legato ai moduli/istanze")
  public Ente getEnte() {
    return ente;
  }
  public void setEnte(Ente ente) {
    this.ente = ente;
  }
  
  /**
   * tipologia dell&#39;utente (ADM,PA,..)
   **/
  @ApiModelProperty(value = "tipologia dell'utente (ADM,PA,..)")
  public TipoUtente getTipoUtente() {
    return tipoUtente;
  }
  public void setTipoUtente(TipoUtente tipoUtente) {
    this.tipoUtente = tipoUtente;
  }
  
  /**
   * nome del ruolo dell&#39;utente
   **/
  @ApiModelProperty(value = "list entiAreeRuoli dell'utente")
  public List<EnteAreeRuoli> getEntiAreeRuoli() {
    return entiAreeRuoli;
  }
  public void setEntiAreeRuoli(List<EnteAreeRuoli> entiAreeRuoli) {
    this.entiAreeRuoli = entiAreeRuoli;
  }
  
  /**
   * nome del ruolo dell&#39;utente
   **/
  @ApiModelProperty(value = "list degli ruoli dell'utente")
  public List<Ruolo> getRuoli() {
    return ruoli;
  }
  public void setRuoli(List<Ruolo> ruoli) {
    this.ruoli = ruoli;
  }

  /**
   * list delle funzioni accessibile all&#39;utente
   **/
  @ApiModelProperty(value = "list delle funzioni accessibile all'utente")
  public List<String> getFunzioni() {
    return funzioni;
  }
  public void setFunzioni(List<String> funzioni) {
    this.funzioni = funzioni;
  }

  /**
   * ID iride dell&#39;utente
   **/
  @ApiModelProperty(value = "ID iride dell'utente")
  public String getIdIride() {
    return idIride;
  }
  public void setIdIride(String idIride) {
    this.idIride = idIride;
  }

  /**
   * ID Moon Token
   **/
  @ApiModelProperty(value = "ID MOOn Token")
  public String getIdMoonToken() {
    return idMoonToken;
  }
  public void setIdMoonToken(String idMoonToken) {
    this.idMoonToken = idMoonToken;
  }
  
  /**
   * JWT dell&#39;utente
   **/
  @ApiModelProperty(value = "JWT dell'utente")
  public String getJwt() {
    return jwt;
  }
  public void setJwt(String jwt) {
    this.jwt = jwt;
  }

	public String getPortalName() {
		return portalName;
	}
	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}
	
	public DatiAggiuntivi getDatiAggiuntivi() {
		return datiAggiuntivi;
	}
	public void setDatiAggiuntivi(DatiAggiuntivi datiAggiuntivi) {
		this.datiAggiuntivi = datiAggiuntivi;
	}
	
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfo userInfo = (UserInfo) o;
    return Objects.equals(nome, userInfo.nome) &&
        Objects.equals(cognome, userInfo.cognome) &&
        Objects.equals(identificativoUtente, userInfo.identificativoUtente) &&
        Objects.equals(mail, userInfo.mail) &&
        Objects.equals(isMultiEntePortale, userInfo.isMultiEntePortale) &&
        Objects.equals(ente, userInfo.ente) &&
        Objects.equals(tipoUtente, userInfo.tipoUtente) &&
        Objects.equals(entiAreeRuoli, userInfo.entiAreeRuoli) &&
        Objects.equals(ruoli, userInfo.ruoli) &&
        Objects.equals(funzioni, userInfo.funzioni) &&
        Objects.equals(gruppi, userInfo.gruppi) &&
        Objects.equals(idIride, userInfo.idIride) &&
        Objects.equals(idMoonToken, userInfo.idMoonToken) &&
        Objects.equals(jwt, userInfo.jwt) &&
        Objects.equals(portalName, userInfo.portalName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, cognome, identificativoUtente, mail, isMultiEntePortale, ente, tipoUtente, entiAreeRuoli, ruoli, funzioni, gruppi, idIride, idMoonToken, jwt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfo {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    identificativoUtente: ").append(toIndentedString(identificativoUtente)).append("\n");
    sb.append("    mail: ").append(toIndentedString(mail)).append("\n");
    sb.append("    isMultiEntePortale: ").append(toIndentedString(isMultiEntePortale)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    tipoUtente: ").append(toIndentedString(tipoUtente)).append("\n");
    sb.append("    entiAreeRuoli: ").append(toIndentedString(entiAreeRuoli)).append("\n");
    sb.append("    ruoli: ").append(toIndentedString(ruoli)).append("\n");
    sb.append("    funzioni: ").append(toIndentedString(funzioni)).append("\n");
    sb.append("    gruppi: ").append(toIndentedString(gruppi)).append("\n");
    sb.append("    idIride: ").append(toIndentedString(idIride)).append("\n");
    sb.append("    idMoonToken: ").append(toIndentedString(idMoonToken)).append("\n");
    sb.append("    jwt: ").append(toIndentedString(jwt)).append("\n");
    sb.append("    portalName: ").append(toIndentedString(portalName)).append("\n");
    sb.append("    datiAggiuntivi: ").append(datiAggiuntivi==null?null:datiAggiuntivi).append("\n");
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
  
  public Boolean hasRuolo(Integer idRuolo) {
	  Boolean hasRuolo = false;
	  if (this.entiAreeRuoli==null)
		  return false;
	  List<EnteAreeRuoli> entiAreeRuoli = this.entiAreeRuoli;
	  for(EnteAreeRuoli enteAR: entiAreeRuoli) {
		  List<AreaRuolo> areeRuoli = enteAR.getAreeRuoli();
		  for(AreaRuolo areaRuolo: areeRuoli) {
			  if (areaRuolo.getIdRuolo().intValue() == idRuolo.intValue()) {
				  hasRuolo = true;
			  }
		  }
	  }
	  return hasRuolo;
  }
  
  public Boolean hasRuoloByCode(String codiceRuolo) {
	  Boolean hasRuolo = false;
	  if (this.entiAreeRuoli==null)
		  return false;
	  List<EnteAreeRuoli> entiAreeRuoli = this.entiAreeRuoli;
	  for(EnteAreeRuoli enteAR: entiAreeRuoli) {
		  List<AreaRuolo> areeRuoli = enteAR.getAreeRuoli();
		  for(AreaRuolo areaRuolo: areeRuoli) {
			  if (areaRuolo.getCodiceRuolo().equals(codiceRuolo)) {
				  hasRuolo = true;
			  }
		  }
	  }
	  return hasRuolo;
  }

public Date getDataOraLogin() {
	return dataOraLogin;
}
public void setDataOraLogin(Date dataOraLogin) {
	this.dataOraLogin = dataOraLogin;
}
public String getProvider() {
	return provider;
}
public void setProvider(String provider) {
	this.provider = provider;
}
  
}

