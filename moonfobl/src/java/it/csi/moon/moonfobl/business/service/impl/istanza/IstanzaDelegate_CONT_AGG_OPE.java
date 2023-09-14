/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.istanza;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.extra.demografia.DocumentoRiconoscimento;
import it.csi.moon.commons.entity.AreaEntity;
import it.csi.moon.commons.entity.EnteEntity;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.entity.UtenteEntity;
import it.csi.moon.commons.util.decodifica.DecodificaRuolo;
import it.csi.moon.commons.util.decodifica.DecodificaTipoUtente;
import it.csi.moon.moonfobl.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.UtenteAreaRuoloDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.UtenteModuloDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ext.OneriCostrIbanDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.CognoneNomeCodFiscEntity;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.OneriCostrIbanEntity;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.UtenteAreaRuoloEntity;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.UtenteModuloEntity;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiIstanzaHelper_CONT_COMUNE;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.util.Constants;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * IstanzaDelegate_CONT_AGG_OPE - Specializzazione.
 *  - saveUlteriori richiamato da saveIstanza per salvataggio ulteriore per l'aggiunto di nuovi operatori Comunale sul modulo CODICE_MODULO_EDIL_CONT_COSTR
 *
 * @author laurent
 * @author danilo
 *
 */
public class IstanzaDelegate_CONT_AGG_OPE extends IstanzaDefaultDelegate implements IstanzaServiceDelegate {

	private static final String CLASS_NAME = "IstanzaDelegate_CONT_AGG_OPE";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String CODICE_AREA_EDIL_REGP = "EDIL_REGP";
	private static final String CODICE_AREA = "EDIL_PRIV";
	
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
	
	public IstanzaDelegate_CONT_AGG_OPE() {
		LOG.debug("[" + CLASS_NAME + "::" + CLASS_NAME + "]");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	//
	// INVI ISTANZA
	//
	@Override
	protected void inviaUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, Long idProcesso) throws BusinessException {
		insertExtOneriCostrAggOpe(user, response);
	}
//	protected void saveUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, boolean insertMode, boolean completaMode, Long idProcesso) throws BusinessException {
//		if (completaMode) {
//			insertExtOneriCostrAggOpe(user, docRiconoscimento, response, idProcesso);
//		}
//	}

	private void insertExtOneriCostrAggOpe(UserInfo user, IstanzaSaveResponse response) throws DAOException, BusinessException {
		
		OneriCostrIbanEntity ibanComuneE = null;
		try {
			DatiIstanzaHelper_CONT_COMUNE oneriCostrHelper = new DatiIstanzaHelper_CONT_COMUNE(String.valueOf(response.getIstanza().getData()));

			// 1. Identificazione dell'ente ; l'ente deve già esistere nella tabella ext
			ibanComuneE = oneriCostrHelper.readComune(); // Lego solo id comune
			ibanComuneE = oneriCostrIbanDAO.findByCodiceIstat(ibanComuneE.getCodIstat()); // Verifica che sia presente con IBAN in ext
			EnteEntity ente = enteDAO.findByCodice(ibanComuneE.getCodIstat()); // Deve gia esistere
			AreaEntity area = areaDAO.findByCd(ente.getIdEnte(), CODICE_AREA); // Deve gia esistere

			AreaEntity areaRegionePiemonte = retrieveAreaEdilRegionePiemonte();
			
			// 2. Gestione NEW Operatori
			List<CognoneNomeCodFiscEntity> operatori = oneriCostrHelper.readOperatori();
			LOG.debug("[" + CLASS_NAME + "::insertExtOneriCostrAggOpe] operatori.size(): " + operatori.size());
			for (CognoneNomeCodFiscEntity o : operatori) {
				// Ricerca o Crea l'utente
				UtenteEntity utente = retrieveUtente(o, user);
				insertUtenteAreaRuoloIfNotExists(utente, area, user);
				insertUtenteAreaRuoloIfNotExists(utente, areaRegionePiemonte, user);
				insertUtenteModuloIfNotExists(utente, user);
			}
		} catch (ItemNotFoundDAOException e) {				
			LOG.warn("[" + CLASS_NAME + "::insertExtOneriCostrAggOpe] ItemNotFoundException"+e.getMessage());			
			response.setCodice("MOONFOBL-CONT_AGG_OPE-001");
			response.setTitolo("AVVISO");
			response.setDescrizione("Il Comune di "+ibanComuneE.getNomeComune()+" non è presente, per un nuovo comune utilizzare il modulo: CONT_COMUNE - Raccolta dati operatori comuni");	
		} catch (Exception e) {						
			LOG.warn("[" + CLASS_NAME + "::insertExtOneriCostrAggOpe] Exception"+e.getMessage());
			throw new BusinessException();
		}
	}

	private AreaEntity retrieveAreaEdilRegionePiemonte() {
		return areaDAO.findByCd(1L, CODICE_AREA_EDIL_REGP);
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
				entity.setAttoreUpd(user.getIdentificativoUtente());
				Long idUtente = utenteDAO.insert(entity);				
				entity.setIdUtente(idUtente);
				result = entity;				
			}			
			return result;
		} catch (DAOException e) {
			LOG.warn("[" + CLASS_NAME + "::retrieveUtente] DAOException "+e.getMessage());
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
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::insertUtenteAreaRuoloIfNotExists] idUtente = " + utente.getIdUtente());
				LOG.debug("[" + CLASS_NAME + "::insertUtenteAreaRuoloIfNotExists] idArea = " + area.getIdArea());
			}
			try {
				result = utenteAreaRuoloDAO.findByUtenteAreaRuolo(utente.getIdUtente(),area.getIdArea(), DecodificaRuolo.OP_SIMP.getId());
			} catch (ItemNotFoundDAOException infe) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("[" + CLASS_NAME + "::insertUtenteAreaRuoloIfNotExists] ItemNotFound insert new utente area ruolo");
				}
				UtenteAreaRuoloEntity entity = new UtenteAreaRuoloEntity();
				entity.setIdUtente(utente.getIdUtente());
				entity.setIdArea(area.getIdArea());
				entity.setIdRuolo(DecodificaRuolo.OP_SIMP.getId());
				entity.setDataUpd(new Date());
				entity.setAttoreUpd(user.getIdentificativoUtente());	
				int resInsert = utenteAreaRuoloDAO.insert(entity);
				LOG.debug("[" + CLASS_NAME + "::insertUtenteAreaRuoloIfNotExists]  record inserito " + resInsert);
				result = entity;
			}
			return result;						
		} catch (DAOException e) {
			LOG.warn("[" + CLASS_NAME + "::insertUtenteAreaRuoloIfNotExists] DAOException " + e.getMessage());
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
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::insertUtenteModuloIfNotExists] idUtente " + utente.getIdUtente());
				LOG.debug("[" + CLASS_NAME + "::insertUtenteModuloIfNotExists] idModulo " + getIdModulo()); // dinamico per ambiente DEV:75, TST:58, PRD:15
			}
			try {
				result = utenteModuloDAO.findByUtenteModulo(utente.getIdUtente(), getIdModulo());
			} catch (ItemNotFoundDAOException infe) {
				UtenteModuloEntity entity = new UtenteModuloEntity();
				entity.setIdUtente(utente.getIdUtente());
				entity.setIdModulo(getIdModulo());
				entity.setDataUpd(new Date());
				entity.setAttoreUpd(user.getIdentificativoUtente());
				int resInsert =  utenteModuloDAO.insert(entity);
				LOG.debug("[" + CLASS_NAME + "::insertUtenteModuloIfNotExists] record inserito "+resInsert);
				result = entity;
			}
			return result;
		} catch (DAOException e) {
			LOG.warn("[" + CLASS_NAME + "::insertUtenteModuloIfNotExists] DAOException "+e.getMessage());
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
			LOG.warn("[" + CLASS_NAME + "::getIdModulo] DAOException "+e.getMessage());
			throw new BusinessException();
		}
	}
}
