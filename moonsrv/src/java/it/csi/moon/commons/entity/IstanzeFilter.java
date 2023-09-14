/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Filter DTO usato dal IstanzaDAO per le ricerche delle istanze
 * 
 * @see IstanzaDAO
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzeFilter extends PagedFilter {

	public static final Integer VISIBILITA_AMBITO_PUBLIC = 1;

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
	
	private Long idIstanza;
	private String codiceIstanza;
	private Long idModulo;
	private Long idVersioneModulo;
	private List<Long> idModuli; // for BO, da generalizzare per sostituire idModulo
	private String titoloModulo;
	private String identificativoUtente;
	private String codiceFiscaleDichiarante;
	private String cognomeDichiarante;
	private String nomeDichiarante;
	private List<Integer> statiIstanza;
	private List<Integer> statiBo;
	private List<Integer> notStatiBo;
	private List<Integer> notStatiFo;
	private EnumFilterFlagEliminata flagEliminata = EnumFilterFlagEliminata.NONELIMINATI;
	private EnumFilterFlagArchiviata flagArchiviata = EnumFilterFlagArchiviata.TUTTI;
	private EnumFilterFlagTest flagTest = EnumFilterFlagTest.NONTEST;
	private Integer importanza;
	private Date createdStart;
	private Date createdEnd;
	private String nomePortale;
	private Integer idTabFo;
	private Long idEnte;
	private Long idArea;
	private Long idTag;
	private String gruppoOperatoreFo;
	private Integer idAmbito;
	private Integer idVisibilitaAmbito;
	private String codiceFiscaleJson;
	private String cognomeJson;
	private String nomeJson;
	private boolean findInJsonData = false;
	private String codiceFiscaleJsonKey;
	private String cognomeJsonKey;
	private String nomeJsonKey;
	

	public Optional<Long> getIdIstanza() {
		return Optional.ofNullable(idIstanza);
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Optional<String> getCodiceIstanza() {
		return Optional.ofNullable(codiceIstanza);
	}
	public void setCodiceIstanza(String codiceIstanza) {
		this.codiceIstanza = codiceIstanza;
	}
	public Optional<Long> getIdModulo() {
		return Optional.ofNullable(idModulo);
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public Optional<Long> getIdVersioneModulo() {
		return Optional.ofNullable(idVersioneModulo);
	}
	public void setIdVersioneModulo(Long idVersioneModulo) {
		this.idVersioneModulo = idVersioneModulo;
	}
	public Optional<List<Long>> getIdModuli() {
		return Optional.ofNullable(idModuli);
	}
	public void setIdModuli(List<Long> list) {
		this.idModuli = list;
	}
	public Optional<String> getTitoloModulo() {
		return Optional.ofNullable(titoloModulo);
	}
	public void setTitoloModulo(String titoloModulo) {
		this.titoloModulo = titoloModulo;
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
	public Optional<List<Integer>> getStatiBo() {
		return Optional.ofNullable(statiBo);
	}
	public void setStatiBo(List<Integer> list) {
		this.statiBo = list;
	}	
	public Optional<List<Integer>> getNotStatiBo() {
		return  Optional.ofNullable(notStatiBo);
	}
	public void setNotStatiBo(List<Integer> notStatiBo) {
		this.notStatiBo = notStatiBo;
	}
	public Optional<List<Integer>> getNotStatiFo() {
		return  Optional.ofNullable(notStatiFo);
	}
	public void setNotStatiFo(List<Integer> notStatiFo) {
		this.notStatiFo = notStatiFo;
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
	public Optional<String> getNomePortale() {
		return Optional.ofNullable(nomePortale);
	}
	public void setNomePortale(String nomePortale) {
		this.nomePortale = nomePortale;
	}
	public Optional<Integer> getIdTabFo() {
		return Optional.ofNullable(idTabFo);
	}
	public void setIdTabFo(Integer idTabFo) {
		this.idTabFo = idTabFo;
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
	public Optional<String> getGruppoOperatoreFo() {
		return Optional.ofNullable(gruppoOperatoreFo);
	}
	public void setGruppoOperatoreFo(String gruppoOperatoreFo) {
		this.gruppoOperatoreFo = gruppoOperatoreFo;
	}
	public Optional<Integer> getIdAmbito() {
		return Optional.ofNullable(idAmbito);
	}
	public void setIdAmbito(Integer idAmbito) {
		this.idAmbito = idAmbito;
	}
	public Optional<Integer> getIdVisibilitaAmbito() {
		return Optional.ofNullable(idVisibilitaAmbito);
	}
	public void setIdVisibilitaAmbito(Integer idVisibilitaAmbito) {
		this.idVisibilitaAmbito = idVisibilitaAmbito;
	}
	public Optional<String> getCodiceFiscaleJson() {
		return Optional.ofNullable(codiceFiscaleJson);
	}
	public void setCodiceFiscaleJson(String codiceFiscaleJson) {
		this.codiceFiscaleJson = codiceFiscaleJson;
	}
	public Optional<String> getCognomeJson() {
		return Optional.ofNullable(cognomeJson);
	}
	public void setCognomeJson(String cognomeJson) {
		this.cognomeJson = cognomeJson;
	}
	public Optional<String> getNomeJson() {
		return Optional.ofNullable(nomeJson);
	}
	public void setNomeJson(String nomeJson) {
		this.nomeJson = nomeJson;
	}
	public String getCodiceFiscaleJsonKey() {
		return codiceFiscaleJsonKey;
	}
	public String getCognomeJsonKey() {
		return cognomeJsonKey;
	}
    public String getNomeJsonKey() {
		return nomeJsonKey;
	}
	public void setCodiceFiscaleJsonKey(String codiceFiscaleJsonKey) {
		this.codiceFiscaleJsonKey = codiceFiscaleJsonKey;
	}
	public void setCognomeJsonKey(String cognomeJsonKey) {
		this.cognomeJsonKey = cognomeJsonKey;
	}
	public void setNomeJsonKey(String nomeJsonKey) {
		this.nomeJsonKey = nomeJsonKey;
	}
	public boolean isFindInJsonData() {
		return findInJsonData;
	}
	public void setFindInJsonData(boolean findInJsonData) {
		this.findInJsonData = findInJsonData;
	}
	
	@Override
	public String toString() {
		return "IstanzeFilter [idIstanza=" + idIstanza + ", codiceIstanza=" + codiceIstanza + ", idModulo=" + idModulo + ", idVersioneModulo=" + idVersioneModulo
				+ ", idModuli=" + idModuli + ", titoloModulo=" + titoloModulo + ", identificativoUtente=" + identificativoUtente + ", "
				+ "codiceFiscaleDichiarante="+ codiceFiscaleDichiarante + ", cognomeDichiarante=" + cognomeDichiarante + ", nomeDichiarante=" + nomeDichiarante
				+ ", statiIstanza=" + statiIstanza + ", statiBo=" + statiBo + ", notStatiBo=" + notStatiBo
				+ ", flagEliminata=" + flagEliminata + ", flagArchiviata=" + flagArchiviata + ", flagTest=" + flagTest
				+ ", importanza=" + importanza + ", createdStart=" + createdStart + ", createdEnd=" + createdEnd
				+ ", nomePortale=" + nomePortale + ", idTabFo=" + idTabFo + ", idEnte=" + idEnte + ", idArea=" + idArea
				+ ", idTag=" + idTag + ", gruppoOperatoreFo=" + gruppoOperatoreFo + ", idAmbito=" + idAmbito 
				+ ", codiceFiscaleJson="+ codiceFiscaleJson + ", cognomeJson=" + cognomeJson + ", nomeJson" + nomeJson
				+ ", codiceFiscaleJsonKey="+ codiceFiscaleJsonKey + ", cognomeJsonKey=" + cognomeJsonKey + ", nomeJsonKey" + nomeJsonKey
				+ ", findInJsonData="+ findInJsonData
				+ ", idVisibilitaAmbito=" + idVisibilitaAmbito + "]";
	}


}
