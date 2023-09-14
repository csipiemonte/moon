/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.dto;


/**
 * Entity della tabella moon_ext_praedi_pratiche
 * <br>
 * @author Alberto
 *
 * @since 1.0.0
 */
public class PraticaEdiliziaEntity {
	
	private Integer idPraedPratiche;
	private Integer idPratica;
	private String anno;
	private Integer idRegistro;
	private String progressivo;

    private String dataProtocollo;
    private String descEstesaOpera;
    private String numArchivio;
    private String maglia;
    private String descTipoProvv;
    private String dataProvv;
    private String numProvv;
	
    private String idViaTopon;
    private String idCivicoTopon;
    private String sedime;
    private String descVia;
    private String numCivico;
    private String bis;
    private String bisinterno;
    private String interno;
    private String interno2;
    private String cap;
    private String scala;
    private String secondario;
    private String flFronte;
    private String angolo;
    private String piano;
    private String flProvvisorio;
    private String dataFine;

	public PraticaEdiliziaEntity() {

	}

	public Integer getIdPraedPratiche() {
		return idPraedPratiche;
	}

	public void setIdPraedPratiche(Integer idPraedPratiche) {
		this.idPraedPratiche = idPraedPratiche;
	}

	public Integer getIdPratica() {
		return idPratica;
	}

	public void setIdPratica(Integer idPratica) {
		this.idPratica = idPratica;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public Integer getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(Integer idRegistro) {
		this.idRegistro = idRegistro;
	}

	public String getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}


	public String getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	public String getDescEstesaOpera() {
		return descEstesaOpera;
	}

	public void setDescEstesaOpera(String descEstesaOpera) {
		this.descEstesaOpera = descEstesaOpera;
	}

	public String getNumArchivio() {
		return numArchivio;
	}

	public void setNumArchivio(String numArchivio) {
		this.numArchivio = numArchivio;
	}

	public String getMaglia() {
		return maglia;
	}

	public void setMaglia(String maglia) {
		this.maglia = maglia;
	}

	public String getDescTipoProvv() {
		return descTipoProvv;
	}

	public void setDescTipoProvv(String descTipoProvv) {
		this.descTipoProvv = descTipoProvv;
	}

	public String getDataProvv() {
		return dataProvv;
	}

	public void setDataProvv(String dataProvv) {
		this.dataProvv = dataProvv;
	}

	public String getNumProvv() {
		return numProvv;
	}

	public void setNumProvv(String numProvv) {
		this.numProvv = numProvv;
	}

	public String getIdViaTopon() {
		return idViaTopon;
	}

	public void setIdViaTopon(String idViaTopon) {
		this.idViaTopon = idViaTopon;
	}

	public String getIdCivicoTopon() {
		return idCivicoTopon;
	}

	public void setIdCivicoTopon(String idCivicoTopon) {
		this.idCivicoTopon = idCivicoTopon;
	}

	public String getSedime() {
		return sedime;
	}

	public void setSedime(String sedime) {
		this.sedime = sedime;
	}

	public String getDescVia() {
		return descVia;
	}

	public void setDescVia(String descVia) {
		this.descVia = descVia;
	}

	public String getNumCivico() {
		return numCivico;
	}

	public void setNumCivico(String numCivico) {
		this.numCivico = numCivico;
	}

	public String getBis() {
		return bis;
	}

	public void setBis(String bis) {
		this.bis = bis;
	}

	public String getBisinterno() {
		return bisinterno;
	}

	public void setBisinterno(String bisinterno) {
		this.bisinterno = bisinterno;
	}

	public String getInterno() {
		return interno;
	}

	public void setInterno(String interno) {
		this.interno = interno;
	}

	public String getInterno2() {
		return interno2;
	}

	public void setInterno2(String interno2) {
		this.interno2 = interno2;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getScala() {
		return scala;
	}

	public void setScala(String scala) {
		this.scala = scala;
	}

	public String getSecondario() {
		return secondario;
	}

	public void setSecondario(String secondario) {
		this.secondario = secondario;
	}

	public String getFlFronte() {
		return flFronte;
	}

	public void setFlFronte(String flFronte) {
		this.flFronte = flFronte;
	}

	public String getAngolo() {
		return angolo;
	}

	public void setAngolo(String angolo) {
		this.angolo = angolo;
	}

	public String getPiano() {
		return piano;
	}

	public void setPiano(String piano) {
		this.piano = piano;
	}

	public String getFlProvvisorio() {
		return flProvvisorio;
	}

	public void setFlProvvisorio(String flProvvisorio) {
		this.flProvvisorio = flProvvisorio;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	@Override
	public String toString() {
		return "ScuolaEntity [idPraedPratiche=" + idPraedPratiche + 
				", idPratica=" + idPratica + 
				", anno=" + anno + 
				", idRegistro="	+ idRegistro + 
				", progressivo=" + progressivo + 
				", data_protocollo=" + dataProtocollo + 
				", desc_estesa_opera=" + descEstesaOpera + 
				", num_archivio=" + numArchivio +
				", maglia=" + maglia +
				", desc_tipo_provv=" + descTipoProvv +
				", data_provv=" + dataProvv +
				", num_provv=" + numProvv +
				"]";
	}

}
