/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.processo;


import java.util.Optional;
import java.util.regex.Pattern;

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

public class ProcessoDelegate_COTO_RESID extends ProcessoDefaultDelegate implements ProcessoServiceDelegate {

	private static final String CLASS_NAME = "ProcessoDelegate_COTO_RESID";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	//
	// inviaIntegrazione - Per COTO_RESID con email
	//
	@Override
	public CompieAzioneResponse compieAzione_inviaIntegrazione(UserInfo user, String datiAzione, IstanzaEntity istanza, Workflow workflow) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::compieAzione_inviaIntegrazione] inviaIntegrazioneConEmail");
		inviaIntegrazioneConEmail(user, datiAzione, istanza, workflow);
		
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
			initDataNomeClass = "it.csi.moon.moonfobl.business.service.impl.initializer.coto.dem.ModIntegrazioneInitializer";
			integrazioneInitializer.initialize(getInitParams(initParams, storico.getDatiAzione()));
			String datiInit = integrazioneInitializer.getDati(initDataNomeClass);
			datiInit = modificaContestoBoToFo(datiInit);
			datiAzione.setData(datiInit);
		}

		return datiAzione;
	}
	
	public static String modificaContestoBoToFo(String jsonData) {

		//LOG.debug("[" + CLASS_NAME + "::modificaContestoFoToBo] IN jsonData"+ jsonData);
		try {
			Pattern p = Pattern.compile("/moonbobl/", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			String result = p.matcher(jsonData).replaceAll("/moonfobl/");
			
			String REGEX = "https://[a-zA-Z0-9\\-]+\\.patrim\\.csi\\.it/";
			Pattern pvh = Pattern.compile(REGEX, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			result = pvh.matcher(result).replaceAll("/");	
			
			String REGEX2 = "https://[a-zA-Z0-9\\-]+\\.csi\\.it/";
			Pattern pvh2 = Pattern.compile(REGEX2, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			result = pvh2.matcher(result).replaceAll("/");
						
			return result;
		} catch (Exception e) {			
			//LOG.debug("[" + CLASS_NAME + "::modificaContestoFoToBo] Errore in sostituzione contesto");
			return jsonData;
		}
	}
}
