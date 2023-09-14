/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.AreaRuolo;
import it.csi.moon.commons.dto.EnteAreeRuoli;
import it.csi.moon.commons.dto.Funzione;
import it.csi.moon.commons.dto.Gruppo;
import it.csi.moon.commons.dto.Ruolo;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Utente;
import it.csi.moon.commons.entity.UtenteEntity;
import it.csi.moon.commons.entity.UtenteModuloEntity;
import it.csi.moon.commons.mapper.UtenteMapper;
import it.csi.moon.commons.util.decodifica.DecodificaTipoUtente;
import it.csi.moon.moonsrv.business.service.UtentiService;
import it.csi.moon.moonsrv.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.UtenteModuloDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi alle utenti
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class UtentiServiceImpl  implements UtentiService {
	
	private static final String CLASS_NAME = "UtentiServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	UtenteModuloDAO utenteModuloDAO;

	@Override
	public List<Utente> getElencoUtenti() throws BusinessException {
		try {
			List<Utente> result = utenteDAO.find().stream()
					.map(UtenteMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoUtenti] Errore generico servizio getElencoUtenti",ex);
			throw new ServiceException("Errore generico servizio elenco Utenti");
		} 
	}

	@Override
	public Utente getUtenteById(Long idUtente) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getUtenteById] BEGIN IN idUtente="+idUtente);
		}
		Utente result = null;
		try {
			UtenteEntity entity = utenteDAO.findById(idUtente);
			result = UtenteMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getUtenteById] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getUtenteById] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca funzione per id");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getUtenteById] END result="+result);
			}
		}
	}

	@Override
	public Utente getUtenteByIdentificativo(String identificativoUtente) throws ItemNotFoundBusinessException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::getUtenteByIdentificativo] IN identificativoUtente="+identificativoUtente);
		}
		Utente result = null;
		try {
			UtenteEntity entity = utenteDAO.findByIdentificativoUtente(identificativoUtente);
			result = UtenteMapper.buildFromEntity(entity);
			return result;
		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getUtenteByIdentificativo] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getUtenteByIdentificativo] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca funzione per identificativo");
		} finally {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getUtenteByIdentificativo] END result="+result);
			}
		}
	}

	@Override
	public Utente createUtente(/*UserInfo user,*/ Utente utente) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::createUtente] IN utente: " + utente);
//			if (!DecodificaTipoUtente.ADM.isCorrectType(user) && !hasRuolo(user, "OP_ADM") && !hasRuolo(user, "RESP")) {
//				LOG.error("[" + CLASS_NAME + "::createUtente] UnauthorizedBusinessException (ADM;RESP;OP_ADM) user=" + user);
//				throw new UnauthorizedBusinessException();
//			}
			UtenteEntity entity = new UtenteEntity();
			entity.setIdentificativoUtente(CLASS_NAME);
			entity.setCognome(CLASS_NAME);
			entity.setNome(CLASS_NAME);
			entity.setEmail(CLASS_NAME);
			entity.setIdTipoUtente(DecodificaTipoUtente.byCodice(utente.getTipoUtente().getCodice()).getId());
			entity.setFlAttivo("S");
			entity.setDataIns(new Date());
			entity.setAttoreIns(utente.getAttoreIns());
	        // Salva
	        Long idUtente = utenteDAO.insert(entity);
	        entity.setIdUtente(idUtente);
			// Rimap utente
			Utente result = UtenteMapper.buildFromEntity(entity);
			return result;
		} catch (DAOException dao) {
			LOG.warn("[" + CLASS_NAME + "::createUtente] Errore servizio createUtente " + dao.getMessage());
			throw new BusinessException(dao);
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::createUtente] Errore servizio createUtente", be);
			throw be;
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::createUtente] Errore generico servizio createUtente", ex);
			throw new BusinessException("Errore generico createUtente");
		}
	}

	@Override
	public Utente updateUtente(Long idUtente, Utente body) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente patchUtente(Long idUtente, Utente partialUtente) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ruolo> getRuoliByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Funzione> getFunzioniByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Gruppo> getGruppiByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	private Boolean hasRuolo (UserInfo user, String codiceRuolo) {
		Boolean hasRuolo = false;
		List<EnteAreeRuoli> entiAreeRuoli = user.getEntiAreeRuoli();
		if (entiAreeRuoli!=null && !entiAreeRuoli.isEmpty()) {
			for (EnteAreeRuoli enteAreaRuolo : entiAreeRuoli) {
				
				if (enteAreaRuolo!=null) {
					ArrayList<AreaRuolo> areeRuolo = (ArrayList<AreaRuolo>) enteAreaRuolo.getAreeRuoli();
					for (AreaRuolo areaRuolo : areeRuolo) {
						if (areaRuolo.getCodiceRuolo().equalsIgnoreCase(codiceRuolo)) {
							hasRuolo = true;
						}
					}
				}
			}
		}
		return hasRuolo;
	}
	
	
	// Moduli Abilitati
	@Override
	public void addUtenteModulo(UserInfo user, Long idUtente, Long idModulo) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::addUtenteModulo] IN idUtente: " + idUtente);
			LOG.debug("[" + CLASS_NAME + "::addUtenteModulo] IN idModulo: " + idModulo);
			UtenteModuloEntity entity = new UtenteModuloEntity();
			entity.setIdUtente(idUtente);
			entity.setIdModulo(idModulo);
			entity.setDataUpd(new Date());
			entity.setAttoreUpd(user.getIdentificativoUtente());
			utenteModuloDAO.insert(entity);
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::addUtenteModulo] Errore servizio addUtenteModulo idUtente:" + idUtente + "  idModulo:" + idModulo, daoe);
			throw new BusinessException(daoe);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::addUtenteModulo] Errore generico servizio addUtenteModulo", ex);
			throw new BusinessException("Errore addUtenteModulo");
		} 
	}
	@Override
	public void deleteUtenteModulo(UserInfo user, Long idUtente, Long idModulo) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteUtenteModulo] IN idUtente: " + idUtente);
			LOG.debug("[" + CLASS_NAME + "::deleteUtenteModulo] IN idModulo: " + idModulo);
			UtenteModuloEntity entity = new UtenteModuloEntity();
			entity.setIdUtente(idUtente);
			entity.setIdModulo(idModulo);
			utenteModuloDAO.delete(entity);
		} catch (DAOException daoe) {
			LOG.error("[" + CLASS_NAME + "::deleteUtenteModulo] Errore servizio deleteUtenteModulo idUtente:" + idUtente + "  idModulo:" + idModulo, daoe);
			throw new BusinessException(daoe);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::deleteUtenteModulo] Errore generico servizio deleteUtenteModulo", ex);
			throw new BusinessException("Errore deleteUtenteModulo");
		} 
	}
	
}
