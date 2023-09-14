/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Istanza (moon_fo_t_istanza)
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzaMoonsrv   {
	  
	  private Long idIstanza = null;
	  private String codiceIstanza = null;
	  private String identificativoUtente = null;
	  private String codiceFiscaleDichiarante = null;
	  private String cognomeDichiarante = null;
	  private String nomeDichiarante = null;
	  private Stato stato = null;
	  private Stato statoBo = null;
	  private Date created = null; // DataInserimento, in seguito DataSubmit = dataInvio
	  private Date modified = null;
	  private Object data = null;
	  private ModuloMoonsrv modulo = null;
	  private String attoreIns = null;
	  private String attoreUpd = null;
	  private boolean flagEliminata = false;
	  private boolean flagArchiviata = false;
	  private boolean flagTest = false;
	  private Integer importanza = 0;
	  private Integer currentStep = 0;
	  private String numeroProtocollo = null;
	  private Date dataProtocollo = null;
	  private Object metadata = null;
	  private Long idEnte = null;
	  private Long idArea = null;
	  private String datiAggiuntivi;
	  private String iuv = null;
	  private String codiceAvviso = null;
	  private Date dataEsitoPagamento = null;
	  private List<Pagamento> pagamenti = null;
		
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
	  
	  public String getIdentificativoUtente() {
		return identificativoUtente;
	  }

	  public void setIdentificativoUtente(String identificativoUtente) {
		this.identificativoUtente = identificativoUtente;
	  }

		/**
	   * Codice Fiscale del Dichiarante, necessario nel BO per inserimento Istanza per inserimento da terzi
	   **/
	  // nome originario nello yaml: stato 
	  public String getCodiceFiscaleDichiarante() {
		return codiceFiscaleDichiarante;
	  }
	  public void setCodiceFiscaleDichiarante(String codiceFiscaleDichiarante) {
		this.codiceFiscaleDichiarante = codiceFiscaleDichiarante;
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
	  public ModuloMoonsrv getModulo() {
	    return modulo;
	  }
	  public void setModulo(ModuloMoonsrv modulo) {
	    this.modulo = modulo;
	  }

	  /**
	   **/
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
	  // nome originario nello yaml: numero_protocollo 
	  public String getNumeroProtocollo() {
	    return numeroProtocollo;
	  }
	  public void setNumeroProtocollo(String numeroProtocollo) {
	    this.numeroProtocollo = numeroProtocollo;
	  }
	  
	  /**
	   * i dati del modulo compilati dall&#39;utente
	   **/
	  // nome originario nello yaml: data_protocollo
	  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	  public Date getDataProtocollo() {
	    return dataProtocollo;
	  }
	  public void setDataProtocollo(Date dataProtocollo) {
	    this.dataProtocollo = dataProtocollo;
	  }
	  
	  /**
	   **/
	  // nome originario nello yaml: current_step 
	  public Integer getCurrentStep() {
	    return currentStep;
	  }
	  public void setCurrentStep(Integer currentStep) {
	    this.currentStep = currentStep;
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
	   * Dati aggiunti (es.: ProviderLogin, DataOraLogin)
	   **/
	  // nome originario nello yaml: datiAggiuntivi 
		public String getDatiAggiuntivi() {
			return datiAggiuntivi;
		}
		public void setDatiAggiuntivi(String datiAggiuntivi) {
			this.datiAggiuntivi = datiAggiuntivi;
		}
		
		public String getIuv() {
			return iuv;
		}
		public void setIuv(String iuv) {
			this.iuv = iuv;
		}
		public String getCodiceAvviso() {
			return codiceAvviso;
		}
		public void setCodiceAvviso(String codiceAvviso) {
			this.codiceAvviso = codiceAvviso;
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

		
	  @Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    IstanzaMoonsrv istanza = (IstanzaMoonsrv) o;
	    return Objects.equals(idIstanza, istanza.idIstanza) &&
	        Objects.equals(codiceIstanza, istanza.codiceIstanza) &&
	        Objects.equals(identificativoUtente, istanza.identificativoUtente) &&
	        Objects.equals(codiceFiscaleDichiarante, istanza.codiceFiscaleDichiarante) &&
	        Objects.equals(cognomeDichiarante, istanza.cognomeDichiarante) &&
	        Objects.equals(nomeDichiarante, istanza.nomeDichiarante) &&
	        Objects.equals(stato, istanza.stato) &&
	        Objects.equals(statoBo, istanza.statoBo) &&
	        Objects.equals(created, istanza.created) &&
	        Objects.equals(modified, istanza.modified) &&
	        Objects.equals(data, istanza.data) &&
	        Objects.equals(modulo, istanza.modulo) &&
	        Objects.equals(attoreIns, istanza.attoreIns) &&
	        Objects.equals(attoreUpd, istanza.attoreUpd) &&
	        Objects.equals(importanza, istanza.importanza) &&
	        Objects.equals(flagEliminata, istanza.flagEliminata) &&
	        Objects.equals(flagArchiviata, istanza.flagArchiviata) &&
	        Objects.equals(flagTest, istanza.flagTest) &&
	        Objects.equals(currentStep, istanza.currentStep) &&
	        Objects.equals(numeroProtocollo, istanza.numeroProtocollo) &&
	        Objects.equals(dataProtocollo, istanza.dataProtocollo) &&
	        Objects.equals(metadata, istanza.metadata) &&
	        Objects.equals(idEnte, istanza.idEnte) &&
	    	Objects.equals(idArea, istanza.idArea) &&
	    	Objects.equals(datiAggiuntivi, istanza.datiAggiuntivi) &&
	    	Objects.equals(iuv, istanza.iuv) &&
	    	Objects.equals(codiceAvviso, istanza.codiceAvviso) &&
	    	Objects.equals(dataEsitoPagamento, istanza.dataEsitoPagamento);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(idIstanza, codiceIstanza, identificativoUtente, codiceFiscaleDichiarante, cognomeDichiarante, nomeDichiarante, stato, statoBo, created, modified, data, modulo, attoreIns, attoreUpd, importanza, flagEliminata, flagArchiviata, flagTest, currentStep, numeroProtocollo, dataProtocollo, metadata, idEnte, idArea, datiAggiuntivi);
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
	    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
	    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
	    sb.append("    idArea: ").append(toIndentedString(idArea)).append("\n");
	    sb.append("    datiAggiuntivi: ").append(toIndentedString(datiAggiuntivi)).append("\n");
	    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
	    sb.append("    codiceAvviso: ").append(toIndentedString(codiceAvviso)).append("\n");
	    sb.append("    dataEsitoPagamento: ").append(toIndentedString(dataEsitoPagamento)).append("\n");
	    sb.append("    pagamenti: ").append(toIndentedString(pagamenti)).append("\n");
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

