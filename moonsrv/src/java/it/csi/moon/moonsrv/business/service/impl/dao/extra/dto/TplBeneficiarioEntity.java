/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.dto;

public class TplBeneficiarioEntity {

	Integer idBeneficiario;
	String cfBeneficiario;
	String nomeBeneficiario;
	String cognomeBeneficiario;
	String tesseraBip;
	String codiceVoucher;
	String cfRichiedente;
	
	public final Integer getIdBeneficiario() {
		return idBeneficiario;
	}
	public final String getCfBeneficiario() {
		return cfBeneficiario;
	}
	public final String getNomeBeneficiario() {
		return nomeBeneficiario;
	}
	public final String getCognomeBeneficiario() {
		return cognomeBeneficiario;
	}
	public final String getTesseraBip() {
		return tesseraBip;
	}
	public final String getCodiceVoucher() {
		return codiceVoucher;
	}
	public final String getCfRichiedente() {
		return cfRichiedente;
	}
	public final void setIdBeneficiario(Integer idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}
	public final void setCfBeneficiario(String cfBeneficiario) {
		this.cfBeneficiario = cfBeneficiario;
	}
	public final void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}
	public final void setCognomeBeneficiario(String cognomeBeneficiario) {
		this.cognomeBeneficiario = cognomeBeneficiario;
	}
	public final void setTesseraBip(String tesseraBip) {
		this.tesseraBip = tesseraBip;
	}
	public final void setCodiceVoucher(String codiceVoucher) {
		this.codiceVoucher = codiceVoucher;
	}
	public final void setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
	}
	
	@Override
	public String toString() {
		return "TplBeneficiarioEntity [idBeneficiario=" + idBeneficiario + ", cfBeneficiario=" + cfBeneficiario
				+ ", nomeBeneficiario=" + nomeBeneficiario + ", cognomeBeneficiario=" + cognomeBeneficiario
				+ ", tesseraBip=" + tesseraBip + ", codiceVoucher=" + codiceVoucher + ", cfRichiedente=" + cfRichiedente
				+ "]";
	}
	
}
