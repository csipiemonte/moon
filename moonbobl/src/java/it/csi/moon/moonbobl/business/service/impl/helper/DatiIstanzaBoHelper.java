/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.RilevazioneServiziInfanzia;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.Struttura03;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.Struttura36;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.WfProtocolloEdil;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.util.LoggerAccessor;

/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class DatiIstanzaBoHelper {
	
	private final static String CLASS_NAME = "DatiIstanzaHelper";
	protected final static Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	EnteDAO enteDAO;
	
	public DatiIstanzaBoHelper() {
        //must provide autowiring support to inject SpringBean
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	/**
	 * Acquisisce un valore di un parametro del json
	 * 
	 * @param datiIstanza
	 * @param nome del parametro
	 * @return valore del parametro
	 */
	public String getStringValueByKey(String datiIstanza, String key) throws Exception {
		String result = "";
		try {
			log.debug("[" + CLASS_NAME + "::getStringValueByKey] key = "+key);

			JsonNode istanzaJsonNode = readIstanzaData(datiIstanza);
			JsonNode data = istanzaJsonNode.get("data");

			String[] keyElements = key.split("\\."); 
			switch (keyElements.length) {
			  case 1:
				  result = data.get(keyElements[0]).getTextValue();
			    break;
			  case 2:
				  result = data.get(keyElements[0]).get(keyElements[1]).getTextValue();
			    break;
			  case 3:
				  result = data.get(keyElements[0]).get(keyElements[1]).get(keyElements[2]).getTextValue();
			    break;
			}
			


			return result;
		} 
		catch(IOException e) {
			log.debug("[" + CLASS_NAME + "::getStringValueByKey] error:"+ e);
			throw e;
		}
		finally {
			log.debug("[" + CLASS_NAME + "::getStringValueByKey] OUT result:"+result);
		}
	}	
	// Lettura delle due strutture variabile
			
				
	public WfProtocolloEdil getProtocolloEdil(String datiIstanza) throws Exception {
		String result = null;
		try {
			log.debug("[" + CLASS_NAME + "::getProtocolloEdil] BEGIN");
			// JsonNode istanzaNode = readIstanzaData(datiIstanza);
			
			ObjectMapper objectMapper = new ObjectMapper()
					.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JsonNode rootNode = objectMapper.readTree(datiIstanza);

			WfProtocolloEdil protocollo = objectMapper.readValue(rootNode.get("data").toString(), WfProtocolloEdil.class);
					
			return protocollo;
		} 
		catch(IOException e) {
			log.debug("[" + CLASS_NAME + "::getProtocolloEdil] error:"+ e);
			throw e;
		}
		finally {
			log.debug("[" + CLASS_NAME + "::getProtocolloEdil] OUT result:"+result);
		}
	}
	
	
	public int getSommaPerIstanza(String datiIstanza, String nomeParametro) throws Exception {
		int result = 0;
		try {
			log.debug("[" + CLASS_NAME + "::getSommaPerIstanza] nomeParametro = "+ nomeParametro);
			// JsonNode istanzaNode = readIstanzaData(datiIstanza);
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(datiIstanza);
			//JsonNode comuneNode = rootNode.path("data/comune/nome");
			RilevazioneServiziInfanzia rilevazione = objectMapper.readValue(rootNode.get("data").toString(), RilevazioneServiziInfanzia.class);
			if (nomeParametro.equals("numero02"))
			{
				List<Struttura03> list03 = rilevazione.getListStruttura03();
				for (Struttura03 struttura: list03) {
					int valore = struttura.getNumero31012020().intValue();
					result = result + valore;
				}
			}
			
			if (nomeParametro.equals("numero36"))
			{
				List<Struttura36> list36 = rilevazione.getListStruttura36();
				for (Struttura36 struttura: list36) {
					int valore = struttura.getNumero31012020().intValue();
					result = result + valore;
				}
			}
			
			if (nomeParametro.equals("servizi02"))
			{
				List<Struttura03> list02 = rilevazione.getListStruttura03();
				result = result + list02.size();
			}
			
			if (nomeParametro.equals("servizi36"))
			{
				List<Struttura36> list36 = rilevazione.getListStruttura36();
				result = result + list36.size();
			}
			
			return result;
		} 
		catch(IOException e) {
			log.debug("[" + CLASS_NAME + "::getSommaPerIstanza] error:"+ e);
			throw e;
		}
		finally {
			log.debug("[" + CLASS_NAME + "::getSommaPerIstanza] OUT result:"+result);
		}
	}

	public String getNomeComune(String datiIstanza) throws Exception {
		String result = null;
		try {
			log.debug("[" + CLASS_NAME + "::getNomeComune] BEGIN");
			// JsonNode istanzaNode = readIstanzaData(datiIstanza);
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(datiIstanza);
			//JsonNode comuneNode = rootNode.path("data/comune/nome");
			RilevazioneServiziInfanzia rilevazione = objectMapper.readValue(rootNode.get("data").toString(), RilevazioneServiziInfanzia.class);
			
			result = rilevazione.getComune().getNome();
			
			return result;
		} 
		catch(IOException e) {
			log.debug("[" + CLASS_NAME + "::getNomeComune] error:"+ e);
			throw e;
		}
		finally {
			log.debug("[" + CLASS_NAME + "::getNomeComune] OUT result:"+result);
		}
	}
	
	public String getNomeEnte(String datiIstanza) throws Exception {
		String result = null;
		try {
			log.debug("[" + CLASS_NAME + "::getNomeEnte] BEGIN");
		
		
			JsonNode istanzaJsonNode = readIstanzaData(datiIstanza);
			JsonNode data = istanzaJsonNode.get("data");
			
			return  data.get("asr").get("nome").getTextValue();
		} 
		catch(IOException e) {
			log.debug("[" + CLASS_NAME + "::getNomeComune] error:"+ e);
			throw e;
		}
		finally {
			log.debug("[" + CLASS_NAME + "::getNomeComune] OUT result:"+result);
		}
	}
	
	
	public String getNomeEnte(String filtro, String datiIstanza) throws Exception {
		String result = "";
		try {

			log.debug("[" + CLASS_NAME + "::getNomeEnte] BEGIN");
			log.debug("[" + CLASS_NAME + "::filtro " + filtro);
						
			if (filtro != null && !filtro.equals(""))
			{
				String[] arrOfParams = filtro.split("@@");
				List<String> listParams = Arrays.asList(arrOfParams);
				if (listParams.size() > 0)
				{
					for (int i = 0; i < listParams.size(); i++) {
					    String param = listParams.get(i);
					    String[] arrKeyValue = param.split("=");
					    if (arrKeyValue[0].equals("asr")) {					    	
					    	JsonNode istanzaJsonNode = readIstanzaData(datiIstanza);
							JsonNode data = istanzaJsonNode.get("data");
							result = data.get("asr").get("nome").getTextValue();					    	
					    }
					    else if (arrKeyValue[0].equals("CCR")) {				    					
					    	result = enteDAO.findByCodice(arrKeyValue[1]).getNomeEnte();
					    }
					}
				}
			}

			return result;
			
		} catch (IOException e) {
			log.debug("[" + CLASS_NAME + "::getNomeComune] error:" + e);
			throw e;
		} finally {
			log.debug("[" + CLASS_NAME + "::getNomeComune] OUT result:" + result);
		}
	}
	
	public RilevazioneServiziInfanzia getRilevazione(String datiIstanza) throws Exception {
		String result = null;
		try {
			log.debug("[" + CLASS_NAME + "::getRilevazione] BEGIN");
			// JsonNode istanzaNode = readIstanzaData(datiIstanza);
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(datiIstanza);

			RilevazioneServiziInfanzia rilevazione = objectMapper.readValue(rootNode.get("data").toString(), RilevazioneServiziInfanzia.class);
					
			return rilevazione;
		} 
		catch(IOException e) {
			log.debug("[" + CLASS_NAME + "::getValue] error:"+ e);
			throw e;
		}
		finally {
			log.debug("[" + CLASS_NAME + "::getValue] OUT result:"+result);
		}
	}

	public String getOperatoreDaAssegnare(String datiIstanza) throws Exception {
		String result = null;
		try {
			log.debug("[" + CLASS_NAME + "::getOperatoreDaAssegnare] BEGIN");
			
			JsonNode istanzaJsonNode = readIstanzaData(datiIstanza);
			JsonNode data = istanzaJsonNode.get("data");
			result = data.get("operatore").get("codiceFiscale").getTextValue();	
			
			return result;
		} 
		catch(IOException e) {
			log.debug("[" + CLASS_NAME + "::getOperatoreDaAssegnare] error:"+ e);
			throw e;
		}
		finally {
			log.debug("[" + CLASS_NAME + "::getOperatoreDaAssegnare] OUT result:"+result);
		}
	}
	
	/**
	 * Legge i dati dell istanza in nell oggetto istanzaJsonNode
	 * 
	 * @param istanza
	 * @throws Exception
	 */
	public static JsonNode readIstanzaData(Istanza istanza) throws Exception {
		try {
			log.debug("[" + CLASS_NAME + "::readIstanzaData] IN istanza: "+istanza);
			if (istanza==null || istanza.getData()==null) {
				new Exception("InputParamIstanzaDataNull");
			}
			return readIstanzaData(istanza.getData().toString());
		} finally {
			log.debug("[" + CLASS_NAME + "::readIstanzaData] END");
		}
	}
	protected static JsonNode readIstanzaData(String datiIstanza) throws Exception {
		try {
			log.debug("[" + CLASS_NAME + "::readIstanzaData(str)] IN datiIstanza: "+datiIstanza);
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			return objectMapper.readValue(datiIstanza, JsonNode.class);

		} catch (IOException e) {
		    e.printStackTrace();
		    log.error("[" + CLASS_NAME + "::readIstanzaData(str)] ERROR "+e.getMessage());
		    throw e;
		} finally {
			log.debug("[" + CLASS_NAME + "::readIstanzaData(str)] END");
		}
	}
	
	public JsonNode getIstanzaData(String datiIstanza) throws Exception {
		try {
			log.debug("[" + CLASS_NAME + "::getIstanzaData] IN datiIstanza: "+datiIstanza);
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			return objectMapper.readValue(datiIstanza, JsonNode.class);

		} catch (IOException e) {
		    e.printStackTrace();
		    log.error("[" + CLASS_NAME + "::getIstanzaData] ERROR "+e.getMessage());
		    throw e;
		} finally {
			log.debug("[" + CLASS_NAME + "::getIstanzaData] END");
		}
	}
	
	/**
	 * Aggiorna il field 'submit' nel json delle submission FormIo a 'false'
	 * 
	 * @param datiIstanza
	 * @return datiIstanza aggiornata con element "submit":false
	 */
	public String updateSubmitFalse(String datiIstanza) throws Exception {
		String result = null;
		try {
			log.debug("[" + CLASS_NAME + "::updateSubmitFalse] IN datiIstanza: "+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			((ObjectNode)istanzaNode.get("data")).put("submit", false);	
			result = istanzaNode.toString();
			return result;
		} finally {
			log.debug("[" + CLASS_NAME + "::updateSubmitFalse] OUT result:"+result);
		}
	}
}
