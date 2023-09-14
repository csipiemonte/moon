/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Istanza   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
  private Long idIstanza = null;
  private String codiceIstanza = null;
  private String identificativoUtente = null;
  private String codiceFiscaleDichiarante = null;
  private String cognomeDichiarante = null;
  private String nomeDichiarante = null;
  private Stato stato = null;
  private Stato statoBo = null;
  private Date created = null; // DataInserimento, in seguito DataSubmit = dataInvio
  private Date dataStato = null;
  private Date modified = null;
  private Object data = null;
  private Modulo modulo = null;
  private String attoreIns = null;
  private String attoreUpd = null;
  private boolean flagEliminata = false;
  private boolean flagArchiviata = false;
  private boolean flagTest = false;
  private Integer importanza = 0;
  private Object metadata = null;
//  private Date dataInvio = null;
  private Integer currentStep = 0;
  private String numeroProtocollo = null;
  private Date dataProtocollo = null;
//  private Long idVersioneModulo = null;
  private Long idEnte = null;
  private Long idArea = null;
  // operatore = utente di bo che ha gestito l'ultima azione sull'istanza
  private String operatore = null;
  private Date dataEsitoPagamento = null;
  private List<Pagamento> pagamenti = null;
  private Boolean isPagato  = null;
  

/**
   * l&#39;identificativo dell&#39;istanza (Long)
   **/
  


  // nome originario nello yaml: idIstanza 
  public Long getIdIstanza() {
    return idIstanza;
  }
  public void setIdIstanza(Long idIstanza) {
    this.idIstanza = idIstanza;
  }

  /**
   * l&#39;identificativo utente dell&#39;istanza secondo progressivo modulo
   **/
  


  // nome originario nello yaml: codiceIstanza 
  public String getCodiceIstanza() {
    return codiceIstanza;
  }
  public void setCodiceIstanza(String codiceIstanza) {
    this.codiceIstanza = codiceIstanza;
  }

  /**
   **/
  // nome originario nello yaml: stato 
  public Stato getStato() {
    return stato;
  }
  public void setStato(Stato stato) {
    this.stato = stato;
  }

  /**
   **/
  // nome originario nello yaml: statoBo 
  public Stato getStatoBo() {
    return statoBo;
  }
  public void setStatoBo(Stato statoBo) {
    this.statoBo = statoBo;
  }
  

  /**
   **/
  // nome originario nello yaml: created 
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
  public Date getCreated() {
    return created;
  }
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   **/
  


  // nome originario nello yaml: modified 
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
  public Date getModified() {
    return modified;
  }
  public void setModified(Date modified) {
    this.modified = modified;
  }

  /**
   * i dati del modulo compilati dall&#39;utente
   **/
  


  // nome originario nello yaml: data 
  public Object getData() {
    return data;
  }
  public void setData(Object data) {
    this.data = data;
  }

  /**
   **/
  


  // nome originario nello yaml: modulo 
  public Modulo getModulo() {
    return modulo;
  }
  public void setModulo(Modulo modulo) {
    this.modulo = modulo;
  }

  /**
   **/
  
	public String getIdentificativoUtente() {
		return identificativoUtente;
	}

	public void setIdentificativoUtente(String identificativoUtente) {
		this.identificativoUtente = identificativoUtente;
	}


  // nome originario nello yaml: attoreIns 
  public String getAttoreIns() {
    return attoreIns;
  }
  public void setAttoreIns(String attoreIns) {
    this.attoreIns = attoreIns;
  }
  
  /**
   **/
  


  // nome originario nello yaml: attoreUpd 
  public String getAttoreUpd() {
    return attoreUpd;
  }
  public void setAttoreUpd(String attoreUpd) {
    this.attoreUpd = attoreUpd;
  }

  /**
   **/
  


  // importanza dell instanza per l owner 
  public Integer getImportanza() {
    return importanza;
  }
  public void setImportanza(Integer importanza) {
    this.importanza = importanza;
  }
  
  
  /**
   **/
  // Flag di cancellazione logica dell instanza
  public boolean getFlagEliminata() {
    return flagEliminata;
  }
  public void setFlagEliminata(boolean flagEliminata) {
    this.flagEliminata = flagEliminata;
  }
  
  /**
   **/
  // Flag di archiviata dell instanza
  public boolean getFlagArchiviata() {
    return flagArchiviata;
  }
  public void setFlagArchiviata(boolean flagArchiviata) {
    this.flagArchiviata = flagArchiviata;
  }
  
  /**
   **/
  // Flag di Test
  public boolean getFlagTest() {
    return flagTest;
  }
  public void setFlagTest(boolean flagTest) {
    this.flagTest = flagTest;
  }
  
  
  /**
   **/
  // nome originario nello yaml: metadata 
  public Object getMetadata() {
    return metadata;
  }
  public void setMetadata(Object metadata) {
    this.metadata = metadata;
  }

//  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
//  public Date getDataInvio() {
//	return dataInvio;
//}
//public void setDataInvio(Date dataInvio) {
//	this.dataInvio = dataInvio;
//}
public Integer getCurrentStep() {
	return currentStep;
}
public void setCurrentStep(Integer currentStep) {
	this.currentStep = currentStep;
}
public String getCodiceFiscaleDichiarante() {
	return codiceFiscaleDichiarante;
}
public void setCodiceFiscaleDichiarante(String codiceFiscaleDichiarante) {
	this.codiceFiscaleDichiarante = codiceFiscaleDichiarante;
}
public String getNumeroProtocollo() {
	return numeroProtocollo;
}
public void setNumeroProtocollo(String numeroProtocollo) {
	this.numeroProtocollo = numeroProtocollo;
}
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
public Date getDataProtocollo() {
	return dataProtocollo;
}
public void setDataProtocollo(Date dataProtocollo) {
	this.dataProtocollo = dataProtocollo;
}
/**
 * l&#39;identificativo dell&#39;versioneModulo (Long)
 **/
// nome originario nello yaml: idVersioneModulo 
//public Long getIdVersioneModulo() {
//  return idVersioneModulo;
//}
//public void setIdVersioneModulo(Long idVersioneModulo) {
//  this.idVersioneModulo = idVersioneModulo;
//}

/**
 * l&#39;identificativo dell&#39;ente (Long)
 **/
// nome originario nello yaml: idEnte 
public Long getIdEnte() {
  return idEnte;
}
public void setIdEnte(Long idEnte) {
  this.idEnte = idEnte;
}

/**
 * l&#39;identificativo dell&#39;area (Long)
 **/
// nome originario nello yaml: idArea 
public Long getIdArea() {
  return idArea;
}
public void setIdArea(Long idArea) {
  this.idArea = idArea;
}

/**
 * Cognome SPID-Shib  del Dichiarante, necessario nel BO per inserimento Istanza per inserimento da terzi
 **/
	public String getCognomeDichiarante() {
	return cognomeDichiarante;
	}
	public void setCognomeDichiarante(String cognomeDichiarante) {
		this.cognomeDichiarante = cognomeDichiarante;
	}
	/**
	  * Nome SPID-Shib del Dichiarante, necessario nel BO per inserimento Istanza per inserimento da terzi
	  **/
	public String getNomeDichiarante() {
		return nomeDichiarante;
	}
	public void setNomeDichiarante(String nomeDichiarante) {
		this.nomeDichiarante = nomeDichiarante;
	}
	public String getOperatore() {
		return operatore;
	}
	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}
	public Date getDataStato() {
		return dataStato;
	}
	public void setDataStato(Date dataStato) {
		this.dataStato = dataStato;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "CET")
	public Date getDataEsitoPagamento() {
		return dataEsitoPagamento;
	}
	public void setDataEsitoPagamento(Date dataEsitoPagamento) {
		this.dataEsitoPagamento = dataEsitoPagamento;
	}
	public List<Pagamento> getPagamenti() {
		return pagamenti;
	}
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}
	public Boolean getIsPagato() {
		return isPagato;
	}
	public void setIsPagato(Boolean isPagato) {
		this.isPagato = isPagato;
	}

@Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Istanza istanza = (Istanza) o;
    return Objects.equals(idIstanza, istanza.idIstanza) &&
        Objects.equals(codiceIstanza, istanza.codiceIstanza) &&
        Objects.equals(identificativoUtente, istanza.identificativoUtente) &&
        Objects.equals(codiceFiscaleDichiarante, istanza.codiceFiscaleDichiarante) &&
        Objects.equals(cognomeDichiarante, istanza.cognomeDichiarante) &&
        Objects.equals(nomeDichiarante, istanza.nomeDichiarante) &&
        Objects.equals(stato, istanza.stato) &&
        Objects.equals(statoBo, istanza.statoBo) &&
        Objects.equals(created, istanza.created) &&
        Objects.equals(dataStato, istanza.dataStato) &&
        Objects.equals(modified, istanza.modified) &&
        Objects.equals(data, istanza.data) &&
        Objects.equals(modulo, istanza.modulo) &&
        Objects.equals(attoreIns, istanza.attoreIns) &&
        Objects.equals(attoreUpd, istanza.attoreUpd) &&
//        Objects.equals(importanza, istanza.importanza) &&
        Objects.equals(flagEliminata, istanza.flagEliminata) &&
        Objects.equals(flagArchiviata, istanza.flagArchiviata) &&
        Objects.equals(flagTest, istanza.flagTest) &&
        Objects.equals(currentStep, istanza.currentStep) &&
        Objects.equals(numeroProtocollo, istanza.numeroProtocollo) &&
        Objects.equals(dataProtocollo, istanza.dataProtocollo) &&
    	Objects.equals(dataEsitoPagamento, istanza.dataEsitoPagamento) &&
    	Objects.equals(pagamenti, istanza.pagamenti) &&
        Objects.equals(metadata, istanza.metadata) &&
        Objects.equals(idEnte, istanza.idEnte) &&
        Objects.equals(idArea, istanza.idArea);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idIstanza, codiceIstanza, identificativoUtente, codiceFiscaleDichiarante, cognomeDichiarante, nomeDichiarante, stato, statoBo, created, dataStato, modified, data, modulo, attoreIns, attoreUpd, importanza, flagEliminata, flagArchiviata, flagTest, currentStep, numeroProtocollo, dataProtocollo, metadata, /*dataInvio,*/ /*idVersioneModulo,*/ idEnte, idArea);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Istanza {\n");
    
    sb.append("    idIstanza: ").append(toIndentedString(idIstanza)).append("\n");
    sb.append("    codiceIstanza: ").append(toIndentedString(codiceIstanza)).append("\n");
    sb.append("    identificativoUtente: ").append(toIndentedString(identificativoUtente)).append("\n");
    sb.append("    codiceFiscaleDichiarante: ").append(toIndentedString(codiceFiscaleDichiarante)).append("\n");
    sb.append("    cognomeDichiarante: ").append(toIndentedString(cognomeDichiarante)).append("\n");
    sb.append("    nomeDichiarante: ").append(toIndentedString(nomeDichiarante)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    statoBo: ").append(toIndentedString(statoBo)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    dataStato: ").append(toIndentedString(dataStato)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    modulo: ").append(toIndentedString(modulo)).append("\n");
    sb.append("    attoreIns: ").append(toIndentedString(attoreIns)).append("\n");
    sb.append("    attoreUpd: ").append(toIndentedString(attoreUpd)).append("\n");
    sb.append("    flagEliminata: ").append(toIndentedString(flagEliminata)).append("\n");
    sb.append("    flagArchiviata: ").append(toIndentedString(flagArchiviata)).append("\n");
    sb.append("    flagTest: ").append(toIndentedString(flagTest)).append("\n");
    sb.append("    importanza: ").append(toIndentedString(importanza)).append("\n");
    sb.append("    currentStep: ").append(toIndentedString(currentStep)).append("\n");
    sb.append("    numeroProtocollo: ").append(toIndentedString(numeroProtocollo)).append("\n");
    sb.append("    dataProtocollo: ").append(toIndentedString(dataProtocollo)).append("\n");
    sb.append("    dataEsitoPagamento: ").append(toIndentedString(dataEsitoPagamento)).append("\n");
    sb.append("    pagamenti: ").append(toIndentedString(pagamenti)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
//    sb.append("    dataInvio: ").append(toIndentedString(dataInvio)).append("\n");
//  sb.append("    idVersioneModulo: ").append(toIndentedString(idVersioneModulo)).append("\n");
    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
    sb.append("    idArea: ").append(toIndentedString(idArea)).append("\n");
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

