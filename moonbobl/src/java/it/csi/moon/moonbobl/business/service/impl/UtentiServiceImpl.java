/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.BackendService;
import it.csi.moon.moonbobl.business.service.UtentiService;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteAreaRuoloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuliFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteAreaRuoloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEnteAbilitatoFlatDTO;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteModuloEntity;
import it.csi.moon.moonbobl.business.service.mapper.TipoUtenteMapper;
import it.csi.moon.moonbobl.business.service.mapper.UtenteMapper;
import it.csi.moon.moonbobl.dto.extra.istat.CodiceNome;
import it.csi.moon.moonbobl.dto.moonfobl.AreaRuolo;
import it.csi.moon.moonbobl.dto.moonfobl.EnteAreeRuoli;
import it.csi.moon.moonbobl.dto.moonfobl.Funzione;
import it.csi.moon.moonbobl.dto.moonfobl.Gruppo;
import it.csi.moon.moonbobl.dto.moonfobl.Ruolo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.Utente;
import it.csi.moon.moonbobl.dto.moonfobl.UtenteEnteAbilitato;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoUtente;


/**
 * @author Laurent
 * Layer di logica servizi che richiama i DAO
 */
@Component
public class UtentiServiceImpl implements UtentiService {
	
	private final static String CLASS_NAME = "UtentiServiceImpl";
	private Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	BackendService backendService;
	@Autowired
	UtenteAreaRuoloDAO utenteAreaRuoloDAO;
	@Autowired
	UtenteModuloDAO utenteModuloDAO;
	
	@Override
	public List<Utente> getElencoUtenti() throws BusinessException {
		try {
			List<Utente> result = utenteDAO.find().stream()
					.map(UtenteMapper::buildFromEntity)
					.collect(Collectors.toList());
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getElencoUtenti] Errore generico servizio getElencoUtenti",ex);
			throw new ServiceException("Errore generico servizio elenco Utenti");
		} 
	}
	
	@Override
	public List<UtenteEnteAbilitato> getUtentiEnteAbilitato(UserInfo user) throws BusinessException {
		return getUtentiEnteAbilitato(user, null); // Tutti utenti dell'ente dello user collegato
	}
	private List<UtenteEnteAbilitato> getUtentiEnteAbilitato(UserInfo user, Long idUtente) throws BusinessException {
		try {
			List<UtenteEnteAbilitato> result = new ArrayList<UtenteEnteAbilitato>();
			List<UtenteEnteAbilitatoFlatDTO> utentiEnteAbilitatoFlat = utenteDAO.findUtentiEnteAbilitatoByIdEnte(user.getEnte().getIdEnte(), idUtente);
			LOG.debug("["+CLASS_NAME+"::getUtentiEnteAbilitato] entiAreeRuoliFlat.size() = "+utentiEnteAbilitatoFlat!=null?utentiEnteAbilitatoFlat.size():null);
			if (utentiEnteAbilitatoFlat!=null && !utentiEnteAbilitatoFlat.isEmpty()) {
				UtenteEnteAbilitato curUtente = null;
				EnteAreeRuoli curEAR = null;
				for (UtenteEnteAbilitatoFlatDTO flat : utentiEnteAbilitatoFlat) {
					if (primoUtenteOrSwitchUtente(curUtente, flat)) {
//						if (curUtente!=null) result.add(curUtente);
						curUtente = estraiNuovoUtente(flat);
						curUtente.setEntiAreeRuoli(new ArrayList<EnteAreeRuoli>());
						result.add(curUtente);
						curEAR = null;
					}
					// Per questa versione il servizio è monoEnte : quello dello currentUser
					if (primoUtenteOrSwitchUtente(curUtente, flat) || primoEnte(curEAR, flat)) {
//						if (curEAR!=null) curUtente.getEntiAreeRuoli().add(curEAR);
						curEAR = estraiNuovoEnte(user); // (flat);
						curUtente.getEntiAreeRuoli().add(curEAR);
					}

					//
					AreaRuolo ar = new AreaRuolo();
					ar.setIdArea(flat.getIdArea());
					ar.setCodiceArea(flat.getCodiceArea());
					ar.setNomeArea(flat.getNomeArea());
					//
					ar.setIdRuolo(flat.getIdRuolo());
					ar.setCodiceRuolo(flat.getCodiceRuolo());
					ar.setNomeRuolo(flat.getNomeRuolo());
					//
					ar.setDataUpdAbilitazione(flat.getDataUpdAbilitazione());
					ar.setAttoreUpdAbilitazione(flat.getAttoreUpdAbilitazione());
					curEAR.getAreeRuoli().add(ar);
				}
//				if (curEAR!=null) curUtente.getEntiAreeRuoli().add(curEAR);
//				if (curUtente!=null) result.add(curUtente);
			}
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getUtentiEnteAbilitato] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio elenco Utenti Ente Abilitato");
		} 
	}
	public List<UtenteEnteAbilitato> getUtentiEnteNoAbilitato(UserInfo user) throws BusinessException {
		try {
			List<UtenteEnteAbilitato> result = new ArrayList<UtenteEnteAbilitato>();
			List<UtenteEnteAbilitatoFlatDTO> utentiEnteNoAbilitatoFlat = utenteDAO.findUtentiEnteNoAbilitatoByIdEnte(user.getEnte().getIdEnte());
			LOG.debug("["+CLASS_NAME+"::getUtentiEnteNoAbilitato] entiAreeRuoliFlat.size() = "+utentiEnteNoAbilitatoFlat!=null?utentiEnteNoAbilitatoFlat.size():null);
			if (utentiEnteNoAbilitatoFlat!=null && !utentiEnteNoAbilitatoFlat.isEmpty()) {
				UtenteEnteAbilitato curUtente = null;
				EnteAreeRuoli curEAR = null;
				for (UtenteEnteAbilitatoFlatDTO flat : utentiEnteNoAbilitatoFlat) {
					curUtente = estraiNuovoUtente(flat);
					result.add(curUtente);
				}
			}
			return result;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::getUtentiEnteNoAbilitato] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio elenco Utenti Ente NO Abilitato");
		}
	}
	
	private EnteAreeRuoli estraiNuovoEnte(UserInfo user) {
		EnteAreeRuoli curEAR;
		curEAR = new EnteAreeRuoli();
		curEAR.setIdEnte(user.getEnte().getIdEnte());
		curEAR.setCodiceEnte(user.getEnte().getCodiceEnte());
		curEAR.setNomeEnte(user.getEnte().getNomeEnte());
		curEAR.setDescrizioneEnte(user.getEnte().getDescrizioneEnte());
		curEAR.setIdTipoEnte(user.getEnte().getIdTipoEnte());
		curEAR.setAreeRuoli(new ArrayList<AreaRuolo>());
		return curEAR;
	}
	
	private boolean primoUtenteOrSwitchUtente(UtenteEnteAbilitato curUtente, UtenteEnteAbilitatoFlatDTO flat) {
		return curUtente==null || !curUtente.getIdUtente().equals(flat.getIdUtente());
	}
	private boolean primoEnte(EnteAreeRuoli curEAR, UtenteEnteAbilitatoFlatDTO flat) {
		return curEAR==null;
	}
	private UtenteEnteAbilitato estraiNuovoUtente(UtenteEnteAbilitatoFlatDTO flat) {
		UtenteEnteAbilitato curUtente;
		curUtente = new UtenteEnteAbilitato();
		curUtente.setIdUtente(flat.getIdUtente());
		curUtente.setIdentificativoUtente(flat.getIdentificativoUtente());
		curUtente.setNome(flat.getNome());
		curUtente.setCognome(flat.getCognome());
//						curUtente.setUsername(flat.getUsername());
		curUtente.setEmail(flat.getEmail());
		curUtente.setFlagAttivo("S".equals(flat.getFlAttivo()));
		curUtente.setTipoUtente(TipoUtenteMapper.buildFromIdTipoUtente(flat.getIdTipoUtente()));
		curUtente.setDataIns(flat.getDataIns());
		curUtente.setDataUpd(flat.getDataUpd());
		curUtente.setAttoreUpd(flat.getAttoreUpd());
		return curUtente;
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
			LOG.warn("[" + CLASS_NAME + "::getUtenteById] Errore invocazione DAO - ", notFoundEx);
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
			LOG.warn("[" + CLASS_NAME + "::getUtenteByIdentificativo] ItemNotFoundDAOException for identificativoUtente = " + identificativoUtente);
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
	public Utente createUtente(UserInfo user, Utente utente) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::createUtente] IN utente: " + utente);
			if (!DecodificaTipoUtente.ADM.isCorrectType(user) && !hasRuolo(user, "OP_ADM") && !hasRuolo(user, "RESP")) {
				LOG.error("[" + CLASS_NAME + "::createUtente] UnauthorizedBusinessException (ADM;RESP;OP_ADM) user=" + user);
				throw new UnauthorizedBusinessException();
			}
			UtenteEntity entity = new UtenteEntity();
			entity.setIdentificativoUtente(utente.getIdentificativoUtente());
			entity.setCognome(utente.getCognome());
			entity.setNome(utente.getNome());
			entity.setEmail(utente.getEmail());
			entity.setIdTipoUtente(DecodificaTipoUtente.byCodice(utente.getTipoUtente().getCodice()).getId());
			entity.setFlAttivo("S");
			entity.setDataIns(new Date());
			entity.setAttoreIns(user.getIdentificativoUtente());	
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
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::createUtente] Errore generico servizio createUtente", ex);
			throw new BusinessException("Errore generico createUtente");
		} 
	}

	@Override
	public Utente updateUtente(UserInfo user, Long idUtente, Utente body) throws BusinessException {
		// TODO Auto-generated method stub
		throw new BusinessException("NOT YET IMPLEMENTED");
	}

	/**
	 * Patch dei campi : Cognome, Nome, identificativoUtente, email
	 */
	@Override
	public Utente patchUtente(UserInfo user, Long idUtente, Utente partialUtente) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::patchUtente] IN idUtente: " + idUtente);
			LOG.debug("[" + CLASS_NAME + "::patchUtente] IN partialUtente: " + partialUtente);
			if (!DecodificaTipoUtente.ADM.isCorrectType(user) && !hasRuolo(user, "OP_ADM") && !hasRuolo(user, "RESP")) {
				LOG.error("[" + CLASS_NAME + "::patchUtente] UnauthorizedBusinessException (ADM;RESP;OP_ADM) user=" + user);
				throw new UnauthorizedBusinessException();
			}
			UtenteEntity entity = utenteDAO.findById(idUtente);
			if (partialUtente.getCognome()!=null) {
				entity.setCognome(partialUtente.getCognome());
			}
			if (partialUtente.getNome()!=null) {
				entity.setNome(partialUtente.getNome());
			}
			if (partialUtente.getIdentificativoUtente()!=null) {
				entity.setIdentificativoUtente(partialUtente.getIdentificativoUtente());
			}
			if (partialUtente.getEmail()!=null) {
				entity.setEmail(partialUtente.getEmail());
			}
			if (partialUtente.getTipoUtente()!=null && partialUtente.getTipoUtente().getCodice()!=null) {
				entity.setIdTipoUtente(DecodificaTipoUtente.byCodice(partialUtente.getTipoUtente().getCodice()).getId());
			}
			if (partialUtente.getFlagAttivo()!=null) {
				entity.setFlAttivo(partialUtente.getFlagAttivo()?"S":"N");
			}
			entity.setDataUpd(new Date());
			entity.setAttoreUpd(user.getIdentificativoUtente());
	        // Salva
	        utenteDAO.update(entity);
			
			// Rilegge l utente
			Utente utente = getUtenteById(idUtente);
			return utente;
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::patchUtente] Errore servizio patchUtente", be);
			throw be;
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::patchUtente] Errore generico servizio patchUtente",ex);
			throw new BusinessException("Errore generico aggiorna partial Utente");
		} 
	}

	@Override
	public List<Ruolo> getRuoliByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException {
		// TODO Auto-generated method stub
		throw new BusinessException("NOT YET IMPLEMENTED");
	}

	@Override
	public UtenteEnteAbilitato addEnteAreaRuolo(UserInfo user, Long idUtente, Long idEnte, Long idArea, Integer idRuolo) throws ItemNotFoundBusinessException, BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::addEnteAreaRuolo] IN idUtente: " + idUtente);
				LOG.debug("[" + CLASS_NAME + "::addEnteAreaRuolo] IN idEnte: " + idEnte);
				LOG.debug("[" + CLASS_NAME + "::addEnteAreaRuolo] IN idArea: " + idArea);
				LOG.debug("[" + CLASS_NAME + "::addEnteAreaRuolo] IN idRuolo: " + idRuolo);
			}
			UtenteAreaRuoloEntity areaRuolo = new UtenteAreaRuoloEntity();
			areaRuolo.setIdUtente(idUtente);
			areaRuolo.setIdArea(idArea);
			areaRuolo.setIdRuolo(idRuolo);
			areaRuolo.setDataUpd(new Date());
			areaRuolo.setAttoreUpd(user.getIdentificativoUtente());
			utenteAreaRuoloDAO.insert(areaRuolo);
			UtenteEnteAbilitato utente = getUtentiEnteAbilitato(user, idUtente).get(0);
			return utente;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::addEnteAreaRuolo] Errore servizio addEnteAreaRuolo",e);
			throw new BusinessException("Errore servizio addEnteAreaRuolo");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::addEnteAreaRuolo] Errore generico servizio addEnteAreaRuolo",ex);
			throw new BusinessException("Errore addEnteAreaRuolo");
		} 
	}
	@Override
	public UtenteEnteAbilitato deleteEnteAreaRuolo(UserInfo user, Long idUtente, Long idEnte, Long idArea, Integer idRuolo) throws ItemNotFoundBusinessException, BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::deleteEnteAreaRuolo] IN idUtente: " + idUtente);
				LOG.debug("[" + CLASS_NAME + "::deleteEnteAreaRuolo] IN idEnte: " + idEnte);
				LOG.debug("[" + CLASS_NAME + "::deleteEnteAreaRuolo] IN idArea: " + idArea);
				LOG.debug("[" + CLASS_NAME + "::deleteEnteAreaRuolo] IN idRuolo: " + idRuolo);
			}
			UtenteAreaRuoloEntity areaRuolo = new UtenteAreaRuoloEntity();
			areaRuolo.setIdUtente(idUtente);
			areaRuolo.setIdArea(idArea);
			areaRuolo.setIdRuolo(idRuolo);
			utenteAreaRuoloDAO.delete(areaRuolo);
			
			UtenteEnteAbilitato result = null;
			List<UtenteEnteAbilitato> utenteAreeRuoli = getUtentiEnteAbilitato(user, idUtente);
			if (!utenteAreeRuoli.isEmpty()) {
				result = utenteAreeRuoli.get(0);
			} else {
				LOG.debug("[" + CLASS_NAME + "::deleteEnteAreaRuolo] Ultimo AreaRuolo cancellato per idUtente=" + idUtente);
			}
			return result;
		} catch (BusinessException e) {
			LOG.error("[" + CLASS_NAME + "::deleteEnteAreaRuolo] Errore servizio deleteEnteAreaRuolo",e);
			throw new BusinessException("Errore servizio deleteEnteAreaRuolo");
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::deleteEnteAreaRuolo] Errore generico servizio deleteEnteAreaRuolo",ex);
			throw new BusinessException("Errore generico deleteEnteAreaRuolo");
		} 
	}
	
	@Override
	public List<Funzione> getFunzioniByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException {
		// TODO Auto-generated method stub
		throw new BusinessException("NOT YET IMPLEMENTED");
	}

	@Override
	public List<Gruppo> getGruppiByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException {
		// TODO Auto-generated method stub
		throw new BusinessException("NOT YET IMPLEMENTED");
	}

	@Override
	public List<CodiceNome> getComuniAbilitati(UserInfo user) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getComuniAbilitati]  start");
			
			List<CodiceNome> comuniAbilitati = utenteDAO.findComuniAbilitatiUtente(user.getIdentificativoUtente());

			return comuniAbilitati;

		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getComuniAbilitati] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getComuniAbilitati] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		} 
	}

	@Override
	public List<CodiceNome> getEntiAbilitati(UserInfo user) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::getEntiAbilitati]  start");
			
			List<CodiceNome> entiAbilitati = utenteDAO.findEntiAbilitatiUtente(user.getIdentificativoUtente());

			return entiAbilitati;

		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getEntiAbilitati] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getEntiAbilitati] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		} 
	}
	
	@Override
	public Boolean isUtenteAbilitato(UserInfo user, Long idModulo, String filtroRicercaDati) throws BusinessException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::isUtenteAbilitato] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::isUtenteAbilitato] IN user="+user);
				LOG.debug("[" + CLASS_NAME + "::isUtenteAbilitato] IN idModulo="+idModulo+"  filtroRicercaDati="+filtroRicercaDati);
			}
			if (idModulo==null) {
				LOG.error("[" + CLASS_NAME + "::isUtenteAbilitato] InvalidParameter idModulo null.");
				throw new BusinessException("Errore isUtenteAbilitato - InvalidParameter idModulo null.");
			}
			if (DecodificaTipoUtente.ADM.isCorrectType(user)) {
				return true; 
			}
			if (hasRuolo(user, "OP_ADV") || hasRuolo(user, "OP_ADM") || hasRuolo(user, "ADMIN"))
			{
				return true;
			}
			
			Boolean isAbilitato = false;
			ModuliFilter filter = new ModuliFilter();
			filter.setUtenteAbilitato(!DecodificaTipoUtente.ADM.isCorrectType(user)?user.getIdentificativoUtente():null);
			filter.setIdModulo(idModulo);
			filter.setOnlyLastVersione(true);
			
			// cerco tutti i moduli su cui l'utente è abilitato
			List<ModuloVersionatoEntity> elenco = moduloDAO.find(filter);
			if (elenco != null && elenco.size() > 0) {
				for (ModuloVersionatoEntity  entity : elenco) {
					if (entity.getIdModulo().equals(idModulo)) {						
						// verifico il caso in cui sia necessaria anche l'abilitazione per comune	
						if (entity.getCodiceModulo().equals("CONT_COSTR")) {
							if (filtroRicercaDati == null) {
								return false;
							}
						}						
						if (entity.getCodiceModulo().equals("RP_RSI") || entity.getCodiceModulo().equals("RP_RSI_2")) {
							return true;
						}						
						if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
						{
							String[] arrOfParams = filtroRicercaDati.split("@@"); 
							List<String> listParams = Arrays.asList(arrOfParams);
							if (listParams.size() > 0) 
							{
								for (int i = 0; i < listParams.size(); i++) {
								    String param = listParams.get(i);
								    String[] arrKeyValue = param.split("=");
								    if (arrKeyValue[0].equals(Constants.FILTRO_RICERCA_DATI_COMUNE)) {
								    	String codiceComune = arrKeyValue[1];
								    	List<CodiceNome> elencoComuni = getComuniAbilitati(user);
								    	for (CodiceNome comune : elencoComuni) {
								    		String codiceComuneAbilitato = comune.getCodice().toString();
								    		
								    		codiceComuneAbilitato = StringUtils.strip(codiceComuneAbilitato,"0");
								    		codiceComune =  StringUtils.strip(codiceComune,"0");
								    										    		
								    		if (codiceComuneAbilitato.equals(codiceComune)) {
								    			isAbilitato = true;
								    		}
								    	}
								    	break;
								    } 
								    else if (arrKeyValue[0].equals(Constants.FILTRO_RICERCA_DATI_ASR)) {
								      	String codiceAsr = arrKeyValue[1];
								    	List<CodiceNome> elencoEnti = getEntiAbilitati(user);
								    	for (CodiceNome ente : elencoEnti) {
								    		String codiceEnteAbilitato = ente.getCodice().toString();
								    		if (codiceEnteAbilitato.equals(codiceAsr)) {
								    			isAbilitato = true;
								    		}
								    	}
								    	break;
								    }
								    else if (arrKeyValue[0].equals(Constants.FILTRO_RICERCA_DATI_CCR)) {
								      	String codiceCcr = arrKeyValue[1];
								    	List<CodiceNome> elencoEnti = getEntiAbilitati(user);
								    	for (CodiceNome ente : elencoEnti) {
								    		String codiceEnteAbilitato = ente.getCodice().toString();
								    		if (codiceEnteAbilitato.equals(codiceCcr)) {
								    			isAbilitato = true;
								    		}
								    	}
								    	break;
								    }								    
								}
							}
						}
						else {
							isAbilitato = true;
						}
					}
				}
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::isUtenteAbilitato] OUT isAbilitato="+isAbilitato);
			}
			return isAbilitato;

		} catch (ItemNotFoundDAOException notFoundEx) {
			LOG.error("[" + CLASS_NAME + "::getComuniAbilitati] Errore invocazione DAO - ", notFoundEx);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::getComuniAbilitati] Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca istanza per id");
		} 
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
		} catch (Throwable ex) {
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
		} catch (Throwable ex) {
			LOG.error("[" + CLASS_NAME + "::deleteUtenteModulo] Errore generico servizio deleteUtenteModulo", ex);
			throw new BusinessException("Errore deleteUtenteModulo");
		} 
	}

}

