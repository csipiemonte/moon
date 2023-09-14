/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.AuditEntity;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.business.service.impl.dao.AuditDAO;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class AuditServiceImpl implements AuditService {
	
	private static final String CLASS_NAME = "AuditServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	AuditDAO auditDAO;
	
	//
	@Override
	public String retrieveUser(UserInfo userInfo) {
		return userInfo!=null ? userInfo.getIdentificativoUtente() + ";" + userInfo.getCognome() + ";" + userInfo.getNome() :"NULL";
	}
	@Override
	public String retrieveUser(IstanzaInitParams initParams) {
		return initParams.getCodiceFiscale() + ";" + initParams.getCognome()+ ";" + initParams.getNome();
	}
	
//	@Override
//	public void insertAuditLogin(String ipAdress, UserInfo userInfo) {
//		try {
//			auditDAO.insertAuditEntity(new AuditEntity(ipAdress, retrieveUser(userInfo),	AuditEntity.EnumOperazione.LOGIN, "", ""));
//		} catch (Exception e) {
//			LOG.error("[AuditServiceImpl::insertAuditLogin] Exception", e);
//		}
//	}
//	@Override
//	public void insertAuditLogin(String ipAdress, LoginResponse loginResponse, String portalName) {
//		try {
//			AuditEntity auditEntity = new AuditEntity(ipAdress, 
//				retrieveUser(loginResponse),
//				AuditEntity.EnumOperazione.LOGIN, 
//				"MODULO;PORTALE", 
//				loginResponse.getModulo().getCodiceModulo()+";"+portalName);
//			auditDAO.insertAuditEntity(auditEntity);
//		} catch (DAOException e) {
//			LOG.error("[AuditServiceImpl::insertAuditLogin] DAOException", e);
//		}
//	}
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
			LOG.error("[" + CLASS_NAME + "::insertAuditLoginIdpShibboleth] DAOException", e);
		}
	}

	@Override
	public void insertAuditLogout(String ipAdress, UserInfo userInfo/*, String result*/) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(ipAdress, retrieveUser(userInfo), AuditEntity.EnumOperazione.LOGOUT, "", "" /*"URL-LOGOUT=>" + result*/));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditLogout]" + e.toString(), e);
		}
	}
	
	@Override
	public void insertAuditANPRGetFamigliaByCF(IstanzaInitParams initParams, String keyOperazione) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(initParams.getIpAdress(), retrieveUser(initParams), AuditEntity.EnumOperazione.READ, "GET_FAMIGLIA_ANPR_BY_CF", keyOperazione));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertAuditANPRGetFamigliaByCF]" + e.toString(), e);
		}
	}

	@Override
	public void insertSearchIstanze(String ipAddress, UserInfo userInfo, String keyOperation) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.READ, "SEARCH ISTANZE", keyOperation));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertSearchIstanze]" + e.toString(), e);
		}
	}
	
	@Override
	public void getPdf(String ipAddress, UserInfo userInfo, Long idIstanza) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.READ, "GET_PDF-IdIstanza", idIstanza.toString()));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getPdf] Exception", e);
		}
	}
	
	@Override
	public void getNotifica(String ipAddress, UserInfo userInfo, Long idIstanza) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.READ, "GET_NOTIFICA", idIstanza.toString()));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getNotifica] Exception", e);
		}
	}
	
	@Override
	public void getDocumentoNotifica(String ipAddress, UserInfo userInfo, Long idIstanza) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.READ, "GET_DOCUMENTO_NOTIFICA", idIstanza.toString()));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoNotifica] Exception", e);
		}
	}
	
	@Override
	public void getDocumentoByFormioNameFile(String ipAddress, UserInfo userInfo, String formioNameFile) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.READ, "GET_DOCUMENTO_BY_FORMIO_NAME_FILE", formioNameFile));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] Exception", e);
		}
	}
	
	@Override
	public void getDocumentoByIdFile(String ipAddress, UserInfo userInfo, Long idFile) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.READ, "GET_DOCUMENTO_BY_ID_FILE", idFile+""));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getDocumentoByIdFile] Exception", e);
		}
	}

	@Override
	public void getIstanza(String ipAddress, UserInfo userInfo, Long idIstanza) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.READ, "IdIstanza", idIstanza.toString()));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Exception", e);
		}
	}
	
	@Override
	public void getIstanza(String ipAddress, UserInfo userInfo, String codiceIstanza) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.READ, "codiceIstanza", codiceIstanza));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getIstanza] Exception", e);
		}		
	}


	@Override
	public void saveIstanza(String ipAddress, UserInfo userInfo, Long idIstanza) {
		try {
			if (idIstanza == null) {
				auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.INSERT, "IdIstanza", null));
			} else {
				auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.UPDATE, "IdIstanza", idIstanza.toString()));
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + ":saveIstanza] Exception", e);
		}
	}
	@Override
	public void deleteIstanza(String ipAddress, UserInfo userInfo, Long idIstanza) {
		try {
			auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.DELETE, "IdIstanza", idIstanza.toString()));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::deleteIstanza] Exception", e);
		}
	}
	@Override
	public void duplicaIstanza(String ipAddress, UserInfo userInfo, Long idIstanza) {
		try {
			if (idIstanza != null) {
				auditDAO.insertAuditEntity(new AuditEntity(ipAddress, retrieveUser(userInfo), AuditEntity.EnumOperazione.INSERT, "DUPLICA_ISTANZA", idIstanza.toString()));
			}
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::duplicaIstanza] Exception", e);
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
			LOG.error("[" + CLASS_NAME + "::insertAuditLoginLocal] DAOException", e);
		}
	}
	
}
