/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.processo;

import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.moonfobl.dto.moonfobl.CompieAzioneResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class ProcessoDelegate_COTO_RIN extends ProcessoDefaultDelegate implements ProcessoServiceDelegate {

	private static final String CLASS_NAME = "ProcessoDelegate_COTO_RIN";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	//
	// inviaIntegrazione - Per COTO_RIN 
	//
	@Override
	public CompieAzioneResponse compieAzione_inviaIntegrazione(UserInfo user, String datiAzione, IstanzaEntity istanza, Workflow workflow) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::compieAzione_inviaIntegrazione] inviaIntegrazione + protocollaAllegati");
		cambiaStatoIstanza(user, datiAzione, istanza, workflow);
		
		CompieAzioneResponse azioneCompiuta = new CompieAzioneResponse();
		azioneCompiuta.setNomeAzione(workflow.getNomeAzione());
		azioneCompiuta.setCodEsitoAzione("ok");			
		return azioneCompiuta;
	}
	
	
//	@Override
//	public DatiAzione getInitData(UserInfo user, Long idIstanza, Workflow workflow) throws BusinessException {
//		
//		DatiAzione datiAzione = new DatiAzione();
//		String initDataNomeClass = "";
//		
//	   if (workflow.getIdAzione().equals(DecodificaAzione.INVIA_INTEGRAZIONE.getIdAzione())) {
//
//			IntegrazioneInitParams initParams = new IntegrazioneInitParams();
//
//			StoricoWorkflowEntity storico = storicoWorkflowDAO.findLastStorico(idIstanza);
//
//			try {
//				ObjectMapper objectMapper = new ObjectMapper();
//				JsonNode jsonNode = objectMapper.readValue((String) storico.getDatiAzione(), JsonNode.class);
//				JsonNode data = jsonNode.get("data");
//
//				if (data.has("testo") && data.get("testo") != null) {
//					initParams.setTesto(data.get("testo").getTextValue());
//				}
//
//				if (data.has("allegati") && data.get("allegati") != null) {
//					initParams.setAllegati((ArrayNode) data.get("allegati"));
//				}
//
//				if (data.has("nome") && data.get("nome") != null) {
//					initParams.setNome(data.get("nome").getTextValue());
//				}
//
//				if (data.has("cognome") && data.get("cognome") != null) {
//					initParams.setCognome(data.get("cognome").getTextValue());
//				}
//
//				if (data.has("codiceFiscale") && data.get("codiceFiscale") != null) {
//					initParams.setCodiceFiscale(data.get("codiceFiscale").getTextValue());
//				}
//
//				if (data.has("email") && data.get("email") != null) {
//					initParams.setEmail(data.get("email").getTextValue());
//				}
//
//				if (data.has("emailCc") && data.get("emailCc") != null) {
//					initParams.setEmailCc((ArrayNode) data.get("emailCc"));
//				}
//									
//
//			} catch (JsonParseException e) {
//				LOG.error("[" + CLASS_NAME + "::insertExtDomandaBuoni] Errore objectMapper.readValue - ",
//						e);
//				throw new BusinessException("JsonParseException");
//			} catch (JsonMappingException e) {
//				LOG.error("[" + CLASS_NAME + "::insertExtDomandaBuoni] Errore objectMapper.readValue - ",
//						e);
//				throw new BusinessException("JsonMappingException");
//			} catch (IOException e) {
//				LOG.error("[" + CLASS_NAME + "::insertExtDomandaBuoni] Errore objectMapper.readValue - ",
//						e);
//				throw new BusinessException("IOException");
//			}
//
//			initDataNomeClass = "it.csi.moon.moonfobl.business.service.impl.initializer.ModIntegrazioneInitializer";
//
//			integrazioneInitializer.initialize(initParams);
//			String datiInit = integrazioneInitializer.getDati(initDataNomeClass);
//			datiAzione.setData(datiInit);
//
//		}
//		return datiAzione;
//	}
	
}
