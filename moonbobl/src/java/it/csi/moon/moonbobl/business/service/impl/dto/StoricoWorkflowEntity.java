/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dto;

import java.util.Date;

import it.csi.moon.moonbobl.util.decodifica.DecodificaAzione;
import it.csi.moon.moonbobl.util.decodifica.DecodificaStatoIstanza;

/**
 * Entity della tabella di storico degli stati delle istanze per la gestione del WF lato BO
 * <br>Viene salavato il primo record solo dallo stato INVIATO dal FO
 * <br> 
 * <br>Tabella moon_wf_t_storico_workflow
 * <br>PK: idStoricoWorkflow
 * 
 * @author Laurent
 * @author Alberto
 *
 * @since 1.0.0
 */
public class StoricoWorkflowEntity {

	private Long idStoricoWorkflow = null;
	private Long idIstanza = null;
	private Long idProcesso = null;
	
	private Integer idStatoWfPartenza = null;
	private Integer idStatoWfArrivo = null;
	private Long idAzione = null;
	
	private String nomeStatoWfPartenza = null;
	private String nomeStatoWfArrivo = null;
	private String nomeAzione = null;
	
	private Long idDatiazione = null;
	private String datiAzione;
	private String descDestinatario = null;
	
	private Date dataInizio = null;
	private Date dataFine = null;
	
	private Long idFileRendering = null;
	private String attoreUpd = null;
	
	public StoricoWorkflowEntity() {
	}

	public StoricoWorkflowEntity(Long idStoricoWorkflow, Long idIstanza, Long idProcesso,
			Integer idStatoWfPartenza, Integer idStatoWfArrivo, Long idAzione, 
			String nomeStatoWfPartenza, String nomeStatoWfArrivo, String nomeAzione,
			String descDestinatario, Date dataInizio, String attoreUpd) {
		super();
		this.idStoricoWorkflow = idStoricoWorkflow;
		this.idIstanza = idIstanza;
		this.idProcesso = idProcesso;
		
		this.idStatoWfPartenza = idStatoWfPartenza;
		this.idStatoWfArrivo = idStatoWfArrivo;
		this.idAzione = idAzione;
		
		this.nomeStatoWfPartenza = nomeStatoWfPartenza;
		this.nomeStatoWfArrivo = nomeStatoWfArrivo;
		this.nomeAzione = nomeAzione;
		
		this.descDestinatario = descDestinatario;
		this.dataInizio = dataInizio;
		this.attoreUpd = attoreUpd;
	}

	public StoricoWorkflowEntity(Long idStoricoWorkflow, Long idIstanza, Long idProcesso,
		DecodificaStatoIstanza statoPartenza, DecodificaStatoIstanza statoArrivo, DecodificaAzione azione, 
		String descDestinatario, Date dataInizio, String attoreUpd)
	{
		this(idStoricoWorkflow, idIstanza, idProcesso,
			statoPartenza.getIdStatoWf(), statoArrivo.getIdStatoWf(), azione.getIdAzione(),
			statoPartenza.getStatoEntity().getNomeStatoWf(), statoArrivo.getStatoEntity().getNomeStatoWf(), azione.getAzioneEntity().getNomeAzione(),
			descDestinatario, dataInizio, attoreUpd);
	}
	
	public Long getIdStoricoWorkflow() {
		return idStoricoWorkflow;
	}
	public void setIdStoricoWorkflow(Long idStoricoWorkflow) {
		this.idStoricoWorkflow = idStoricoWorkflow;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
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
	public Long getIdAzione() {
		return idAzione;
	}
	public void setIdAzione(Long idAzione) {
		this.idAzione = idAzione;
	}
	public String getNomeStatoWfPartenza() {
		return nomeStatoWfPartenza;
	}
	public void setNomeStatoWfPartenza(String nomeStatoWfPartenza) {
		this.nomeStatoWfPartenza = nomeStatoWfPartenza;
	}
	public String getNomeStatoWfArrivo() {
		return nomeStatoWfArrivo;
	}
	public void setNomeStatoWfArrivo(String nomeStatoWfArrivo) {
		this.nomeStatoWfArrivo = nomeStatoWfArrivo;
	}
	public String getNomeAzione() {
		return nomeAzione;
	}
	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}
	public Long getIdDatiazione() {
		return idDatiazione;
	}
	public void setIdDatiazione(Long idDatiazione) {
		this.idDatiazione = idDatiazione;
	}
	public String getDatiAzione() {
		return datiAzione;
	}
	public void setDatiAzione(String datiAzione) {
		this.datiAzione = datiAzione;
	}
	public String getDescDestinatario() {
		return descDestinatario;
	}
	public void setDescDestinatario(String descDestinatario) {
		this.descDestinatario = descDestinatario;
	}
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	public Long getIdFileRendering() {
		return idFileRendering;
	}
	public void setIdFileRendering(Long idFileRendering) {
		this.idFileRendering = idFileRendering;
	}
	public String getAttoreUpd() {
		return attoreUpd;
	}
	public void setAttoreUpd(String attoreUpd) {
		this.attoreUpd = attoreUpd;
	}

	@Override
	public String toString() {
		return "StoricoWorkflowEntity [idStoricoWorkflow=" + idStoricoWorkflow + ", idIstanza=" + idIstanza
				+ ", idProcesso=" + idProcesso + ", idStatoWfPartenza=" + idStatoWfPartenza + ", idStatoWfArrivo="
				+ idStatoWfArrivo + ", idAzione=" + idAzione + ", nomeStatoWfPartenza=" + nomeStatoWfPartenza
				+ ", nomeStatoWfArrivo=" + nomeStatoWfArrivo + ", nomeAzione=" + nomeAzione + ", idDatiazione="
				+ idDatiazione + ", datiAzione=" + datiAzione + ", descDestinatario=" + descDestinatario
				+ ", dataInizio=" + dataInizio + ", dataFine=" + dataFine + ", idFileRendering=" + idFileRendering
				+ ", attoreUpd=" + attoreUpd + "]";
	}

}
