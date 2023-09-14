/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.edilizia.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.edilizia.PraticaEdilizia;
import it.csi.moon.moonsrv.business.service.edilizia.GestionePraticheService;
import it.csi.moon.moonsrv.business.service.edilizia.mapper.PraticaEdiliziaMapper;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.coto.GestionePraticheApiRestDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.util.JsonPathUtil;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business Pratiche Edilizie
 * 
 * @author Danilo
 *
 * @since 1.0.0 
 */
@Component
//@Qualifier("RS")
public class GestionePraticheServiceRSImpl implements GestionePraticheService {
	
	private static final String CLASS_NAME = "GestionePraticheServiceRSImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	private GestionePraticheApiRestDAO gestionePraticheApiRestDAO;
	
	
	@Override
	public PraticaEdilizia getPratica(Integer registro, Integer progressivo, Integer anno)
			throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getPratica] BEGIN IN registro/progressivo/anno="+registro+"/"+progressivo+"/"+anno);			
			String jsonOutput = gestionePraticheApiRestDAO.getJsonPratica(registro, progressivo,anno);		
			
			if(!JsonPathUtil.checkPath(jsonOutput, "$.root")) throw new ItemNotFoundBusinessException("Pratica non presente");
			PraticaEdilizia result = new PraticaEdiliziaMapper().buildPratica(jsonOutput,".root.pratiche.pratica");
		
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getPratica] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getPratica] Exception " + e.getMessage());
	    	throw new BusinessException("getPratica non trovata ");
		}
	}
	
	@Override
	public List<PraticaEdilizia> getPratiche(Integer registro, Integer progressivo, Integer anno)
			throws BusinessException, ItemNotFoundBusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getPratiche] BEGIN IN registro/progressivo/anno="+registro+"/"+progressivo+"/"+anno);			
			String jsonOutput = gestionePraticheApiRestDAO.getJsonPratica(registro, progressivo,anno);		
			
			if(!JsonPathUtil.checkPath(jsonOutput, "$.root")) throw new ItemNotFoundBusinessException("Pratiche non presenti");
			List<PraticaEdilizia> result = new PraticaEdiliziaMapper().buildElencoPratiche(jsonOutput);
		
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getPratiche] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getPratiche] Exception " + e.getMessage());
	    	throw new BusinessException("getPratiche non trovate");
		}
	}
	

	@Override
	public String getPraticaJson(Integer registro, Integer progressivo, Integer anno) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getPraticaJson] BEGIN IN registro/progressivo/anno="+registro+"/"+progressivo+"/"+anno);					
			String jsonOutput = gestionePraticheApiRestDAO.getJsonPratica(registro, progressivo,anno);	
			if(!JsonPathUtil.checkPath(jsonOutput, "$.root")) throw new ItemNotFoundBusinessException("Pratica non presente");
			return jsonOutput;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getPraticaJson] BusinessException " + be.getMessage());
			throw be;
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getPraticaJson] Exception " + e.getMessage());
	    	throw new BusinessException("getPraticaJson non trovata generico");
		}
	}


   

}
