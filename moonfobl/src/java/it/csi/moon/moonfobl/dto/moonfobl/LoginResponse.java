/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

import it.csi.moon.commons.dto.DatiAggiuntivi;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.UserInfo;

/**
 * Risposta della login
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 01/04/2020 - Version Initiale
 */
public class LoginResponse extends UserInfo {

	private Modulo modulo;
	private String urlLogout;
	private DatiAggiuntivi datiAggiuntivi;
	private String photoUrl;
	private EmbeddedNavigator embeddedNavigator;
	
	public LoginResponse() {
		super();
	}
	public LoginResponse(LoginResponse loginResponseToClone) {
		super(loginResponseToClone);
		this.modulo = loginResponseToClone.getModulo()==null?null:new Modulo(loginResponseToClone.getModulo());
		this.urlLogout = loginResponseToClone.getUrlLogout();
		this.datiAggiuntivi = loginResponseToClone.getDatiAggiuntivi()==null?null:new DatiAggiuntivi(loginResponseToClone.getDatiAggiuntivi());
		this.photoUrl = loginResponseToClone.getPhotoUrl();
		this.embeddedNavigator = loginResponseToClone.getEmbeddedNavigator()==null?null:new EmbeddedNavigator(loginResponseToClone.getEmbeddedNavigator());
	}

	public LoginResponse(String codFisc, String cognome, String nome) {
		super(codFisc, cognome, nome);
	}

	public LoginResponse(UserInfo ui) {
		super();
		setIdentificativoUtente(ui.getIdentificativoUtente());
		setCodFiscDichIstanza(ui.getCodFiscDichIstanza());
		setCognome(ui.getCognome());
		setNome(ui.getNome());
//	setIdEnte(ui.getIdEnte());
		setEnte(ui.getEnte());
		setMultiEntePortale(ui.isMultiEntePortale());
		setPortalName(ui.getPortalName());
		setOperatore(ui.isOperatore());
		setGruppoOperatoreFo(ui.getGruppoOperatoreFo());
		setIdAmbito(ui.getIdAmbito());
		setDescrizioneAmbito(ui.getDescrizioneAmbito());
		setIdFruitore(ui.getIdFruitore());
//		setJwt(ui.getJwt()); // cookie size exceeded
	}
  
	
	public Modulo getModulo() {
		return modulo;
	}
	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
	public String getUrlLogout() {
		return urlLogout;
	}
	public void setUrlLogout(String urlLogout) {
		this.urlLogout = urlLogout;
	}
	public DatiAggiuntivi getDatiAggiuntivi() {
		return datiAggiuntivi;
	}
	public void setDatiAggiuntivi(DatiAggiuntivi datiAggiuntivi) {
		this.datiAggiuntivi = datiAggiuntivi;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public EmbeddedNavigator getEmbeddedNavigator() {
		return embeddedNavigator;
	}
	public void setEmbeddedNavigator(EmbeddedNavigator embeddedNavigator) {
		this.embeddedNavigator = embeddedNavigator;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginResponse other = (LoginResponse) obj;
		if (modulo == null) {
			if (other.modulo != null)
				return false;
		} else if (!modulo.equals(other.modulo))
			return false;
		if (embeddedNavigator == null) {
			if (other.embeddedNavigator != null)
				return false;
		} else if (!embeddedNavigator.equals(other.embeddedNavigator))
			return false;
		return true;
	}

	  @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((modulo == null) ? 0 : modulo.hashCode());
		result = prime * result + ((embeddedNavigator == null) ? 0 : embeddedNavigator.hashCode());
		return result;
	}

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("LoginResponse {\n");
	    sb.append(super.toString()).append("\n");
	    sb.append("    modulo: ").append(toIndentedString(modulo)).append("\n");
	    sb.append("    urlLogout: ").append(toIndentedString(urlLogout)).append("\n");
	    sb.append("    datiAggiuntivi: ").append(toIndentedString(datiAggiuntivi)).append("\n");
	    sb.append("    photoUrl: ").append(toIndentedString(photoUrl)).append("\n");
	    sb.append("    embeddedNavigator: ").append(toIndentedString(embeddedNavigator)).append("\n");
	    sb.append("}");
	    return sb.toString();
	  }

}
