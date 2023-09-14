/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.edilizia.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.util.ObjectUtils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import it.csi.moon.commons.dto.extra.edilizia.PraticaEdilizia;
import it.csi.moon.moonsrv.util.JsonPathUtil;

public class PraticaEdiliziaMapper {
	
	public PraticaEdiliziaMapper() {
		// TODO Auto-generated constructor stub
	}

	public static final String NULL_VALUE = " ";
	public static final List<String> NULL_LIST = new ArrayList<>();
	
	public List<PraticaEdilizia> buildElencoPratiche(String jsonPratica) {
		
		JsonPathUtil jsonPathUtil = new JsonPathUtil(jsonPratica);
		List<PraticaEdilizia> listOfPratiche = new ArrayList<>();
		
		List<Object> ref = JsonPath.using(JsonPathUtil.RETURN_LIST).parse(jsonPratica).read("$.root.pratiche");
	    for (Object item : ref) {	    	
            Map<String, Object> map = (Map<String, Object>) item;               
            String json = new JSONObject(map).toString();            
            jsonPathUtil = new JsonPathUtil(json);            
            PraticaEdilizia pratica = buildPratica(json,".pratica");            
            listOfPratiche.add(pratica);
        }		
		return listOfPratiche;
	}
		
	public PraticaEdilizia buildPratica(String jsonPratica, String root) {
		
		JsonPathUtil<?> jsonPathUtil = new JsonPathUtil(jsonPratica);
		jsonPathUtil.setDefaultIfNull("");
			
		PraticaEdilizia pratica = new PraticaEdilizia();
		
		List<String> paths = new ArrayList<>();
		List<String> labels = new ArrayList<>();
				
		//codice
		String codice = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".codice"));
		
		codice = StringUtils.isNotBlank(codice) ? codice : NULL_VALUE;
		pratica.setCodice(codice);
			
		//primoIntestatario
		paths.add("$.referente.persona.anagrafica.cognome_ragsociale");
		paths.add("$.referente.persona.anagrafica.nome");
		String primoIntestatario = jsonPathUtil.getStringValueByPaths("$"+root+".referenti.referente_coinvolto[?(@.flagIntestPrinc == true)]",paths," ");
		primoIntestatario =  StringUtils.isNotBlank(primoIntestatario) ? primoIntestatario : NULL_VALUE;
		pratica.setPrimoIntestatario(primoIntestatario);
		
		//dataPresentazione
		String dataPresentazione = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".data_presentazione"));
		dataPresentazione = StringUtils.isNotBlank(dataPresentazione) ? dataPresentazione : NULL_VALUE;
		pratica.setDataPresentazione(dataPresentazione);
		
        //descrizioneOpera
		String descrizioneOpera = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".oggetto"));
		descrizioneOpera = StringUtils.isNotBlank(descrizioneOpera) ? descrizioneOpera : NULL_VALUE;
		pratica.setDescrizioneOpera(descrizioneOpera);
		
		//elencoIndirizzi
		paths = new ArrayList<>();
		paths.add("$.indirizzo.descrizione_completa");
//		paths.add("$.indirizzo.codcivicosit");
//		paths.add("$.indirizzo.codviasit");
		
		labels = new ArrayList<>();
		labels.add("");
//		labels.add("Cod. civico:");
//		labels.add("Cod. via:");
//		List<String> indirizziPratica = jsonPathUtil.getListValues("$"+root+".oggetti_territoriali.oggetto_territoriale.ot.anagrafica_ot.ot_indirizzi.ot_indirizzo", paths," - ");
		List<String> indirizziPratica = jsonPathUtil.getListLabelledValues("$"+root+".oggetti_territoriali.oggetto_territoriale.ot.anagrafica_ot.ot_indirizzi.ot_indirizzo", labels,paths," - ");
		indirizziPratica = (indirizziPratica != null) ? indirizziPratica : NULL_LIST;
		pratica.setElencoIndirizzi(indirizziPratica);
		
		if (indirizziPratica.size() == 0)
			pratica.setIndirizzi(NULL_VALUE);
		
				
		//elencoProvvedimenti 	
		paths = new ArrayList<>();
		paths.add("$.tipo_provvedimento.descrizione");
		paths.add("$.numero");
		paths.add("$.data_adozione");
		List<String> provvedimentiAmministrativiPratica = jsonPathUtil.getListValues("$"+root+".atti.atto", paths," - ");
		provvedimentiAmministrativiPratica = (provvedimentiAmministrativiPratica != null) ? provvedimentiAmministrativiPratica : NULL_LIST;
	    pratica.setElencoProvvedimenti(provvedimentiAmministrativiPratica);
	    
	    if (provvedimentiAmministrativiPratica.isEmpty()) {
	    	pratica.setProvvedimenti(NULL_VALUE);
	    }
				
		//numeroProgressivoConservazione
		paths = new ArrayList<>();
		paths.add("$"+root+".dati_archivio.numero");
		paths.add("$"+root+".dati_archivio.data");
		paths.add("$"+root+".dati_archivio.note");
		String numeroProgressivo = jsonPathUtil.getStringValueByPaths(paths, "-");
		numeroProgressivo = StringUtils.isNotBlank(numeroProgressivo) ? numeroProgressivo : NULL_VALUE;
		pratica.setNumeroProgressivoConservazione(numeroProgressivo);
			
		//identificativoRepertorioCartellini
		List<String> idCartellinoList = new ArrayList<>(3);
		List<Object> mtdMaglia = JsonPath.using(jsonPathUtil.RETURN_LIST).parse(jsonPratica).read("$"+root+".metadata.metadato[?(@.nome == 'MAGLIA')]");		
		
	    for (Object item : mtdMaglia) {	    	
            Map<String, Object> map = (Map<String, Object>) item;               
            String json = new JSONObject(map).toString();            				
			Object metadato = Configuration.defaultConfiguration().jsonProvider().parse(json);
			
			if (!ObjectUtils.isEmpty(metadato))
			{
				if (!JsonPath.read(metadato, "$.valore").toString().equals("{}"))
					idCartellinoList.add(JsonPath.read(metadato, "$.valore").toString());
							
			}		
        }
		List<Object> mtdMagliaLet = JsonPath.using(jsonPathUtil.RETURN_LIST).parse(jsonPratica).read("$"+root+".metadata.metadato[?(@.nome == 'MAGLIALET')]");						
	    for (Object item : mtdMagliaLet) {	    	
            Map<String, Object> map = (Map<String, Object>) item;               
            String json = new JSONObject(map).toString();            				
			Object metadato = Configuration.defaultConfiguration().jsonProvider().parse(json);
//			idCartellinoList.add("MAGLIALET: "+JsonPath.read(metadato, "$.valore").toString());   
			if (!ObjectUtils.isEmpty(metadato))
			{
				if (!JsonPath.read(metadato, "$.valore").toString().equals("{}"))
					idCartellinoList.add(JsonPath.read(metadato, "$.valore").toString());
			}
	
        }
		List<Object> mtdMagliaBis = JsonPath.using(jsonPathUtil.RETURN_LIST).parse(jsonPratica).read("$"+root+".metadata.metadato[?(@.nome == 'MAGLIABIS')]");						
	    for (Object item : mtdMagliaBis) {	    	
            Map<String, Object> map = (Map<String, Object>) item;               
            String json = new JSONObject(map).toString();            				
			Object metadato = Configuration.defaultConfiguration().jsonProvider().parse(json);
//			idCartellinoList.add("MAGLIABIS: "+JsonPath.read(metadato, "$.valore").toString()); 
			if (!ObjectUtils.isEmpty(metadato))
			{
				if (!JsonPath.read(metadato, "$.valore").toString().equals("{}"))
					idCartellinoList.add(JsonPath.read(metadato, "$.valore").toString());
			}
	
        }	    	   	    
	    List<String> idCartellinoNotNull  = idCartellinoList.stream().filter(el -> el!=null).collect(Collectors.toList());
	    String identificativoRepCartellini = null;
	    if (idCartellinoNotNull != null && idCartellinoNotNull.size() > 0)
	    {
	        identificativoRepCartellini = String.join("-", idCartellinoNotNull);
	    }
	    else identificativoRepCartellini = NULL_VALUE;
	    pratica.setIdentificativoRepCartellini(identificativoRepCartellini);
	    
	    
		//elencoIndirizzi
		paths = new ArrayList<>();
		paths.add("$.parere.esito.tipologia");
		paths.add("$.parere.esito.numero");
		
		labels = new ArrayList<>();
		labels.add("Tipologia:");
		labels.add("Numero:");

	    	    	   
	    //dataProtocollo
		String dataProtocollo = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".protocollo.data"));
		dataProtocollo = StringUtils.isNotBlank(dataProtocollo) ? dataProtocollo : NULL_VALUE;
//		pratica.setDataProtocollo(dataProtocollo);		
		String numeroProtocollo = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".protocollo.numero"));
		numeroProtocollo =StringUtils.isNotBlank(numeroProtocollo) ? numeroProtocollo : NULL_VALUE;
		
		String protocollo = numeroProtocollo + " - " + dataProtocollo;
		pratica.setProtocollo(protocollo);
		
		//esito pratica
		String esitoPratica = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".esito_pratica.esito"));
		String dataEsitoPratica = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".esito_pratica.data"));
		
		esitoPratica = StringUtils.isNotBlank(esitoPratica) ? esitoPratica : NULL_VALUE;
		dataEsitoPratica = StringUtils.isNotBlank(dataEsitoPratica) ? dataEsitoPratica : NULL_VALUE;
		
		pratica.setEsito(esitoPratica+ " - "+dataEsitoPratica);
	   		
		return pratica;

	}
	

}
