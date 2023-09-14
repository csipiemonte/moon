/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.processo;

import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.FruitoreDatiAzioneEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.moonfobl.business.service.impl.dao.FruitoreDatiAzioneDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.IntegrazioneInitParams;
import it.csi.moon.moonfobl.dto.moonfobl.CompieAzioneResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class ProcessoDelegate_COSMO extends ProcessoDefaultDelegate implements ProcessoServiceDelegate {

	private static final String CLASS_NAME = "ProcessoDelegate_COSMO";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private JsonNode confPostAzioni = null;
	private JsonNode confAllegatiAzione = null;
	
	@Autowired
	FruitoreDatiAzioneDAO fruitoreDatiAzioneDAO;
	
	//
	// inviaIntegrazione - COSMO
	//
	@Override
	public CompieAzioneResponse compieAzione_inviaIntegrazione(UserInfo user, String datiAzione, IstanzaEntity istanza, Workflow workflow) throws BusinessException {
		cambiaStatoIstanza(user, datiAzione, istanza, workflow);
		
		CompieAzioneResponse azioneCompiuta = new CompieAzioneResponse();
		azioneCompiuta.setNomeAzione(workflow.getNomeAzione());
		azioneCompiuta.setUrl("/istanze/"+istanza.getIdIstanza()+"/integrazione-cosmo");
		azioneCompiuta.setCodEsitoAzione("ok");	
		
		return azioneCompiuta;
		//INVIO SEGNALAZIONE COSMO
		//moonsrvDAO.inviaRispostaIntegrazioneCosmo(istanza.getIdIstanza());
		
	}
	
	@Override
	public DatiAzione getInitData(UserInfo user, Long idIstanza, Workflow workflow, String ipAddress) throws BusinessException {

		DatiAzione datiAzione = new DatiAzione();
		String initDataNomeClass = "";

		if (workflow.getIdAzione().equals(DecodificaAzione.INVIA_INTEGRAZIONE.getIdAzione())) {
			IntegrazioneInitParams initParams = new IntegrazioneInitParams();
			StoricoWorkflowEntity storico = storicoWorkflowDAO.findLastStoricoAzione(idIstanza,DecodificaAzione.RICHIEDI_INTEGRAZIONE.getIdAzione());
			initDataNomeClass = "it.csi.moon.moonfobl.business.service.impl.initializer.cosmo.CosmoIntegrazioneInitializer";
			
			FruitoreDatiAzioneEntity dati = fruitoreDatiAzioneDAO.findByIdStoricoWorkflow(storico.getIdStoricoWorkflow());	
			initParams = setInitParams(initParams,dati);						
			integrazioneInitializer.initialize(initParams);
			
			String datiInit = integrazioneInitializer.getDati(initDataNomeClass);
			datiAzione.setData(datiInit);
		}
		return datiAzione;
	}
	
	private IntegrazioneInitParams setInitParams(IntegrazioneInitParams initParams, FruitoreDatiAzioneEntity dati) {		
		initParams.setTesto(dati.getDatiAzione());	
		try {			
			confPostAzioni = readConfJson(dati.getPostAzioni());			
			confAllegatiAzione = readConfJson(dati.getAllegatiAzione());			
			ArrayNode nodeEmail = getNodeEmail();
			ArrayNode nodeAllegati = getNodeAllegati(dati);			
			initParams.setEmailCc(nodeEmail);
			initParams.setAllegati(nodeAllegati);			
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::setInitParams] ERROR "+e.getMessage());
		}
		
		return initParams;
	}
	
	
	private ArrayNode getNodeEmail() {		
		String email = "";
		String emailCc = "";		
		ObjectMapper mapperCc = new ObjectMapper()
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);			
		JsonNode emailCcNode = JsonNodeFactory.instance.objectNode();									
		ArrayNode emailCcArrayNode = mapperCc.createArrayNode();
					
		Iterator<JsonNode> it = confPostAzioni.iterator();
		while (it.hasNext()) {
			JsonNode postAzione = it.next();
		    if (postAzione.has("postAzioneType") && postAzione.get("postAzioneType").asText().equals("SEND_EMAIL")) {			    	
		    	email = postAzione.has("to") ? postAzione.get("to").asText():"";			    	
		    	emailCc = postAzione.has("cc") ? postAzione.get("cc").asText():"";			    	
			    ((ObjectNode) emailCcNode).put("email",emailCc);
			    emailCcArrayNode.add(emailCcNode);
		    }			 
		}								
		return emailCcArrayNode;
	}
	
	private ArrayNode getNodeAllegati(FruitoreDatiAzioneEntity dati) {

		ObjectMapper mapperAllegati = new ObjectMapper()
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);														
	
		ArrayNode allegatiOuterArrayNode = mapperAllegati.createArrayNode();		
		Iterator<JsonNode> it = confAllegatiAzione.iterator();
		
		Long idIstanza = dati.getIdIstanza();
		Long idStoricoWorkflow = dati.getIdStoricoWorkflow();
		
		while (it.hasNext()) {
							
			JsonNode allegato = it.next();
			String url = null;
			String nomeFile = allegato.has("nomeFile") ? allegato.get("nomeFile").asText(): "";			
			RepositoryFileEntity entity = repositoryFileDAO.findByNomeFileIstanzaIdStWf(nomeFile, idIstanza, idStoricoWorkflow);
			
			if (allegato.has("refUrl") && allegato.get("refUrl").asText().length() > 0) {
				url = allegato.get("refUrl").asText();
			}
			
			JsonNode allegatiInnerNode = JsonNodeFactory.instance.objectNode();			
		    ((ObjectNode) allegatiInnerNode).put("storage","url");
		    ((ObjectNode) allegatiInnerNode).put("name","");
//		    ((ObjectNode) allegatiInnerNode).put("url","/moonfobl/restfacade/be/file/notifica?baseUrl=http%3A%2F%2Flocalhost%3A10110%2Fproject&project=&form=/test-71f8d023-6c30-4fd1-8c73-3af893449a69.pdf");
		    ((ObjectNode) allegatiInnerNode).put("url","/moonfobl/restfacade/be/file/notifica/fruitore?baseUrl="+url+"&form="+nomeFile+"&idIstanza="+idIstanza+"&idStoricoWorkflow="+idStoricoWorkflow);
		    ((ObjectNode) allegatiInnerNode).put("type",allegato.has("contentType") ? allegato.get("contentType").asText(): "");
		    ((ObjectNode) allegatiInnerNode).put("size",entity.getLunghezza());
		    ((ObjectNode) allegatiInnerNode).put("originalName",nomeFile);
		    
			ArrayNode allegatiInnerArrayNode = mapperAllegati.createArrayNode();
		    allegatiInnerArrayNode.add(allegatiInnerNode);
			JsonNode allegatifatherNode = JsonNodeFactory.instance.objectNode();
		    ((ObjectNode) allegatifatherNode).put("allegato",allegatiInnerArrayNode);
		    ((ObjectNode) allegatifatherNode).put("descrizioneAllegato",entity.getDescrizione());
		    
		    allegatiOuterArrayNode.add(allegatifatherNode);
		}			
		return allegatiOuterArrayNode;
	}
	
	
	private JsonNode readConfJson(String strJson) throws Exception {
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::readConfJson] IN strJson: "+strJson);
			}

			ObjectMapper objectMapper = new ObjectMapper()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			JsonNode result = objectMapper.readValue(strJson, JsonNode.class);

			return result;
		} catch (IOException e) {
		    LOG.error("[" + CLASS_NAME + "::readConfJson] ERROR "+e.getMessage());
		    throw e;
		} finally {
			LOG.debug("[" + CLASS_NAME + "::readConfJson] END");
		}
	}

}
