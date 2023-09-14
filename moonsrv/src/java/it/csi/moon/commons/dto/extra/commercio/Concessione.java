/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.extra.commercio;

import java.util.Objects;

public class Concessione   {

  private Long idConcessione;
  private String cfConcessionario;
  private String concessionario;
  private String autorizzazione;
  private String mercato;
  private String posteggio;
  private String settore;
  private String giorno;
	

  public Long getIdConcessione() {
	return idConcessione;
}


public void setIdConcessione(Long idConcessione) {
	this.idConcessione = idConcessione;
}


public String getCfConcessionario() {
	return cfConcessionario;
}


public void setCfConcessionario(String cfConcessionario) {
	this.cfConcessionario = cfConcessionario;
}


public String getConcessionario() {
	return concessionario;
}


public void setConcessionario(String concessionario) {
	this.concessionario = concessionario;
}


public String getAutorizzazione() {
	return autorizzazione;
}


public void setAutorizzazione(String autorizzazione) {
	this.autorizzazione = autorizzazione;
}


public String getMercato() {
	return mercato;
}


public void setMercato(String mercato) {
	this.mercato = mercato;
}


public String getPosteggio() {
	return posteggio;
}


public void setPosteggio(String posteggio) {
	this.posteggio = posteggio;
}


public String getSettore() {
	return settore;
}


public void setSettore(String settore) {
	this.settore = settore;
}


public String getGiorno() {
	return giorno;
}


public void setGiorno(String giorno) {
	this.giorno = giorno;
}


public Concessione() {
		super();
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Concessione concessione = (Concessione) o;
    return Objects.equals(cfConcessionario, concessione.cfConcessionario) &&
        Objects.equals(autorizzazione, concessione.autorizzazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cfConcessionario, autorizzazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Comune {\n");
    sb.append("    cfConcessionario: ").append(toIndentedString(cfConcessionario)).append("\n");
    sb.append("    autorizzazione: ").append(toIndentedString(autorizzazione)).append("\n");
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

