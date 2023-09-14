/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Pagamento inteso come richiesta di pagamento (moon_ep_t_richiesta)
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class Pagamento {

	private String idEpay;
	private Long idIstanza;
	private Long idModulo;
	private Integer idTipologiaEpay;
	private Long idStoricoWorkflow;
	private String richiesta;
	private String iuv;
	private String codiceAvviso;
	private BigDecimal importo;
	private Date dataInserimento;
	private Date dataAnnullamento;
	private PagamentoNotifica notifica;

	public Pagamento() {	
	}

	public String getIdEpay() {
		return idEpay;
	}
	public void setIdEpay(String idEpay) {
		this.idEpay = idEpay;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Integer getIdTipologiaEpay() {
		return idTipologiaEpay;
	}
	public void setIdTipologiaEpay(Integer idTipologiaEpay) {
		this.idTipologiaEpay = idTipologiaEpay;
	}
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	public String getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(String richiesta) {
		this.richiesta = richiesta;
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
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "CET")
	public Date getDataAnnullamento() {
		return dataAnnullamento;
	}
	public void setDataAnnullamento(Date dataAnnullamento) {
		this.dataAnnullamento = dataAnnullamento;
	}
	public PagamentoNotifica getNotifica() {
		return notifica;
	}
	public void setNotifica(PagamentoNotifica notifica) {
		this.notifica = notifica;
	}


	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    Pagamento p = (Pagamento) o;
	    return Objects.equals(idEpay, p.idEpay) &&
	        Objects.equals(idIstanza, p.idIstanza) &&
	        Objects.equals(idModulo, p.idModulo) &&
	        Objects.equals(idTipologiaEpay, p.idTipologiaEpay) &&
	        Objects.equals(idStoricoWorkflow, p.idStoricoWorkflow) &&
	        Objects.equals(iuv, p.iuv) &&
	        Objects.equals(codiceAvviso, p.codiceAvviso) &&
	        Objects.equals(importo, p.importo) &&
	        Objects.equals(dataInserimento, p.dataInserimento) &&
	        Objects.equals(dataAnnullamento, p.dataAnnullamento) &&
	        Objects.equals(notifica, p.notifica);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idEpay, idIstanza, idModulo, idTipologiaEpay, idStoricoWorkflow, iuv, codiceAvviso, importo, dataAnnullamento, notifica);
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class Pagamento {\n");	    
	    sb.append("    idEpay: ").append(toIndentedString(idEpay)).append("\n");
	    sb.append("    idIstanza: ").append(toIndentedString(idIstanza)).append("\n");
	    sb.append("    idModulo: ").append(toIndentedString(idModulo)).append("\n");
	    sb.append("    idTipologiaEpay: ").append(toIndentedString(idTipologiaEpay)).append("\n");
	    sb.append("    idStoricoWorkflow: ").append(toIndentedString(idStoricoWorkflow)).append("\n");
	    sb.append("    richiesta: ").append(toIndentedString(richiesta)).append("\n");
	    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
	    sb.append("    codiceAvviso: ").append(toIndentedString(codiceAvviso)).append("\n");
	    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
	    sb.append("    dataInserimento: ").append(toIndentedString(dataInserimento)).append("\n");
	    sb.append("    dataAnnullamento: ").append(toIndentedString(dataAnnullamento)).append("\n");
	    sb.append("    notifica: ").append(toIndentedString(notifica)).append("\n");
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
