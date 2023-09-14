/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.extra.territorio.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.territorio.regp.ComuneLA;
import it.csi.moon.commons.dto.extra.territorio.regp.ProvinciaLA;
import it.csi.moon.commons.dto.extra.territorio.regp.RegioneLA;
import it.csi.moon.moonfobl.business.be.extra.territorio.LimammApi;
import it.csi.moon.moonfobl.business.be.impl.MoonBaseApiImpl;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.territorio.LimammDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class LimammApiImpl extends MoonBaseApiImpl implements LimammApi {
	
	private static final String CLASS_NAME = "LimammApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	@Qualifier("moonsrv")
	LimammDAO limammDAO;
    
	//
	// REGIONI
	@Override
	public Response getRegioni( String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		List<RegioneLA> result = new ArrayList<>();
		try {			
			result = limammDAO.getRegioni();
			return Response.ok(result).build();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getRegioni] BusinessException");
			throw new ServiceException("Errore generico servizio elenco Regioni");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRegioni] Errore generico servizio getRegioni", e);
			throw new ServiceException("Errore generico servizio elenco Regioni");
		}
	}

	@Override
    public Response getRegioneById( String idRegionePP, String fields,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		RegioneLA result = null;
		try {			
			Long idRegione = validaLongRequired(idRegionePP);
			result = limammDAO.getRegioneById(idRegione);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getRegioneById] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getRegioneById] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getRegioneById] BusinessException");
			throw new ServiceException("Errore generico servizio getRegioneById");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRegioneById] Errore generico servizio getRegioneById", e);
			throw new ServiceException("Errore generico servizio getRegioneById");
		}
	}
	@Override
	public Response getRegioneByIstat( String istatPP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		RegioneLA result = null;
		try {
			String istat = validaIstatRegione(istatPP);
			result = limammDAO.getRegioneByIstat(istat);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getRegioneByIstat] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getRegioneByIstat] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getRegioneByIstat] BusinessException");
			throw new ServiceException("Errore generico servizio getRegioneByIstat");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getRegioneByIstat] Errore generico servizio getRegioneByIstat", e);
			throw new ServiceException("Errore generico servizio getRegioneByIstat");
		}
	}
	
	//
	// PROVINCE
	@Override
	public Response getProvince( String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		List<ProvinciaLA> result = new ArrayList<>();
		try {			
			result = limammDAO.getProvince();
			return Response.ok(result).build();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getProvince] BusinessException");
			throw new ServiceException("Errore generico servizio elenco Province");
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvince] Errore generico servizio getProvince", e);
			throw new ServiceException("Errore generico servizio elenco Province");
	    }
	}
	@Override
    public Response getProvinciaById( String idProvinciaPP, String fields,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		ProvinciaLA result = null;
		try {
			Long idProvincia = validaLongRequired(idProvinciaPP);
			result = limammDAO.getProvinciaById(idProvincia);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaById] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaById] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaById] BusinessException");
			throw new ServiceException("Errore generico servizio getProvinciaById");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaById] Errore generico servizio getProvinciaById", e);
			throw new ServiceException("Errore generico servizio getProvinciaById");
		}
	}
	@Override
	public Response getProvinciaByIstat( String istatPP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		ProvinciaLA result = null;
		try {
			String istat = validaIstatProvincia(istatPP);
			result = limammDAO.getProvinciaByIstat(istat);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] BusinessException");
			throw new ServiceException("Errore generico servizio getProvinciaByIstat");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByIstat] Errore generico servizio getProvinciaByIstat", e);
			throw new ServiceException("Errore generico servizio getProvinciaByIstat");
		}
	}
	@Override
    public Response getProvinceByRegione( String idRegionePP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ServiceException {
		List<ProvinciaLA> result = new ArrayList<>();
		try {
			Long idRegione = validaLongRequired(idRegionePP);
			result = limammDAO.getProvinceByRegione(idRegione);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByRegione] UnprocessableEntityException");
			throw uee;
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByRegione] BusinessException");
			throw new ServiceException("Errore generico servizio getProvinceByRegione");
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByRegione] Errore generico servizio getProvinceByRegione", e);
			throw new ServiceException("Errore generico servizio elenco getProvinceByRegione");
	    }
	}
	@Override
	public Response getProvinceByIstatRegione(String istatRegionePP, String fields, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		List<ProvinciaLA> result = new ArrayList<>();
		try {
			String istatRegione = validaIstatRegione(istatRegionePP);
			result = limammDAO.getProvinceByIstatRegione(istatRegione);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByIstatRegione] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByIstatRegione] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByIstatRegione] BusinessException");
			throw new ServiceException("Errore generico servizio getProvinceByIstatRegione");
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinceByIstatRegione] Errore generico servizio getProvinceByIstatRegione", e);
			throw new ServiceException("Errore generico servizio elenco getProvinceByIstatRegione");
	    }
	}
	@Override
    public Response getProvinciaByRegione( String idRegionePP, String idProvinciaPP, String fields,
	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		ProvinciaLA result = null;
		try {
			Long idRegione = validaLongRequired(idRegionePP);
			Long idProvincia = validaLongRequired(idProvinciaPP);
			result = limammDAO.getProvinciaById(idProvincia);
			if (result==null || !idRegione.equals(result.getIdRegione())) { // FIX xk il DAO non implementa ancora ItemNotFoundDAOException
				throw new ResourceNotFoundException();
			}
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] BusinessException");
			throw new ServiceException("Errore generico servizio getProvinciaByRegione");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] Errore generico servizio getProvinciaByRegione", e);
			throw new ServiceException("Errore generico servizio getProvinciaByRegione");
		}
	}
	@Override
    public Response getProvinciaByIstatRegione( String istatRegionePP, String istatProvinciaPP, String fields,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		ProvinciaLA result = null;
		try {
			String istatRegione = validaIstatRegione(istatRegionePP);
			String istatProvincia = validaIstatProvincia(istatProvinciaPP);
			result = limammDAO.getProvinciaByIstatRegione(istatRegione, istatProvincia);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] BusinessException");
			throw new ServiceException("Errore generico servizio getProvinciaByRegione");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getProvinciaByRegione] Errore generico servizio getProvinciaByRegione", e);
			throw new ServiceException("Errore generico servizio getProvinciaByRegione");
		}
	}
	
	//
	// COMUNI
	@Override
	public Response getComuni( String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		List<ComuneLA> result = new ArrayList<>();
		try {			
			result = limammDAO.getComuni();
			return Response.ok(result).build();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getComuni] BusinessException");
			throw new ServiceException("Errore generico servizio getComuni");
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuni] Errore generico servizio getComuni", e);
			throw new ServiceException("Errore generico servizio elenco Comuni");
	     }
	}
	@Override
	public Response getComuneById(String idComunePP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		ComuneLA result = null;
		try {
			Long idComune = validaLongRequired(idComunePP);
			result = limammDAO.getComuneById(idComune);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getComuneById] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getComuneById] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getComuneById] BusinessException");
			throw new ServiceException("Errore generico servizio getComuneById");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuneById] Errore generico servizio getComuneById", e);
			throw new ServiceException("Errore generico servizio getComuneById");
		}
	}
	@Override
	public Response getComuneByIstat(String istatPP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		ComuneLA result = null;
		try {
			String istat = validaIstatComune(istatPP);
			result = limammDAO.getComuneByIstat(istat);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstat] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstat] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstat] BusinessException getProvinciaByIstat");
			throw new ServiceException("Errore generico servizio getProvinciaByIstat");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstat] Errore generico servizio getComuneByIstat", e);
			throw new ServiceException("Errore generico servizio getComuneByIstat");
		}
	}
	
    // List comuni
	
	@Override
	public Response getComuniByProvincia(String idProvinciaPP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ServiceException {
		List<ComuneLA> result = new ArrayList<>();
		try {
			Long idProvincia = validaLongRequired(idProvinciaPP);
			result = limammDAO.getComuniByProvincia(idProvincia);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getComuniByProvincia] UnprocessableEntityException");
			throw uee;
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getComuniByProvincia] BusinessException");
			throw new ServiceException("Errore generico servizio getComuniByProvincia");
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuniByProvincia] Errore generico servizio getComuniByProvincia", e);
			throw new ServiceException("Errore generico servizio getComuniByProvincia");
	    }
	}
	@Override
	public Response getComuniByIstatProvincia(String istatProvinciaPP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		List<ComuneLA> result = new ArrayList<>();
		try {
			String istatProvincia = validaIstatProvincia(istatProvinciaPP);
			ProvinciaLA prov = limammDAO.getProvinciaByIstat(istatProvincia);
			result = limammDAO.getComuniByProvincia(prov.getId());
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatProvincia] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatProvincia] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatProvincia] BusinessException");
			throw new ServiceException("Errore generico servizio getComuniByIstatProvincia");
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatProvincia] Errore generico servizio getComuniByIstatProvincia", e);
			throw new ServiceException("Errore generico servizio getComuniByIstatProvincia");
	    }
	}
	@Override
	public Response getComuniByRegioneProvincia(String idRegionePP, String idProvinciaPP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ServiceException {
		List<ComuneLA> result = new ArrayList<>();
		try {
			Long idRegione = validaLongRequired(idRegionePP);
			Long idProvincia = validaLongRequired(idProvinciaPP);
			ProvinciaLA prov = limammDAO.getProvinciaById(idProvincia);
			if (prov==null || !idRegione.equals(prov.getIdRegione())) {
				LOG.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] Incongruenza idRegione e idProvincia");
				throw new UnprocessableEntityException();
			}
			result = limammDAO.getComuniByProvincia(idProvincia);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] BusinessException");
			throw new ServiceException("Errore generico servizio getComuniByRegioneProvincia");
	    } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] Errore generico servizio getComuniByRegioneProvincia", e);
			throw new ServiceException("Errore generico servizio getComuniByRegioneProvincia");
	    }
	}
	@Override
	public Response getComuniByIstatRegioneProvincia(String istatRegionePP, String istatProvinciaPP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ServiceException {
		List<ComuneLA> result = new ArrayList<>();
		try {
			String istatRegione = validaIstatRegione(istatRegionePP);
			String istatProvincia = validaIstatProvincia(istatProvinciaPP);
			RegioneLA reg = limammDAO.getRegioneByIstat(istatRegione);
			ProvinciaLA prov = limammDAO.getProvinciaByIstat(istatProvincia);
			if (reg==null || prov==null || !((Long)reg.getId()).equals(prov.getIdRegione())) {
				LOG.error("[" + CLASS_NAME + "::getComuniByRegioneProvincia] Incongruenza istatRegione e istatProvincia");
				throw new UnprocessableEntityException();
			}
			result = limammDAO.getComuniByProvincia(prov.getId());
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatRegioneProvincia] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatRegioneProvincia] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatRegioneProvincia] BusinessException");
			throw new ServiceException("Errore generico servizio getComuniByIstatProvincia");
	     } catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuniByIstatRegioneProvincia] Errore generico servizio getComuniByIstatRegioneProvincia", e);
			throw new ServiceException("Errore generico servizio getComuniByIstatRegioneProvincia");
	     }
	}


	// Comuni singoli	
	@Override
	public Response getComuneByRegioneProvincia(String idRegionePP, String idProvinciaPP, String idComunePP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		ComuneLA result = null;
		try {
			Long idRegione = validaLongRequired(idRegionePP);
			Long idProvincia = validaLongRequired(idProvinciaPP);
			Long idComune = validaLongRequired(idComunePP);
			result = limammDAO.getComuneByRegioneProvincia(idRegione, idProvincia, idComune);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] BusinessException");
			throw new ServiceException("Errore generico servizio getComuneByRegioneProvincia");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuneByRegioneProvincia] Errore generico servizio getComuneByRegioneProvincia", e);
			throw new ServiceException("Errore generico servizio getComuneByRegioneProvincia");
		}
	}
	@Override
	public Response getComuneByIstatRegioneProvincia(String istatRegionePP, String istatProvinciaPP, String istatComunePP, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		ComuneLA result = null;
		try {
			if (LOG.isDebugEnabled()) {
				LOG.error("[" + CLASS_NAME + "::getComuneByIstatRegioneProvincia] IN istat = " + istatRegionePP + " / " + istatProvinciaPP + " / " + istatComunePP);
			}
			String istatRegione = validaIstatRegione(istatRegionePP);
			String istatProvincia = validaIstatProvincia(istatProvinciaPP);
			String istatComune = validaIstatComune(istatComunePP);
			result = limammDAO.getComuneByIstatRegioneProvincia(istatRegione, istatProvincia, istatComune);
			return Response.ok(result).build();
		} catch (UnprocessableEntityException uee) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstatRegioneProvincia] UnprocessableEntityException");
			throw uee;
		} catch(ItemNotFoundBusinessException nfbe) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstatRegioneProvincia] ItemNotFoundBusinessException");
			throw new ResourceNotFoundException();
		} catch(BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstatRegioneProvincia] BusinessException getComuneByIstatRegioneProvincia");
			throw new ServiceException("Errore generico servizio getComuneByIstatRegioneProvincia");
		} catch(Exception e) {
			LOG.error("[" + CLASS_NAME + "::getComuneByIstatRegioneProvincia] Errore generico servizio getComuneByIstatRegioneProvincia", e);
			throw new ServiceException("Errore generico servizio getComuneByIstatRegioneProvincia");
		}
	}
	
}
