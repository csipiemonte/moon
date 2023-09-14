/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.istanza;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import it.csi.moon.moonbobl.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteAreaRuoloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.OneriCostrIbanDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrIbanEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.AreaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.EnteEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaSaveResponse;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteAreaRuoloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ext.CognoneNomeCodFiscEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.DatiIstanzaHelper_CONT_COMUNE;
import it.csi.moon.moonbobl.dto.moonfobl.DocumentoRiconoscimento;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaRuolo;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoEnte;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoUtente;

/**
 * IstanzaDelegate_CONT_COMUNE - Specializzazione.
 *  - saveUlteriori richiamato da saveIstanza per salvataggio ulteriore in moon_ext_oneri_costr_iban via oneriCostrIbanDAO
 *
 * @author laurent
 * @author danilo
 *
 */
public class IstanzaDelegate_CONT_COMUNE extends IstanzaDefaultDelegate implements IstanzaServiceDelegate {

	private final static String CLASS_NAME = "IstanzaDelegate_CONT_COMUNE";
	private Logger log = LoggerAccessor.getLoggerBusiness();

	private final static String CODICE_AREA_EDIL_REGP = "EDIL_REGP";
	private final static String CODICE_AREA = "EDIL_PRIV";
	private final static String DESC_AREA = "Ufficio comunale Edilizia privata";
	
	@Autowired
	OneriCostrIbanDAO oneriCostrIbanDAO;
	@Autowired
	EnteDAO enteDAO;
	@Autowired
	AreaDAO areaDAO;
	@Autowired
	UtenteDAO utenteDAO;	
	@Autowired
	UtenteModuloDAO utenteModuloDAO;
	@Autowired
	UtenteAreaRuoloDAO utenteAreaRuoloDAO;
	
	private Long idModulo; // Constants.CODICE_MODULO_EDIL_CONT_COSTR
	
	public IstanzaDelegate_CONT_COMUNE() {
		log.debug("[" + CLASS_NAME + "::" + CLASS_NAME + "]");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	//
	// INVI ISTANZA
	//
	@Override
	protected void inviaUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, Long idProcesso) throws BusinessException {
		insertExtOneriCostrIBAN(user, response);
	}
//	protected void saveUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, boolean insertMode, boolean completaMode, Long idProcesso) throws BusinessException {
//		if (completaMode) {
//			insertExtOneriCostrIBAN(user, docRiconoscimento, response, idProcesso);
//		}
//	}

	private void insertExtOneriCostrIBAN(UserInfo user, IstanzaSaveResponse response) throws DAOException, BusinessException {
		try {
			DatiIstanzaHelper_CONT_COMUNE oneriCostrHelper = new DatiIstanzaHelper_CONT_COMUNE(String.valueOf(response.getIstanza().getData()));

			// 1. Salvataggio in ext con la response
			OneriCostrIbanEntity ibanComuneE = oneriCostrHelper.readComune();
			ibanComuneE.setDataIns(new Date());
			ibanComuneE.setAttoreIns(user.getIdentificativoUtente());
			try {
				oneriCostrIbanDAO.insert(ibanComuneE);
			} catch (DAOException e) {				
				log.warn("[" + CLASS_NAME + "::insertExtOneriCostrIBAN] DAOException"+e.getMessage());			
				response.setCodice("MOONFOBL-CONT_COMUNE-001");
				response.setTitolo("AVVISO");
				response.setDescrizione("Il Comune di "+ibanComuneE.getNomeComune()+" è già presente. Utilizzare i moduli di aggiornamento.");
				throw e;
			}

			// Ricerca o Crea l'ente e l'area EDIL_PRIV
			EnteEntity ente = retrieveEnte(ibanComuneE, user);
			AreaEntity area = retrieveAreaEdilPriv(ente, user);

			AreaEntity areaRegionePiemonte = retrieveAreaEdilRegionePiemonte();

			// 2. Gestione NEW Operatori
			List<CognoneNomeCodFiscEntity> operatori = oneriCostrHelper.readOperatori();
			log.debug("[" + CLASS_NAME + "::insertExtOneriCostr] operatori.size(): " + operatori.size());
			for (CognoneNomeCodFiscEntity o : operatori) {
				// Ricerca o Crea l'utente
				UtenteEntity utente = retrieveUtente(o, user);
				insertUtenteAreaRuoloIfNotExists(utente, area, user);
				insertUtenteAreaRuoloIfNotExists(utente, areaRegionePiemonte, user);
				insertUtenteModuloIfNotExists(utente, user);
			}

		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::insertExtOneriCostr] Exception"+e.getMessage());
			throw new BusinessException();
		}
	}

	private AreaEntity retrieveAreaEdilRegionePiemonte() {
		return areaDAO.findByCd(1L, CODICE_AREA_EDIL_REGP);
	}

	
	/**
	 * Ricerca o Crea l'ente di tipo COMUNE
	 * @param ibanComuneE i dati del modulo compilato (provincia e comune Istat)
	 * @param user di tracciamento di inserimento
	 * @return la resorse ricercata o creata
	 */
	private EnteEntity retrieveEnte(OneriCostrIbanEntity ibanComuneE, UserInfo user) throws BusinessException {
		try {
			log.debug("[" + CLASS_NAME + "::retriveEnte] codiceIstat="+ibanComuneE.getCodIstat());
			EnteEntity result = null;
			try {
				result = enteDAO.findByCodice(ibanComuneE.getCodIstat());
			} catch (ItemNotFoundDAOException infe) {
				log.warn("[" + CLASS_NAME + "::retriveEnte] CREO ENTE codiceIstat="+ibanComuneE.getCodIstat());
				EnteEntity entity = new EnteEntity();
				entity.setCodiceEnte(ibanComuneE.getCodIstat());
				entity.setNomeEnte(ibanComuneE.getNomeComune());
				entity.setDescrizioneEnte("Comune di "+ibanComuneE.getNomeComune());
				entity.setFlAttivo("S");
				entity.setIdTipoEnte(DecodificaTipoEnte.COMUNE.getId());
				entity.setDataUpd(new Date());
				entity.setAttoreUpd(user.getIdentificativoUtente());
				Long idEnte = enteDAO.insert(entity);
				entity.setIdEnte(idEnte);
				result = entity;
			}
			return result;
		} catch (DAOException e) {
			log.warn("[" + CLASS_NAME + "::retriveEnte] DAOException "+e.getMessage());
			throw new BusinessException();
		}
	}

	/**
	 * Ricerca o Crea l'area EDIL_PRIL (CODICE_AREA) per l'ente
	 * @param ente a quale deve essere associato l'area
	 * @param user di tracciamento di inserimento
	 * @return la resorse ricercata o creata
	 */
	private AreaEntity retrieveAreaEdilPriv(EnteEntity ente, UserInfo user) {
		try {
			log.debug("[" + CLASS_NAME + "::retrieveAreaEdilPriv] " + CODICE_AREA + " for codiceIstat="	+ ente.getIdEnte());
			AreaEntity result = null;
			try {
				result = areaDAO.findByCd(ente.getIdEnte(), CODICE_AREA);
			} catch (ItemNotFoundDAOException infe) {
				AreaEntity entity = new AreaEntity();
				entity.setCodiceArea(CODICE_AREA);
				entity.setIdEnte(ente.getIdEnte());
				entity.setNomeArea(DESC_AREA);
				entity.setDataUpd(new Date());
				entity.setAttoreUpd(user.getIdentificativoUtente());
				Long idArea = areaDAO.insert(entity);
				entity.setIdArea(idArea);
				result = entity;
			}
			return result;
		} catch (DAOException e) {
			log.warn("[" + CLASS_NAME + "::retrieveAreaEdilPriv] DAOException " + e.getMessage());
			throw new BusinessException();
		}
	}

	/**
	 * Ricerca o Crea l'utente
	 * @param utenteDaCercare
	 * @param user di tracciamento di inserimento
	 * @return la resorse ricercata o creata
	 */
	private UtenteEntity retrieveUtente(CognoneNomeCodFiscEntity utenteDaCercare, UserInfo user) {
		try {
			UtenteEntity result = null;
			try {
				result = utenteDAO.findByIdentificativoUtente(utenteDaCercare.getCodicefiscale().trim().toUpperCase());
			} catch (ItemNotFoundDAOException infe) {
				UtenteEntity entity = new UtenteEntity();
				entity.setIdentificativoUtente(utenteDaCercare.getCodicefiscale().trim().toUpperCase());
				entity.setNome(utenteDaCercare.getNome().trim().toUpperCase());
				entity.setCognome(utenteDaCercare.getCognome().trim().toUpperCase());
				entity.setEmail("");
				entity.setFlAttivo("S");
				entity.setIdTipoUtente(DecodificaTipoUtente.PA.getId());
				entity.setPassword("");
				Date now = new Date();
				entity.setDataIns(now);
				entity.setDataUpd(now);
				entity.setAttoreIns(user.getIdentificativoUtente());
				entity.setAttoreUpd(user.getIdentificativoUtente());
				Long idUtente = utenteDAO.insert(entity);				
				entity.setIdUtente(idUtente);
				result = entity;				
			}			
			return result;
		} catch (DAOException e) {
			log.warn("[" + CLASS_NAME + "::retrieveUtente] DAOException "+e.getMessage());
			throw new BusinessException();
		}
	}
	
	
	/**
	 * Inserisce la relazione tripla : utente/area/ruolo se non esiste per il ruolo 4-OP_SIMP
	 * @param utente 
	 * @param area
	 * @param ente
	 * @param user di tracciamento di inserimento
	 * @return la resorse ricercata o creata
	 */
	private UtenteAreaRuoloEntity insertUtenteAreaRuoloIfNotExists(UtenteEntity utente, AreaEntity area, UserInfo user) throws BusinessException {
		try {
			UtenteAreaRuoloEntity result = null;
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::insertUtenteAreaRuoloIfNotExists] idUtente = " + utente.getIdUtente());
				log.debug("[" + CLASS_NAME + "::insertUtenteAreaRuoloIfNotExists] idArea = " + area.getIdArea());
			}
			try {
				result = utenteAreaRuoloDAO.findByUtenteAreaRuolo(utente.getIdUtente(),area.getIdArea(), DecodificaRuolo.OP_SIMP.getId());
			} catch (ItemNotFoundDAOException infe) {
				if (log.isDebugEnabled()) {
					log.debug("[" + CLASS_NAME + "::insertUtenteAreaRuoloIfNotExists] ItemNotFound insert new utente area ruolo");
				}
				UtenteAreaRuoloEntity entity = new UtenteAreaRuoloEntity();
				entity.setIdUtente(utente.getIdUtente());
				entity.setIdArea(area.getIdArea());
				entity.setIdRuolo(DecodificaRuolo.OP_SIMP.getId());
				entity.setDataUpd(new Date());
				entity.setAttoreUpd(user.getIdentificativoUtente());	
				int resInsert = utenteAreaRuoloDAO.insert(entity);
				log.debug("[" + CLASS_NAME + "::insertUtenteAreaRuoloIfNotExists]  record inserito " + resInsert);
				result = entity;
			}
			return result;					
		} catch (DAOException e) {
			log.warn("[" + CLASS_NAME + "::insertUtenteAreaRuoloIfNotExists] DAOException " + e.getMessage());
			throw new BusinessException();
		}
	}

	
	/**
	 * Inserisce la relazione : utente-modulo per il modulo CONT_COSTR
	 * @param utente a quale abilitare l'accesso da BO sul modulo CONT_COSTR
	 * @param user di tracciamento di inserimento
	 * @return la resorse ricercata o creata
	 */
	private UtenteModuloEntity insertUtenteModuloIfNotExists(UtenteEntity utente, UserInfo user) throws BusinessException {
		try {
			UtenteModuloEntity result = null;
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::insertUtenteModuloIfNotExists] idUtente " + utente.getIdUtente());
				log.debug("[" + CLASS_NAME + "::insertUtenteModuloIfNotExists] idModulo " + getIdModulo()); // dinamico per ambiente DEV:75, TST:58, PRD:15
			}
			try {
//				result = utenteModuloDAO.findByUtenteModulo(utente.getIdUtente(), getIdModulo());
				result = utenteModuloDAO.findByIdUtenteModulo(utente.getIdUtente(), getIdModulo());
			} catch (ItemNotFoundDAOException infe) {
				UtenteModuloEntity entity = new UtenteModuloEntity();
				entity.setIdUtente(utente.getIdUtente());
				entity.setIdModulo(getIdModulo());
				entity.setDataUpd(new Date());
				entity.setAttoreUpd(user.getIdentificativoUtente());
				int resInsert =  utenteModuloDAO.insert(entity);
				log.debug("[" + CLASS_NAME + "::insertUtenteModuloIfNotExists] record inserito "+resInsert);
				result = entity;
			}
			return result;	
		} catch (DAOException e) {
			log.warn("[" + CLASS_NAME + "::insertUtenteModuloIfNotExists] DAOException "+e.getMessage());
			throw new BusinessException();
		}
	}
	
	
	/**
	 * Ritorna idModulo di CONT_COSTR, dinamico per ambiente DEV:75, TST:58, PRD:15
	 * con gestione cache locale
	 * @return idModulo di CONT_COSTR
	 * @throws BusinessException
	 */
	private Long getIdModulo() throws BusinessException {
		try {
			if (idModulo==null) {
				ModuloEntity modulo = moduloDAO.findByCodice(Constants.CODICE_MODULO_EDIL_CONT_COSTR);
				idModulo = modulo.getIdModulo();
			}
			return idModulo;
		} catch (DAOException e) {
			log.warn("[" + CLASS_NAME + "::getIdModulo] DAOException "+e.getMessage());
			throw new BusinessException();
		}
	}
}
