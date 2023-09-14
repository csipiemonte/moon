/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.mapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.PagamentoNotifica;
import it.csi.moon.commons.entity.EpayNotificaPagamentoEntity;

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
			if (mapDatiTransazionePSP!=null) {
//				try {
//					printMap(mapDatiTransazionePSP);
//				} catch (Exception e) {	System.out.println("ERRORE: printMap " + e.getMessage()); System.out.println("Continue... "); }
				np.setIdPsp(readString(mapDatiTransazionePSP, "idPSP"));
				np.setRagioneSocialePsp(readString(mapDatiTransazionePSP, "ragioneSocialePSP"));
				np.setDataOraAvvioTransazione(readDateFromLong(mapDatiTransazionePSP, "dataOraAvvioTransazione"));
				np.setImportoTransato(readBigDecimal(mapDatiTransazionePSP, "importoTransato"));
				np.setImportoCommissioni(readBigDecimal(mapDatiTransazionePSP, "importoCommissioni"));
			}
		}
		return np;
	}

	private static Date readDateFromLong(Map<String, Object> mapDatiTransazionePSP, String key) {
		try {
			Object obj = mapDatiTransazionePSP.get(key);
			if (obj == null)
				return null;
			Date result = new Date((Long)obj);
//			System.out.println("PagamentoNotificaMapper::readString of " + obj + " is Type of " + obj.getClass().getName() );
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	private static String readString(Map<String, Object> mapDatiTransazionePSP, String key) {
		try {
			Object obj = mapDatiTransazionePSP.get(key);
			if (obj == null)
				return null;
			String result = (String) obj;
//			System.out.println("PagamentoNotificaMapper::readString of " + obj + " is Type of " + obj.getClass().getName() );
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	private static BigDecimal readBigDecimal(Map<String, Object> mapDatiTransazionePSP, String key) {
		try {
			Object obj = mapDatiTransazionePSP.get(key);
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
