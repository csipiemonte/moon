/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.AuditEntity;
import it.csi.moon.moonsrv.business.service.AuditService;
import it.csi.moon.moonsrv.business.service.impl.dao.AuditDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class AuditServiceImpl implements AuditService {
	
	private static final String CLASS_NAME = "AuditServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	AuditDAO auditDAO;
	
	@Override
	public void insertAuditLogin(String ipAdress, UserInfo userInfo) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				retrieveUser(userInfo),
				AuditEntity.EnumOperazione.LOGIN, 
				"", 
				"");
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[IrideIdAdapterFilter::insertAuditLogin] DAOException", e);
		}
	}
	
	@Override
	public void insertAuditLogout(String ipAdress, UserInfo userInfo, String result) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				retrieveUser(userInfo), 
				AuditEntity.EnumOperazione.LOGOUT,
				"",
				"URL-LOGOUT=>" + result);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditLogout]" + e.toString(), e);
		}
	}
	

	@Override
	public void insertAuditANPRGetFamigliaByCF(IstanzaInitParams initParams, String keyOperazione) {
		try {
			AuditEntity auditEntity = new AuditEntity(initParams.getIpAdress(), 
				retrieveUser(initParams), 
				AuditEntity.EnumOperazione.READ,
				"GET_FAMIGLIA_ANPR_BY_CF", 
				keyOperazione);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditANPRGetFamiglia]" + e.toString(), e);
		}
	}

	@Override
	public void insertAuditANPRGetFamigliaByCF(String utente, String ipAdress, String keyOperazione) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				utente, 
				AuditEntity.EnumOperazione.READ,
				"GET_FAMIGLIA_ANPR_BY_CF", 
				keyOperazione);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditANPRGetFamiglia]" + e.toString(), e);
		}
	}

	//
	@Override
	public String retrieveUser(UserInfo userInfo) {
		return userInfo!=null ? userInfo.getIdentificativoUtente() + ";" + userInfo.getCognome() + ";" + userInfo.getNome() :"NULL";
	}
	@Override
	public String retrieveUser(IstanzaInitParams initParams) {
		return initParams.getCodiceFiscale() + ";" + initParams.getCognome()+ ";" + initParams.getNome();
	}

	@Override
	public void insertAuditAPICambioStato(String ipAdress, String user, String params) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				user,
				AuditEntity.EnumOperazione.UPDATE, 
				"cambioDiStato - /istanze/{codice-istanza}/azione/{codice-azione}", 
				params);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditAPICambioStato] DAOException", e);
		}		
	}

	@Override
	public void insertAuditAPINotifica(String ipAdress, String user, String params) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				user,
				AuditEntity.EnumOperazione.UPDATE, 
				"notifica - /istanze/{codice-istanza}/notifica", 
				params);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditAPINotifica] DAOException", e);
		}
	}

	@Override
	public void insertAuditAPIIstanze(String ipAdress, String user, String params) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				user,
				AuditEntity.EnumOperazione.READ, 
				"elencoIstanze - /istanze", 
				params);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditAPIIstanze] DAOException", e);
		}
	}
	
	@Override
	public void insertAuditAPIIstanzePaginate(String ipAdress, String user, String params) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				user,
				AuditEntity.EnumOperazione.READ, 
				"elencoIstanze - /istanze-paginate", 
				params);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditAPIIstanzePaginate] DAOException", e);
		}
	}

	@Override
	public void insertAuditAPIDettaglioIstanza(String ipAdress, String user, String params) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				user,
				AuditEntity.EnumOperazione.READ, 
				"cercaDettaglioIstanza - /istanze/{codice-istanza}", 
				params);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditAPIDettaglioIstanza] DAOException", e);
		}
	}
	
	@Override
	public void insertAuditAPIDettaglioIstanzaPdf(String ipAdress, String user, String params) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				user,
				AuditEntity.EnumOperazione.READ, 
				"cercaDettaglioIstanza - /istanze/{codice-istanza}/pdf", 
				params);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditAPIDettaglioIstanzaPdf] DAOException", e);
		}
	}
	
	@Override
	public void insertAuditAPIReport(String ipAdress, String user, String params) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				user,
				AuditEntity.EnumOperazione.READ, 
				"report - /moduli/{codice-modulo}/report", 
				params);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditAPIReport] DAOException", e);
		}
	}

	@Override
	public void insertAuditAPICercaAllegato(String ipAdress, String user, String params) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				user,
				AuditEntity.EnumOperazione.READ, 
				"cercaAllegato - /istanze/{codice-istanza}/allegati", 
				params);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditAPICercaAllegato] DAOException", e);
		}
	}

	@Override
	public void insertAuditAPIModuloVersione(String ipAdress, String user, String params) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAdress, 
				user,
				AuditEntity.EnumOperazione.READ, 
				"getModuloVersione - /moduli/{codice-modulo}/v/{versione-modulo}", 
				params);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditAPIModuloVersione] DAOException", e);
		}
	}
	
}
