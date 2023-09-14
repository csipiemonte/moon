/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.wf.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.cosmo.callback.v1.dto.AggiornaStatoPraticaRequest;
import it.csi.cosmo.callback.v1.dto.Esito;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.LogPraticaCosmo;
import it.csi.moon.commons.entity.CosmoLogPraticaEntity;
import it.csi.moon.commons.mapper.LogPraticaCosmoMapper;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.impl.dao.CosmoLogPraticaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.FruitoreDatiAzioneDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.wf.CosmoDAO;
import it.csi.moon.moonsrv.business.service.wf.CosmoService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business del Notificatore
 * 
 * @author Laurent
 */

@Component
public class CosmoServiceImpl implements CosmoService {
	
	private static final String CLASS_NAME = "CosmoServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final String CODICE_SEGNALE_INTEGRAZIONE = "RICHIESTA_INTEGRAZIONE_DOCUMENTI";
	
	@Autowired
	IstanzeService istanzeService;
	@Autowired
	CosmoLogPraticaDAO cosmoLogPraticaDAO;
	@Autowired
	PrintIstanzeService printIstanzeService;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	FruitoreDatiAzioneDAO fruitoreDatiAzioneDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	@Qualifier("apimint")  // via APIM internet con token
	CosmoDAO cosmoDAO;

	@Override
	public String creaPraticaEdAvviaProcesso(Long idIstanza) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] IN idIstanza = " + idIstanza);
		return creaPraticaEdAvviaProcesso(istanzeService.getIstanzaById(idIstanza,"attributiCOSMO"));
	}

	@Override
	public String creaPraticaEdAvviaProcesso(Istanza istanza) throws BusinessException {
		LOG.debug("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] IN istanza = " + istanza);
		return new CosmoDelegateFactory().getDelegate(istanza).creaPraticaEdAvviaProcesso();
	}

	@Override
	public Esito callbackPutStatoPraticaV1(String idPratica, AggiornaStatoPraticaRequest pratica) {
		LOG.debug("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] IN idPratica = " + idPratica);
		LOG.debug("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] IN pratica.getId() = " + pratica.getId());
		//
		List<CosmoLogPraticaEntity> lastCalls = cosmoLogPraticaDAO.findByIdPratica(pratica.getId());
		if (lastCalls.isEmpty()) {
			LOG.error("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] cosmoLogPratica NotFound pratica.getId()=" + pratica.getId());
			throw new BusinessException("cosmoLogPratica non trovato.","MOONSRV-30507");
		}
		if (lastCalls.size()!=1) {
			LOG.error("[" + CLASS_NAME + "::callbackPutStatoPraticaV1] cosmoLogPratica non univoca. pratica.getId()=" + pratica.getId());
			throw new BusinessException("cosmoLogPratica non univoca. NOT IMPLEMENTED multi log with idx","MOONSRV-30508");
		}
		CosmoLogPraticaEntity cosmoLogPratica = lastCalls.get(0);
		//
		Istanza istanza = istanzeService.getIstanzaById(cosmoLogPratica.getIdIstanza(),"attributiCOSMO");
		return new CosmoDelegateFactory().getDelegate(istanza).callbackPutStatoPraticaV1(pratica, cosmoLogPratica);
	}
	
	@Override
	public List<LogPraticaCosmo> getElencoLogPraticaByIdIstanza(Long idIstanza) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getElencoLogPraticaByIdIstanza] IN idIstanza = " + idIstanza);
			//
			List<LogPraticaCosmo> result = cosmoLogPraticaDAO.findByIdIstanza(idIstanza).stream()
				.map(LogPraticaCosmoMapper::buildFromEntity)
				.collect(Collectors.toList());
			return result;
		} catch (DAOException dao) {
			LOG.warn("[" + CLASS_NAME + "::getElencoLogPraticaByIdIstanza] Errore generico servizio getElencoLogPraticaByIdIstanza for idIstanza=" + idIstanza);
			throw new ServiceException(dao);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoLogPraticaByIdIstanza] Errore generico servizio getElencoLogPraticaByIdIstanza", ex);
			throw new ServiceException("Errore generico servizio LogPraticaCosmo");
		} 
	}
	
	@Override
	public byte[] getAllegato(String idPratica) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getAllegato] IN idPratica = " + idPratica);
			//
			byte[] result = cosmoDAO.getContenuto(idPratica);
			return result;
		} catch (DAOException dao) {
			LOG.warn("[" + CLASS_NAME + "::getAllegato] Errore generico servizio getAllegato for idPratica=" + idPratica);
			throw new ServiceException(dao);
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getAllegato] Errore generico servizio getAllegato", ex);
			throw new ServiceException("Errore generico servizio getAllegato");
		} 
	}
	
	@Override
	public String inviaRispostaIntegrazione(Long idIstanza) throws BusinessException {
		 LOG.debug("[" + CLASS_NAME + "::inviaRispostaIntegrazione] IN id istanza = " + idIstanza);
		 
	     Istanza istanza = istanzeService.getIstanzaById(idIstanza,"attributiCOSMO");		
		 return new CosmoDelegateFactory().getDelegate(istanza).inviaIntegrazione();	
	}
	
	
}
