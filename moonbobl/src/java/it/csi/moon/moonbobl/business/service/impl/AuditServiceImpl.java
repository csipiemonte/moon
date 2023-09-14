/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.impl.dao.AuditDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaInitParams;
import it.csi.moon.moonbobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class AuditServiceImpl implements AuditService {
	
	private final static String CLASS_NAME = "AuditServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
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
			log.error("[" + CLASS_NAME + "::insertAuditLogin] DAOException", e);
		}
		catch (Throwable thr) {
			log.error("[" + CLASS_NAME + "::insertAuditLogin] Throwable Exception", thr);
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
			log.error("[" + CLASS_NAME + "::insertAuditLogout]" + e.toString(), e);
		}
		catch (Throwable thr) {
			log.error("[" + CLASS_NAME + "::insertAuditLogout] Throwable Exception", thr);
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
			log.error("[" + CLASS_NAME + "::insertAuditANPRGetFamiglia]" + e.toString(), e);
		}
		catch (Throwable thr) {
			log.error("[" + CLASS_NAME + "::insertAuditANPRGetFamiglia] Throwable Exception", thr);
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
	public void insertSearchIstanze(HttpServletRequest req, String keyOperation) {
		try {
			String ipAddress = req.getRemoteAddr();
			String user = retrieveUser(getUserInfoFromRequest(req));
			AuditEntity auditEntity = new AuditEntity(ipAddress, user, AuditEntity.EnumOperazione.READ, "SEARCH ISTANZE", keyOperation);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			log.error("[" + CLASS_NAME + "::insertSearchIstanze]" + e.toString(), e);
		}
		catch (Throwable thr) {
			log.error("[" + CLASS_NAME + "::insertSearchIstanze] Throwable Exception", thr);
		}
	}

	private UserInfo getUserInfoFromRequest(HttpServletRequest request) {
		return  (UserInfo)request.getSession().getAttribute(Constants.SESSION_USERINFO);
	}

	@Override
	public void getPdf(String ipAddress, UserInfo userInfo, Long idIstanza) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAddress, 
				retrieveUser(userInfo),
				AuditEntity.EnumOperazione.READ, 
				"GET_PDF-IdIstanza", 
				idIstanza.toString());
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getPdf] DAOException", e);
		}
		catch (Throwable thr) {
			log.error("[" + CLASS_NAME + "::getPdf] Throwable Exception", thr);
		}
	}

	@Override
	public void getIstanza(String ipAddress, UserInfo userInfo, Long idIstanza, String nomePortale) {
		try {
			AuditEntity auditEntity = new AuditEntity(ipAddress, 
				retrieveUser(userInfo),
				AuditEntity.EnumOperazione.READ, 
				"IdIstanza-Portale", 
				idIstanza.toString() + "-" + nomePortale);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getIstanza] DAOException", e);
		}
		catch (Throwable thr) {
			log.error("[" + CLASS_NAME + "::getIstanza] Throwable Exception", thr);
		}
	}

	@Override
	public void saveIstanza(String ipAddress, UserInfo userInfo, Long idIstanza) {
		try {
			AuditEntity auditEntity;
			if (idIstanza == null) {
				// insert prima volta
				 auditEntity = new AuditEntity(ipAddress, retrieveUser(userInfo),
						AuditEntity.EnumOperazione.INSERT, "IdIstanza", null);
			} else {
				 auditEntity = new AuditEntity(ipAddress, retrieveUser(userInfo),
						AuditEntity.EnumOperazione.UPDATE, "IdIstanza", idIstanza.toString());
			}
			auditDAO.insertAuditEntity(auditEntity);
			
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::saveIstanza] DAOException", e);
		}
		catch (Throwable thr) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Throwable Exception", thr);
		}
	
	}

	@Override
	public void traceOperazione(AuditEntity auditEntity) {
		// TODO Auto-generated method stub
		try {
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::traceOperazione] DAOException", e);
		}
		catch (Throwable thr) {
			log.error("[" + CLASS_NAME + "::traceOperazione] Throwable Exception", thr);
		}
	}
	
	@Override
	public void insertAuditLoginIdpShibboleth(String ipAdress, LoginResponse loginResponse, String portalName, String provider) {
		try {
			 AuditEntity auditEntity = new AuditEntity(ipAdress, 
				retrieveUser(loginResponse),
				AuditEntity.EnumOperazione.LOGIN, 
				"PORTALE;PROVIDER", 
				portalName+";"+provider);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::insertAuditLoginIdpShibboleth] DAOException", e);
		}
	}
	
	@Override
	public void insertAuditLoginLocal(String ipAdress, LoginResponse loginResponse, String portalName, String loginMode) {
		try {
			 AuditEntity auditEntity = new AuditEntity(ipAdress, 
				retrieveUser(loginResponse),
				AuditEntity.EnumOperazione.LOGIN, 
				"PORTALE;PROVIDER", 
				portalName+";"+loginMode);
			auditDAO.insertAuditEntity(auditEntity);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::insertAuditLoginLocal] DAOException", e);
		}
	}
	
}
