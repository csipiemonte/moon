/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

public class LoginResponse extends UserInfo implements Cloneable {

	private Modulo modulo;
//	private String portaleName;
	private String urlLogout;
	private DatiAggiuntivi datiAggiuntivi;
	private String photoUrl;
//	private EmbeddedNavigator embeddedNavigator;
//	private List<EnteAreeRuoli> entiAreeRuoli = null;
	
	public LoginResponse() {
		super();
	}

	public LoginResponse(String codFisc, String cognome, String nome) {
		super(codFisc, cognome, nome);
	}

	public LoginResponse(UserInfo ui) {
		super();
		setIdentificativoUtente(ui.getIdentificativoUtente());
		setCognome(ui.getCognome());
		setNome(ui.getNome());
		setEnte(ui.getEnte());
		setMultiEntePortale(ui.isMultiEntePortale());
		setPortalName(ui.getPortalName());
		setTipoUtente(ui.getTipoUtente());
		setIdIride(ui.getIdIride());
		setEntiAreeRuoli(ui.getEntiAreeRuoli());
		setDatiAggiuntivi(ui.getDatiAggiuntivi());
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
		
/*	
	public EmbeddedNavigator getEmbeddedNavigator() {
		return embeddedNavigator;
	}
	public void setEmbeddedNavigator(EmbeddedNavigator embeddedNavigator) {
		this.embeddedNavigator = embeddedNavigator;
	}
*/
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
//		if (portaleName == null) {
//			if (other.portaleName != null)
//				return false;
//		} else if (!portaleName.equals(other.portaleName))
//			return false;
/*		
		if (embeddedNavigator == null) {
			if (other.embeddedNavigator != null)
				return false;
		} else if (!embeddedNavigator.equals(other.embeddedNavigator))
			return false;
*/		
		
		return true;
	}

	  @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((modulo == null) ? 0 : modulo.hashCode());
//		result = prime * result + ((portaleName == null) ? 0 : portaleName.hashCode());
//		result = prime * result + ((embeddedNavigator == null) ? 0 : embeddedNavigator.hashCode());
		return result;
	}

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("LoginResponse {\n");
	    sb.append(super.toString()).append("\n");
	    sb.append("    modulo: ").append(toIndentedString(modulo)).append("\n");
//	    sb.append("    portaleName: ").append(toIndentedString(portaleName)).append("\n");
	    sb.append("    urlLogout: ").append(toIndentedString(urlLogout)).append("\n");
	    sb.append("    datiAggiuntivi: ").append(toIndentedString(datiAggiuntivi)).append("\n");
	    sb.append("    photoUrl: ").append(toIndentedString(photoUrl)).append("\n");
//	    sb.append("    embeddedNavigator: ").append(toIndentedString(embeddedNavigator)).append("\n");
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

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
