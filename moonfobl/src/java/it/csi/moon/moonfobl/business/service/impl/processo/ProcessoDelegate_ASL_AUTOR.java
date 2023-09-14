/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.processo;

import org.apache.log4j.Logger;

import it.csi.moon.commons.dto.DatiAzione;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Workflow;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.StoricoWorkflowEntity;
import it.csi.moon.commons.util.decodifica.DecodificaAzione;
import it.csi.moon.moonfobl.business.service.impl.dto.IntegrazioneInitParams;
import it.csi.moon.moonfobl.dto.moonfobl.CompieAzioneResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class ProcessoDelegate_ASL_AUTOR extends ProcessoDefaultDelegate implements ProcessoServiceDelegate {

	private static final String CLASS_NAME = "ProcessoDelegate_ASL_AUTOR";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	//
	// inviaIntegrazione - ASL_AUTOR
	//
	@Override
	public CompieAzioneResponse compieAzione_inviaIntegrazione(UserInfo user, String datiAzione, IstanzaEntity istanza, Workflow workflow) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::compieAzione_inviaIntegrazione] inviaIntegrazione + protocollaAllegati");
		
		//TO_CHECK check se integrazione con email o non email
		cambiaStatoIstanza(user, datiAzione, istanza, workflow);
		
		CompieAzioneResponse azioneCompiuta = new CompieAzioneResponse();
		azioneCompiuta.setNomeAzione(workflow.getNomeAzione());
		azioneCompiuta.setCodEsitoAzione("ok");			
		return azioneCompiuta;
	}
	
	@Override
	public DatiAzione getInitData(UserInfo user, Long idIstanza, Workflow workflow, String ipAddress) throws BusinessException {

		DatiAzione datiAzione = new DatiAzione();
		String initDataNomeClass = "";

		if (workflow.getIdAzione().equals(DecodificaAzione.INVIA_INTEGRAZIONE.getIdAzione())) {
			IntegrazioneInitParams initParams = new IntegrazioneInitParams();
			StoricoWorkflowEntity storico = storicoWorkflowDAO.findLastStorico(idIstanza)
					.orElseThrow(ItemNotFoundBusinessException::new);
			initDataNomeClass = "it.csi.moon.moonfobl.business.service.impl.initializer.ModIntegrazioneInitializer";
			integrazioneInitializer.initialize(getInitParams(initParams, storico.getDatiAzione()));
			String datiInit = integrazioneInitializer.getDati(initDataNomeClass);
			datiAzione.setData(datiInit);
		}
		return datiAzione;
	}

}
