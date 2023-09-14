/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Pagamento inteso come richiesta di pagamento (moon_ep_t_notifica_pagamento)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class PagamentoNotifica {

	private String idPosizioneDebitoria;
	private BigDecimal importoPagato;
	private Date dataEsitoPagamento;
	private BigDecimal importoTransato;
	private BigDecimal importoCommissioni;
	private String idPsp; // "CIPBITMM"
	private String ragioneSocialePsp; // "Nexi"
	private Date dataOraAvvioTransazione;
	
	public PagamentoNotifica() {	
	}

	public String getIdPosizioneDebitoria() {
		return idPosizioneDebitoria;
	}
	public void setIdPosizioneDebitoria(String idPosizioneDebitoria) {
		this.idPosizioneDebitoria = idPosizioneDebitoria;
	}
	public BigDecimal getImportoPagato() {
		return importoPagato;
	}
	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "CET")
	public Date getDataEsitoPagamento() {
		return dataEsitoPagamento;
	}
	public void setDataEsitoPagamento(Date dataEsitoPagamento) {
		this.dataEsitoPagamento = dataEsitoPagamento;
	}
	public BigDecimal getImportoTransato() {
		return importoTransato;
	}
	public void setImportoTransato(BigDecimal importoTransato) {
		this.importoTransato = importoTransato;
	}
	public BigDecimal getImportoCommissioni() {
		return importoCommissioni;
	}
	public void setImportoCommissioni(BigDecimal importoCommissioni) {
		this.importoCommissioni = importoCommissioni;
	}
	public String getIdPsp() {
		return idPsp;
	}
	public void setIdPsp(String idPsp) {
		this.idPsp = idPsp;
	}
	public String getRagioneSocialePsp() {
		return ragioneSocialePsp;
	}
	public void setRagioneSocialePsp(String ragioneSocialePsp) {
		this.ragioneSocialePsp = ragioneSocialePsp;
	}
	public Date getDataOraAvvioTransazione() {
		return dataOraAvvioTransazione;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public void setDataOraAvvioTransazione(Date dataOraAvvioTransazione) {
		this.dataOraAvvioTransazione = dataOraAvvioTransazione;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPosizioneDebitoria == null) ? 0 : idPosizioneDebitoria.hashCode());
		result = prime * result + ((importoPagato == null) ? 0 : importoPagato.hashCode());
		result = prime * result + ((dataOraAvvioTransazione == null) ? 0 : dataOraAvvioTransazione.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagamentoNotifica other = (PagamentoNotifica) obj;
		if (idPosizioneDebitoria == null) {
			if (other.idPosizioneDebitoria != null)
				return false;
		} else if (!idPosizioneDebitoria.equals(other.idPosizioneDebitoria))
			return false;
		if (importoPagato == null) {
			if (other.importoPagato != null)
				return false;
		} else if (!importoPagato.equals(other.importoPagato))
			return false;
		if (dataOraAvvioTransazione == null) {
			if (other.dataOraAvvioTransazione != null)
				return false;
		} else if (!dataOraAvvioTransazione.equals(other.dataOraAvvioTransazione))
			return false;
		return true;
	}

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    sb.append("class PagamentoNotifica {\n");
	    sb.append("    idPosizioneDebitoria: ").append(toIndentedString(idPosizioneDebitoria)).append("\n");
	    sb.append("    importoPagato: ").append(toIndentedString(importoPagato)).append("\n");
	    sb.append("    dataEsitoPagamento: ").append(toIndentedString(dataEsitoPagamento)).append("\n");
	    sb.append("    importoTransato: ").append(toIndentedString(importoTransato)).append("\n");
	    sb.append("    importoCommissioni: ").append(toIndentedString(importoCommissioni)).append("\n");
	    sb.append("    idPsp: ").append(toIndentedString(idPsp)).append("\n");
	    sb.append("    ragioneSocialePsp: ").append(toIndentedString(ragioneSocialePsp)).append("\n");
	    sb.append("    dataOraAvvioTransazione: ").append(dataOraAvvioTransazione==null?null:dataOraAvvioTransazione).append("\n");
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
