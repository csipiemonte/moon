/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.istanza;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.extra.demografia.DocumentoRiconoscimento;
import it.csi.moon.commons.entity.AreaEntity;
import it.csi.moon.commons.entity.EnteEntity;
import it.csi.moon.commons.util.decodifica.DecodificaTipoEnte;
import it.csi.moon.moonfobl.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.ext.OneriCostrIbanDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.OneriCostrIbanEntity;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiIstanzaHelper_CONT_COMUNE;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * IstanzaDelegate_CONT_AGGIORNAMENTO_IBAN - Specializzazione.
 *  - saveUlteriori richiamato da saveIstanza per salvataggio ulteriore in moon_ext_oneri_costr_iban via oneriCostrIbanDAO
 *
 * Gestisce solo la prima parte di creazione ente COMUNE se non esiste e aggiornamento dell'IBAN del comune
 * 
 * @see IstanzaDelegate_CONT_COMUNE  per la gestione initiale di un nuovo comune con modulo CONT_COMUNE
 *
 * @author laurent
 * @author danilo
 *
 */
public class IstanzaDelegate_CONT_AGGIORNAMENTO_IBAN extends IstanzaDefaultDelegate implements IstanzaServiceDelegate {

	private static final String CLASS_NAME = "IstanzaDelegate_CONT_AGGIORNAMENTO_IBAN";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String CODICE_AREA = "EDIL_PRIV";
	private static final String DESC_AREA = "Ufficio comunale Edilizia privata";
	
	@Autowired
	OneriCostrIbanDAO oneriCostrIbanDAO;
	@Autowired
	EnteDAO enteDAO;
	@Autowired
	AreaDAO areaDAO;
	
	public IstanzaDelegate_CONT_AGGIORNAMENTO_IBAN() {
		LOG.debug("[" + CLASS_NAME + "::" + CLASS_NAME + "]");
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
			try {
				OneriCostrIbanEntity entity = oneriCostrIbanDAO.findByCodiceIstat(ibanComuneE.getCodIstat());
				entity.setIban(ibanComuneE.getIban());
				entity.setDataUpd(new Date());
				entity.setAttoreUpd(user.getIdentificativoUtente());
				oneriCostrIbanDAO.update(entity);
			} catch (ItemNotFoundDAOException infe) {
				LOG.warn("[" + CLASS_NAME + "::retriveEnte] CREO ONERI_IBAN codiceIstat="+ibanComuneE.getCodIstat());
				ibanComuneE.setDataIns(new Date());
				ibanComuneE.setAttoreIns(user.getIdentificativoUtente());
				oneriCostrIbanDAO.insert(ibanComuneE);
			}

			// Ricerca o Crea l'ente e l'area EDIL_PRIV
			EnteEntity ente = retrieveEnte(ibanComuneE, user);
			AreaEntity area = retrieveAreaEdilPriv(ente, user);

		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::insertExtOneriCostr] Exception"+e.getMessage());
			throw new BusinessException();
		}
	}

	/**
	 * Ricerca o Crea l'ente di tipo COMUNE
	 * @param ibanComuneE i dati del modulo compilato (provincia e comune Istat)
	 * @param user di tracciamento di inserimento
	 * @return la resorse ricercata o creata
	 */
	private EnteEntity retrieveEnte(OneriCostrIbanEntity ibanComuneE, UserInfo user) throws BusinessException {
		try {
			LOG.debug("[" + CLASS_NAME + "::retriveEnte] codiceIstat="+ibanComuneE.getCodIstat());
			EnteEntity result = null;
			try {
				result = enteDAO.findByCodice(ibanComuneE.getCodIstat());
			} catch (ItemNotFoundDAOException infe) {
				LOG.warn("[" + CLASS_NAME + "::retriveEnte] CREO ENTE codiceIstat="+ibanComuneE.getCodIstat());
				EnteEntity entity = new EnteEntity();
				entity.setCodiceEnte(ibanComuneE.getCodIstat());
				entity.setNomeEnte(ibanComuneE.getNomeComune());
				entity.setDescrizioneEnte("COMUNE DI "+ibanComuneE.getNomeComune());
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
			LOG.warn("[" + CLASS_NAME + "::retriveEnte] DAOException "+e.getMessage());
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
			LOG.debug("[" + CLASS_NAME + "::retrieveAreaEdilPriv] " + CODICE_AREA + " for codiceIstat="	+ ente.getIdEnte());
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
			LOG.warn("[" + CLASS_NAME + "::retrieveAreaEdilPriv] DAOException " + e.getMessage());
			throw new BusinessException();
		}
	}

}
