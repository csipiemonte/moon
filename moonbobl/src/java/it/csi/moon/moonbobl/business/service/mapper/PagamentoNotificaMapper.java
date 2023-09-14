/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import it.csi.moon.moonbobl.business.service.impl.dto.EpayNotificaPagamentoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.PagamentoNotifica;

/**
 * Contruttore di oggetto JSON Pagamento
 *  da EpayRichiestaEntity {@code entity}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public class PagamentoNotificaMapper {
	
	private static final String CLASS_NAME = "PagamentoNotificaMapper";
	static ObjectMapper mapper;
	
	public static PagamentoNotifica buildFromEntity(EpayNotificaPagamentoEntity entity) {
		PagamentoNotifica np = new PagamentoNotifica();
		np.setIdPosizioneDebitoria(entity.getIdPosizioneDebitoria());
		np.setImportoPagato(entity.getImportoPagato());
		np.setDataEsitoPagamento(entity.getDataEsitoPagamento());
		if (entity.getDatiTransazionePsp()!=null) {
			Map<String, Object> mapDatiTransazionePSP = retrieveMap(entity.getDatiTransazionePsp());
//			try {
//				printMap(mapDatiTransazionePSP);
//			} catch (Exception e) {	System.out.println("ERRORE: printMap " + e.getMessage()); System.out.println("Continue... "); }
			try {
				np.setIdPsp((String)mapDatiTransazionePSP.get("idPSP"));
			} catch (Exception e) {	System.out.println("PagamentoNotificaMapper ERRORE: idPSP"); e.printStackTrace(); }
			try {
				np.setRagioneSocialePsp((String)mapDatiTransazionePSP.get("ragioneSocialePSP"));
			} catch (Exception e) {	System.out.println("PagamentoNotificaMapper ERRORE: ragioneSocialePSP"); e.printStackTrace(); }
			try {
				np.setDataOraAvvioTransazione(mapDatiTransazionePSP.get("dataOraAvvioTransazione")!=null?
					new Date((Long)mapDatiTransazionePSP.get("dataOraAvvioTransazione")):null);
			} catch (Exception e) {	System.out.println("PagamentoNotificaMapper ERRORE: dataOraAvvioTransazione"); e.printStackTrace(); }
			try {
				np.setImportoTransato(readBigDecimal(mapDatiTransazionePSP.get("importoTransato")));
			} catch (Exception e) {	System.out.println("PagamentoNotificaMapper ERRORE: importoTransato"); e.printStackTrace(); }
			try {
				np.setImportoCommissioni(readBigDecimal(mapDatiTransazionePSP.get("importoCommissioni")));
			} catch (Exception e) {	System.out.println("PagamentoNotificaMapper ERRORE: importoCommissioni"); e.printStackTrace(); }
		}
		return np;
	}

	private static BigDecimal readBigDecimal(Object obj) {
		try {
			if (obj == null)
				return null;
			BigDecimal result = null;
//			System.out.println("PagamentoNotificaMapper::readBigDecimal of " + obj + " is Type of " + obj.getClass().getName() );
			if (obj instanceof Double) {
				System.out.println("Double...");
				Double dObj = (Double) obj;
				result = BigDecimal.valueOf(dObj);
			}
			if (obj instanceof Integer) {
				System.out.println("Integer...");
				Integer iObj = (Integer) obj;
				result = BigDecimal.valueOf(iObj);
			}
			return result;
		} catch (Exception e) {
			System.out.println("PagamentoNotificaMapper::readBigDecimal ERRORE");
			e.printStackTrace();
			return null;
		}
	}

//	private static void printMap(Map<String, Object> map) {
//		System.out.println("PagamentoNotificaMapper::printMap");
//		map.forEach((key, value) -> System.out.println(value==null?key + ":null":(key + ":" + value.getClass().getName() + ":" + value)));
//	}

	private static Map<String, Object> retrieveMap(String strJson) {
		try {
	    	return getMapper().readValue(strJson,
	        		new TypeReference<Map<String,Object>>(){});
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ObjectMapper getMapper() {
		if(mapper == null) {
			mapper = new ObjectMapper();
//			DeserializationConfig config = mapper
//				.getDeserializationConfig()
////				.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
////				.with(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
//				;
//			
//			mapper.setDeserializationConfig(config);
		}
		return mapper;
	}
	
}
