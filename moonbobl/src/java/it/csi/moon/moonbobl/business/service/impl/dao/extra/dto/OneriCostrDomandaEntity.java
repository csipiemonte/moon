/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.dto;

import java.util.Date;

/**
 * Entity della tabella instanza relativa alle richieste di contributi di Oneri Construzione
 * <br>
 * <br>Tabella moon_ext_oneri_costr_domanda
 * <br>PK: idExtDomanda
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class OneriCostrDomandaEntity {

	private Long idExtDomanda;
	private Long idIstanza;
	private String codiceIstanza;
	private String codiceFiscale;
	
	private String cognome;
	private String nome;
	private String email;
	private String cellulare;
	private String flagDichiaranteBeneficiario;
	
	private String benTipologiaBeneficiario; // 30
	private String benCodiceFiscale; // 16
	private String benCognome; // 50
	private String benNome; // 50
	private String benRagioneSociale; // 100
	private String benPiva; // 16
	private Date benDataFineEsercizio;
	private String benDimensione; // 30
	private String benCodiceAteco; // 30
	private String benFormaGiuridica; // 30
	
	private String benSedeNazione; // 100
	private String benSedeRegione; // 100
	private String benSedeProvincia; // 100
	private String benSedeComune; // 100
	private String benSedeIndirizzo; // 100
	private String benSedeCap; // 30
	
	private String prtCodiceProvincia; // 3
	private String prtNomeProvincia; // 100
	private String prtCodiceComune; // 6
	private String prtNomeComune; // 100
	private String prtIndirizzo; // 100
	private String prtCap; // 30
	private String prtTipologiaTitoloEdilizio; // 50
	private Date prtDataPresentazione;
	private String prtNumeroPratica; // 30
	private String prtTipologiaIntervento; // 30
	private Long prtCostoPresunto; // in cents, valore costo costruzione del professionista
	private Long prtImportoPresunto; // in cents, calcolato presunto
	private String foCodiceResponse;
	
	private String boEsitoControlli;
	private String boOperatore;
	private Long boCostoValidato; // in cents, valore costo costruzione del comune
	private Long boImportoValidato; // in cents, calcolato da costo del comune

	private Long boImportoPagato; // in cents
	
	private Date dataIns;
	private Date dataUpd;
	private String attoreUpd;
	private Date dataInvioContabilia;
	public Long getIdExtDomanda() {
		return idExtDomanda;
	}
	public void setIdExtDomanda(Long idExtDomanda) {
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
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCellulare() {
		return cellulare;
	}
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
	public String getFlagDichiaranteBeneficiario() {
		return flagDichiaranteBeneficiario;
	}
	public void setFlagDichiaranteBeneficiario(String flagDichiaranteBeneficiario) {
		this.flagDichiaranteBeneficiario = flagDichiaranteBeneficiario;
	}
	public String getBenTipologiaBeneficiario() {
		return benTipologiaBeneficiario;
	}
	public void setBenTipologiaBeneficiario(String benTipologiaBeneficiario) {
		this.benTipologiaBeneficiario = benTipologiaBeneficiario;
	}
	public String getBenCodiceFiscale() {
		return benCodiceFiscale;
	}
	public void setBenCodiceFiscale(String benCodiceFiscale) {
		this.benCodiceFiscale = benCodiceFiscale;
	}
	public String getBenCognome() {
		return benCognome;
	}
	public void setBenCognome(String benCognome) {
		this.benCognome = benCognome;
	}
	public String getBenNome() {
		return benNome;
	}
	public void setBenNome(String benNome) {
		this.benNome = benNome;
	}
	public String getBenRagioneSociale() {
		return benRagioneSociale;
	}
	public void setBenRagioneSociale(String benRagioneSociale) {
		this.benRagioneSociale = benRagioneSociale;
	}
	public String getBenPiva() {
		return benPiva;
	}
	public void setBenPiva(String benPiva) {
		this.benPiva = benPiva;
	}
	public Date getBenDataFineEsercizio() {
		return benDataFineEsercizio;
	}
	public void setBenDataFineEsercizio(Date benDataFineEsercizio) {
		this.benDataFineEsercizio = benDataFineEsercizio;
	}
	public String getBenDimensione() {
		return benDimensione;
	}
	public void setBenDimensione(String benDimensione) {
		this.benDimensione = benDimensione;
	}
	public String getBenCodiceAteco() {
		return benCodiceAteco;
	}
	public void setBenCodiceAteco(String benCodiceAteco) {
		this.benCodiceAteco = benCodiceAteco;
	}
	public String getBenFormaGiuridica() {
		return benFormaGiuridica;
	}
	public void setBenFormaGiuridica(String benFormaGiuridica) {
		this.benFormaGiuridica = benFormaGiuridica;
	}
	public String getBenSedeNazione() {
		return benSedeNazione;
	}
	public void setBenSedeNazione(String benSedeNazione) {
		this.benSedeNazione = benSedeNazione;
	}
	public String getBenSedeRegione() {
		return benSedeRegione;
	}
	public void setBenSedeRegione(String benSedeRegione) {
		this.benSedeRegione = benSedeRegione;
	}
	public String getBenSedeProvincia() {
		return benSedeProvincia;
	}
	public void setBenSedeProvincia(String benSedeProvincia) {
		this.benSedeProvincia = benSedeProvincia;
	}
	public String getBenSedeComune() {
		return benSedeComune;
	}
	public void setBenSedeComune(String benSedeComune) {
		this.benSedeComune = benSedeComune;
	}
	public String getBenSedeIndirizzo() {
		return benSedeIndirizzo;
	}
	public void setBenSedeIndirizzo(String benSedeIndirizzo) {
		this.benSedeIndirizzo = benSedeIndirizzo;
	}
	public String getBenSedeCap() {
		return benSedeCap;
	}
	public void setBenSedeCap(String benSedeCap) {
		this.benSedeCap = benSedeCap;
	}
	public String getPrtCodiceProvincia() {
		return prtCodiceProvincia;
	}
	public void setPrtCodiceProvincia(String prtCodiceProvincia) {
		this.prtCodiceProvincia = prtCodiceProvincia;
	}
	public String getPrtNomeProvincia() {
		return prtNomeProvincia;
	}
	public void setPrtNomeProvincia(String prtNomeProvincia) {
		this.prtNomeProvincia = prtNomeProvincia;
	}
	public String getPrtCodiceComune() {
		return prtCodiceComune;
	}
	public void setPrtCodiceComune(String prtCodiceComune) {
		this.prtCodiceComune = prtCodiceComune;
	}
	public String getPrtNomeComune() {
		return prtNomeComune;
	}
	public void setPrtNomeComune(String prtNomeComune) {
		this.prtNomeComune = prtNomeComune;
	}
	public String getPrtIndirizzo() {
		return prtIndirizzo;
	}
	public void setPrtIndirizzo(String prtIndirizzo) {
		this.prtIndirizzo = prtIndirizzo;
	}
	public String getPrtCap() {
		return prtCap;
	}
	public void setPrtCap(String prtCap) {
		this.prtCap = prtCap;
	}
	public String getPrtTipologiaTitoloEdilizio() {
		return prtTipologiaTitoloEdilizio;
	}
	public void setPrtTipologiaTitoloEdilizio(String prtTipologiaTitoloEdilizio) {
		this.prtTipologiaTitoloEdilizio = prtTipologiaTitoloEdilizio;
	}
	public Date getPrtDataPresentazione() {
		return prtDataPresentazione;
	}
	public void setPrtDataPresentazione(Date prtDataPresentazione) {
		this.prtDataPresentazione = prtDataPresentazione;
	}
	public String getPrtNumeroPratica() {
		return prtNumeroPratica;
	}
	public void setPrtNumeroPratica(String prtNumeroPratica) {
		this.prtNumeroPratica = prtNumeroPratica;
	}
	public String getPrtTipologiaIntervento() {
		return prtTipologiaIntervento;
	}
	public void setPrtTipologiaIntervento(String prtTipologiaIntervento) {
		this.prtTipologiaIntervento = prtTipologiaIntervento;
	}
	public Long getPrtCostoPresunto() {
		return prtCostoPresunto;
	}
	public void setPrtCostoPresunto(Long prtCostoPresunto) {
		this.prtCostoPresunto = prtCostoPresunto;
	}
	public Long getPrtImportoPresunto() {
		return prtImportoPresunto;
	}
	public void setPrtImportoPresunto(Long prtImportoPresunto) {
		this.prtImportoPresunto = prtImportoPresunto;
	}
	public String getFoCodiceResponse() {
		return foCodiceResponse;
	}
	public void setFoCodiceResponse(String foCodiceResponse) {
		this.foCodiceResponse = foCodiceResponse;
	}
	public String getBoEsitoControlli() {
		return boEsitoControlli;
	}
	public void setBoEsitoControlli(String boEsitoControlli) {
		this.boEsitoControlli = boEsitoControlli;
	}
	public String getBoOperatore() {
		return boOperatore;
	}
	public void setBoOperatore(String boOperatore) {
		this.boOperatore = boOperatore;
	}
	public Long getBoCostoValidato() {
		return boCostoValidato;
	}
	public void setBoCostoValidato(Long boCostoValidato) {
		this.boCostoValidato = boCostoValidato;
	}
	public Long getBoImportoValidato() {
		return boImportoValidato;
	}
	public void setBoImportoValidato(Long boImportoValidato) {
		this.boImportoValidato = boImportoValidato;
	}
	public Long getBoImportoPagato() {
		return boImportoPagato;
	}
	public void setBoImportoPagato(Long boImportoPagato) {
		this.boImportoPagato = boImportoPagato;
	}
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
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
	public Date getDataInvioContabilia() {
		return dataInvioContabilia;
	}
	public void setDataInvioContabilia(Date dataInvioContabilia) {
		this.dataInvioContabilia = dataInvioContabilia;
	}
	
	@Override
	public String toString() {
		return "OneriCostrDomandaEntity [idExtDomanda=" + idExtDomanda + ", idIstanza=" + idIstanza + ", codiceIstanza="
				+ codiceIstanza + ", codiceFiscale=" + codiceFiscale + ", cognome=" + cognome + ", nome=" + nome
				+ ", email=" + email + ", cellulare=" + cellulare + ", flagDichiaranteBeneficiario="
				+ flagDichiaranteBeneficiario + ", benTipologiaBeneficiario=" + benTipologiaBeneficiario
				+ ", benCodiceFiscale=" + benCodiceFiscale + ", benCognome=" + benCognome + ", benNome=" + benNome
				+ ", benRagioneSociale=" + benRagioneSociale + ", benPiva=" + benPiva + ", benDataFineEsercizio="
				+ benDataFineEsercizio + ", benDimensione=" + benDimensione + ", benCodiceAteco=" + benCodiceAteco
				+ ", benFormaGiuridica=" + benFormaGiuridica + ", benSedeNazione=" + benSedeNazione
				+ ", benSedeRegione=" + benSedeRegione + ", benSedeProvincia=" + benSedeProvincia + ", benSedeComune="
				+ benSedeComune + ", benSedeIndirizzo=" + benSedeIndirizzo + ", benSedeCap=" + benSedeCap
				+ ", prtCodiceProvincia=" + prtCodiceProvincia + ", prtNomeProvincia=" + prtNomeProvincia
				+ ", prtCodiceComune=" + prtCodiceComune + ", prtNomeComune=" + prtNomeComune + ", prtIndirizzo="
				+ prtIndirizzo + ", prtCap=" + prtCap + ", prtTipologiaTitoloEdilizio=" + prtTipologiaTitoloEdilizio
				+ ", prtDataPresentazione=" + prtDataPresentazione + ", prtNumeroPratica=" + prtNumeroPratica
				+ ", prtTipologiaIntervento=" + prtTipologiaIntervento + ", prtCostoPresunto=" + prtCostoPresunto
				+ ", prtImportoPresunto=" + prtImportoPresunto + ", foCodiceResponse=" + foCodiceResponse
				+ ", boEsitoControlli=" + boEsitoControlli + ", boOperatore=" + boOperatore + ", boCostoValidato="
				+ boCostoValidato + ", boImportoValidato=" + boImportoValidato + ", boImportoPagato=" + boImportoPagato
				+ ", dataIns=" + dataIns + ", dataUpd=" + dataUpd + ", attoreUpd=" + attoreUpd
				+ ", dataInvioContabilia=" + dataInvioContabilia + "]";
	}
		
}
