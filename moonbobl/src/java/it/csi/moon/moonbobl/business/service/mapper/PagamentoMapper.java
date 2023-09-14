/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper;

import java.io.IOException;
import java.math.BigDecimal;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import it.csi.moon.moonbobl.business.service.impl.dto.EpayRichiestaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.extra.epay.IUVChiamanteEsternoRequest;
import it.csi.moon.moonbobl.dto.moonfobl.Pagamento;

/**
 * Contruttore di oggetto JSON Pagamento
 *  da EpayRichiestaEntity {@code entity}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public class PagamentoMapper {
	
	private static final String CLASS_NAME = "PagamentoMapper";
	static ObjectMapper mapper;
	
	public static Pagamento buildFromEpayRichiestaEntity(EpayRichiestaEntity entity) {

			Pagamento obj = new Pagamento();
			obj.setIdEpay(entity.getIdEpay());
			obj.setIdIstanza(entity.getIdIstanza());
			obj.setIdModulo(entity.getIdModulo());
			obj.setIdTipologiaEpay(entity.getIdTipologiaEpay());
			obj.setIdStoricoWorkflow(entity.getIdStoricoWorkflow());
			obj.setRichiesta(entity.getRichiesta());
			obj.setIuv(entity.getIuv());
			obj.setCodiceAvviso(entity.getCodiceAvviso());
			obj.setImporto(retrieveImporto(entity));
			obj.setDataInserimento(entity.getDataIns());
			obj.setDataAnnullamento(entity.getDataDel());
			obj.setNotifica(null);
			
			return obj;
	}

	private static BigDecimal retrieveImporto(EpayRichiestaEntity entity) {
		try {
			return getMapper().readValue(entity.getRichiesta(), IUVChiamanteEsternoRequest.class).getImporto();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ObjectMapper getMapper() {
		if(mapper == null) {
			mapper = new ObjectMapper();
			SerializationConfig config = mapper
				.getSerializationConfig()
				.withSerializationInclusion(Inclusion.NON_EMPTY)
				.without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
			mapper.setSerializationConfig(config);
		}
		return mapper;
	}
}
