/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dto.ext;

import java.util.Date;

/**
 * Entity della tabella instanza relativa all'emissione dei buoni spesa dell'emergenza covid 19
 * <br>
 * <br>Tabella moon_ext_domanda_buoni
 * <br>PK: idExtBuono
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class DomandaBuoniEntity {
	/*
    id_ext_domanda,id_istanza,codice_istanza,codice_fiscale,
    tipo_documento,numero_documento,data_emissione_documento,
    nome ,cognome ,num_componenti, telefono ,cellulare ,email,
    chk_reddito_cittadinanza,chk_altri_sostegni,
    flag_verificatoanpr,flag_mail_inviata,flag_controlli_eseguiti,esito_controlli,
    data_ins_domanda,periodo,operatore_ins,pin
*/
	private Integer idExtDomanda;
	private Long idIstanza;
	private String codiceIstanza;
	private String codiceFiscale;
	
	private String tipoDocumento;
	private String numeroDocumento; // 20
	private Date dataEmissioneDocumento;
	
	private String nome; // 50
	private String cognome; // 50
	private Integer numComponenti;
	private String telefono; // 30
	private String cellulare; // 30
	private String email; // 200
	
	private String chkRedditoCittadinanza;
	private String chkAltriSostegni;
	private String flagVerificatoAnpr;
	private String flagMailInviata;
	private String flagControlliEseguiti;

	private String esitoControlli; // Flag
	private Date dataInsDomanda;
	private String periodo;
	private String operatoreIns;
	private String pin; // 50
	
	
	public Integer getIdExtDomanda() {
		return idExtDomanda;
	}
	public void setIdExtDomanda(Integer idExtDomanda) {
		this.idExtDomanda = idExtDomanda;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public String getCodiceIstanza() {
		return codiceIstanza;
	}
	public void setCodiceIstanza(String codiceIstanza) {
		this.codiceIstanza = codiceIstanza;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public Date getDataEmissioneDocumento() {
		return dataEmissioneDocumento;
	}
	public void setDataEmissioneDocumento(Date dataEmissioneDocumento) {
		this.dataEmissioneDocumento = dataEmissioneDocumento;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public Integer getNumComponenti() {
		return numComponenti;
	}
	public void setNumComponenti(Integer numComponenti) {
		this.numComponenti = numComponenti;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCellulare() {
		return cellulare;
	}
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getChkRedditoCittadinanza() {
		return chkRedditoCittadinanza;
	}
	public void setChkRedditoCittadinanza(String chkRedditoCittadinanza) {
		this.chkRedditoCittadinanza = chkRedditoCittadinanza;
	}
	public String getChkAltriSostegni() {
		return chkAltriSostegni;
	}
	public void setChkAltriSostegni(String chkAltriSostegni) {
		this.chkAltriSostegni = chkAltriSostegni;
	}
	public String getFlagVerificatoAnpr() {
		return flagVerificatoAnpr;
	}
	public void setFlagVerificatoAnpr(String flagVerificatoAnpr) {
		this.flagVerificatoAnpr = flagVerificatoAnpr;
	}
	public String getFlagMailInviata() {
		return flagMailInviata;
	}
	public void setFlagMailInviata(String flagMailInviata) {
		this.flagMailInviata = flagMailInviata;
	}
	public String getFlagControlliEseguiti() {
		return flagControlliEseguiti;
	}
	public void setFlagControlliEseguiti(String flagControlliEseguiti) {
		this.flagControlliEseguiti = flagControlliEseguiti;
	}
	public String getEsitoControlli() {
		return esitoControlli;
	}
	public void setEsitoControlli(String esitoControlli) {
		this.esitoControlli = esitoControlli;
	}
	public Date getDataInsDomanda() {
		return dataInsDomanda;
	}
	public void setDataInsDomanda(Date dataInsDomanda) {
		this.dataInsDomanda = dataInsDomanda;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getOperatoreIns() {
		return operatoreIns;
	}
	public void setOperatoreIns(String operatoreIns) {
		this.operatoreIns = operatoreIns;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}

}
