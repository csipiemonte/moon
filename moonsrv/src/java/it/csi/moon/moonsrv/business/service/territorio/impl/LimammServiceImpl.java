/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.territorio.regp.ComuneLA;
import it.csi.moon.commons.dto.extra.territorio.regp.ProvinciaLA;
import it.csi.moon.commons.dto.extra.territorio.regp.RegioneLA;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.territorio.LimammDAO;
import it.csi.moon.moonsrv.business.service.territorio.LimammService;
import it.csi.moon.moonsrv.business.service.territorio.mapper.regp.ComuneLAMapper;
import it.csi.moon.moonsrv.business.service.territorio.mapper.regp.ProvinciaLAMapper;
import it.csi.moon.moonsrv.business.service.territorio.mapper.regp.RegioneLAMapper;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.csi.territorio.svista.limamm.ente.cxfclient.Provincia;
import it.csi.territorio.svista.limamm.ente.cxfclient.Regione;

@Component
public class LimammServiceImpl implements LimammService {

	private static final String CLASS_NAME = "LimammServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	LimammDAO limammDao;
	
	//
	// REGIONI
	@Override
	public List<RegioneLA> getRegioni() {
		List<RegioneLA> result = new ArrayList<>();
		try {			
			result = limammDao.getRegioni().stream()
						.map(RegioneLAMapper::remap)
						.collect(Collectors.toList());
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getRegioni] DAOException");
			throw new BusinessException(daoe);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRegioni] Errore generico servizio getRegioni", e);
			throw new BusinessException("Errore generico servizio elenco Regioni");
		}
	}
	@Override
    public RegioneLA getRegioneById(Long idRegione) {
		RegioneLA result = null;
		try {			
			result = RegioneLAMapper.remap(limammDao.getRegioneById(idRegione));
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getRegioneById] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getRegioneById] DAOException");
			throw new BusinessException(daoe);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRegioneById] Errore generico servizio getRegioneById", e);
			throw new BusinessException("Errore generico servizio getRegioneById");
		}
	}
	@Override
	public RegioneLA getRegioneByIstat(String istat) {
		RegioneLA result = null;
		try {
			result = RegioneLAMapper.remap(limammDao.getRegioneByIstat(istat));
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getRegioneByIstat] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getRegioneByIstat] DAOException");
			throw new BusinessException(daoe);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRegioneByIstat] Errore generico servizio getRegioneByIstat", e);
			throw new BusinessException("Errore generico servizio getRegioneByIstat");
		}
	}

	
	//
	// PROVINCE
	@Override
	public List<ProvinciaLA> getProvince() {
		List<ProvinciaLA> result = new ArrayList<>();
		try {
			result = limammDao.getProvince().stream()
						.map(ProvinciaLAMapper::remap)
						.collect(Collectors.toList());
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getProvince] DAOException");
			throw new BusinessException(daoe);
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvince] Errore generico servizio getProvince", e);
			throw new BusinessException("Errore generico servizio elenco Province");
	    }
	}
	@Override
    public ProvinciaLA getProvinciaById(Long idProvincia) {
		ProvinciaLA result = null;
		try {
			result = ProvinciaLAMapper.remap(limammDao.getProvinciaById(idProvincia));
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaById] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaById] DAOException");
			throw new BusinessException(daoe);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaById] Errore generico servizio getProvinciaById", e);
			throw new BusinessException("Errore generico servizio getProvinciaById");
		}
	}
	@Override
	public ProvinciaLA getProvinciaByIstat(String istat) {
		ProvinciaLA result = null;
		try {
			result = ProvinciaLAMapper.remap(limammDao.getProvinciaByIstat(istat));
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] DAOException");
			throw new BusinessException(daoe);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] Errore generico servizio getProvinciaByIstat", e);
			throw new BusinessException("Errore generico servizio getProvinciaByIstat");
		}
	}
	@Override
    public List<ProvinciaLA> getProvinceByRegione(Long idRegione) {
		List<ProvinciaLA> result = new ArrayList<>();
		try {
			result = limammDao.getProvincePerIdRegione(idRegione).stream()
						.map(ProvinciaLAMapper::remap)
						.collect(Collectors.toList());
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByRegione] DAOException");
			throw new BusinessException(daoe);
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByRegione] Errore generico servizio getProvinceByRegione", e);
			throw new BusinessException("Errore generico servizio getProvinceByRegione");
	    }
	}
	@Override
    public List<ProvinciaLA> getProvinceByIstatRegione(String istatRegione) {
		List<ProvinciaLA> result = new ArrayList<>();
		try {
			Regione reg = limammDao.getRegioneByIstat(istatRegione);
			result = limammDao.getProvincePerIdRegione(reg.getId()).stream()
						.map(ProvinciaLAMapper::remap)
						.collect(Collectors.toList());
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByIstatRegione] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByIstatRegione] DAOException");
			throw new BusinessException(daoe);
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByIstatRegione] Errore generico servizio getProvinceByIstatRegione", e);
			throw new BusinessException("Errore generico servizio getProvinceByIstatRegione");
	    }
	}
	@Override
    public ProvinciaLA getProvinciaByRegione(Long idRegione, Long idProvincia) {
		ProvinciaLA result = null;
		try {
			result = ProvinciaLAMapper.remap(limammDao.getProvinciaById(idProvincia));
			// FIX xk il DAO non implementa ancora ItemNotFoundDAOException
			if (result==null || !idRegione.equals(result.getIdRegione())) {
				throw new ItemNotFoundBusinessException();
			}
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] DAOException");
			throw new BusinessException(daoe);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] Errore generico servizio getProvince", e);
			throw new BusinessException("Errore generico servizio elenco Province");
		}
	}
	@Override
	public ProvinciaLA getProvinciaByIstatRegione(String istatRegione, String istatProvincia) {
		ProvinciaLA result = null;
		try {
			Regione reg = limammDao.getRegioneByIstat(istatRegione);
			result = ProvinciaLAMapper.remap(limammDao.getProvinciaByIstat(istatProvincia));
			if ( result==null || !result.getIdRegione().equals(reg.getId()) ) {
				throw new ItemNotFoundBusinessException();
			}
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] DAOException");
			throw new BusinessException(daoe);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] Errore generico servizio getProvinciaByIstat", e);
			throw new BusinessException("Errore generico servizio getProvinciaByIstat");
		}
	}
	
	
	//
	// COMUNI
	@Override
	public List<ComuneLA> getComuni() {
		List<ComuneLA> result = new ArrayList<>();
		try {			
			result = limammDao.getComuni().stream()
						.map(ComuneLAMapper::remap)
						.collect(Collectors.toList());
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getComuni] DAOException");
			throw new BusinessException(daoe);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuni] Errore generico servizio getComuni", e);
			throw new BusinessException("Errore generico servizio elenco Comuni");
	   }
	}
	@Override
	public ComuneLA getComuneById(Long idComune) {
		ComuneLA result = null;
		try {
			result = ComuneLAMapper.remap(limammDao.getComuneById(idComune));
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getComuneById] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getComuneById] DAOException");
			throw new BusinessException(daoe);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuneById] Errore generico servizio getComuneById", e);
			throw new BusinessException("Errore generico servizio getComuneById");
		}
	}
	@Override
	public ComuneLA getComuneByIstat(String istat) {
		ComuneLA result = null;
		try {
			result = ComuneLAMapper.remap(limammDao.getComuneByIstat(istat));
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstat] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstat] DAOException");
			throw new BusinessException(daoe);
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstat] Errore generico servizio getComuneByIstat", e);
			throw new BusinessException("Errore generico servizio getComuneByIstat");
		}
	}
	
    // List comuni
	
	@Override
	public List<ComuneLA> getComuniByProvincia(Long idProvincia) {
		List<ComuneLA> result = new ArrayList<>();
		try {
			result = limammDao.getComuniPerIdProvincia(idProvincia).stream()
						.map(ComuneLAMapper::remap)
						.collect(Collectors.toList());
			return result;
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getComuniByProvincia] DAOException");
			throw new BusinessException(daoe);
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuniByProvincia] Errore generico servizio getComuniByProvincia", e);
			throw new BusinessException("Errore generico servizio getComuniByProvincia");
	    }
	}
	@Override
	public List<ComuneLA> getComuniByIstatProvincia(String istatProvincia) {
		List<ComuneLA> result = new ArrayList<>();
		try {
			Provincia prov = limammDao.getProvinciaByIstat(istatProvincia);
			result = limammDao.getComuniPerIdProvincia(prov.getId()).stream()
						.map(ComuneLAMapper::remap)
						.collect(Collectors.toList());
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatProvincia] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatProvincia] DAOException");
			throw new BusinessException(daoe);
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatProvincia] Errore generico servizio getComuniByIstatProvincia", e);
			throw new BusinessException("Errore generico servizio getComuniByIstatProvincia");
	    }
	}
	@Override
	public List<ComuneLA> getComuniByRegioneProvincia(Long idRegione, Long idProvincia) {
		List<ComuneLA> result = new ArrayList<>();
		try {
			Provincia prov = limammDao.getProvinciaById(idProvincia);
			if (prov==null || !idRegione.equals(prov.getIdRegione())) {
				LOG.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] Incongruenza idRegione e idProvincia");
				throw new ItemNotFoundBusinessException();
			}
			result = limammDao.getComuniPerIdProvincia(idProvincia).stream()
						.map(ComuneLAMapper::remap)
						.collect(Collectors.toList());
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] DAOException");
			throw new BusinessException(daoe);
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] Errore generico servizio getComuniByRegioneProvincia", e);
			throw new BusinessException("Errore generico servizio getComuniByRegioneProvincia");
	    }
	}
	@Override
	public List<ComuneLA> getComuniByIstatRegioneProvincia(String istatRegione, String istatProvincia) {
		List<ComuneLA> result = new ArrayList<>();
		try {
			Regione reg = limammDao.getRegioneByIstat(istatRegione);
			Provincia prov = limammDao.getProvinciaByIstat(istatProvincia);
			if (reg==null || prov==null || !((Long)reg.getId()).equals(prov.getIdRegione())) {
				LOG.error("[" + CLASS_NAME + "::getComuniByIstatRegioneProvincia] Incongruenza istatRegione e istatProvincia");
				throw new ItemNotFoundBusinessException();
			}
			result = limammDao.getComuniPerIdProvincia(prov.getId()).stream()
						.map(ComuneLAMapper::remap)
						.collect(Collectors.toList());
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatRegioneProvincia] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatRegioneProvincia] DAOException");
			throw new BusinessException(daoe);
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatRegioneProvincia] Errore generico servizio getComuniByIstatRegioneProvincia", e);
			throw new BusinessException("Errore generico servizio getComuniByIstatRegioneProvincia");
	    }
	}
	

	// Comuni singoli
	@Override
	public ComuneLA getComuneByRegioneProvincia(Long idRegione, Long idProvincia, Long idComune) {
		ComuneLA result = null;
		try {
			Provincia prov = limammDao.getProvinciaById(idProvincia);
			if (prov==null || !idRegione.equals(prov.getIdRegione())) {
				LOG.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] Incongruenza idRegione e idProvincia");
				throw new ItemNotFoundBusinessException();
			}
			result = ComuneLAMapper.remap(limammDao.getComuneById(idComune));
			if (result==null || result.getIdProvincia()!=prov.getId()) {
				LOG.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] Incongruenza idProvincia e idComune");
				throw new ItemNotFoundBusinessException();
			}
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] DAOException");
			throw new BusinessException(daoe);
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] Errore generico servizio getComuneByRegioneProvincia", e);
			throw new BusinessException("Errore generico servizio getComuneByRegioneProvincia");
	    }
	}
	@Override
	public ComuneLA getComuneByIstatRegioneProvincia(String istatRegione, String istatProvincia, String istatComune) {
		ComuneLA result = null;
		try {
			Regione reg = limammDao.getRegioneByIstat(istatRegione);
			Provincia prov = limammDao.getProvinciaByIstat(istatProvincia);
			if (reg==null || prov==null || !prov.getIdRegione().equals(reg.getId())) {
				LOG.error("[" + CLASS_NAME + "::getComuneByIstatRegioneProvincia] Incongruenza istatRegione e istatProvincia");
				throw new ItemNotFoundBusinessException();
			}
			result = ComuneLAMapper.remap(limammDao.getComuneByIstat(istatComune));
			if (result==null || result.getIdProvincia()!=prov.getId()) {
				LOG.error("[" + CLASS_NAME + "::getComuneByIstatRegioneProvincia] Incongruenza istatProvincia e istatComune");
				throw new ItemNotFoundBusinessException();
			}
			return result;
		} catch(ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstatRegioneProvincia] ItemNotFoundDAOException");
			throw new ItemNotFoundBusinessException(nfe);
		} catch(DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstatRegioneProvincia] DAOException");
			throw new BusinessException(daoe);
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstatRegioneProvincia] Errore generico servizio getComuneByIstatRegioneProvincia", e);
			throw new BusinessException("Errore generico servizio getComuneByIstatRegioneProvincia");
	    }
	}

}
