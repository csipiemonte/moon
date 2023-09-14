/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.helper.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.CampoModulo;
import it.csi.moon.commons.dto.CampoModuloFormioFileName;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonfobl.business.service.impl.helper.DatiIstanzaHelper;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.util.LoggerAccessor;


/**
 * PostSaveInstanzaTask AssociaAllegatiAdIstanzaTask
 * 
 * @author laurent
 * 
 */
public class AssociaRepositoryFileAdIstanzaTask implements Callable<String> {

	private static final String CLASS_NAME = "AssociaAllegatiAdIstanzaTask";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private Long idIstanza;
	private Long idDatiAzione;
	private String contentDatiAzione;
	private DatiIstanzaHelper datiIstanzaHelper;
	
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;

	public AssociaRepositoryFileAdIstanzaTask(UserInfo user, Long idIstanza, Long idDatiAzione, String contentDatiAzione) {
		super();
//		this.user = user;
		this.idIstanza = idIstanza;
		this.idDatiAzione = idDatiAzione;
		this.contentDatiAzione = contentDatiAzione;
//		this.moduloE = moduloE;
//		this.threadName = Thread.currentThread().getName();
		LOG.debug("[" + CLASS_NAME + "::AssociaRepositoryFileAdIstanzaTask] CONSTRUCTOR idIstanza: " + idIstanza);

		// must provide autowiring support to inject SpringBean
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/**
	 * Associa l'istanza ai suoi allegati : valorizza
	 * moon_fo_t_allegati_istanza.id_istanza per tutti allegati associati
	 * all'istanza in stato INVIATA
	 */
	public String call() throws BusinessException {
		try {
			String result = null;
			LOG.debug("[" + CLASS_NAME + "::call] BEGIN Task...");

			// 1. Recupero dei campi di tipo "file" nel modulo via moonsrv
			try {
				List<CampoModulo> campiFile = moonsrvDAO.getCampiDatiAzione(idDatiAzione, "file");
				if (campiFile == null || campiFile.isEmpty()) {
					result = "Nessun campo di tipo file nel Modulo WF.";
				} else {
					// 2. Ricerca la dove il dichiarante a mandato un allegato nell'istanza
					List<CampoModuloFormioFileName> campiFormIoFileNames = retrieveFormIoFileNames(campiFile);

					// 3. Aggiorna gli Allegati assegnando idIstanza
					int i = updateIdIstanzaOnRepositoryFile(campiFormIoFileNames, idIstanza); //idTipologia
					result = "Sono associati " + i + " allegati all'istanza.";
				}
			} catch (DAOException e) {
				result = "Non è stato possibile recuperare il DatiAzione " + idDatiAzione;
			}

			LOG.debug("[" + CLASS_NAME + "::call] END result=" + result);
			return result;
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::call] BusinessException");
			throw be;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::call] Exception ", e);
			throw new BusinessException();
		}
	}

	private List<CampoModuloFormioFileName> retrieveFormIoFileNames(List<CampoModulo> campiFile) throws BusinessException {
		List<CampoModuloFormioFileName> result = new ArrayList<>();
		for (CampoModulo campo : campiFile) {
			LOG.debug("[" + CLASS_NAME + "::retrieveFormIoFileNames] campo " + campo);
			List<String> formIoFileNames = getDatiIstanzaHelper().extractFormIoFileNameByCampo(campo);
			if (formIoFileNames!=null) {
				for (String formIoFileName : formIoFileNames) {
					result.add(new CampoModuloFormioFileName(campo,formIoFileName));
				}
			}
		}
		return result;
	}
	
	private int updateIdIstanzaOnRepositoryFile(List<CampoModuloFormioFileName> campiFormIoFileNames, Long idIstanza) throws DAOException {
		int result = 0;
		for (CampoModuloFormioFileName campoFormIoFileName : campiFormIoFileNames) {
			result += repositoryFileDAO.updateIdIstanza(campoFormIoFileName, idIstanza);
		}
		return result;
	}
	
	private DatiIstanzaHelper getDatiIstanzaHelper() throws BusinessException {
		if (datiIstanzaHelper==null) {
			datiIstanzaHelper = new DatiIstanzaHelper();
			datiIstanzaHelper.initDataNode(contentDatiAzione);
		}
		return datiIstanzaHelper;
	}
	
}
