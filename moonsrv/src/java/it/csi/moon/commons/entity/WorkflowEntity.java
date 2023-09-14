/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

/**
 * 
 * Tabella moon_wf_d_workflow
 * PK: idWorkflow
 * 
 * @author Alberto
 *
 */
public class WorkflowEntity {
	
	private Long idWorkflow;
	private Long idProcesso;
	private Integer idStatoWfPartenza;
	private Integer idStatoWfArrivo;
	private String campoCondizione;
	private String valoreCondizione;
	private Long idAzione;
	private String nomeAzione;
	private String codiceAzione;
	private String emailDestinatario;
	private Long idUtenteDestinatario;
	private Long idTipoUtenteDestinatario;
	private Long idGruppoUtentiDestinatari;
	private String flagArchiviabile;
	private String flagAnnullabile;
	private Long idDatiazione;
	private String flagStatoIstanza;
	private String flagApi;
	private String flagAutomatico;
	private Long idCondition;
	
	public Long getIdWorkflow() {
		return idWorkflow;
	}
	public void setIdWorkflow(Long idWorkflow) {
		this.idWorkflow = idWorkflow;
	}
	public Long getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}
	public Integer getIdStatoWfPartenza() {
		return idStatoWfPartenza;
	}
	public void setIdStatoWfPartenza(Integer idStatoWfPartenza) {
		this.idStatoWfPartenza = idStatoWfPartenza;
	}
	public Integer getIdStatoWfArrivo() {
		return idStatoWfArrivo;
	}
	public void setIdStatoWfArrivo(Integer idStatoWfArrivo) {
		this.idStatoWfArrivo = idStatoWfArrivo;
	}
	public String getCampoCondizione() {
		return campoCondizione;
	}
	public void setCampoCondizione(String campoCondizione) {
		this.campoCondizione = campoCondizione;
	}
	public String getValoreCondizione() {
		return valoreCondizione;
	}
	public void setValoreCondizione(String valoreCondizione) {
		this.valoreCondizione = valoreCondizione;
	}
	public Long getIdAzione() {
		return idAzione;
	}
	public void setIdAzione(Long idAzione) {
		this.idAzione = idAzione;
	}
	public String getNomeAzione() {
		return nomeAzione;
	}
	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}
	public String getCodiceAzione() {
		return codiceAzione;
	}
	public void setCodiceAzione(String codiceAzione) {
		this.codiceAzione = codiceAzione;
	}
	public String getEmailDestinatario() {
		return emailDestinatario;
	}
	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}
	public Long getIdUtenteDestinatario() {
		return idUtenteDestinatario;
	}
	public void setIdUtenteDestinatario(Long idUtenteDestinatario) {
		this.idUtenteDestinatario = idUtenteDestinatario;
	}
	public Long getIdTipoUtenteDestinatario() {
		return idTipoUtenteDestinatario;
	}
	public void setIdTipoUtenteDestinatario(Long idTipoUtenteDestinatario) {
		this.idTipoUtenteDestinatario = idTipoUtenteDestinatario;
	}
	public Long getIdGruppoUtentiDestinatari() {
		return idGruppoUtentiDestinatari;
	}
	public void setIdGruppoUtentiDestinatari(Long idGruppoUtentiDestinatari) {
		this.idGruppoUtentiDestinatari = idGruppoUtentiDestinatari;
	}
	public String getFlagArchiviabile() {
		return flagArchiviabile;
	}
	public void setFlagArchiviabile(String flagArchiviabile) {
		this.flagArchiviabile = flagArchiviabile;
	}
	public String getFlagAnnullabile() {
		return flagAnnullabile;
	}
	public void setFlagAnnullabile(String flagAnnullabile) {
		this.flagAnnullabile = flagAnnullabile;
	}
	public Long getIdDatiazione() {
		return idDatiazione;
	}
	public void setIdDatiazione(Long idDatiazione) {
		this.idDatiazione = idDatiazione;
	}
	public String getFlagStatoIstanza() {
		return flagStatoIstanza;
	}
	public void setFlagStatoIstanza(String flagStatoIstanza) {
		this.flagStatoIstanza = flagStatoIstanza;
	}
	public String getFlagApi() {
		return flagApi;
	}
	public void setFlagApi(String flagApi) {
		this.flagApi = flagApi;
	}
	public String getFlagAutomatico() {
		return flagAutomatico;
	}
	public void setFlagAutomatico(String flagAutomatico) {
		this.flagAutomatico = flagAutomatico;
	}
	public Long getIdCondition() {
		return idCondition;
	}
	public void setIdCondition(Long idCondition) {
		this.idCondition = idCondition;
	}

}
