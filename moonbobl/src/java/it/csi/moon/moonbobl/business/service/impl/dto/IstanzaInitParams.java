/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;
import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;

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
	  private String jwt;
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
	    return jwt;
	  }
	  public void setJwt(String jwt) {
	    this.jwt = jwt;
	  }
		
	  public Boolean getFlagAnprSpento() {
			return flagAnprSpento;
	  }
	  
	  public void setFlagAnprSpento(Boolean flagAnprSpento) {
			this.flagAnprSpento = flagAnprSpento;		
	  }
		
	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    IstanzaInitParams istanza = (IstanzaInitParams) o;
	    return Objects.equals(idModulo, istanza.idModulo) &&
	    	Objects.equals(idVersioneModulo, istanza.idVersioneModulo) &&
	    	Objects.equals(idEnte, istanza.idEnte) &&
	        Objects.equals(codiceFiscale, istanza.codiceFiscale) &&
	        Objects.equals(cognome, istanza.cognome) &&
	        Objects.equals(nome, istanza.nome) &&
	        Objects.equals(jwt, istanza.jwt) &&
//	        Objects.equals(numeroCartaIdentita, istanza.numeroCartaIdentita) &&
//	        Objects.equals(dataCartaIdentita, istanza.dataCartaIdentita) &&
//	        Objects.equals(numeroPermessoSoggiorno, istanza.numeroPermessoSoggiorno) &&
//	        Objects.equals(dataPermessoSoggiorno, istanza.dataPermessoSoggiorno) &&
	        Objects.equals(flagAnprSpento, istanza.flagAnprSpento) &&
	        Objects.equals(flagCompilaBo, istanza.flagCompilaBo) &&
	        Objects.equals(blParams, istanza.blParams);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(idModulo, idVersioneModulo, idEnte, codiceFiscale, cognome, nome, jwt/*, numeroCartaIdentita, dataCartaIdentita, numeroPermessoSoggiorno, dataPermessoSoggiorno*/, flagAnprSpento,flagCompilaBo,blParams);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class IstanzaInitParams {\n");
	    
	    sb.append("    idModulo: ").append(toIndentedString(idModulo)).append("\n");
	    sb.append("    idModuloVersione: ").append(toIndentedString(idVersioneModulo)).append("\n");
	    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
	    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
	    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
	    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
	    sb.append("    jwt: ").append(toIndentedString(jwt)).append("\n");
//	    sb.append("    numeroCartaIdentita: ").append(toIndentedString(numeroCartaIdentita)).append("\n");
//	    sb.append("    dataCartaIdentita: ").append(toIndentedString(dataCartaIdentita)).append("\n");
//	    sb.append("    permessoSoggiorno: ").append(toIndentedString(numeroPermessoSoggiorno)).append("\n");
//	    sb.append("    dataPermessoSoggiorno: ").append(toIndentedString(dataPermessoSoggiorno)).append("\n");
	    sb.append("    flagAnprSpento: ").append(toIndentedString(flagAnprSpento)).append("\n");
	    sb.append("    flagCompilaBo: ").append(toIndentedString(flagCompilaBo)).append("\n");
	    sb.append("    blParams: ").append(toIndentedString(blParams)).append("\n");
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
	public IstanzaInitBLParams getBlParams() {
		return blParams;
	}
	public void setBlParams(IstanzaInitBLParams blParams) {
		this.blParams = blParams;
	}
	public Boolean getFlagCompilaBo() {
		return flagCompilaBo;
	}
	public void setFlagCompilaBo(Boolean flagCompilaBo) {
		this.flagCompilaBo = flagCompilaBo;
	}
	  
	}

