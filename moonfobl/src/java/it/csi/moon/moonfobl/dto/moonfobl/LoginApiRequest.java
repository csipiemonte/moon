/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

import java.util.Objects;

/**
 * Richiesta di login per Fruitore via API_FO
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 03/06/2020 - Version Initiale
 */
public class LoginApiRequest extends LoginRequest {
	
	private String email;
	private String idIride;
	private String shibIdentitaJwt;
	private String codiceEnte;
	private String codiceAmbito;
	private String gruppoOperatore;
	private String codiceModulo;
	private String versioneModulo;
	private String clientProfile;
	private String xRequestId;
	private Integer idFruitore; // xMOOn Embedded
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdIride() {
		return idIride;
	}
	public void setIdIride(String idIride) {
		this.idIride = idIride;
	}
	public String getShibIdentitaJwt() {
		return shibIdentitaJwt;
	}
	public void setShibIdentitaJwt(String shibIdentitaJwt) {
		this.shibIdentitaJwt = shibIdentitaJwt;
	}
	public String getCodiceEnte() {
		return codiceEnte;
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public String getCodiceAmbito() {
		return codiceAmbito;
	}
	public void setCodiceAmbito(String codiceAmbito) {
		this.codiceAmbito = codiceAmbito;
	}
	public String getGruppoOperatore() {
		return gruppoOperatore;
	}
	public void setGruppoOperatore(String gruppoOperatore) {
		this.gruppoOperatore = gruppoOperatore;
	}
	public String getCodiceModulo() {
		return codiceModulo;
	}
	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}
	public String getVersioneModulo() {
		return versioneModulo;
	}
	public void setVersioneModulo(String versioneModulo) {
		this.versioneModulo = versioneModulo;
	}
	public String getClientProfile() {
		return clientProfile;
	}
	public void setClientProfile(String clientProfile) {
		this.clientProfile = clientProfile;
	}
	public String getxRequestId() {
		return xRequestId;
	}
	public void setxRequestId(String xRequestId) {
		this.xRequestId = xRequestId;
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
	    LoginApiRequest r = (LoginApiRequest) o;
	    return Objects.equals(email, r.email) &&
	    	Objects.equals(idIride, r.idIride) &&
	    	Objects.equals(shibIdentitaJwt, r.shibIdentitaJwt) &&
			Objects.equals(codiceEnte, r.codiceEnte) &&
			Objects.equals(codiceAmbito, r.codiceAmbito) &&
			Objects.equals(gruppoOperatore, r.gruppoOperatore) &&
			Objects.equals(codiceModulo, r.codiceModulo) &&
			Objects.equals(versioneModulo, r.versioneModulo) &&
			Objects.equals(clientProfile, r.clientProfile) &&
			Objects.equals(xRequestId, r.xRequestId) &&
			Objects.equals(idFruitore, r.idFruitore);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(email, idIride, shibIdentitaJwt, codiceEnte, codiceAmbito, gruppoOperatore, codiceModulo, versioneModulo, clientProfile, xRequestId, idFruitore);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("LoginApiRequest {\n");
	    sb.append("    super: ").append(super.toString()).append("\n");
	    sb.append("    email: ").append(toIndentedString(email)).append("\n");
	    sb.append("    idIride: ").append(toIndentedString(idIride)).append("\n");
	    sb.append("    shibIdentitaJwt: ").append(toIndentedString(shibIdentitaJwt)).append("\n");
	    sb.append("    codiceEnte: ").append(toIndentedString(codiceEnte)).append("\n");
	    sb.append("    codiceAmbito: ").append(toIndentedString(codiceAmbito)).append("\n");
	    sb.append("    gruppoOperatore: ").append(toIndentedString(gruppoOperatore)).append("\n");
	    sb.append("    codiceModulo: ").append(toIndentedString(codiceModulo)).append("\n");
	    sb.append("    versioneModulo: ").append(toIndentedString(versioneModulo)).append("\n");
	    sb.append("    xRequestId: ").append(toIndentedString(xRequestId)).append("\n");
	    sb.append("    idFruitore: ").append(toIndentedString(idFruitore)).append("\n");
	    sb.append("}");
	    return sb.toString();
	  }

}
