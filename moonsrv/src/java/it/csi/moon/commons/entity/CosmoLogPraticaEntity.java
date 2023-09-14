/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella dei log delle pratiche che devono essere create su COSMO
 * <br>
 * <br>Tabella moon_cs_t_pratica
 * <br>PK: idLogPratica
 * <br>AK: idIstanza,idx
 * 
 * @author Laurent
 */
public class CosmoLogPraticaEntity {

	private Long idLogPratica;
	private String idPratica;
	private Long idIstanza;
	private Integer idx;
	private Long idModulo;
	private Date dataIns;
	private Date dataAvvio;
	private Date dataUpd;
	private String creaRichiesta;
	private String creaRisposta;
	private String creaDocumentoRichiesta;
	private String creaDocumentoRisposta;
	private String avviaProcessoRichiesta;
	private String avviaProcessoRisposta;
	private String errore;
	private String pratica;

	public Long getIdLogPratica() {
		return idLogPratica;
	}
	public void setIdLogPratica(Long idLogPratica) {
		this.idLogPratica = idLogPratica;
	}
	public String getIdPratica() {
		return idPratica;
	}
	public void setIdPratica(String idPratica) {
		this.idPratica = idPratica;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	public Date getDataAvvio() {
		return dataAvvio;
	}
	public void setDataAvvio(Date dataAvvio) {
		this.dataAvvio = dataAvvio;
	}
	public Date getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Date dataUpd) {
		this.dataUpd = dataUpd;
	}
	public String getCreaRichiesta() {
		return creaRichiesta;
	}
	public void setCreaRichiesta(String creaRichiesta) {
		this.creaRichiesta = creaRichiesta;
	}
	public String getCreaRisposta() {
		return creaRisposta;
	}
	public void setCreaRisposta(String creaRisposta) {
		this.creaRisposta = creaRisposta;
	}
	public String getCreaDocumentoRichiesta() {
		return creaDocumentoRichiesta;
	}
	public void setCreaDocumentoRichiesta(String creaDocumentoRichiesta) {
		this.creaDocumentoRichiesta = creaDocumentoRichiesta;
	}
	public String getCreaDocumentoRisposta() {
		return creaDocumentoRisposta;
	}
	public void setCreaDocumentoRisposta(String creaDocumentoRisposta) {
		this.creaDocumentoRisposta = creaDocumentoRisposta;
	}
	public String getAvviaProcessoRichiesta() {
		return avviaProcessoRichiesta;
	}
	public void setAvviaProcessoRichiesta(String avviaProcessoRichiesta) {
		this.avviaProcessoRichiesta = avviaProcessoRichiesta;
	}
	public String getAvviaProcessoRisposta() {
		return avviaProcessoRisposta;
	}
	public void setAvviaProcessoRisposta(String avviaProcessoRisposta) {
		this.avviaProcessoRisposta = avviaProcessoRisposta;
	}
	public String getErrore() {
		return errore;
	}
	public void setErrore(String errore) {
		this.errore = errore;
	}
	public String getPratica() {
		return pratica;
	}
	public void setPratica(String pratica) {
		this.pratica = pratica;
	}
	
}
