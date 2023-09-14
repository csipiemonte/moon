/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Arrays;
import java.util.Date;

/**
 * Entity della tabella dei pdf di un instanze 
 * <br>Un solo record presente per ogni istanza inviata in seguito alla sua generazione
 * <br>
 * <br>Tabella moon_fo_t_pdf_istanza
 * <br>PK: idPdfIstanza
 * <br>AK: idIstanza
 * 
 * @see IstanzaEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzaPdfEntity {

	private Long idPdfIstanza;
	private Long idIstanza;
	private Long idModulo;
	private String hashPdf;
	private byte[] contenutoPdf;
	private String resoconto;
	private Date dataIns;
	private String attoreIns;
	private Date dataUpd;
	private String attoreUpd;
	private String uuidIndex;
	
	public Long getIdPdfIstanza() {
		return idPdfIstanza;
	}
	public void setIdPdfIstanza(Long idPdfIstanza) {
		this.idPdfIstanza = idPdfIstanza;
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
	public String getHashPdf() {
		return hashPdf;
	}
	public void setHashPdf(String hashPdf) {
		this.hashPdf = hashPdf;
	}
	public byte[] getContenutoPdf() {
		return contenutoPdf;
	}
	public void setContenutoPdf(byte[] contenutoPdf) {
		this.contenutoPdf = contenutoPdf;
	}
	public String getResoconto() {
		return resoconto;
	}
	public void setResoconto(String resoconto) {
		this.resoconto = resoconto;
	}
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	public String getAttoreIns() {
		return attoreIns;
	}
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
	}
	public Date getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Date dataUpd) {
		this.dataUpd = dataUpd;
	}
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}
	public String getUuidIndex() {
		return uuidIndex;
	}
	public void setUuidIndex(String uuidIndex) {
		this.uuidIndex = uuidIndex;
	}
	
	@Override
	public String toString() {
		return "IstanzaPdfEntity [idPdfIstanza=" + idPdfIstanza + ", idIstanza=" + idIstanza + ", idModulo=" + idModulo
				+ ", hashPdf=" + hashPdf 
				+ ", contenuto=" + ((contenutoPdf==null)?"null":(Arrays.toString(contenutoPdf).substring(0, 10)+"... len="+contenutoPdf.length))
				+ ", resoconto=" + resoconto + ", dataIns=" + dataIns + ", attoreIns=" + attoreIns + ", dataUpd=" + dataUpd
				+ ", attoreUpd=" + attoreUpd 
				+ ", uuidIndex=" + uuidIndex + "]";
	}
	
	public String toStringFULL() {
		return "IstanzaPdfEntity [idPdfIstanza=" + idPdfIstanza + ", idIstanza=" + idIstanza + ", idModulo=" + idModulo
				+ ", hashPdf=" + hashPdf + ", contenutoPdf=" + Arrays.toString(contenutoPdf) + ", resoconto="
				+ resoconto + ", dataIns=" + dataIns + ", attoreIns=" + attoreIns + ", dataUpd=" + dataUpd
				+ ", attoreUpd=" + attoreUpd
				+ ", uuidIndex=" + uuidIndex + "]";
	}
}
