/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella FAQ
 * <br>
 * <br>Tabella:  <code>moon_fo_t_faq</code>
 * <br>PK:  <code>id_faq</code>
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class FaqEntity {
	
	private Long idFaq;
	private String titolo;
	private String contenuto;
	private Date dataIns;
	private String attoreIns;
	private Date dataUpd;
	private String attoreUpd;
	
	public FaqEntity() {
		super();
	}

	public FaqEntity(Long idFaq, String titolo, String contenuto, Date dataIns, String attoreIns, Date dataUpd,
			String attoreUpd) {
		super();
		this.idFaq = idFaq;
		this.titolo = titolo;
		this.contenuto = contenuto;
		this.dataIns = dataIns;
		this.attoreIns = attoreIns;
		this.dataUpd = dataUpd;
		this.attoreUpd = attoreUpd;
	}

	public Long getIdFaq() {
		return idFaq;
	}
	public void setIdFaq(Long idFaq) {
		this.idFaq = idFaq;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getContenuto() {
		return contenuto;
	}
	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
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

	@Override
	public String toString() {
		return "Faq [idFaq=" + idFaq + ", titolo=" + titolo + ", contenuto=" + contenuto + ", dataIns=" + dataIns
				+ ", attoreIns=" + attoreIns + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd + "]";
	}

}