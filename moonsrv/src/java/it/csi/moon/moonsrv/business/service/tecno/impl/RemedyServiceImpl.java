/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.tecno.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.apimint.troubleticketing.v1.dto.CategoriaApplicativa;
import it.csi.apimint.troubleticketing.v1.dto.CategoriaOperativaTicket;
import it.csi.apimint.troubleticketing.v1.dto.ConfigurationItem;
import it.csi.apimint.troubleticketing.v1.dto.Ente;
import it.csi.apimint.troubleticketing.v1.dto.LavorazioneTicket;
import it.csi.apimint.troubleticketing.v1.dto.RichiedenteDaAnagrafica;
import it.csi.apimint.troubleticketing.v1.dto.Ticket;
import it.csi.apimint.troubleticketing.v1.dto.TicketExpo;
import it.csi.apimint.troubleticketing.v1.dto.TicketSnapshot;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm.RemedyApimintDAO;
import it.csi.moon.moonsrv.business.service.tecno.RemedyService;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business di TroubleTicketing Remedy via servizi REST
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 17/05/2021 - Versione initiale
 */
@Component
public class RemedyServiceImpl implements RemedyService {
	
	private static final String CLASS_NAME = "RemedyServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	private RemedyApimintDAO remedyDAO = null;
	
	@Override
	public List<RichiedenteDaAnagrafica> getRichiedenteDaAnagrafica(String email, String cognome, String nome) {
		List<RichiedenteDaAnagrafica> result = null;
		try {
			result = remedyDAO.findUsers(email, cognome, nome);
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getRichiedenteDaAnagrafica] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getRichiedenteDaAnagrafica] Exception " + e.getMessage());
	    	throw new BusinessException("getRichiedenteDaAnagrafica errore generico");
		}
		return result; 
	}
	
	@Override
	public RichiedenteDaAnagrafica getRichiedenteDaAnagraficaById(String personId) {
		RichiedenteDaAnagrafica result = null;
		try {
			result = remedyDAO.findUserById(personId);
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getRichiedenteDaAnagraficaById] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getRichiedenteDaAnagraficaById] Exception " + e.getMessage());
	    	throw new BusinessException("getRichiedenteDaAnagraficaById non trovata generico");
		}
		return result; 
	}


	//
	// Entity Ente
	@Override
	public List<Ente> getEnti() {
		List<Ente> result = null;
		try {
			result = remedyDAO.findEnti();
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::findEnti] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::findEnti] Exception " + e.getMessage());
	    	throw new BusinessException("findEnti errore generico");
		}
		return result; 
	}

	//
	// Entity Categorie
	@Override
	public List<CategoriaOperativaTicket> getCategorieOperative() {
		List<CategoriaOperativaTicket> result = null;
		try {
			result = remedyDAO.findCategorieOperative();
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getCategorieOperative] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getCategorieOperative] Exception " + e.getMessage());
	    	throw new BusinessException("getCategorieOperative errore generico");
		}
		return result; 
	}

	@Override
	public List<CategoriaApplicativa> getCategorieApplicative() {
		List<CategoriaApplicativa> result = null;
		try {
			result = remedyDAO.getCategorieApplicative();
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getCategorieApplicative] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getCategorieApplicative] Exception " + e.getMessage());
	    	throw new BusinessException("getCategorieApplicative errore generico");
		}
		return result; 
	}

	
	//
	// Entity ConfigurationItem
	@Override
	public List<ConfigurationItem> getConfigurationItems(String personId) {
		List<ConfigurationItem> result = null;
		try {
			result = remedyDAO.getConfigurationItems(personId);
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getConfigurationItems] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getConfigurationItems] Exception " + e.getMessage());
	    	throw new BusinessException("getConfigurationItems errore generico");
		}
		return result; 
	}

	//
	// Entity Ticket
	public Ticket createTicket(Ticket ticket) {
		Ticket result = null;
		try {
			result = remedyDAO.createTicket(ticket);
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::createTicket] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::createTicket] Exception " + e.getMessage());
	    	throw new BusinessException("createTicket non trovata generico");
		}
		return result; 
	}
	public LavorazioneTicket getWorkinfoTicket(String ticketId) {
		LavorazioneTicket result = null;
		try {
			result = remedyDAO.getWorkinfoTicket(ticketId);
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getWorkinfoTicket] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getWorkinfoTicket] Exception " + e.getMessage());
	    	throw new BusinessException("getWorkinfoTicket non trovata generico");
		}
		return result; 
	}
	public List<TicketSnapshot> getLastUpdated() {
		List<TicketSnapshot> result = null;
		try {
			result = remedyDAO.getLastUpdated();
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getLastUpdated] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getLastUpdated] Exception " + e.getMessage());
	    	throw new BusinessException("getLastUpdated errore generico");
		}
		return result; 
	}
	public List<TicketExpo> getLastRegistered() {
		List<TicketExpo> result = null;
		try {
			result = remedyDAO.getLastRegistered();
	    } catch(DAOException daoe) {
	    	LOG.warn("[" + CLASS_NAME + "::getLastRegistered] DAOException " + daoe.getMessage());
	    	throw new BusinessException(daoe);
		} catch (Exception e) {
	    	LOG.error("[" + CLASS_NAME + "::getLastRegistered] Exception " + e.getMessage());
	    	throw new BusinessException("getLastRegistered errore generico");
		}
		return result; 
	}
}
