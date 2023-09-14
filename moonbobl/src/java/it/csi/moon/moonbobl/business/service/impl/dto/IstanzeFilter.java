/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;

/**
 * Filter DTO usato dal IstanzaDAO per le ricerche delle istanze
 * 
 * @see IstanzaDAO
 * 
 * @author Laurent
 *
 */
public class IstanzeFilter extends PagedFilter {

	public enum EnumFilterFlagEliminata {
		TUTTI,
		ELIMINATI,
		NONELIMINATI
	};
	public enum EnumFilterFlagArchiviata {
		TUTTI,
		ARCHIVIATI,
		NONARCHIVIATI
	};
	public enum EnumFilterFlagTest {
		TUTTI,
		TEST,
		NONTEST
	};
	
	private String idIstanza;
	private String codiceIstanza;
	private String protocollo;
	private List<Long> idModuli;
	private String identificativoUtente;
	private String codiceFiscaleDichiarante;
	private String cognomeDichiarante;
	private String nomeDichiarante;
	private List<Integer> statiIstanza;
	private EnumFilterFlagEliminata flagEliminata = EnumFilterFlagEliminata.NONELIMINATI;
	private EnumFilterFlagArchiviata flagArchiviata = EnumFilterFlagArchiviata.TUTTI;
	private EnumFilterFlagTest flagTest = EnumFilterFlagTest.NONTEST;
	private Integer importanza;
	private String sort;
	private Date createdStart;
	private Date createdEnd;
	private Date stateStart;
	private Date stateEnd;	
	private Long idEnte;
	private Long idArea;
	private Long idTag;
	private String strContainsInDati;
	private String filtroEpay;

	public Optional<String> getIdIstanza() {
		return Optional.ofNullable(idIstanza);
	}
	public void setIdIstanza(String idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Optional<String> getCodiceIstanza() {
		return Optional.ofNullable(codiceIstanza);
	}
	public void setCodiceIstanza(String codiceIstanza) {
		this.codiceIstanza = codiceIstanza;
	}
	public Optional<List<Long>> getIdModuli() {
		return Optional.ofNullable(idModuli);
	}
	public void setIdModuli(List<Long> list) {
		this.idModuli = list;
	}
	public Optional<String> getIdentificativoUtente() {
		return Optional.ofNullable(identificativoUtente);
	}
	public void setIdentificativoUtente(String identificativoUtente) {
		this.identificativoUtente = identificativoUtente;
	}
	public Optional<String> getCodiceFiscaleDichiarante() {
		return Optional.ofNullable(codiceFiscaleDichiarante);
	}
	public void setCodiceFiscaleDichiarante(String codiceFiscaleDichiarante) {
		this.codiceFiscaleDichiarante = codiceFiscaleDichiarante;
	}
	public Optional<String> getCognomeDichiarante() {
		return Optional.ofNullable(cognomeDichiarante);
	}
	public void setCognomeDichiarante(String cognomeDichiarante) {
		this.cognomeDichiarante = cognomeDichiarante;
	}
	public Optional<String> getNomeDichiarante() {
		return Optional.ofNullable(nomeDichiarante);
	}
	public void setNomeDichiarante(String nomeDichiarante) {
		this.nomeDichiarante = nomeDichiarante;
	}	
		
	public Optional<List<Integer>> getStatiIstanza() {
		return Optional.ofNullable(statiIstanza);
	}
	public void setStatiIstanza(List<Integer> list) {
		this.statiIstanza = list;
	}
	public EnumFilterFlagEliminata getFlagEliminata() {
		return flagEliminata;
	}
	public void setFlagEliminata(EnumFilterFlagEliminata statoEliminata) {
		assert flagEliminata!=null : "flagEliminata non presente.";
		this.flagEliminata = statoEliminata;
	}
	public EnumFilterFlagArchiviata getFlagArchiviata() {
		return flagArchiviata;
	}
	public void setFlagArchiviata(EnumFilterFlagArchiviata flagArchiviata) {
		assert flagArchiviata!=null : "flagArchiviata non presente.";
		this.flagArchiviata = flagArchiviata;
	}
	public EnumFilterFlagTest getFlagTest() {
		return flagTest;
	}
	public void setFlagTest(EnumFilterFlagTest flagTest) {
		assert flagTest!=null : "flagTest non presente.";
		this.flagTest = flagTest;
	}
	public Optional<Integer> getImportanza() {
		return Optional.ofNullable(importanza);
	}
	public void setImportanza(Integer importanza) {
		this.importanza = importanza;
	}
	
	public Optional<String> getSort() {
		return Optional.ofNullable(sort);
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public Optional<String> getProtocollo() {
		return Optional.ofNullable(protocollo);
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public Optional<Date> getCreatedStart() {
		return Optional.ofNullable(createdStart);
	}
	public void setCreatedStart(Date createdStart) {
		this.createdStart = createdStart;
	}
	public Optional<Date> getCreatedEnd() {
		return Optional.ofNullable(createdEnd);
	}
	public void setCreatedEnd(Date createdEnd) {
		this.createdEnd = createdEnd;
	}
	public Optional<Date> getStateStart() {
		return Optional.ofNullable(stateStart);
	}
	public void setStateStart(Date stateStart) {
		this.stateStart = stateStart;
	}
	public Optional<Date>  getStateEnd() {
		return Optional.ofNullable(stateEnd);	
	}
	public void setStateEnd(Date stateEnd) {
		this.stateEnd = stateEnd;
	}
	
	public Optional<Long> getIdEnte() {
		return Optional.ofNullable(idEnte);
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public Optional<Long> getIdArea() {
		return Optional.ofNullable(idArea);
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}
	public Optional<Long> getIdTag() {
		return Optional.ofNullable(idTag);
	}
	public void setIdTag(Long idTag) {
		this.idTag = idTag;
	}
	public String getStrContainsInDati() {
		return strContainsInDati;
	}
	public void setStrContainsInDati(String strContainsInDati) {
		this.strContainsInDati = strContainsInDati;
	}
	public String getFiltroEpay() {
		return filtroEpay;
	}
	public void setFiltroEpay(String filtroEpay) {
		this.filtroEpay = filtroEpay;
	}
	
	@Override
	public String toString() {
		return "IstanzeFilter [idIstanza=" + idIstanza + ", codiceIstanza=" + codiceIstanza + ", protocollo="
				+ protocollo + ", idModuli=" + idModuli + ", identificativoUtente=" + identificativoUtente 
				+ ", codiceFiscaleDichiarante=" + codiceFiscaleDichiarante
				+ ", cognomeDichiarante=" + cognomeDichiarante + ", nomeDichiarante=" + nomeDichiarante
				+ ", statiIstanza=" + statiIstanza + ", flagEliminata=" + flagEliminata + ", flagArchiviata="
				+ flagArchiviata + ", flagTest=" + flagTest + ", importanza=" + importanza + ", sort=" + sort
				+ ", createdStart=" + createdStart + ", createdEnd=" + createdEnd + ", stateStart=" + stateStart
				+ ", stateEnd=" + stateEnd + ", idEnte=" + idEnte + ", idArea=" + idArea + ", idTag=" + idTag
				+ ", strContainsInDati=" + strContainsInDati
				+ ", filtroEpay=" + filtroEpay+ "]";
	}


	
}
