/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

/**
 * Entity della tabella instanza
 * <br>Deve essere presente almeno un record di Cronologia e di Dati 
 * <br>
 * <br>Tabella moon_fo_t_istanza
 * <br>PK: idIstanza
 * 
 * @see IstanzaCronologiaStatiEntity
 * @see IstanzaDatiEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzaEntity {
	
	private Long idIstanza;
	private String codiceIstanza; // MAXLENGTH(50)
	private Long idModulo;
	private Long idVersioneModulo;
	private String identificativoUtente;
	private String codiceFiscaleDichiarante;
	private String cognomeDichiarante = null;
	private String nomeDichiarante = null;
	private Integer idStatoWf;
	private Date dataCreazione;
	private String attoreIns;
	private String attoreUpd;
	private String flagEliminata;
	private String flagArchiviata;
	private String flagTest;
	private Integer importanza;
	private String numeroProtocollo;
	private Date dataProtocollo;
	private Integer currentStep;
	private Long idEnte;
	private String hashUnivocita;
	private String gruppoOperatoreFo;
	private String datiAggiuntivi;
	//
	private Integer idStatoWfArrivo;
	private Date dataStato;
	
	
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	/**
	 * Recupera il Codice Univoco dell istanza per il modulo
	 * MAXLENGTH 50
	 * @return codiceIstanza
	 */
	public String getCodiceIstanza() {
		return codiceIstanza;
	}

	/**
	 * Assegna il Codice Univoco dell istanza per il modulo
	 * MAXLENGTH 50
	 * @return codiceIstanza
	 */
	public void setCodiceIstanza(String codiceIstanza) {
		this.codiceIstanza = codiceIstanza;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public String getIdentificativoUtente() {
		return identificativoUtente;
	}
	public void setIdentificativoUtente(String identificativoUtente) {
		this.identificativoUtente = identificativoUtente;
	}
	public String getCodiceFiscaleDichiarante() {
		return codiceFiscaleDichiarante;
	}
	public void setCodiceFiscaleDichiarante(String codiceFiscaleDichiarante) {
		this.codiceFiscaleDichiarante = codiceFiscaleDichiarante;
	}
	public String getCognomeDichiarante() {
		return cognomeDichiarante;
	}
	public void setCognomeDichiarante(String cognomeDichiarante) {
		this.cognomeDichiarante = cognomeDichiarante;
	}
	public String getNomeDichiarante() {
		return nomeDichiarante;
	}
	public void setNomeDichiarante(String nomeDichiarante) {
		this.nomeDichiarante = nomeDichiarante;
	}
	public Integer getIdStatoWf() {
		return idStatoWf;
	}
	public void setIdStatoWf(Integer idStatoWf) {
		this.idStatoWf = idStatoWf;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public String getAttoreIns() {
		return attoreIns;
	}
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
	}
	public String getFlagEliminata() {
		return flagEliminata;
	}
	public void setFlagEliminata(String flagEliminata) {
		this.flagEliminata = flagEliminata;
	}
	public void setFlagArchiviata(String flagArchiviata) {
		this.flagArchiviata = flagArchiviata;
	}
	public String getFlagArchiviata() {
		return flagArchiviata;
	}
	public String getFlagTest() {
		return flagTest;
	}
	public void setFlagTest(String flagTest) {
		this.flagTest = flagTest;
	}
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}
	public Integer getImportanza() {
		return importanza;
	}
	public void setImportanza(Integer importanza) {
		this.importanza = importanza;
	}
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}
	public Date getDataProtocollo() {
		return dataProtocollo;
	}
	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	public Integer getCurrentStep() {
		return currentStep;
	}
	public void setCurrentStep(Integer currentStep) {
		this.currentStep = currentStep;
	}
	public Long getIdVersioneModulo() {
		return idVersioneModulo;
	}
	public void setIdVersioneModulo(Long idVersioneModulo) {
		this.idVersioneModulo = idVersioneModulo;
	}
	public Long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}
	public String getHashUnivocita() {
		return hashUnivocita;
	}
	public void setHashUnivocita(String hashUnivocita) {
		this.hashUnivocita = hashUnivocita;
	}
	public String getGruppoOperatoreFo() {
		return gruppoOperatoreFo;
	}
	public void setGruppoOperatoreFo(String gruppoOperatoreFo) {
		this.gruppoOperatoreFo = gruppoOperatoreFo;
	}
	public String getDatiAggiuntivi() {
		return datiAggiuntivi;
	}
	public void setDatiAggiuntivi(String datiAggiuntivi) {
		this.datiAggiuntivi = datiAggiuntivi;
	}
	
	//
	public Integer getIdStatoWfArrivo() {
		return idStatoWfArrivo;
	}
	public void setIdStatoWfArrivo(Integer idStatoWfArrivo) {
		this.idStatoWfArrivo = idStatoWfArrivo;
	}
	public Date getDataStato() {
		return dataStato;
	}
	public void setDataStato(Date dataStato) {
		this.dataStato = dataStato;
	}

}
