/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.azione;

import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Azione;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

/**
 * Delegate delle azioni di WorkflowServiceImpl che possono essere specializzate per Modulo
 * Attualmente 1 metodo :
 * - generaSalvaRicevutaPdf : genera la ricevuta e la salva in repositoryFile
 * 
 * @author laurent
 *
 */
public interface AzioneService {

	public Long generaSalvaRicevutaPdf(UserInfo user, String datiAzione, Istanza istanza) throws BusinessException;

	public void inviaRicevuta(UserInfo user, String datiAzione, Istanza istanza) throws BusinessException;
	
	public void richiediIntegrazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;
	
	public void richiediRicevutaPagamento(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;
	
	public void richiediOsservazioni(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;
	
	public void richiediUlterioreIntegrazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;
	
	public void respingiConComunicazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;
	
	public void invitoConformareDebiti(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;
	
	public void concludiEsitoPositivo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;

	public void inviaComunicazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;

	public void inviaComunicazioneAvvio(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;

	public void accogli(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;

	public void approva(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;
	
	public void approvaParzialmente(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;

	public void elimina(UserInfo user, Istanza istanza) throws BusinessException;
	
	public void presaAtto(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;
	
	public void riportaInBozza(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;
	
	public void protocollaRicevuta(UserInfo user, Azione azione, Long idIstanza) throws BusinessException;

	public void diniego(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity);
	
	public void accoglimento(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity);

	public void comunicaEsitoPositivo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity);

	public void respingi(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity);

	public void chiudi(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity);

	public void creaRispostaConProtocollo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity);

	public void inviaDocumentazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;
		
	public void inserisciProtocollo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;

	public void protocolla(UserInfo user, Long idIstanza) throws BusinessException;

	public void verificaAnpr(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;

	public void inviaIntegrazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;

	public String assegnaOperatore(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException;

}
