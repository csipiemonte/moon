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
 * Modulo (moon_io_d_modulo)
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Modulo   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 

	  private Long idVersioneModulo = null; // FROM ModuloVersione
	  private Long idModulo = null;
	  private String codiceModulo = null;
	  private String versioneModulo = null; // FROM ModuloVersione
	  private String oggettoModulo = null;
	  private String descrizioneModulo = null;
	  private Date dataIns = null;
	  private Date dataUpd = null;
	  private Boolean flagIsRiservato = null;
	  private Integer idTipoCodiceIstanza = null;
	  private Boolean flagProtocolloIntegrato = null;
	  private String attoreUpd = null;
	  private Long idModuloStruttura = null;
	  private String tipoStruttura = null;
	  private String struttura = null;
	  private StatoModulo stato = null;
	  private Categoria categoria = null;
	  private List<ModuloVersioneStato> versioni = null;
	  
	  private List<ModuloAttributo> attributi = null;
	  private String objAttributi = null;

	  private List<Portale> portali = null;
	  private Processo processo = null;

	  /**
	   * l&#39;identificativo del modulo Versione (Long)
	   **/
	  // nome originario nello yaml: idVersioneModulo
	  public Long getIdVersioneModulo() {
	    return idVersioneModulo;
	  }
	  public void setIdVersioneModulo(Long idVersioneModulo) {
	    this.idVersioneModulo = idVersioneModulo;
	  }
	  
	  /**
	   * l&#39;identificativo del modulo (Long)
	   **/
	  // nome originario nello yaml: idModulo 
	  public Long getIdModulo() {
	    return idModulo;
	  }
	  public void setIdModulo(Long idModulo) {
	    this.idModulo = idModulo;
	  }

	  /**
	   * il codice del modulo
	   **/
	  // nome originario nello yaml: codiceModulo 
	  public String getCodiceModulo() {
	    return codiceModulo;
	  }
	  public void setCodiceModulo(String codiceModulo) {
	    this.codiceModulo = codiceModulo;
	  }

	  /**
	   * la versione del modulo
	   **/
	  // nome originario nello yaml: versioneModulo 
	  public String getVersioneModulo() {
	    return versioneModulo;
	  }
	  public void setVersioneModulo(String versioneModulo) {
	    this.versioneModulo = versioneModulo;
	  }

	  /**
	   * l&#39;oggetto del modulo
	   **/
	  // nome originario nello yaml: oggettoModulo 
	  public String getOggettoModulo() {
	    return oggettoModulo;
	  }
	  public void setOggettoModulo(String oggettoModulo) {
	    this.oggettoModulo = oggettoModulo;
	  }

	  /**
	   * la descrizione del modulo
	   **/
	  // nome originario nello yaml: descrizioneModulo 
	  public String getDescrizioneModulo() {
	    return descrizioneModulo;
	  }
	  public void setDescrizioneModulo(String descrizioneModulo) {
	    this.descrizioneModulo = descrizioneModulo;
	  }

	  /**
	   * la data/oraio di inserimento/creazione del modulo
	   **/
	  // nome originario nello yaml: dataIns 
	  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	  //@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone = "CET")
	  public Date getDataIns() {
	    return dataIns;
	  }
	  public void setDataIns(Date dataIns) {
	    this.dataIns = dataIns;
	  }

	  /**
	   * la data/orario di ultimo aggiornamento del modulo
	   **/
	  // nome originario nello yaml: dataUpd 
	  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
//	  @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone = "CET")
	  public Date getDataUpd() {
	    return dataUpd;
	  }
	  public void setDataUpd(Date dataUpd) {
	    this.dataUpd = dataUpd;
	  }

	  /**
	   * true se il modulo e&#39; riservato
	   **/
	  // nome originario nello yaml: flagIsRiservato 
	  public Boolean isFlagIsRiservato() {
	    return flagIsRiservato;
	  }
	  public void setFlagIsRiservato(Boolean flagIsRiservato) {
	    this.flagIsRiservato = flagIsRiservato;
	  }

	  /**
	   * id del TipoCodiceIstanza
	   **/
	  // nome originario nello yaml: idTipoCodiceIstanza 
	  public Integer getIdTipoCodiceIstanza() {
	    return idTipoCodiceIstanza;
	  }
	  public void setIdTipoCodiceIstanza(Integer idTipoCodiceIstanza) {
	    this.idTipoCodiceIstanza = idTipoCodiceIstanza;
	  }

	  /**
	   * true se deve essere protocollato
	   **/
	  // nome originario nello yaml: flagProtocolloIntegrato 
	  public Boolean isFlagProtocolloIntegrato() {
	    return flagProtocolloIntegrato;
	  }
	  public void setFlagProtocolloIntegrato(Boolean flagProtocolloIntegrato) {
	    this.flagProtocolloIntegrato = flagProtocolloIntegrato;
	  }

	  /**
	   * l&#39;attore di inserimento/creazione o ultimo aggiornamento del modulo
	   **/
	  // nome originario nello yaml: attoreUpd 
	  public String getAttoreUpd() {
	    return attoreUpd;
	  }
	  public void setAttoreUpd(String attoreUpd) {
	    this.attoreUpd = attoreUpd;
	  }

	  /**
	   * l&#39;identificativo del modulo struttura (Long)
	   **/
	  // nome originario nello yaml: idModuloStruttura 
	  public Long getIdModuloStruttura() {
	    return idModuloStruttura;
	  }
	  public void setIdModuloStruttura(Long idModuloStruttura) {
	    this.idModuloStruttura = idModuloStruttura;
	  }

	  /**
	   * il tipo della struttura WIZ-Wizard FRM-Form
	   **/
	  // nome originario nello yaml: tipoStruttura 
	  public String getTipoStruttura() {
	    return tipoStruttura;
	  }
	  public void setTipoStruttura(String tipoStruttura) {
	    this.tipoStruttura = tipoStruttura;
	  }

	  /**
	   * la struttura del form in formato JSON formIO
	   **/
	  // nome originario nello yaml: struttura 
	  public String getStruttura() {
	    return struttura;
	  }
	  public void setStruttura(String struttura) {
	    this.struttura = struttura;
	  }

	  // nome originario nello yaml: stato 
	  public StatoModulo getStato() {
	    return stato;
	  }
	  public void setStato(StatoModulo stato) {
	    this.stato = stato;
	  }

	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	public List<ModuloAttributo> getAttributi() {
		return attributi;
	}
	public void setAttributi(List<ModuloAttributo> attributi) {
		this.attributi = attributi;
	}
	public String getObjAttributi() {
		return objAttributi;
	}
	public void setObjAttributi(String objAttributi) {
		this.objAttributi = objAttributi;
	}
	
	public List<Portale> getPortali() {
		return portali;
	}
	public void setPortali(List<Portale> portali) {
		this.portali = portali;
	}
	public Processo getProcesso() {
		return processo;
	}
	public void setProcesso(Processo processo) {
		this.processo = processo;
	}
	public List<ModuloVersioneStato> getVersioni() {
		return versioni;
	}
	public void setVersioni(List<ModuloVersioneStato> versioni) {
		this.versioni = versioni;
	}
	
	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Modulo modulo = (Modulo) o;
	    return Objects.equals(idVersioneModulo, modulo.idVersioneModulo) &&
		    Objects.equals(idModulo, modulo.idModulo) &&
	        Objects.equals(codiceModulo, modulo.codiceModulo) &&
	        Objects.equals(versioneModulo, modulo.versioneModulo) &&
	        Objects.equals(oggettoModulo, modulo.oggettoModulo) &&
	        Objects.equals(descrizioneModulo, modulo.descrizioneModulo) &&
	        Objects.equals(dataIns, modulo.dataIns) &&
	        Objects.equals(dataUpd, modulo.dataUpd) &&
	        Objects.equals(flagIsRiservato, modulo.flagIsRiservato) &&
	        Objects.equals(idTipoCodiceIstanza, modulo.idTipoCodiceIstanza) &&
	        Objects.equals(flagProtocolloIntegrato, modulo.flagProtocolloIntegrato) &&
	        Objects.equals(attoreUpd, modulo.attoreUpd) &&
	        Objects.equals(idModuloStruttura, modulo.idModuloStruttura) &&
	        Objects.equals(tipoStruttura, modulo.tipoStruttura) &&
	        Objects.equals(struttura, modulo.struttura) &&
	        Objects.equals(stato, modulo.stato) &&
	        Objects.equals(attributi, modulo.attributi) &&
	        Objects.equals(objAttributi, modulo.objAttributi) &&
	        Objects.equals(portali, modulo.portali) &&
	        Objects.equals(processo, modulo.processo);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(idVersioneModulo, idModulo, codiceModulo, versioneModulo, oggettoModulo, descrizioneModulo, dataIns, dataUpd, flagIsRiservato, idTipoCodiceIstanza, flagProtocolloIntegrato, attoreUpd, idModuloStruttura, tipoStruttura, struttura, stato, attributi, objAttributi, portali, processo);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Modulo {\n");
	    
	    sb.append("    idModulo: ").append(toIndentedString(idModulo)).append("\n");
	    sb.append("    codiceModulo: ").append(toIndentedString(codiceModulo)).append("\n");
	    sb.append("    idVersioneModulo: ").append(toIndentedString(idVersioneModulo)).append("\n");
	    sb.append("    versioneModulo: ").append(toIndentedString(versioneModulo)).append("\n");
	    sb.append("    oggettoModulo: ").append(toIndentedString(oggettoModulo)).append("\n");
	    sb.append("    descrizioneModulo: ").append(toIndentedString(descrizioneModulo)).append("\n");
	    sb.append("    dataIns: ").append(toIndentedString(dataIns)).append("\n");
	    sb.append("    dataUpd: ").append(toIndentedString(dataUpd)).append("\n");
	    sb.append("    flagIsRiservato: ").append(toIndentedString(flagIsRiservato)).append("\n");
	    sb.append("    idTipoCodiceIstanza: ").append(toIndentedString(idTipoCodiceIstanza)).append("\n");
	    sb.append("    flagProtocolloIntegrato: ").append(toIndentedString(flagProtocolloIntegrato)).append("\n");
	    sb.append("    attoreUpd: ").append(toIndentedString(attoreUpd)).append("\n");
	    sb.append("    idModuloStruttura: ").append(toIndentedString(idModuloStruttura)).append("\n");
	    sb.append("    tipoStruttura: ").append(toIndentedString(tipoStruttura)).append("\n");
	    sb.append("    struttura (length): ").append(struttura!=null?struttura.length():"null").append("\n");
	    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
	    sb.append("    attributi: ").append(toIndentedString(attributi)).append("\n");
	    sb.append("    objAttributi: ").append(toIndentedString(objAttributi)).append("\n");
	    sb.append("    portali: ").append(toIndentedString(portali)).append("\n");
	    sb.append("    processo: ").append(toIndentedString(processo)).append("\n");
	    sb.append("}");
	    return sb.toString();
	  }

	  /**
	   * Stampa con la struttura
	   * @return
	   */
	  public String toStringFULL() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Modulo {\n");
	    
	    sb.append("    idModulo: ").append(toIndentedString(idModulo)).append("\n");
	    sb.append("    codiceModulo: ").append(toIndentedString(codiceModulo)).append("\n");
	    sb.append("    idVersioneModulo: ").append(toIndentedString(idVersioneModulo)).append("\n");
	    sb.append("    versioneModulo: ").append(toIndentedString(versioneModulo)).append("\n");
	    sb.append("    oggettoModulo: ").append(toIndentedString(oggettoModulo)).append("\n");
	    sb.append("    descrizioneModulo: ").append(toIndentedString(descrizioneModulo)).append("\n");
	    sb.append("    dataIns: ").append(toIndentedString(dataIns)).append("\n");
	    sb.append("    dataUpd: ").append(toIndentedString(dataUpd)).append("\n");
	    sb.append("    flagIsRiservato: ").append(toIndentedString(flagIsRiservato)).append("\n");
	    sb.append("    idTipoCodiceIstanza: ").append(toIndentedString(idTipoCodiceIstanza)).append("\n");
	    sb.append("    flagProtocolloIntegrato: ").append(toIndentedString(flagProtocolloIntegrato)).append("\n");
	    sb.append("    attoreUpd: ").append(toIndentedString(attoreUpd)).append("\n");
	    sb.append("    idModuloStruttura: ").append(toIndentedString(idModuloStruttura)).append("\n");
	    sb.append("    tipoStruttura: ").append(toIndentedString(tipoStruttura)).append("\n");
	    sb.append("    struttura: ").append(toIndentedString(struttura)).append("\n");
	    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
	    sb.append("    attributi: ").append(toIndentedString(attributi)).append("\n");
	    sb.append("    objAttributi: ").append(toIndentedString(objAttributi)).append("\n");
	    sb.append("    portali: ").append(toIndentedString(portali)).append("\n");
	    sb.append("    processo: ").append(toIndentedString(processo)).append("\n");
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

