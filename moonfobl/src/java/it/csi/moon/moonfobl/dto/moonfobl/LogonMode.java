/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

import java.util.Objects;

/**
 * Modalita di Logon per accedere ad un modulo
 * Serve per construire la pagina di login lato cliente dinamicamente
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 17/04/2020 - Version Initiale
 */
public class LogonMode {

  private String codiceModulo;
  private String logonMode = null;
  private String args = null;
  //
  private Long idEnte = null;
  private String logo = null;
  private String titoloHeader = null;
  private String urlLogout = null;

  public LogonMode() {
  }
//  public LogonMode(String codiceModulo, String logonMode, String args) {
//	  this.codiceModulo = codiceModulo;
//	  this.logonMode = logonMode;
//	  this.args = args;
//  }
	
	public LogonMode(Long idEnte, String logo, String titoloHeader, String urlLogout) {
		super();
		this.idEnte = idEnte;
		this.logo = logo;
		this.titoloHeader = titoloHeader;
		this.urlLogout = urlLogout;
	}
	
	public String getCodiceModulo() {
		return codiceModulo;
	}
	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}
		
	  /**
	   * l&#39;identificativo della modalita di logon
	   **/
	  // nome originario nello yaml: logonMode 
	  public String getLogonMode() {
	    return logonMode;
	  }
	  public void setLogonMode(String logonMode) {
	    this.logonMode = logonMode;
	  }
		
	  /**
	   * argumenti metadati <K,V> della modalita di logon
	   **/
	  // nome originario nello yaml: args 
	  public String getArgs() {
	    return args;
	  }
	  public void setArgs(String args) {
	    this.args = args;
	  }
	  
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public String getTitoloHeader() {
		return titoloHeader;
	}
	public void setTitoloHeader(String titoloHeader) {
		this.titoloHeader = titoloHeader;
	}
	
	public String getUrlLogout() {
		return urlLogout;
	}
	public void setUrlLogout(String urlLogout) {
		this.urlLogout = urlLogout;
	}
	
	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    LogonMode s = (LogonMode) o;
	    return Objects.equals(codiceModulo, s.codiceModulo) &&
	    	Objects.equals(logonMode, s.logonMode) &&
	    	Objects.equals(args, s.args) &&
	    	Objects.equals(idEnte, s.idEnte) &&
	    	Objects.equals(logo, s.logo) &&
	    	Objects.equals(titoloHeader, s.titoloHeader) &&
	    	Objects.equals(urlLogout, s.urlLogout);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(codiceModulo, logonMode, args, idEnte, logo, titoloHeader, urlLogout, super.hashCode());
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class LogonMode {\n");
	    sb.append("    codiceModulo: ").append(toIndentedString(codiceModulo)).append("\n");
	    sb.append("    logonMode: ").append(toIndentedString(logonMode)).append("\n");
	    sb.append("    args: ").append(toIndentedString(args)).append("\n");
	    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
	    sb.append("    logo: ").append(toIndentedString(logo)).append("\n");
	    sb.append("    titoloHeader: ").append(toIndentedString(titoloHeader)).append("\n");
	    sb.append("    urlLogout: ").append(toIndentedString(urlLogout)).append("\n");
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
